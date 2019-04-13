package ir.co.realtime.disaster.auth.jwt;

public class JwtAuthenticationResponse {
    public final static String TOKEN_TYPE = "Bearer";

    private String accessToken;
    private String tokenType = TOKEN_TYPE;

    public JwtAuthenticationResponse() {

    }

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public String toString() {
        return tokenType + " " + accessToken;
    }
}
