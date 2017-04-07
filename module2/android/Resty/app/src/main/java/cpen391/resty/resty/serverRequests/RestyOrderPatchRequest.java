package cpen391.resty.resty.serverRequests;

import android.app.Activity;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.List;

import cpen391.resty.resty.Objects.Order;
import cpen391.resty.resty.Objects.RSOrder;
import cpen391.resty.resty.Objects.Table;
import cpen391.resty.resty.dataStore.RestyStore;
import cpen391.resty.resty.menu.RestaurantMenuItem;
import cpen391.resty.resty.serverRequests.ServerRequestConstants.Endpoint;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyOrdersPatchCallback;

public class RestyOrderPatchRequest {

    private final RestyOrdersPatchCallback orderPatchCallback;
    private static final String TAG = "OrderPatch";
    private RestyStore restyStore;
    private final Activity activity;

    public RestyOrderPatchRequest(RestyOrdersPatchCallback callback){
        this.activity = null;
        this.orderPatchCallback = callback;
        restyStore = RestyStore.getInstance();
    }

    public RestyOrderPatchRequest(RestyOrdersPatchCallback callback, Activity activity){
        this.activity = activity;
        this.orderPatchCallback = callback;
        restyStore = RestyStore.getInstance();
    }

    public void patchOrders(RSOrder[] orders, String restaurantID){

        final String REQUEST_URL = Endpoint.UPDATE_ORDERS.getUrl();
        JSONObject requestObject = null;

        try{
            requestObject = RSOrder.createJSONOrdersPatch(orders, restaurantID);
        }catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Endpoint.UPDATE_ORDERS.getMethod(), REQUEST_URL, requestObject,
                        listener, errorListener);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RestyRequestManager.getInstance().makeRequest(jsObjRequest);

    }

    private final Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            orderPatchCallback.ordersUpdateSuccess(activity);
        }
    };

    private final Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("Order Error", error.toString());
            orderPatchCallback.ordersUpdateError(error);
        }
    };
}
