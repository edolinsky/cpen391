package cpen391.resty.resty.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.net.HttpURLConnection;

import cpen391.resty.resty.Objects.User;
import cpen391.resty.resty.R;
import cpen391.resty.resty.serverRequests.RestySignupRequest;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestySignupCallback;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    private static final int SERVER_ERROR = 500;

    private EditText usernameText;
    private EditText passwordText;
    private View restaurantIDField;
    private View staffOnlyCheckbox;
    private Switch staffSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameText = (EditText) findViewById(R.id.signupNameText);
        passwordText = (EditText) findViewById(R.id.signupPasswordText);
        restaurantIDField = findViewById(R.id.SignupRestaurantIDText);
        staffOnlyCheckbox = findViewById(R.id.checkBox);
        staffSwitch = (Switch) findViewById(R.id.signupStaffSwitch);

        // Staff fields default to hidden.
        restaurantIDField.setVisibility(View.GONE);
        staffOnlyCheckbox.setVisibility(View.GONE);

        // Show/Hide relevant fields based on status of switch.
        staffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Show restaurant ID field, staff account checkbox.
                    restaurantIDField.setVisibility(View.VISIBLE);
                    staffOnlyCheckbox.setVisibility(View.VISIBLE);
                } else {
                    // Hide restaurant ID field, staff account checkbox.
                    restaurantIDField.setVisibility(View.GONE);
                    staffOnlyCheckbox.setVisibility(View.GONE);
                }
            }
        });

    }

    public void signupOnClick(View view){

        String username = usernameText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        int duration = Toast.LENGTH_LONG;
        CharSequence text;
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

        RestySignupRequest signupRequest = new RestySignupRequest(signupCallback);
        signupRequest.signUp(username, password);
    }

    public void signupOnSuccess(User user){
        Log.d(TAG, "created new account");
    }

    private void handleSignupError(VolleyError error){
        CharSequence text;
        int duration = Toast.LENGTH_LONG;

        switch (error.networkResponse.statusCode){
            case SERVER_ERROR:
                    text =  "Server Error. Please try again.";
                break;
            default:
                try {
                    JsonObject jsonObject = new JsonParser().parse(new String(error.networkResponse.data)).getAsJsonObject();
                    text = jsonObject.get("error").getAsString();
                } catch (JsonParseException e) {
                    text = "Something went wrong. Please try again.";
                }
        }

        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    private RestySignupCallback signupCallback = new RestySignupCallback() {
        @Override
        public void signupCompleted(User user) {
            signupOnSuccess(user);
        }

        @Override
        public void signupError(VolleyError error) {
            handleSignupError(error);
        }
    };

}
