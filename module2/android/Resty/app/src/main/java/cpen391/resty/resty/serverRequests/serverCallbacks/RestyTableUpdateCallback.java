package cpen391.resty.resty.serverRequests.serverCallbacks;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import cpen391.resty.resty.Objects.respTable;

/**
 * Created by annal on 2017-04-06.
 */

public interface RestyTableUpdateCallback {
    void updateRetrieved(String table);
    //void tablesRetrieved(String table);
    void updateError(VolleyError error);
}
