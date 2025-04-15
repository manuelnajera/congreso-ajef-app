package com.meb1.manuel.congresoajef;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Register_user extends AppCompatActivity implements View.OnClickListener {

    private EditText txtName;
    private EditText txtEmail;
    private EditText txtPass;
    private ProgressDialog mProgress;
    private FirebaseAuth firAuth;
    private SignInButton mGoogleBtn;
    private Button btnLogin;
    private Button btnRegister;
    private DatabaseReference mDataBase;
    private DatabaseReference usuarioAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String emailescrito = getIntent().getStringExtra("emailEscrito");
        String passescrito = getIntent().getStringExtra("passwordEscrito");
        firAuth = FirebaseAuth.getInstance();
        txtName = (EditText) findViewById(R.id.txtName);
        txtEmail = (EditText) findViewById(R.id.email);
        txtEmail.setText(emailescrito);
        txtPass = (EditText) findViewById(R.id.password);
        txtPass.setText(passescrito);
        txtName.setVisibility(View.VISIBLE);
        txtName.setHint(R.string.nombre_completo);
        btnLogin = (Button) findViewById(R.id.loginbtnEnter);
        btnLogin.setVisibility(View.INVISIBLE);
        mGoogleBtn = (SignInButton) findViewById(R.id.googlebtn);
        mGoogleBtn.setVisibility(View.INVISIBLE);
        btnRegister = (Button) findViewById(R.id.email_sign_in_button);
        btnRegister.setOnClickListener(this);
        btnRegister.setHint(R.string.nombre_completo);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        usuarioAuth = mDataBase.child("usuariosAuth");
        mProgress = new ProgressDialog(this);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.email_sign_in_button) {

            registerUser();
        }
    }
    private void registerUser() {
        final String _name = txtName.getText().toString().trim();
        final String _email = txtEmail.getText().toString().trim();
        String _pass = txtPass.getText().toString().trim();

        if (!TextUtils.isEmpty(_name) && !TextUtils.isEmpty(_email) && !TextUtils.isEmpty(_pass)) {

            mProgress.setMessage("Registrando ...");
            mProgress.show();

            firAuth.createUserWithEmailAndPassword(_email, _pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        //ususario ID
                        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                        final String usuarioID = currentFirebaseUser.getUid().toString();
                        usuarioAuth.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(!dataSnapshot.hasChild(usuarioID)) {
                                    usuarioAuth.child(usuarioID).child("Nombre").setValue(_name);
                                    usuarioAuth.child(usuarioID).child("Email").setValue(_email);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        sendVerificationEmail();
                        Toast.makeText(Register_user.this, "Revisa el correo de verificación para poder iniciar sesión",
                                Toast.LENGTH_SHORT).show();



                        finish();
                        //the user has been registered

                    } else {
                        Toast.makeText(Register_user.this, "Ya hay una cuenta con este correo",
                                Toast.LENGTH_SHORT).show();
                       finish();
                    }
                }
            });
        }
    }


    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent


                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }

}
