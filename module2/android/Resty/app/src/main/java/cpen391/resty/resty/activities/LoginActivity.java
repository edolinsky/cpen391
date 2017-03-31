package cpen391.resty.resty.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import cpen391.resty.resty.Objects.User;
import cpen391.resty.resty.R;
import cpen391.resty.resty.dataStore.RestyStore;
import cpen391.resty.resty.serverRequests.RestyLoginRequest;
import cpen391.resty.resty.serverRequests.RestyRequestManager;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyLoginCallback;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameText;
    private EditText passwordText;
    private RestyStore dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // TODO: the request manager singleton needs to be initialized before making requests, is this the best place to do that?
        RestyRequestManager.getInstance().initManager(this);

        usernameText = (EditText) findViewById(R.id.loginNameText);
        passwordText = (EditText) findViewById(R.id.loginPasswordText);

        dataStore = RestyStore.getInstance(this);

    }

    public void loginOnClick(View view){

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        // Store username for later queries.
        dataStore.put(RestyStore.Key.USER, username);
        signIn(username, password);
    }

    public void signIn(String username, String password){
        RestyLoginRequest request = new RestyLoginRequest(loginCallback);
        request.signIn(username, password);
    }

    public void signupOnClick(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    private void onLoginSuccess(User user){
        Intent intent = new Intent(this, HubAuthenticationActivity.class);
        startActivity(intent);
    }

    private void onLoginError(RestyLoginCallback.LoginError error){
        switch (error){
            case UnknownError:
                Log.i("ERROR","Unable to login");
                break;
            default:
                break;
        }
    }

    private RestyLoginCallback loginCallback = new RestyLoginCallback() {
        @Override
        public void loginCompleted(User user) {
            onLoginSuccess(user);
        }

        @Override
        public void loginError(LoginError error) {
            onLoginError(error);
        }
    };

}
