package cpen391.resty.resty.serverRequests.serverCallbacks;
import com.android.volley.VolleyError;

public interface RestyOrdersPatchCallback {
    void ordersUpdateSuccess();
    void ordersUpdateError(VolleyError error);
}
