package com.meb1.manuel.congresoajef;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MenuPrincipal extends AppCompatActivity implements View.OnClickListener {

    public String isAdmin;
    public boolean isAdminEvento;
    public boolean checkIfDataExist2;
    public DatabaseReference usuarioVinculo;

    private ImageView fotoView;
    private Button organizadorButton;
    private Button menuButton;
    private TextView nombreView;
    private Button logoutButton;

    //vincularperfilView
    private RelativeLayout vincularperfilView;
    private Button vincularButton;
    private TextView vincularInfo;
    private TextView vincularText;

    //perfilInfoView
    private RelativeLayout perfil_info;
    private TextView nombreViewIntro;
    private TextView habViewIntro;
    private CheckBox oratoriaBox;
    private CheckBox declamacionBox;
    private CheckBox ajedrezBox;
    private CheckBox dominoBox;
    private CheckBox cuentoBox;
    private CheckBox futbolBox;
    private CheckBox basquetBox;
    private CheckBox cantoBox;
    private CheckBox premioBox;
    public boolean datoVinculado;


    private DatabaseReference mDatabase;
    private DatabaseReference usuarioAuth;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    final String usuarioID = currentFirebaseUser.getUid().toString();

    private final Context mContext = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        usuarioAuth = mDatabase.child("usuariosAuth").child(usuarioID);
        vincularInfo = (TextView) findViewById(R.id.vincular_info);
        vincularText = (TextView) findViewById(R.id.VinculaCuentaText);


        usuarioAuth.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild("userKey")) {
                    usuarioAuth.child("userKey").setValue("default");
                    datoVinculado = false;

                } else {
                    final String valorKey = dataSnapshot.child("userKey").getValue().toString();
                    mDatabase.child("usuarios").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(valorKey)){

                                String nombreDato = dataSnapshot.child(valorKey).child("Nombre_completo").getValue(String.class);
                                nombreViewIntro.setText(nombreDato);
                                if(dataSnapshot.child(valorKey).hasChild("Habitacion")) {

                                    String habDato = dataSnapshot.child(valorKey).child("Habitacion").getValue(String.class);
                                    habViewIntro.setText(getString(R.string.habViewIntro, habDato));
                                    habViewIntro.setVisibility(View.VISIBLE);

                                }else {
                                    habViewIntro.setVisibility(View.GONE);
                                }

                                if (valorKey.equals("default")) {
                                    datoVinculado = false;

                                    vincularButton.setVisibility(View.VISIBLE);
                                    vincularInfo.setVisibility(View.VISIBLE);
                                    vincularText.setVisibility(View.VISIBLE);



                                    nombreViewIntro.setVisibility(View.GONE);
                                    habViewIntro.setVisibility(View.GONE);
                                    oratoriaBox.setVisibility(View.GONE);
                                    declamacionBox.setVisibility(View.GONE);
                                    ajedrezBox.setVisibility(View.GONE);
                                    dominoBox.setVisibility(View.GONE);
                                    cuentoBox.setVisibility(View.GONE);
                                    futbolBox.setVisibility(View.GONE);
                                    basquetBox.setVisibility(View.GONE);
                                    cantoBox.setVisibility(View.GONE);
                                    premioBox.setVisibility(View.GONE);

                                    vincularButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            isAdmin = "false";
                                            Intent intent = new Intent(getBaseContext(), Identificador.class);
                                            intent.putExtra("isadminInfo", isAdmin);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });


                                } else if (!valorKey.equals("default")){
                                    datoVinculado = true;
                                    Toast.makeText(MenuPrincipal.this,"Perfil encontrado", Toast.LENGTH_LONG).show();

                                    perfilInfo("Oratoria", oratoriaBox);
                                    perfilInfo("Declamacion", declamacionBox);
                                    perfilInfo("Ajedrez", ajedrezBox);
                                    perfilInfo("Dominó", dominoBox);
                                    perfilInfo("Cuento", cuentoBox);
                                    perfilInfo("Futbol", futbolBox);
                                    perfilInfo("Basquetbol", basquetBox);
                                    perfilInfo("Canto", cantoBox);
                                    perfilInfo("Premio AJEF", premioBox);

                                    nombreViewIntro.setVisibility(View.VISIBLE);
                                    oratoriaBox.setVisibility(View.VISIBLE);
                                    declamacionBox.setVisibility(View.VISIBLE);
                                    ajedrezBox.setVisibility(View.VISIBLE);
                                    dominoBox.setVisibility(View.VISIBLE);
                                    cuentoBox.setVisibility(View.VISIBLE);
                                    futbolBox.setVisibility(View.VISIBLE);
                                    basquetBox.setVisibility(View.VISIBLE);
                                    cantoBox.setVisibility(View.VISIBLE);
                                    premioBox.setVisibility(View.VISIBLE);
                                    vincularButton.setVisibility(View.GONE);
                                    vincularInfo.setVisibility(View.GONE);
                                    vincularText.setVisibility(View.GONE);

                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        usuarioVinculo = usuarioAuth.child("userKey");

        isAdminEvento = false;

        fotoView = (ImageView) findViewById(R.id.imageView2);
        organizadorButton = (Button) findViewById(R.id.organizadorButton);
        menuButton = (Button) findViewById(R.id.menuButton);
        nombreView = (TextView) findViewById(R.id.nombreView);
        logoutButton = (Button) findViewById(R.id.logoutButton);

        nombreViewIntro = (TextView) findViewById(R.id.nombre_view_intro);
        habViewIntro = (TextView) findViewById(R.id.hab_view_intro);
        oratoriaBox = (CheckBox) findViewById(R.id.oratoriaBox);
        declamacionBox = (CheckBox) findViewById(R.id.declamacionBox);
        ajedrezBox = (CheckBox) findViewById(R.id.ajedrezView);
        dominoBox = (CheckBox) findViewById(R.id.dominoBox);
        cuentoBox = (CheckBox) findViewById(R.id.cuentoBox);
        futbolBox = (CheckBox) findViewById(R.id.futbolBox);
        basquetBox = (CheckBox) findViewById(R.id.basquetBox);
        cantoBox = (CheckBox) findViewById(R.id.cantoBox);
        premioBox = (CheckBox) findViewById(R.id.premioBox);
        vincularButton = (Button) findViewById(R.id.vincularButton);

        //parte central

    if(!datoVinculado) {
        vincularButton.setVisibility(View.VISIBLE);
        vincularInfo.setVisibility(View.VISIBLE);
        vincularText.setVisibility(View.VISIBLE);



        nombreViewIntro.setVisibility(View.GONE);
        habViewIntro.setVisibility(View.GONE);
        oratoriaBox.setVisibility(View.GONE);
        declamacionBox.setVisibility(View.GONE);
        ajedrezBox.setVisibility(View.GONE);
        dominoBox.setVisibility(View.GONE);
        cuentoBox.setVisibility(View.GONE);
        futbolBox.setVisibility(View.GONE);
        basquetBox.setVisibility(View.GONE);
        cantoBox.setVisibility(View.GONE);
        premioBox.setVisibility(View.GONE);
        vincularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAdmin = "false";
                Intent intent = new Intent(getBaseContext(), Identificador.class);
                intent.putExtra("isadminInfo", isAdmin);
                startActivity(intent);
                finish();
            }
        });



    }

    if(datoVinculado) {


    }
        //organizadores
        organizadorButton.setOnClickListener(this);
        menuButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);



        usuarioAuth.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("FotoPerfil")){
                    String _fotoPerfil = dataSnapshot.child("FotoPerfil").getValue(String.class);
                    Uri uri = Uri.parse(_fotoPerfil);
                    Picasso.with(mContext)
                            .load(uri)
                            .placeholder(R.drawable.anon_user_48dp)
                            .error(R.drawable.anon_user_48dp)
                            .into(fotoView);
                }

                String _name = dataSnapshot.child("Nombre").getValue(String.class);
                nombreView.setText(getString(R.string.nombre_bienvenida, _name));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    } //onCreate

    @Override
    public void onClick (View view){
        if(view.getId() == R.id.organizadorButton){

            organizadorAction();

        } else if (view.getId() == R.id.menuButton){

        Intent intent = new Intent (MenuPrincipal.this, MenuSecundario.class);
            startActivity(intent);

        }else if (view.getId() == R.id.logoutButton){

            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(MenuPrincipal.this, LoginActivity.class);
            startActivity(intent);

        }

    }// onClick

    private void organizadorAction() {

        mDatabase.child("usuarioAdmin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.hasChild(usuarioID)){

                    isAdmin = "true";
                    isAdminEvento = true;
                    Intent intent = new Intent(MenuPrincipal.this, Identificador.class);
                    intent.putExtra("isadminInfo",isAdmin);
                    startActivity(intent);

                }else {

                    Toast.makeText(MenuPrincipal.this, "No tienes permiso de administrador.",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }// organizadorAction


    public void perfilInfo(final String eventoBox, final CheckBox eventoCheck){


        usuarioVinculo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!isAdminEvento) {
                    String usuarioIDVinculo = dataSnapshot.getValue().toString();

                    whereIsPerfil(usuarioIDVinculo);

                }



    }//perfil info

            public void whereIsPerfil (String usuarioIDVinculo)   {
                final DatabaseReference eventoInsc = mDatabase.child("usuarios").child(usuarioIDVinculo).child("Eventos");
                eventoInsc.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (!dataSnapshot.hasChild(eventoBox)){

                            eventoInsc.child(eventoBox).setValue("no");

                        }else if (dataSnapshot.child(eventoBox).getValue().toString().equals("si")){

                            eventoCheck.setChecked(true);

                        }else if (dataSnapshot.child(eventoBox).getValue().toString().equals("no")){

                            eventoCheck.setChecked(false);

                        }

                        //checkboxEdit
                        mDatabase.child("usuarioAdmin").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.hasChild(usuarioID) && !(eventoBox.equals("Premio Ajef") || eventoBox.equals("Cuento")  )){

                                    if(eventoCheck.isChecked()){

                                        eventoCheck.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                AlertDialog.Builder builder = new AlertDialog.Builder(MenuPrincipal.this,R.style.MyAlertDialogStyle);
                                                builder.setCancelable(true);
                                                builder.setTitle(eventoCheck.getText().toString());
                                                builder.setMessage(getString(R.string.eventoChange, eventoCheck.getText().toString()));
                                                builder.setPositiveButton(R.string.aceptar,
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                                eventoInsc.child(eventoBox).setValue("no");
                                                                eventoCheck.setChecked(false);
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

                                    }//iff is checked

                                    if(!eventoCheck.isChecked()){

                                        eventoCheck.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                AlertDialog.Builder builder = new AlertDialog.Builder(MenuPrincipal.this,R.style.MyAlertDialogStyle);
                                                builder.setCancelable(true);
                                                builder.setTitle(eventoCheck.getText().toString());
                                                builder.setMessage(getString(R.string.eventoUp, eventoCheck.getText().toString()));
                                                builder.setPositiveButton(R.string.aceptar,
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                                eventoInsc.child(eventoBox).setValue("si");
                                                                eventoCheck.setChecked(true);

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

                                    }//is not checked

                                } else if (!dataSnapshot.hasChild(usuarioID) && (eventoBox.equals("Premio Ajef") || eventoBox.equals("Cuento"))){
                                    eventoCheck.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Toast.makeText(MenuPrincipal.this, "Lo siento, no se puede modificar estas casillas",Toast.LENGTH_LONG).show();
                                        }
                                    });



                                }else if(dataSnapshot.hasChild(usuarioID)){

                                    if(eventoCheck.isChecked()){

                                        eventoCheck.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                AlertDialog.Builder builder = new AlertDialog.Builder(MenuPrincipal.this,R.style.MyAlertDialogStyle);
                                                builder.setCancelable(true);
                                                builder.setTitle(eventoCheck.getText().toString());
                                                builder.setMessage(getString(R.string.eventoChange, eventoCheck.getText().toString()));
                                                builder.setPositiveButton(R.string.aceptar,
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                                eventoInsc.child(eventoBox).setValue("no");
                                                                eventoCheck.setChecked(false);
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

                                    }//iff is checked

                                    if(!eventoCheck.isChecked()){

                                        eventoCheck.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                AlertDialog.Builder builder = new AlertDialog.Builder(MenuPrincipal.this,R.style.MyAlertDialogStyle);
                                                builder.setCancelable(true);
                                                builder.setTitle(eventoCheck.getText().toString());
                                                builder.setMessage(getString(R.string.eventoUp, eventoCheck.getText().toString()));
                                                builder.setPositiveButton(R.string.aceptar,
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                                eventoInsc.child(eventoBox).setValue("si");
                                                                eventoCheck.setChecked(true);

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

                                    }//is not checked

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });





            }//onDataChange usuarioVinculo

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }//whereIs

}//Menuprincipal
