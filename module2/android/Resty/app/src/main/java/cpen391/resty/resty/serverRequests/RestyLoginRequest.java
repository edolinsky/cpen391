package cpen391.resty.resty.serverRequests;

import android.content.Context;
import android.content.Intent;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import cpen391.resty.resty.Objects.User;
import cpen391.resty.resty.activities.HubAuthenticationActivity;
import cpen391.resty.resty.serverRequests.ServerRequestConstants.Endpoint;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyLoginCallback;
import cpen391.resty.resty.serverRequests.serverCallbacks.ServerCallback;


public class RestyLoginRequest {

    private final RestyLoginCallback loginCallback;

    public RestyLoginRequest(RestyLoginCallback callback){
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
                        serverCallback, serverCallback);
        RestyRequestManager.getInstance().makeRequest(jsObjRequest);

    }

    private final ServerCallback serverCallback = new ServerCallback() {
        @Override
        public void onErrorResponse(VolleyError error) {
            loginCallback.loginError(RestyLoginCallback.LoginError.UnknownError);
        }

        @Override
        public void onResponse(Object response) {
            JSONObject result = (JSONObject) response;
            loginCallback.loginCompleted(null);
        }
    };

}
