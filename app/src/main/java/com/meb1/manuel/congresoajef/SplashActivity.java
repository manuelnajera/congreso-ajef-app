package com.meb1.manuel.congresoajef;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

/**
 * Created by Manuel on 7/7/17.
 */


public class SplashActivity extends AppCompatActivity {

    private FirebaseUser firAuth;
    private static boolean calledAlready = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //debug

        if (!calledAlready) {
            getInstance().setPersistenceEnabled(true);

            calledAlready = true;
        }

        // normal


        firAuth = FirebaseAuth.getInstance().getCurrentUser();

        if (firAuth!=null) {
            // User is signed in.
            Intent intent = new Intent(this, MenuPrincipal.class);
            startActivity(intent);
            finish();

        } else {
            // No user is signed in.
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    };


    }

