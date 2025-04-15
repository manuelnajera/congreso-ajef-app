package com.meb1.manuel.congresoajef;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class contactoActivity extends AppCompatActivity {
    private TextView taxiJaguar;
    private TextView taxi5Estrellas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
        taxiJaguar= (TextView) findViewById(R.id.taxiJaguar);
        taxiJaguar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(contactoActivity.this,R.style.MyAlertDialogStyle);
                builder.setCancelable(true);
                builder.setTitle("Radio Taxi");
                builder.setMessage("¿Desea llamar a Radio Taxi Jaguar?");
                builder.setPositiveButton(R.string.aceptar,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            dialContactPhone("+529616126786");

                            }
                        });
                builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        taxi5Estrellas = (TextView) findViewById(R.id.taxi5estrellas);
        taxi5Estrellas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(contactoActivity.this,R.style.MyAlertDialogStyle);
                builder.setCancelable(true);
                builder.setTitle("Radio Taxi");
                builder.setMessage("¿Desea llamar a Radio Taxi 5 Estrellas");
                builder.setPositiveButton(R.string.aceptar,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialContactPhone("+529616168010");

                            }
                        });
                builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }
}
