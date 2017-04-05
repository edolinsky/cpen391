package cpen391.resty.resty.serverRequests;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cpen391.resty.resty.activities.HubAuthenticationActivity;

public class RestyRequestManager{

    // the global request manager
    private static RestyRequestManager instance = new RestyRequestManager();

    // the request queue
    private RequestQueue mRequestQueue = null;

    // privatize constructer to implement as singleton
    private RestyRequestManager(){}

    public static RestyRequestManager getInstance(){ return instance;}

    public void initManager(Context context){

        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
    }

    public void makeRequest(Request request) {

        if (mRequestQueue == null)
            System.out.println("Make sure initManager is called before making requests");
        mRequestQueue.add(request);
    }
}
