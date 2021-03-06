package be.kdg.teame.kandoe.dashboard;

import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.UserService;
import be.kdg.teame.kandoe.di.Injector;
import be.kdg.teame.kandoe.models.users.User;
import be.kdg.teame.kandoe.util.http.HttpStatus;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This class is responsible for managing and performing the actions that the user can initiate in {@link DashboardActivity}.
 * It implements {@link DashboardContract.UserActionsListener} and notifies the view after completing its actions.
 *
 * @see DashboardContract.UserActionsListener
 * */
public class DashboardPresenter implements DashboardContract.UserActionsListener {

    private DashboardContract.View mDashboardView;
    private final UserService mUserService;
    private final PrefManager mPrefManager;

    @Inject
    public DashboardPresenter(UserService userService, PrefManager prefManager) {
        mUserService = userService;
        mPrefManager = prefManager;
    }

    @Override
    public void setView(@NonNull DashboardContract.View view) {
        mDashboardView = view;
    }

    @Override
    public void loadUserdata() {
        final String username = mPrefManager.retrieveUsername();
        if (username != null) {
            mUserService.getUser(username, new Callback<User>() {
                @Override
                public void success(User user, Response response) {
                    if (user.getProfilePictureUrl() != null)
                        user.setProfilePictureUrl(Injector.getApiBaseUrl().concat("/").concat(user.getProfilePictureUrl()));

                    mDashboardView.showUserdata(user);
                }

                @Override
                public void failure(RetrofitError error) {
                    if (error != null && error.getResponse() != null) {

                        if (error.getResponse().getStatus() == HttpStatus.UNAUTHORIZED)
                            mDashboardView.launchUnauthenticatedRedirectActivity();
                        else
                            mDashboardView.showErrorConnectionFailure("Unable to retrieve profile information for " + username);

                    }
                }
            });
        } else {
            Log.w(getClass().getSimpleName(), "Couldn't load user data because username is null");
        }
    }

    @Override
    public void openProfile() {
        mDashboardView.showProfile();
    }

    @Override
    public void openOrganizations() {
        mDashboardView.showOrganizations();
    }

    @Override
    public void openSessions() {
        mDashboardView.showSessions();
    }

    @Override
    public void signOut() {
        mPrefManager.clearAccessToken();
        mDashboardView.showSignIn();
    }

    @Override
    public void checkUserIsAuthenticated() {
        AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mDashboardView);
    }
}
