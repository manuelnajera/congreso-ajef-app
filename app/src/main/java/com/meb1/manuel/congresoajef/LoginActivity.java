package com.meb1.manuel.congresoajef;


import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;

import android.content.IntentSender;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import android.content.SharedPreferences;
import android.widget.CheckBox;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.CredentialRequest;
import com.google.android.gms.auth.api.credentials.CredentialRequestResult;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.credentials.IdentityProviders;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.google.firebase.database.FirebaseDatabase.getInstance;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private EditText txtEmail;
    private EditText txtPass;
    private EditText txtName;
    private CheckBox saveLoginCheckBox;
    private Button btnLogin;
    private Button btnRegister;
    private boolean SignedWithGoogle;
    private DatabaseReference mDatabase;

    //private HintRequest hintRequest;

    private ProgressDialog mProgress;

    //firebase
    private FirebaseAuth firAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference firDatabaseUsers;

   // private GoogleApiClient mCredentialsApiClient;
    //private CredentialRequest mCredentialRequest;

   //** private static final int RC_HINT = 1;
   // private static final int RC_READ = 2;
    //private static final int RC_SAVE = 3;
    //private Intent data;
    //private FirebaseUser userDatabaseContains;

    //google sign in
    private SignInButton mGoogleBtn;
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "LoginActivity";
    private GoogleApiClient mGoogleApiClient;
    private FirebaseDatabase mFirebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firAuth = FirebaseAuth.getInstance();

        txtEmail = (EditText) findViewById(R.id.email);
        txtPass = (EditText) findViewById(R.id.password);
        txtName = (EditText) findViewById(R.id.txtName);
        txtName.setVisibility(View.INVISIBLE);
        txtName.setHint("");

        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);

        btnLogin = (Button) findViewById(R.id.loginbtnEnter);
        btnLogin.setOnClickListener(this);

        mGoogleBtn = (SignInButton) findViewById(R.id.googlebtn);
        mGoogleBtn.setOnClickListener(this);

        btnRegister = (Button) findViewById(R.id.email_sign_in_button);
        btnRegister.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        mProgress = new ProgressDialog(this);

      //  mCredentialsApiClient = new GoogleApiClient.Builder(this)
               // .addConnectionCallbacks(this)
                //.enableAutoManage(this, this)
                //.addApi(Auth.CREDENTIALS_API)
                //.build();

       // mCredentialRequest = new CredentialRequest.Builder()
              //  .setPasswordLoginSupported(true)
                //.setAccountTypes(IdentityProviders.GOOGLE)
                //.build();




        //HintRequest hintRequest = new HintRequest.Builder()
          //      .setHintPickerConfig(new CredentialPickerConfig.Builder()
            //            .setShowCancelButton(true)
              //          .build())
                //.setEmailAddressIdentifierSupported(true)
                //.setAccountTypes(IdentityProviders.GOOGLE)
                //.build();

    /*   PendingIntent intent =
              Auth.CredentialsApi.getHintPickerIntent(mCredentialsApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), RC_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Could not start hint picker Intent", e);
        }




        Auth.CredentialsApi.save(mCredentialsApiClient, credential).setResultCallback(
                new ResultCallback() {
                    @Override
                    public void onResult(Result result) {
                        Status status = result.getStatus();
                        if (status.isSuccess()) {
                            Log.d(TAG, "SAVE: OK");
                            Toast.makeText(getBaseContext(), "Credentials saved", Toast.LENGTH_SHORT).show();
                        } else {
                            if (status.hasResolution()) {
                                // Try to resolve the save request. This will prompt the user if
                                // the credential is new.
                                try {
                                    status.startResolutionForResult(LoginActivity.this, RC_SAVE);
                                } catch (IntentSender.SendIntentException e) {
                                    // Could not resolve the request
                                    Log.e(TAG, "STATUS: Failed to send resolution.", e);
                                    Toast.makeText(LoginActivity.this, "Save failed", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Request has no resolution
                                Toast.makeText(LoginActivity.this, "Save failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });*/


        // ---------- GOOGLE SIGN IN ------------

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    /*private void onCredentialRetrieved(Credential credential) {
        String accountType = credential.getAccountType();
        if (accountType == null) {
            // Sign the user in with information from the Credential.
            firAuth.signInWithEmailAndPassword(credential.getId(),credential.getPassword());
        } else if (accountType.equals(IdentityProviders.GOOGLE)) {
            // The user has previously signed in with Google Sign-In. Silently
            // sign in the user with the same ID.
            // See https://developers.google.com/identity/sign-in/android/
            GoogleSignInOptions gso =
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .build();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .setAccountName(credential.getId())
                    .build();
            OptionalPendingResult<GoogleSignInResult> opr =
                    Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            // ...
        }
    }

    private void resolveResult(Status status) {
        if (status.getStatusCode() == CommonStatusCodes.RESOLUTION_REQUIRED) {
            // Prompt the user to choose a saved credential; do not show the hint
            // selector.
            try {
                status.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, "STATUS: Failed to send resolution.", e);
            }
        } else {
            // The user must create an account or sign in manually.
            Log.e(TAG, "STATUS: Unsuccessful credential request.");
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == RC_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                Intent intent;
                // Check for the user ID in your user database.
                if (userDatabaseContains(credential.getId())){
                    txtEmail.setText(credential.getId());
                    txtPass.setText(credential.getPassword());
                } else {
                    intent = new Intent(this, Register_user.class);
                    intent.putExtra("emailEscrito", txtEmail.getText().toString());
                    intent.putExtra("passwordEscrito",txtPass.getText().toString());
                    startActivity(intent);
                }
                intent.putExtra("com.meb1.manuel.congresoajef.SIGNIN_HINTS", credential);
                startActivity(intent);
            } else {
                Log.e(TAG, "Hint Read: NOT OK");
                Toast.makeText(this, "Hint Read Failed", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == RC_READ) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                onCredentialRetrieved(credential);
            } else {
                Log.e(TAG, "Credential Read: NOT OK");
                Toast.makeText(this, "Credential Read Failed", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == RC_SAVE) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "SAVE: OK");
                Toast.makeText(this, "Credentials saved", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "SAVE: Canceled by user");
            }
        }


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);*/
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);


            mProgress.setMessage("Iniciando sesión...");
            mProgress.show();
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed, update UI appropriately
                // ...

                mProgress.dismiss();
                Toast.makeText(LoginActivity.this, "Error al ingresar", Toast.LENGTH_LONG).show();

            }
        }


    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.loginbtnEnter) {

            checkLogin();
        } else if (view.getId() == R.id.googlebtn) {

            signInGoogle();
        } else if (view.getId() == R.id.email_sign_in_button){
            Intent intent = new Intent (getBaseContext(),Register_user.class);
            intent.putExtra("emailEscrito", txtEmail.getText().toString());
            intent.putExtra("passwordEscrito",txtPass.getText().toString());
            startActivity(intent);
        }



    }



    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Autenticación Fallida.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            mProgress.dismiss();
                            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                            final String usuarioID = currentFirebaseUser.getUid().toString();
                            mDatabase.child("usuariosAuth").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if(!dataSnapshot.hasChild(usuarioID)) {
                                        String _name = acct.getDisplayName();
                                        String _email = acct.getEmail();
                                        String _profilePicture = acct.getPhotoUrl().toString();
                                        mDatabase.child("usuariosAuth").child(usuarioID).child("Nombre").setValue(_name);
                                        mDatabase.child("usuariosAuth").child(usuarioID).child("Email").setValue(_email);
                                        mDatabase.child("usuariosAuth").child(usuarioID).child("FotoPerfil").setValue(_profilePicture);

                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });




                            checkUserExist();
                            SignedWithGoogle = true;
                        }
                        // ...


                    }
                });
    }
/*
    GoogleSignInAccount gsa = GoogleSignInResult.getSignInAccount();
    Credential credential = new Credential.Builder(gsa.getEmail())
            .setAccountType(IdentityProviders.GOOGLE)
            .setName(gsa.getDisplayName())
            .setProfilePictureUri(gsa.getPhotoUrl())
            .build();


    CredentialsApi credentialsApi = CredentialsApi.request(mCredentialsApiClient, mCredentialRequest).setResultCallback(
            new ResultCallback<CredentialRequestResult>() {
                @Override
                public void onResult(CredentialRequestResult credentialRequestResult) {
                    if (credentialRequestResult.getStatus().isSuccess()) {
                        // See "Handle successful credential requests"
                        onCredentialRetrieved(credentialRequestResult.getCredential());
                    } else {
                        // See "Handle unsuccessful and incomplete credential requests"
                        resolveResult(credentialRequestResult.getStatus());
                    }
                }
            });
*/
    private void checkLogin() {

        String _email = txtEmail.getText().toString().trim();
        String _pass = txtPass.getText().toString().trim();

        if (!TextUtils.isEmpty(_email) && !TextUtils.isEmpty(_pass)) {

            mProgress.setMessage("Comprobando");
            mProgress.show();

            firAuth.signInWithEmailAndPassword(_email, _pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        mProgress.dismiss();

                        checkUserExist();
                        SignedWithGoogle =false;

                       /* Credential credential = new Credential.Builder(_email)
                                .setPassword(_pass)
                                .build();*/
                    } else {

                        mProgress.dismiss();
                        Toast.makeText(LoginActivity.this, "Error al ingresar", Toast.LENGTH_LONG).show();
                    }
                }
            });


        } else {
            Toast.makeText(LoginActivity.this, "Completa todos los campos", Toast.LENGTH_LONG).show();
        }
    }



    private void checkUserExist() {

        firDatabaseUsers = getInstance().getReference().child("Users");
        firDatabaseUsers.keepSynced(true);

        if (firAuth.getCurrentUser() != null) {


            final String _user_id = firAuth.getCurrentUser().getUid().toString();

            firDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if (_user_id!= null) {

                        if (!firAuth.getCurrentUser().isEmailVerified() && !SignedWithGoogle)
                        {
                            // user is verified, so you can finish this activity or send user to activity which you want.

                            Toast.makeText(LoginActivity.this, "Revisa el correo de verificación en tu bandeja", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();

                            ;
                        } else {

                        Toast.makeText(LoginActivity.this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show();
                        Intent iSetup = new Intent(LoginActivity.this, MenuPrincipal.class);
                        iSetup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(iSetup);
                        }

                    }else {

                        Toast.makeText(LoginActivity.this, "Revisa el correo de verificación en tu bandeja id", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();

                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}