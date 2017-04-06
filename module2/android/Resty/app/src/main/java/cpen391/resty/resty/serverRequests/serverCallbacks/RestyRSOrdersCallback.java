package cpen391.resty.resty.serverRequests.serverCallbacks;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;
import cpen391.resty.resty.Objects.RSOrder;

public interface RestyRSOrdersCallback {

    void ordersRetrieved(ArrayList<RSOrder> orders);
    void ordersError(VolleyError error);
}
