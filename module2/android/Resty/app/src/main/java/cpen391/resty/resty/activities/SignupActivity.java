package cpen391.resty.resty.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import cpen391.resty.resty.Objects.User;
import cpen391.resty.resty.R;
import cpen391.resty.resty.serverRequests.RestyRequestManager;
import cpen391.resty.resty.serverRequests.RestySignupRequest;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestySignupCallback;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameText;
    private EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameText = (EditText) findViewById(R.id.signupNameText);
        passwordText = (EditText) findViewById(R.id.signupPasswordText);

    }


    public void signupOnClick(View view){

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        RestySignupRequest signupRequest = new RestySignupRequest(signupCallback);
        signupRequest.signUp(username, password);
    }

    public void signupOnSuccess(User user){
        System.out.println("created new account");
    }

    private void handleSignupError(RestySignupCallback.SignupError error){
        switch (error){
            case unknownError:
                System.out.println("Signup error unhandled");
                break;
            default:
                break;
        }
    }

    private RestySignupCallback signupCallback = new RestySignupCallback() {
        @Override
        public void signupCompleted(User user) {
            signupOnSuccess(user);
        }

        @Override
        public void signupError(SignupError error) {
            handleSignupError(error);
        }
    };

}
