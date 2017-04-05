package cpen391.resty.resty.serverRequests.serverCallbacks;

import com.android.volley.VolleyError;

import cpen391.resty.resty.Objects.User;

public interface RestyLoginCallback {

    void loginCompleted(User user, boolean isStaff);
    void loginError(VolleyError error);
}
