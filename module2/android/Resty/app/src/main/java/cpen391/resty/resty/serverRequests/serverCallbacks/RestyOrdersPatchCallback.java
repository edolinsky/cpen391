package cpen391.resty.resty.serverRequests.serverCallbacks;
import android.app.Activity;

import com.android.volley.VolleyError;

public interface RestyOrdersPatchCallback {
    void ordersUpdateSuccess(Activity activity);
    void ordersUpdateError(VolleyError error);
}
