package cpen391.resty.resty.serverRequests.serverCallbacks;

import com.google.gson.JsonObject;

public interface RestyMenuCallback{

    enum FetchMenuError{
        UnknownError
    }

    void fetchMenuSuccess(String menu);
    void fetchMenuError(FetchMenuError error);
}
