package ir.co.realtime.disaster.auth.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import ir.co.realtime.disaster.auth.model.PasswordReset;
import ir.co.realtime.disaster.auth.model.User;
import ir.co.realtime.disaster.auth.repository.UserRepository;
import ir.co.realtime.disaster.auth.sms.SmsService;
import ir.co.realtime.disaster.exception.AppException;
import ir.co.realtime.disaster.exception.UserNotFoundException;
import ir.co.realtime.disaster.exception.VerificationCodeException;
import ir.co.realtime.disaster.util.RandomCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class VerificationService {

    @Autowired
    private SmsService smsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Value("${auth.password.reset-subject}")
    private String resetSubject;

    @Value("${auth.password.reset-base-url}")
    private String resetBaseUrl;

    private LoadingCache<String, Integer> userToCode;
    private LoadingCache<Integer, String> codeToUser;

    @PostConstruct
    public void init() {
        CacheLoader<String, Integer> loader = new CacheLoader<String, Integer>() {
            @Override
            public Integer load(String s) {
                return 0;
            }
        };

        userToCode = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build(loader);

        codeToUser = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build(new CacheLoader<Integer, String>() {
                    @Override
                    public String load(Integer code) {
                        return null;
                    }
                });
    }


    public Integer createVerificationCode(String username) {
        int newCode = RandomCodeGenerator.createNewCode();
        userToCode.put(username, newCode);
        return newCode;
    }

    public Integer getVerificationCode(String username) throws ExecutionException {
        return userToCode.get(username);
    }


    public ResetPasswordType requestPassReset(String username) throws AppException {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (!byUsername.isPresent())
            throw new UserNotFoundException(username);

        User user = byUsername.get();
        int newCode;
        while (true) {
            newCode = RandomCodeGenerator.createNewCode();
            String un = codeToUser.getIfPresent(newCode);
            if (un == null) {
                codeToUser.put(newCode, username);
                break;
            }
        }

        if (user.isMobileConfirmed()) {
            userToCode.put(username, newCode);
            smsService.send(Long.parseLong(user.getMobile()), newCode + "");
            return ResetPasswordType.mobile();
        } else if (user.isEmailConfirmed()){
            userToCode.put(username, newCode);
            String link = resetBaseUrl + "?code=" + newCode;
            String text = "To reset click on \n" + link;
            emailService.sendEmail(user.getEmail(), resetSubject, text);
            return ResetPasswordType.email();
        }

        throw new AppException("user not confirmed");
    }

    public String findUsername(PasswordReset passwordReset) throws VerificationCodeException {
        String username = codeToUser.getIfPresent(passwordReset.getCode());
        if (username == null)
            throw new VerificationCodeException(passwordReset.getCode());

        Integer code = userToCode.getIfPresent(username);
        if (code == null || !code.equals(passwordReset.getCode())) {
            throw new VerificationCodeException(passwordReset.getCode());
        }

        return username;
    }


    public static class ResetPasswordType {

        private ResetType resetType;
        private String message;

        public ResetType getResetType() {
            return resetType;
        }

        public void setResetType(ResetType resetType) {
            this.resetType = resetType;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public static ResetPasswordType mobile() {
            ResetPasswordType resetPasswordType = new ResetPasswordType();
            resetPasswordType.setResetType(ResetType.MOBILE);
            resetPasswordType.setMessage("کد بازیابی به موبایل ارسال شده است");
            return resetPasswordType;
        }

        public static ResetPasswordType email() {
            ResetPasswordType resetPasswordType = new ResetPasswordType();
            resetPasswordType.setResetType(ResetType.EMAIL);
            resetPasswordType.setMessage("لینک بازیابی به ایمیل ارسال شده است");
            return resetPasswordType;
        }
    }

    public enum ResetType {

        MOBILE,
        EMAIL
    }

}