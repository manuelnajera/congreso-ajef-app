package com.meb1.manuel.congresoajef;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.view.SurfaceView;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;


public class Identificador extends AppCompatActivity {
    private static final String TAG = Identificador.class.getName();
    private TextView textView;
    private SurfaceView mySurfaceView;
    private lector_qr lectorQr;
    private FirebaseDatabase mFirebaseDatabase;
    public FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDataBase;
    String isAdminInfo = "isAdminInfo";
    String isAdmin;



    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    final String usuarioID = currentFirebaseUser.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identificador);

        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        final Button btn = (Button) findViewById(R.id.flashButton);
        btn.startAnimation(animation);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.clearAnimation();


            }
        });

        final String isAdmin = getIntent().getStringExtra("isadminInfo");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
        textView = (TextView) findViewById(R.id.code_info);

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.keepSynced(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
            }
        }


        Button restartbtn = (Button) findViewById(R.id.btn_restart_activity);
        restartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

        // Setup SurfaceView
        // -----------------
        mySurfaceView = (SurfaceView) findViewById(R.id.camera_view);

        // Init QREader
        // ------------
        lectorQr = new lector_qr.Builder(this, mySurfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                Log.d("Lector", "Value : " + data);
                textView.post(new Runnable() {

                    @Override
                    public void run() {

                        DatabaseReference idData = mDataBase.child("usuarios");
                        idData.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(!dataSnapshot.child(data).exists()) {

                                    textView.setText(R.string.escaneo_incorrecto);
                                    lectorQr.stop();
                                } else {//debug

                                    if(isAdmin.equals("true")) {
                                        textView.setText(R.string.escaneo_correcto);
                                        Intent intent = new Intent(getBaseContext(), InfoRegistro.class);
                                        intent.putExtra("datoescaneado", data);
                                        intent.putExtra("isadminInfo",isAdmin);
                                        startActivity(intent);
                                        lectorQr.stop();

                                    }else {
                                        Intent intent = new Intent(getBaseContext(),MenuPrincipal.class);
                                        mDataBase.child("usuariosAuth").child(usuarioID).child("userKey").setValue(data);
                                        intent.putExtra("datoescaneado",data);
                                        startActivity(intent);
                                        lectorQr.stop();
                                        recreate();
                                    }

                                    //debug
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }

                });
            }
        }).facing(lector_qr.BACK_CAM)
                .enableAutofocus(true)
                .height(mySurfaceView.getHeight())
                .width(mySurfaceView.getWidth())
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Init and Start with SurfaceView
        // -------------------------------
        lectorQr.initAndStart(mySurfaceView);


    }

    @Override
    protected void onPause() {
        super.onPause();

        // Cleanup in onPause()
        // --------------------
        lectorQr.releaseAndCleanup();
    }






    }

