package ir.co.realtime.disaster.auth.model;

import javax.validation.constraints.NotNull;

public class PasswordReset {

    @NotNull
    private Integer code;

    @NotNull
    private String password;

    @NotNull
    private String confirmedPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
