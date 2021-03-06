package be.kdg.teame.kandoe.session.gamelauncher;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;

/**
 * Interface contract for the MVP architecture of the Game launcher structure
 */

public interface SessionGameLauncherContract {

    interface View extends AuthenticatedContract.View, WebDataView {
        void setProgressIndicator(boolean active);
    }

    interface UserActionsListener extends InjectableUserActionsListener<View>, AuthenticatedContract.UserActionsListener {
        void launchGame(int sessionId);
    }
}
