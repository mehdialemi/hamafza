package ir.co.realtime.disaster.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VerificationCodeException extends RuntimeException {

    public VerificationCodeException(Integer code) {
        super("verification code {" + code + "} is invalid");
    }
}
