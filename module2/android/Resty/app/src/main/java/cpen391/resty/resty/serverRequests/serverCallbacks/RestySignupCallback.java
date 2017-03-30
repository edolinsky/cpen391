package cpen391.resty.resty.serverRequests.serverCallbacks;

import cpen391.resty.resty.Objects.User;


public interface RestySignupCallback{

    public enum SignupError{
        unknownError
    }

    void signupCompleted(User user);
    void signupError(SignupError error);
}
