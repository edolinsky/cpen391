package cpen391.resty.resty.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.common.hash.Hashing;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.nio.charset.StandardCharsets;

import cpen391.resty.resty.Objects.User;
import cpen391.resty.resty.R;
import cpen391.resty.resty.dataStore.RestyStore;
import cpen391.resty.resty.serverRequests.RestyLoginRequest;
import cpen391.resty.resty.serverRequests.RestyRequestManager;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyLoginCallback;

public class LoginActivity extends AppCompatActivity {

    private static final int SERVER_ERROR = 500;

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

        String username = usernameText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        int duration = Toast.LENGTH_LONG;
        CharSequence text;

        // Check for valid username and password.
        if(!LoginActivity.isValidEmail(username)) {
            text = "Invalid email address.";
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
            return;
        } else if (password.isEmpty()) {
            text = "Please enter a password.";
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
            return;
        }

        final String hashedPassword = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();

        // Store username for later queries.
        dataStore.put(RestyStore.Key.USER, username);

        // Send log in request.
        signIn(username, hashedPassword);
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
        String affinity = user.getAffinity();
        Intent intent;
        switch (affinity){
            case "customer":
                intent = new Intent(this, HubAuthenticationFragment.class);
                break;
            case "staff":
                intent = new Intent(this, StaffPickusageActivity.class);
                break;
            case "staff_only":
                intent = new Intent(this, StaffMainActivity.class);
                break;
            default:
                throw new IllegalArgumentException();
        }
        startActivity(intent);
    }

    private void onLoginError(VolleyError error){
        CharSequence text;
        int duration = Toast.LENGTH_LONG;

        switch (error.networkResponse.statusCode){
            case SERVER_ERROR:
                text =  "Server Error. Please try again.";
                break;
            default:
                try {
                    JsonObject jsonObject = new JsonParser()
                            .parse(new String(error.networkResponse.data)).getAsJsonObject();
                    text = jsonObject.get("error").getAsString();
                } catch (JsonParseException e) {
                    text = "Something went wrong. Please try again.";
                }
        }

        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    private RestyLoginCallback loginCallback = new RestyLoginCallback() {
        @Override
        public void loginCompleted(User user, boolean isStaff) {
            onLoginSuccess(user);
        }

        @Override
        public void loginError(VolleyError error) {
            onLoginError(error);
        }
    };

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
