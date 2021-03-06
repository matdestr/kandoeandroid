package be.kdg.teame.kandoe.data.retrofit;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;
import java.util.Date;

import be.kdg.teame.kandoe.data.retrofit.services.SignInService;
import be.kdg.teame.kandoe.util.exceptions.TokenException;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.RetrofitError;

public class TokenAuthenticator implements Authenticator {

    private PrefManager prefManager;

    public TokenAuthenticator(PrefManager prefManager) {
        this.prefManager = prefManager;
    }

    @Override
    public Request authenticate(Proxy proxy, Response response) throws IOException {
        SignInService service = ServiceGenerator.createService(SignInService.class);

        final AccessToken oldAccessToken;
        final AccessToken newAccessToken;

        try {
            oldAccessToken = prefManager.retrieveAccessToken();
            newAccessToken = service.refreshToken(SignInService.GRANT_TYPE_REFRESH, oldAccessToken.getRefreshToken());
            newAccessToken.setDateAcquired(new Date());

            prefManager.saveAccessToken(newAccessToken);

            return response.request().newBuilder()
                    .addHeader("Authorization", newAccessToken.getAuthorizationHeader())
                    .build();
        } catch (RetrofitError error) {
            return null;
        } catch (TokenException e) {
            return null;
        }

    }

    @Override
    public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        return null;
    }
}
