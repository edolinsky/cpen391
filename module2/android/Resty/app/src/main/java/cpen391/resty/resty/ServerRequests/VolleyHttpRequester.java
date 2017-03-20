package cpen391.resty.resty.ServerRequests;

import android.content.Context;

import com.android.volley.Response;

public interface VolleyHttpRequester {
    public Response.Listener createSuccessListener(final Context context);
    public Response.ErrorListener createErrorListener();
}
