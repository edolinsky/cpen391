package cpen391.resty.resty.serverRequests;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cpen391.resty.resty.Objects.RSOrder;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyRSOrdersCallback;
import cpen391.resty.resty.serverRequests.ServerRequestConstants.Endpoint;

public class RestyRSOrdersRequest {
    public static final String ORDERS = "cpen391.resty.resty.ORDERS";

    private final RestyRSOrdersCallback ordersCallback;

    public RestyRSOrdersRequest(RestyRSOrdersCallback callback){
        this.ordersCallback = callback;
    }

    public void getOrders(final String restaurant_id){

        String url = String.format(Endpoint.VIEW_ORDERS.getUrl(), restaurant_id, "open");
        StringRequest request = new StringRequest(Endpoint.VIEW_ORDERS.getMethod(), url,
                listener, errorListener);

        request.setRetryPolicy(new DefaultRetryPolicy(50000, 5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RestyRequestManager.getInstance().makeRequest(request);
    }

    private final Response.Listener<String> listener = new Response.Listener<String>(){
        @Override
        public void onResponse(String response) {

            JsonParser parser = new JsonParser();
            JsonObject jsonMenu = (JsonObject)parser.parse(response);

            Gson gson = new Gson();
            Type rsorderType = new TypeToken<List<RSOrder>>(){}.getType();
            ArrayList<RSOrder> orders = gson.fromJson(jsonMenu.getAsJsonArray("orders"), rsorderType);
            ordersCallback.ordersRetrieved(orders);
        }
    };

    private final Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            ordersCallback.ordersError(error);
        }
    };
}