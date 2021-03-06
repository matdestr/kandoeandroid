package be.kdg.teame.kandoe.authentication.signin;


import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;

/**
 * Interface that defines a contract between {@link SignInActivity} and {@link SignInPresenter}.
 * This allows the view and the presenter to communicate with each other.
 * */
public interface SignInContract {

    /**
     * Interface that defines the methods a View should implement
     * @see WebDataView
     * */
    interface View extends WebDataView {

        void showProgressIndicator(boolean active);

        /**
         * This method should be called when an attempt to login fails because the user entered wrong credentials.
         * */
        void showErrorWrongCredentials();

        /**
         * Shows an error dialog if the {@link be.kdg.teame.kandoe.data.retrofit.AccessToken} is invalid.
         */
        void showErrorInvalidToken();

        /**
         * This method switches to the {@link be.kdg.teame.kandoe.dashboard.DashboardActivity}.
         * */
        void showDashboard();

        /**
         * This method gets called when the user taps the sign-up button.
         * @see be.kdg.teame.kandoe.authentication.signin.SignInContract.UserActionsListener#signUp()
         * */
        void showSignUp();

    }

    /**
     * Interface that defines the methods that can be fired because of user interactions
     * @see InjectableUserActionsListener
     * */
    interface UserActionsListener extends InjectableUserActionsListener<SignInContract.View> {
        /**
         * This method gets called when the user taps the sign-in button. It should handle the login process.
         * @param username the username of the user
         * @param password the password of the user
         * */
        void signIn(String username, String password);

        /**
         * This method gets called when the user taps the sign-up button. It should handle the registering process.
         * */
        void signUp();
    }
}
