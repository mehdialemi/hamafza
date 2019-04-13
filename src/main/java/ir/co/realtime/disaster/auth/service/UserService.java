package ir.co.realtime.disaster.auth.service;

import ir.co.realtime.disaster.auth.model.Role;
import ir.co.realtime.disaster.auth.model.RoleName;
import ir.co.realtime.disaster.auth.model.User;
import ir.co.realtime.disaster.auth.repository.RoleRepository;
import ir.co.realtime.disaster.auth.repository.UserRepository;
import ir.co.realtime.disaster.auth.sms.SmsService;
import ir.co.realtime.disaster.exception.AppException;
import ir.co.realtime.disaster.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    private static final User EMPTY_USER = new User();
    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationService activationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SmsService smsService;

    User update(final User user) {
        Optional <User> byUsername = userRepository.findByUsername(user.getUsername());
        byUsername.orElseThrow(() -> new UserNotFoundException(user.getUsername()));

        return userRepository.saveAndFlush(byUsername.get());
    }

    User addRole(String username, String roleName) throws UserNotFoundException, RoleNotFoundException {
        Optional<User> byUsername = userRepository.findByUsername(username);
        byUsername.orElseThrow(() -> new UserNotFoundException(username));

        Role role;
        try {
            role = roleRepository.findByName(RoleName.valueOf(roleName)).get();
        } catch (Throwable e) {
            throw new RoleNotFoundException(roleName);
        }

        User user = byUsername.get();
        user.getRoles().add(role);

        return userRepository.save(user);
    }

    User deleteRole(String username, String roleName) throws UserNotFoundException, RoleNotFoundException {
        Optional<User> byUsername = userRepository.findByUsername(username);
        byUsername.orElseThrow(() -> new UserNotFoundException(username));

        Role role;
        try {
            role = roleRepository.findByName(RoleName.valueOf(roleName)).get();
        } catch (Throwable e) {
            throw new RoleNotFoundException(roleName);
        }

        User user = byUsername.get();
        user.getRoles().remove(role);

        return userRepository.save(user);
    }


    public User changeUserPassword(String username, String password) throws AppException {
        return changeUserPassword(username, password, password);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User changeUserPassword(String username, String password, String confirmedPassword)
            throws AppException {
        if (password == null || !password.equals(confirmedPassword))
            throw new AppException("password does not match");

        Optional<User> byUsername = userRepository.findByUsername(username);
        byUsername.orElseThrow(() -> new UserNotFoundException(username));

        User user = byUsername.get();
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public User get(String username) throws UserNotFoundException {
        Optional<User> byUsername = userRepository.findByUsername(username);
        byUsername.orElseThrow(() -> new UserNotFoundException(username));

        return byUsername.get();
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    boolean getMobileVerificationCode(String username) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        byUsername.orElseThrow(() -> new UserNotFoundException(username));

        long mobile;
        try {
            mobile = Long.parseLong(byUsername.get().getMobile());
        } catch (NumberFormatException e) {
            throw new AppException("شماره موبایل صحیح نیست");
        }

        Integer code = activationService.createVerificationCode(username);
        // Send activation code to mobile
        boolean result = smsService.send(mobile, code + " ");
        if (!result) {
            throw new AppException("مشکلی در ارسال بوجود آمده است لطفا دوباره تلاش کنید");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("کد به موبایل");
        sb.append(" ").append(mobile).append(" ");
        sb.append("ارسال شد");
        logger.info("Generated activation code {} for username {} with mobile {}",
                code, username, mobile);
        return true;
    }

    boolean checkMobileVerificationCode(String username, Integer code) {
        Integer activationKey = null;
        try {
            activationKey = activationService.getVerificationCode(username);
        } catch (ExecutionException e) {
            throw new AppException("مشکل در بازیابی کد بوجود آمده است. دوباره تلاش کنید");
        }

        if (activationKey == null || !activationKey.equals(code))
            throw new AppException("کد ورودی صحیح نیست");

        Optional<User> byUsername = userRepository.findByUsername(username);
        byUsername.orElseThrow(() -> new UserNotFoundException(username));

        User user = byUsername.get();
        user.setMobileConfirmed(true);
        userRepository.save(user);
        return true;
    }

}
