package com.chema.genapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chema.genapp.MainActivity;
import com.chema.genapp.R;

public class Login extends AppCompatActivity {

    EditText etPass1, etPass2;
    Button btnConfirmar;
    SharedPreferences sp;

    private static final String SHARED_PREF = "mi_sp";
    private static final String KEY_PASS = "password";
    private static final String KEY = "key_encriptacion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Declaro esta ventana como una ventana segura que no permite capturas de pantalla ni ser visualizada en pantallas no seguras
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_login);

        initVariables();
        comprobarPass();

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = etPass1.getText().toString().trim();
                String pass2 = etPass2.getText().toString().trim();
                String key;

                //Validacion
                if(pass.isEmpty()){
                    Toast.makeText(Login.this, "El campo contraseña es obligatorio", Toast.LENGTH_SHORT).show();
                }else if(pass.length()<4){
                    Toast.makeText(Login.this, "La contraseña maestra debe tener al menos 4 caracteres", Toast.LENGTH_SHORT).show();
                }else if(pass2.isEmpty()){
                    Toast.makeText(Login.this, "Debes escribir de nuevo la contraseña", Toast.LENGTH_SHORT).show();
                }else if(!pass2.equals(pass)){
                    Toast.makeText(Login.this, "Las contraseñas deben ser las mismas", Toast.LENGTH_SHORT).show();
                }else{
                    key = pass;
                    if(key.length() < 16){
                        key = key+key+key+key;
                    }

                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString(KEY_PASS, pass);  //Clave maestra
                    edit.putString(KEY, key);        //Clave encriptacion
                    edit.apply();

                    Intent i = new Intent(Login.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });


    }


    private void initVariables(){
        etPass1 = findViewById(R.id.logEtPass);
        etPass2 = findViewById(R.id.logEtConPass);
        btnConfirmar = findViewById(R.id.logBtnConfirmar);
        sp = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
    }

    //Compruebo si ya se ha registrado una contraseña maestra
    private void comprobarPass(){
        String passRegistrada = sp.getString(KEY_PASS, null);

        //Si ya existe una contraseña maestra registrada
        if(passRegistrada != null){
            Intent i = new Intent(Login.this, LoginUsuarioRegistrado.class);
            startActivity(i);
            finish();
        }
    }
}