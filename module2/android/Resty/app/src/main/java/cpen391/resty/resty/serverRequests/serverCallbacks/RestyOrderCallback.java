package cpen391.resty.resty.serverRequests.serverCallbacks;


public interface RestyOrderCallback {

    enum OrderError{
        UnknownError
    }

    void orderComplete();
    void orderError(OrderError error);
}
