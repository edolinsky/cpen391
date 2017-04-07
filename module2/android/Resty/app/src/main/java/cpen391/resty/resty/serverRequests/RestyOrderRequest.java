package cpen391.resty.resty.serverRequests;


import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cpen391.resty.resty.Objects.Order;
import cpen391.resty.resty.Objects.Table;
import cpen391.resty.resty.dataStore.RestyStore;
import cpen391.resty.resty.Objects.RestaurantMenuItem;
import cpen391.resty.resty.serverRequests.ServerRequestConstants.Endpoint;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyOrderCallback;

public class RestyOrderRequest {

    private final RestyOrderCallback orderCallback;
    private static final String TAG = "OrderRequest";
    private RestyStore restyStore;

    public RestyOrderRequest(RestyOrderCallback callback){

        this.orderCallback = callback;
        restyStore = RestyStore.getInstance();
    }

    public void order(List<RestaurantMenuItem> order, String userId, Table table){

        final String REQUEST_URL = Endpoint.PLACE_ORDER.getUrl();
        JSONObject requestObject = null;

        try{
            requestObject = new JSONObject(Order.create(order, userId, table).toJson());
        }catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Endpoint.PLACE_ORDER.getMethod(), REQUEST_URL, requestObject,
                        listener, errorListener);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RestyRequestManager.getInstance().makeRequest(jsObjRequest);

    }

    private final Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            String orderId = "";
            try {
                orderId = response.getString("order_id");
            } catch (JSONException e) {
                Log.e(TAG, "Order ID not included in response.");
            }

            // Store Order ID in data store.
            restyStore.put(RestyStore.Key.ORDER_ID, orderId);

            // Trigger action in activity.
            orderCallback.orderComplete();
        }
    };

    private final Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("Order Error", error.toString());
            orderCallback.orderError(error);
        }
    };

}
