package com.chema.genapp.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chema.genapp.MainActivity;
import com.chema.genapp.R;
import com.chema.genapp.utils.Encriptacion;

public class LoginUsuarioRegistrado extends AppCompatActivity {

    EditText etPass;
    Button btnAcceder, btnHuella;
    SharedPreferences sp;

    private static final String SHARED_PREF = "mi_sp";
    private static final String KEY_PASS = "password";

    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo infoPrompt;

    @Override    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Declaro esta ventana como una ventana segura que no permite capturas de pantalla ni ser visualizada en pantallas no seguras
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_login_usuario_registrado);

        initVariables();




        btnAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = etPass.getText().toString().trim();
                //Busco la pass del sharedPreferences
                String passSp = sp.getString(KEY_PASS,null);

                if(pass.equals(passSp)){
                    Intent i  = new Intent(LoginUsuarioRegistrado.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else if(pass.isEmpty()){
                    Toast.makeText(LoginUsuarioRegistrado.this, "Debes rellenar el campo contraseña", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginUsuarioRegistrado.this, "La contraseña no es correcta", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Inicializacion del prompt biometrico
        biometricPrompt = new BiometricPrompt(LoginUsuarioRegistrado.this, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(LoginUsuarioRegistrado.this, "Huella no reconocida", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(LoginUsuarioRegistrado.this, "Control biometrico verificado", Toast.LENGTH_SHORT).show();
                Intent i  = new Intent(LoginUsuarioRegistrado.this, MainActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(LoginUsuarioRegistrado.this, "Error la leer la huella", Toast.LENGTH_SHORT).show();
            }
        });

        //Comportamiento del prompt biometrico
        infoPrompt = new BiometricPrompt.PromptInfo.Builder().setTitle("Autentificacion Biométrica")
                .setNegativeButtonText("Cancelar").build();


        btnHuella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                biometricPrompt.authenticate(infoPrompt);
            }
        });

    }

    private void initVariables(){

        etPass = findViewById(R.id.logUsEtPass);
        btnAcceder = findViewById(R.id.logUsBtnAcceder);
        sp = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        btnHuella = findViewById(R.id.huellaBtn);
    }
}