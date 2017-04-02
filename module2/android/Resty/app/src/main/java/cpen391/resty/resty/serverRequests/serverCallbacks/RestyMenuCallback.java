package cpen391.resty.resty.serverRequests.serverCallbacks;

import com.android.volley.VolleyError;

public interface RestyMenuCallback{

    void fetchMenuSuccess(String menu);
    void fetchMenuError(VolleyError error);
}
