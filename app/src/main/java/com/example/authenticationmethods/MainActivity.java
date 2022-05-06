package com.example.authenticationmethods;

import androidx.appcompat.app.AppCompatActivity;


import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import androidx.annotation.NonNull;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Initialize variables

    private static final int RC_SIGN_IN = 101;
    GoogleSignInClient mGoogleSignInClient;

    private CallbackManager callbackManager;

    GoogleSignInClient googleSignInClient;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    ProgressDialog progressDialog;

    EditText Email, Password;
    TextView ForgotPassword, CreateAccount;
    Button Login, SignUp;
    ImageView Google, Facebook, Phone, Twitter, Github,show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Google Account...");


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        FacebookSdk.sdkInitialize(getApplicationContext());

        googleSigninAuth();



        initViews();

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("Message", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Message", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Message", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void googleSigninAuth() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("138349092343-bt2nmej1lsgl93ieoqe9bvl204ot8oc7.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void initViews() {


        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        Email = findViewById(R.id.et_email);
        Password = findViewById(R.id.et_password);
        Google = findViewById(R.id.Google);
        Facebook = findViewById(R.id.Facebook);
        Phone = findViewById(R.id.Phone);
        Twitter = findViewById(R.id.Twitter);
        Github = findViewById(R.id.Github);
        Login = findViewById(R.id.loginButton);
        ForgotPassword = findViewById(R.id.forgot_Password);
        CreateAccount =findViewById(R.id.createAccount);

        show = findViewById(R.id.showpass);
        show.setImageResource(R.drawable.hide);


        Google.setOnClickListener(this);
        Facebook.setOnClickListener(this);
        Phone.setOnClickListener(this);
        Login.setOnClickListener(this);
        Twitter.setOnClickListener(this);
        Github.setOnClickListener(this);
        show.setOnClickListener(this);

    }


    public void facebookauth(){

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    public void onClick(View v) {
        if (v == Login) {
            startActivity(new Intent(MainActivity.this, GoogleSignInActivity.class));
        }
//        else if (v == Login) {
//            String email = Email.getText().toString().replace(" ","");
//            String password = Password.getText().toString();
//
//            if (password.equals("") || email.equals("")) {
//                Toast.makeText(getApplicationContext(), "Please Enter the Required Fields", Toast.LENGTH_SHORT).show();
//            } else {
//                createUserAccount(Email, Password);
//
//                sharedPref = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPref.edit();
//                editor.putString("pref_username",Email);
//                editor.putString("password",Password);
//                editor.apply();
//            }
//        }
        else if (v == show){
            if (Password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                show.setImageResource(R.drawable.hide);
            }else {
                Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                show.setImageResource(R.drawable.show);
            }
        }

        else if (v == Google){
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        else if (v == Facebook){
            facebookauth();
        }

        else if (v==SignUp){
            Intent intent = new Intent(MainActivity.this,GoogleSignInActivity.class);
            startActivity(intent);
        }

        else if (v == Phone){

        }

        else if (v == Twitter){

        }

        else if (v == Github){

        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
               // Log.d("testdebug", account.getIdToken());
                firebaseAuthWithGoogle(account.getIdToken());
                progressDialog.show();
            } catch (ApiException e) {
                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
            }
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Account is Created is Successfully", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Some Error Occurred" , Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        Intent intent = new Intent(MainActivity.this, GoogleSignInActivity.class);
        startActivity(intent);

    }

}
