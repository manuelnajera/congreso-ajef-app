package com.meb1.manuel.congresoajef;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class turismoActivity extends AppCompatActivity {
    private ImageView tuxtlaView;
    private ImageView chiapaView;
    private ImageView sancrisView;
    private ImageView miradoresView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turismo);
        tuxtlaView = (ImageView) findViewById(R.id.tuxtlaView);
        tuxtlaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://turismo.tuxtla.gob.mx/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        chiapaView = (ImageView) findViewById(R.id.chiapaView);
        chiapaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.chiapadecorzo.gob.mx/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        sancrisView = (ImageView) findViewById(R.id.sancrisView);
        sancrisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.sancristobal.gob.mx/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        miradoresView = (ImageView) findViewById(R.id.miradorView);
        miradoresView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.turismochiapas.gob.mx/sectur/parque-nacional-can-del-sumidero-miradores"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


    }
}
