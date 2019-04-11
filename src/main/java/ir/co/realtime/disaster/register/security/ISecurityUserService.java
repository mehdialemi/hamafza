package ir.co.realtime.disaster.register.security;

public interface ISecurityUserService {

    String validatePasswordResetToken(long id, String token);

}
