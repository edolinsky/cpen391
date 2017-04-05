package cpen391.resty.resty.serverRequests;

import android.nfc.Tag;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import cpen391.resty.resty.Objects.StaffUser;
import cpen391.resty.resty.Objects.User;
import cpen391.resty.resty.dataStore.RestyStore;
import cpen391.resty.resty.serverRequests.ServerRequestConstants.Endpoint;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyLoginCallback;


public class RestyLoginRequest {

    private static final String TAG = "LoginRequest";
    private final RestyLoginCallback loginCallback;
    private RestyStore restyStore;

    public RestyLoginRequest(RestyLoginCallback callback){
        restyStore = RestyStore.getInstance();
        this.loginCallback = callback;
    }

    public void signIn(final String username, final String password){

        final String LOGIN_REQUEST_URL = Endpoint.LOGIN.getUrl();
        JSONObject requestObject = null;


        try{
            requestObject = new JSONObject(User.getLoginJsonObject(username, password));
        }catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Endpoint.LOGIN.getMethod(), LOGIN_REQUEST_URL, requestObject,
                        listener, errorListener);
        RestyRequestManager.getInstance().makeRequest(jsObjRequest);

    }

    private final Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {

            try {
                User resultUser;
                Gson gson = new Gson();

                String userId = response.getString("id");
                restyStore.put(RestyStore.Key.USER_ID, userId);

                String affinity = response.getString("affinity");

                // Note: A switch statement looks nicer here but java is being weird about using strings in switch statements
                if (affinity.matches("staff_only") || affinity.matches("staff")){
                    resultUser = gson.fromJson(response.toString(), StaffUser.class);
                    User.setCurrentUser(resultUser);
                    loginCallback.loginCompleted(resultUser, true);
                }else if (affinity.matches("customer")){
                    resultUser = gson.fromJson(response.toString(), User.class);
                    User.setCurrentUser(resultUser);
                    loginCallback.loginCompleted(resultUser, false);
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };

    private final Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            loginCallback.loginError(error);
        }
    };

}
