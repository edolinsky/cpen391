package cpen391.resty.resty.serverRequests.serverCallbacks;


import com.android.volley.VolleyError;

public interface RestyOrderCallback {

    void orderComplete();
    void orderError(VolleyError error);
}
