package cpen391.resty.resty.serverRequests;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.json.JSONObject;

import cpen391.resty.resty.serverRequests.ServerRequestConstants.Endpoint;

public class RestyUpdateFCMIDRequest extends RestyRequest {
    private static final String TAG = "UpdateFCMIDRequest";

    private static class UpdateFCMIDRequest {
        String user;
        String android_reg_id;

        UpdateFCMIDRequest(String user, String token) {
            this.user = user;
            this.android_reg_id = token;
        }

        String toJson() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }

    public void updateFCMID(final String username, final String token, final Context context) {
        final String UPDATE_FCM_ID_REQUEST_URL = Endpoint.UPDATE_FCM_ID.getUrl();
        UpdateFCMIDRequest request = new UpdateFCMIDRequest(username, token);

        String jsonRequest = request.toJson();
        JSONObject requestObject = null;

        try{
            requestObject = new JSONObject(jsonRequest);
        }catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Endpoint.UPDATE_FCM_ID.getMethod(), UPDATE_FCM_ID_REQUEST_URL, requestObject,
                createSuccessListener(context), createErrorListener());
        makeRequest(jsonObjectRequest, context);

    }

    @Override
    public Response.Listener createSuccessListener(Context context) {
        Log.i(TAG, "FCM ID update request successful.");
        return null;
    }

    @Override
    public Response.ErrorListener createErrorListener() {
        Log.i(TAG, "FCM ID update request failed.");
        return null;
    }
}
