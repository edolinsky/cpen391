package cpen391.resty.resty.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import cpen391.resty.resty.dataStore.RestyStore;
import cpen391.resty.resty.serverRequests.RestyUpdateFCMIDRequest;


public class RestyFirebaseIIDService extends FirebaseInstanceIdService {
    private static final String TAG = "RestyFirebaseIID";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // Send the Instance ID token to our app server.
        sendRegistrationToServer(refreshedToken);
    }

    void sendRegistrationToServer(String token) {
        RestyStore restyStore = RestyStore.getInstance();
        String user = restyStore.getString(RestyStore.Key.USER);

        RestyUpdateFCMIDRequest updateRequest = new RestyUpdateFCMIDRequest();
        updateRequest.updateFCMID(user, token, this);
    }
}
