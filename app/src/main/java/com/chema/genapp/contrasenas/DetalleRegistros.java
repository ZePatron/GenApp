package com.chema.genapp.contrasenas;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chema.genapp.R;
import com.chema.genapp.basedatos.BDHelper;
import com.chema.genapp.modelo.PasswordModel;
import com.chema.genapp.utils.Encriptacion;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DetalleRegistros extends AppCompatActivity {

    TextView d_titulo, d_cuenta, d_nombreUsuario, d_sitoWeb, d_nota, d_tiempoRegistro, d_tiempoActualizacion;
    EditText d_contrasenna;
    String id_pass;
    BDHelper bd;
    PasswordModel password;
    ImageButton btnDireccionar;
    Encriptacion encriptacion;
    SharedPreferences sp;
    private static final String SHARED_PREF = "mi_sp";
    private static final String KEY = "key_encriptacion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Declaro esta ventana como una ventana segura que no permite capturas de pantalla ni ser visualizada en pantallas no seguras
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_contrasenas);

        ActionBar actionBar = getSupportActionBar();

        Intent i = getIntent();
        id_pass = i.getStringExtra("id_pass");

        encriptacion = new Encriptacion();

        bd = new BDHelper(this);

        inicializacionVariables();

        password = bd.BuscarRegistrosId(id_pass);

        String llave = sp.getString(KEY,null);

        //Desencripto la contraseña y la establezco en el objeto password
        password.setContrasena(encriptacion.Desencriptar(password.getContrasena(), llave));



        cargarTextView();





        String tituloRegistro = d_titulo.getText().toString();
        //Establezco el titulo al actionBar y añado el icono de regreso
        actionBar.setTitle(tituloRegistro);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        btnDireccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url_web = d_sitoWeb.getText().toString();

                // copio la contraseña en el cortapapeles
                String text = d_contrasenna.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text",  text);
                clipboard.setPrimaryClip(clip);

                if(!url_web.equals("")){
                    AbrirWeb(url_web);
                    Toast.makeText(DetalleRegistros.this, "Contraseña copiada en el cortapapeles", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(DetalleRegistros.this, "El registro no contiene ninguna url", Toast.LENGTH_SHORT).show();
                    Toast.makeText(DetalleRegistros.this, "Contraseña copiada en el cortapapeles", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void inicializacionVariables(){
        d_titulo = findViewById(R.id.d_titulo);
        d_cuenta = findViewById(R.id.d_cuenta);
        d_nombreUsuario = findViewById(R.id.d_nombre_usuario);
        d_contrasenna = findViewById(R.id.d_contrasenna);
        d_sitoWeb = findViewById(R.id.d_sitio_web);
        d_nota = findViewById(R.id.d_nota);
        d_tiempoRegistro = findViewById(R.id.d_tiempo_registro);
        d_tiempoActualizacion = findViewById(R.id.d_tiempo_actualizacion);
        btnDireccionar = findViewById(R.id.imgBtnSitioWeb);
        sp = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);



    }

    private void cargarTextView(){
        d_titulo.setText(password.getTitulo());
        d_cuenta.setText(password.getCuenta());
        d_nombreUsuario.setText(password.getUsuario());
        d_contrasenna.setText(password.getContrasena());
        d_contrasenna.setEnabled(false); //Deshabilito el editText
        d_contrasenna.setBackgroundColor(Color.TRANSPARENT);
        d_contrasenna.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD); // De esta forma cuando se muestra el texto la primera vez queda oculto
        d_sitoWeb.setText(password.getSitioWeb());
        d_nota.setText(password.getNota());

        //Paso las fechas de formato String a Date y de milisegundos a 'dd/MM/yyyy hh:mm'
        Date date = new Date();
        date.setTime(Long.parseLong(password.getT_registro()));
        String tiempoRegistroStr = new SimpleDateFormat("dd/MM/yyyy hh:mm").format(date);
        d_tiempoRegistro.setText(tiempoRegistroStr);

        date.setTime(Long.parseLong(password.getT_actualizacion()));
        String tiempoActualizacionStr = new SimpleDateFormat("dd/MM/yyyy hh:mm").format(date);
        d_tiempoActualizacion.setText(tiempoActualizacionStr);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void AbrirWeb(String url){
        Intent abrir;
        if(url.contains("https://")){
            abrir = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(abrir);
        }else{
            abrir = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+url));
            startActivity(abrir);
        }

    }
}