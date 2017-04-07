package cpen391.resty.resty.serverRequests;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cpen391.resty.resty.Objects.Mapping;
import cpen391.resty.resty.Objects.SingleMapping;
import cpen391.resty.resty.Objects.Table;
import cpen391.resty.resty.Objects.respTable;
import cpen391.resty.resty.Objects.tempTable;
import cpen391.resty.resty.dataStore.RestyStore;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyTableUpdateCallback;

/**
 * Created by anna on 2017-04-06.
 */

public class RestyTableUpdateRequest {
    private final RestyTableUpdateCallback orderCallback;
    private static final String TAG = "OrderRequest";
    private RestyStore restyStore;

    public RestyTableUpdateRequest(RestyTableUpdateCallback callback){

        this.orderCallback = callback;
        restyStore = RestyStore.getInstance();
    }

    public void changeServer(List<SingleMapping> mappings){

        final String REQUEST_URL = ServerRequestConstants.Endpoint.EDIT_TABLE_ATTENDANTS.getUrl();
        JSONObject requestObject = null;

        //String restaurant_id = restyStore.getString(RestyStore.Key.RESTAURANT_ID);
        String restaurant_id = "test_resto";
        Log.d("restauarant id", restaurant_id);

        try{
            requestObject = new JSONObject(new Mapping(restaurant_id, mappings).toJson());
        }catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("OBJECT", requestObject.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (ServerRequestConstants.Endpoint.EDIT_TABLE_ATTENDANTS.getMethod(), REQUEST_URL, requestObject,
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
                orderId = response.getString("mappings");
            } catch (JSONException e) {
                Log.e(TAG, "Order ID not included in response.");
            }

            // Store Order ID in data store.
            //restyStore.put(RestyStore.Key.ORDER_ID, orderId);

            // Trigger action in activity.
            orderCallback.updateRetrieved(orderId);
        }
    };

    private final Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("Order Error", error.toString());
            orderCallback.updateError(error);
        }
    };

}
