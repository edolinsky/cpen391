package cpen391.resty.resty.serverRequests;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import cpen391.resty.resty.activities.MenuActivity;
import cpen391.resty.resty.serverRequests.ServerRequestConstants.Endpoint;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyMenuCallback;
import cpen391.resty.resty.serverRequests.serverCallbacks.ServerCallback;

public class RestyMenuRequest {

    public static final String MENU = "cpen391.resty.resty.MENU";

    private final RestyMenuCallback menuCallback;

    public RestyMenuRequest(RestyMenuCallback callback){
        this.menuCallback = callback;
    }

    public void getMenu(final String restaurant_id, @Nullable String item_type){

        if(item_type == null) {
            item_type = "";
        }

        String url = String.format(Endpoint.MENU.getUrl(), restaurant_id);
        StringRequest request = new StringRequest(Endpoint.MENU.getMethod(), url,
                callback, callback);

        request.setRetryPolicy(new DefaultRetryPolicy(50000, 5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RestyRequestManager.getInstance().makeRequest(request);

    }

    private ServerCallback callback = new ServerCallback() {
        @Override
        public void onErrorResponse(VolleyError error) {
            menuCallback.fetchMenuError(RestyMenuCallback.FetchMenuError.UnknownError);
        }

        @Override
        public void onResponse(Object response) {
            String result = (String) response;
            menuCallback.fetchMenuSuccess(result);
        }
    };

}
