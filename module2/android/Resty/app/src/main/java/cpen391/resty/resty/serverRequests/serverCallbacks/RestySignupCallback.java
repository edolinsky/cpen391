package cpen391.resty.resty.serverRequests.serverCallbacks;

import com.android.volley.VolleyError;

import cpen391.resty.resty.Objects.User;


public interface RestySignupCallback{
    void signupCompleted(User user, boolean isStaff);
    void signupError(VolleyError error);
}
