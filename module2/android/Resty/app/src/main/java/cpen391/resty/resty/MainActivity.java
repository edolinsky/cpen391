package cpen391.resty.resty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cpen391.resty.resty.ServerRequests.RestySigninRequest;

import static com.android.volley.Request.Method.HEAD;

public class MainActivity extends AppCompatActivity {

    private EditText usernameText;
    private EditText passwordText;
    private Button loginButton;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameText = (EditText) findViewById(R.id.loginNameText);
        passwordText = (EditText) findViewById(R.id.loginPasswordText);
        loginButton = (Button) findViewById(R.id.loginButton);
        signupButton = (Button) findViewById(R.id.loginSignupButton);
    }

    public void loginOnClick(View view){

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        signIn(username, password);
    }

    public void signIn(String username, String password){
        RestySigninRequest request = new RestySigninRequest();
        request.signIn(username, password, this);
    }

    public void signupOnClick(View view){


        // pretend sign-in request was successful
        // switch to main screen

        Intent intent = new Intent(this, HubAuthenticationActivity.class);
        startActivity(intent);

    }

}
