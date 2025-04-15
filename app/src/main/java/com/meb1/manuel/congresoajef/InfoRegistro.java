package com.meb1.manuel.congresoajef;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.Result;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class InfoRegistro extends AppCompatActivity{
    private DatabaseReference mDataBase;
    private DatabaseReference datosId;
    private DatabaseReference datoNombre;
    private DatabaseReference datoTaller;
    private DatabaseReference datoEstado;
    private DatabaseReference datoAlergia;
    private DatabaseReference datoCopChia;
    private DatabaseReference datoInscrip;
    private DatabaseReference datoHotel;
    private DatabaseReference datoHab;
    private DatabaseReference datoDieta;
    private DatabaseReference datoEvento;
    private DatabaseReference datoCena;
    private TextView nombreView;
    private TextView tallerView;
    private TextView estadoView;
    private TextView alergiaView;
    private TextView eventosText;
    private TextView dietaView;
    private EditText habitView;
    private ImageView mImageView;
    private CheckedTextView coopChiaView;
    private CheckedTextView inscripView;
    private CheckedTextView hospedajeView;
    private ProgressDialog mProgres;
    private StorageReference mStorageReference;
    private static final int CAMERA_REQUEST_CODE = 1;
    private DatabaseReference usuarioAuth;

    //perfilInfoView
    private RelativeLayout evento_info;
    private CheckBox oratoriaBox;
    private CheckBox declamacionBox;
    private CheckBox ajedrezBox;
    private CheckBox dominoBox;
    private CheckBox cuentoBox;
    private CheckBox futbolBox;
    private CheckBox basquetBox;
    private CheckBox cantoBox;
    private CheckBox premioBox;
    private CheckBox cenaBox;


    //ususario ID
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    final String usuarioID = currentFirebaseUser.getUid().toString();

    // imagen

    boolean isAdmin = true;

    // create
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String datoescaneado = getIntent().getStringExtra("datoescaneado");


        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.keepSynced(true);
        usuarioAuth = mDataBase.child("usuariosAuth").child(usuarioID);
        datosId = mDataBase.child("usuarios").child(datoescaneado);

        setContentView(R.layout.activity_info_registro);
        nombreView = (TextView) findViewById(R.id.nombreView);
        tallerView = (TextView) findViewById(R.id.tallerView);
        estadoView = (TextView) findViewById(R.id.estadoView);
        alergiaView = (TextView) findViewById(R.id.alergiaView);
        mImageView = (ImageView) findViewById(R.id.imageView);
        coopChiaView = (CheckedTextView) findViewById(R.id.coopChia);
        inscripView = (CheckedTextView) findViewById(R.id.inscripCheck);
        hospedajeView = (CheckedTextView) findViewById(R.id.hospedajeView);
        eventosText = (TextView) findViewById(R.id.eventosTextView);
        habitView = (EditText) findViewById(R.id.habitView);
        habitView.setTag(habitView.getKeyListener());
        habitView.setKeyListener(null);
        dietaView = (TextView) findViewById(R.id.dietaView);

        mProgres = new ProgressDialog(this);

        oratoriaBox = (CheckBox) findViewById(R.id.oratoriaBox2);
        declamacionBox = (CheckBox) findViewById(R.id.declamacionBox2);
        ajedrezBox = (CheckBox) findViewById(R.id.ajedrezView2);
        dominoBox = (CheckBox) findViewById(R.id.dominoBox2);
        cuentoBox = (CheckBox) findViewById(R.id.cuentoBox2);
        futbolBox = (CheckBox) findViewById(R.id.futbolBox2);
        basquetBox = (CheckBox) findViewById(R.id.basquetBox2);
        cantoBox = (CheckBox) findViewById(R.id.cantoBox2);
        premioBox = (CheckBox) findViewById(R.id.premioBox2);
        cenaBox = (CheckBox) findViewById(R.id.cenaBox);


        //eventos

                                    perfilInfo("Oratoria", oratoriaBox);
                                    perfilInfo("Declamacion", declamacionBox);
                                    perfilInfo("Ajedrez", ajedrezBox);
                                    perfilInfo("Dominó", dominoBox);
                                    perfilInfo("Cuento", cuentoBox);
                                    perfilInfo("Futbol", futbolBox);
                                    perfilInfo("Basquetbol", basquetBox);
                                    perfilInfo("Canto", cantoBox);
                                    perfilInfo("Premio AJEF", premioBox);


        //Cambiar imagen //
        //datosID//
        datosId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild("asistencia_cena")){
                    datosId.child("asistencia_cena").setValue("no");
                    cenaBox.setChecked(false);
                }else {

                    datoCena = datosId.child("asistencia_cena");
                    datoCena.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue().toString().equals("no")){
                                cenaBox.setChecked(false);
                                cenaBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(InfoRegistro.this,R.style.MyAlertDialogStyle);
                                        builder.setCancelable(true);
                                        builder.setTitle("Cena gala");
                                        builder.setMessage("¿Ya asistió al salón?");
                                        builder.setPositiveButton(R.string.aceptar,
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        mDataBase.child("usuarioAdmin").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if(dataSnapshot.hasChild(usuarioID)){
                                                                    datoCena.setValue("si");
                                                                    Toast.makeText(InfoRegistro.this, "Dato actualizado", Toast.LENGTH_LONG).show();
                                                                    recreate();
                                                                }else{
                                                                    Toast.makeText(InfoRegistro.this, "Error. No tienes privilegios de administrador", Toast.LENGTH_LONG).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });


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

                            if(dataSnapshot.getValue().toString().equals("si")){

                                cenaBox.setChecked(true);
                                cenaBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(InfoRegistro.this,R.style.MyAlertDialogStyle);
                                        builder.setCancelable(true);
                                        builder.setTitle("Cena gala");
                                        builder.setMessage("¿Salió del salón?");
                                        builder.setPositiveButton(R.string.aceptar,
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        mDataBase.child("usuarioAdmin").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if(dataSnapshot.hasChild(usuarioID)){
                                                                    datoCena.setValue("no");
                                                                    Toast.makeText(InfoRegistro.this, "Dato actualizado", Toast.LENGTH_LONG).show();
                                                                    recreate();
                                                                }else{
                                                                    Toast.makeText(InfoRegistro.this, "Error. No tienes privilegios de administrador", Toast.LENGTH_LONG).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });


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

        //total pagados//
        Query totalInsc = datosId.child("Cuota_inscripcion").orderByValue().equalTo("si");

        totalInsc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        // Mostrar datos //



        datoDieta = datosId.child("Tipo_dieta");
        datoDieta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dieta = dataSnapshot.getValue().toString();
                dietaView.setText(dieta);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        datoEvento = datosId.child("Eventos");
        datoEvento.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String eventos = dataSnapshot.getValue().toString();
                eventosText.setText(eventos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        datoNombre = datosId.child("Nombre_completo");
        datoNombre.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nombre = dataSnapshot.getValue(String.class);
                nombreView.setText(nombre);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        datoEstado = datosId.child("Lugar_procedencia");
        datoCopChia = datosId.child("Coop_logia_Chiapas");
        datoEstado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String estado = dataSnapshot.getValue(String.class);

                if(estado.equals("Chiapas")){
                    estadoView.setText(estado);

                    datoCopChia.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String coopChia = dataSnapshot.getValue(String.class);
                            if(coopChia.equals("si")){
                               coopChiaView.setVisibility(View.VISIBLE);
                                coopChiaView.setChecked(true);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else{
                    estadoView.setText(estado);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        datoTaller = datosId.child("Taller_procedencia");
        datoTaller.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String taller =dataSnapshot.getValue(String.class);
                tallerView.setText(taller);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        datoAlergia = datosId.child("Alergias");
        datoAlergia.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String alergia = dataSnapshot.getValue(String.class);
                alergiaView.setText(getString(R.string.alergia, alergia));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        datoInscrip = datosId.child("Cuota_inscripcion");
        datoInscrip.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String inscrip = dataSnapshot.getValue(String.class);

                if(inscrip!= null && inscrip.equals("si")) {
                    inscripView.setChecked(true);
                    inscripView.setCheckMarkDrawable(R.drawable.done_check_mark);
                } else {
                    inscripView.setChecked(true);
                    inscripView.setCheckMarkDrawable(R.drawable.ic_dialog_close_light);
                    inscripView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(InfoRegistro.this,R.style.MyAlertDialogStyle);
                            builder.setCancelable(true);
                            builder.setTitle("Inscripción");
                            builder.setMessage("¿Recibió el pago de inscripción?");
                            builder.setPositiveButton(R.string.aceptar,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            mDataBase.child("usuarioAdmin").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.hasChild(usuarioID)){
                                                        datoInscrip.setValue("si");
                                                        Toast.makeText(InfoRegistro.this, "Dato actualizado", Toast.LENGTH_LONG).show();
                                                        recreate();
                                                    }else{
                                                        Toast.makeText(InfoRegistro.this, "Error. No tienes privilegios de administrador", Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        datoHotel = datosId.child("Hospedaje");
        datoHotel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String hospedaje = dataSnapshot.getValue(String.class);

                if(hospedaje!=null && hospedaje.equals("si")) {
                    hospedajeView.setChecked(true);
                    hospedajeView.setCheckMarkDrawable(R.drawable.done_check_mark);
                    habNum();

                } else {
                    hospedajeView.setChecked(true);
                    hospedajeView.setCheckMarkDrawable(R.drawable.ic_dialog_close_light);
                    habitView.setVisibility(View.INVISIBLE);

                    hospedajeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(InfoRegistro.this,R.style.MyAlertDialogStyle);
                            builder.setCancelable(true);
                            builder.setTitle("Hospedaje");
                            builder.setMessage("¿Recibió el pago de hospedaje?");
                            builder.setPositiveButton(R.string.aceptar,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            mDataBase.child("usuarioAdmin").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.hasChild(usuarioID)){
                                                        datoHotel.setValue("si");
                                                        Toast.makeText(InfoRegistro.this, "Dato actualizado", Toast.LENGTH_LONG).show();
                                                        recreate();
                                                    }else{
                                                        Toast.makeText(InfoRegistro.this, "Error. No tienes privilegios de administrador", Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //sigue aca





    }//onCreate

    private void habNum() {


        datosId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("Habitacion")){

                    String numHab = dataSnapshot.child("Habitacion").getValue(String.class);
                    habitView.setText(getString(R.string.habitacion_num,numHab));

                    //cambiar numero de habitacion
                    habitView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mDataBase.child("usuarioAdmin").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                 if(dataSnapshot.hasChild(usuarioID)) {
                                     habitView.setText("");
                                     habitView.setKeyListener((KeyListener) habitView.getTag());
                                     showSoftKeyboard(habitView);
                                     Toast.makeText(InfoRegistro.this, "Introduce el número de habitación", Toast.LENGTH_LONG).show();
                                 } else {
                                     Toast.makeText(InfoRegistro.this, "No tienes permiso de administrador", Toast.LENGTH_LONG).show();
                                 }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });




                    }
                    });

                    habitView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                            if ( (actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN ))){
                                datoHab = datosId.child("Habitacion");
                                datoHab.setValue(habitView.getText().toString());
                                habitView.setTag(habitView.getKeyListener());
                                habitView.setKeyListener(null);
                                hideSoftKeyboard();

                                return true;
                            }
                            else{
                                return false;
                            }

                        }
                    });

                } else {
                    datoHab = datosId.child("Habitacion");
                    datoHab.setValue("");

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }


   public void perfilInfo(final String eventoBox, final CheckBox eventoCheck){
       final String datoescaneado = getIntent().getStringExtra("datoescaneado");

       final DatabaseReference eventoInsc = mDataBase.child("usuarios").child(datoescaneado).child("Eventos");
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

               // editarBox
               if(eventoCheck.isChecked()){

                   eventoCheck.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                           AlertDialog.Builder builder = new AlertDialog.Builder(InfoRegistro.this,R.style.MyAlertDialogStyle);
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

                           AlertDialog.Builder builder = new AlertDialog.Builder(InfoRegistro.this,R.style.MyAlertDialogStyle);
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

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }



       });//eventoinscListener


    }//perfilInfo




}//InfoRegistro






