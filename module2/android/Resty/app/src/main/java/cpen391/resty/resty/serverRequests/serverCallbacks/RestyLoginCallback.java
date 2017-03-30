package cpen391.resty.resty.serverRequests.serverCallbacks;

import cpen391.resty.resty.Objects.User;

public interface RestyLoginCallback {

    enum LoginError{
        UnknownError
    }

    void loginCompleted(User user);
    void loginError(LoginError error);
}
