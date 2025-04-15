package com.meb1.manuel.congresoajef;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuSecundario extends AppCompatActivity {
    private Button itinerarioButton;
    private Button turismoButton;
    private Button contactoButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_secundario);

        itinerarioButton = (Button) findViewById(R.id.itinerarioButton);
        itinerarioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MenuSecundario.this, itinerarioActivity.class);
                startActivity(intent);

            }
        });
        turismoButton = (Button) findViewById(R.id.turismoButton);
        turismoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuSecundario.this, turismoActivity.class);
                startActivity(intent);
            }
        });


        contactoButton = (Button) findViewById(R.id.contactoButton);
        contactoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenuSecundario.this, contactoActivity.class);
                startActivity(intent);

            }
        });





    }//onCreate
}//MenuSecundario.class
