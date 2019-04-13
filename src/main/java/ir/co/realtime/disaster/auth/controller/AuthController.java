package ir.co.realtime.disaster.auth.controller;

import ir.co.realtime.disaster.auth.jwt.JwtAuthenticationResponse;
import ir.co.realtime.disaster.auth.jwt.JwtTokenProvider;
import ir.co.realtime.disaster.auth.model.*;
import ir.co.realtime.disaster.auth.repository.RoleRepository;
import ir.co.realtime.disaster.auth.service.UserService;
import ir.co.realtime.disaster.auth.service.VerificationService;
import ir.co.realtime.disaster.common.ApiResponse;
import ir.co.realtime.disaster.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private VerificationService verificationService;

    @PostMapping("/signin")
    public ResponseEntity <?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity <?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity <>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity <>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userService.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    @GetMapping("password/reset/request")
    public ResponseEntity <?> resetPassRequest(@NotNull @RequestParam("username") String username) {

        VerificationService.ResetPasswordType resetPasswordType = verificationService.requestPassReset(username);
        return new ResponseEntity <>(resetPasswordType, HttpStatus.OK);

    }

    @PostMapping("password/reset/confirm")
    public ResponseEntity <?> resetResponse(@Valid @RequestBody PasswordReset passwordReset) {

        String username = verificationService.findUsername(passwordReset);
        userService.changeUserPassword(username, passwordReset.getPassword(), passwordReset.getConfirmedPassword());
        return ResponseEntity.ok().build();
    }
}