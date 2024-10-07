package com.chema.genapp.opciones_pass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chema.genapp.MainActivity;
import com.chema.genapp.R;
import com.chema.genapp.basedatos.BDHelper;
import com.chema.genapp.modelo.PasswordModel;
import com.chema.genapp.utils.Encriptacion;
import com.chema.genapp.utils.GeneradorPassword;

public class AgregarActualizarPassword extends AppCompatActivity {

    EditText etTitulo, etCuenta, etNombreUsuario, etContrasena, etSitioWeb, etNota;
    Button btnAleatoria;
    String passGenerada;
    SharedPreferences sp;
    private static final String SHARED_PREF = "mi_sp";
    private static final String KEY = "key_encriptacion";

    PasswordModel pass;

    private BDHelper dbHelper;

    private boolean editar = false;

    private Encriptacion encriptacion;

    Dialog dialogAleatoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Declaro esta ventana como una ventana segura que no permite capturas de pantalla ni ser visualizada en pantallas no seguras
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_agregar_password);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        inicializarVariables();
        ObtenerDatos();

        btnAleatoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogPassAleatoria();
            }
        });
    }


    private void inicializarVariables(){
        etTitulo = findViewById(R.id.EtTitulo);
        etCuenta = findViewById(R.id.EtCuenta);
        etNombreUsuario = findViewById(R.id.etNombreUsuario);
        etContrasena = findViewById(R.id.etContrasena);
        etSitioWeb = findViewById(R.id.etSitioWeb);
        etNota =  findViewById(R.id.etNota);
        btnAleatoria = findViewById(R.id.btnAleatoria);

        dbHelper = new BDHelper(this);
        encriptacion = new Encriptacion();
        dialogAleatoria = new Dialog(this);

        sp = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
    }

    private void ObtenerDatos(){
        String id;

        Intent i = getIntent();
        editar = i.getBooleanExtra("editar", false);

        if(editar){
            id = i.getStringExtra("id");
            pass = dbHelper.BuscarRegistrosId(id);
            String llave = sp.getString(KEY, null);

            //Desencripto la contraseña
            String passDesencriptada = encriptacion.Desencriptar(pass.getContrasena(), llave);

            etTitulo.setText(pass.getTitulo());
            etCuenta.setText(pass.getCuenta());
            etNombreUsuario.setText(pass.getUsuario());
            etContrasena.setText(passDesencriptada);
            etSitioWeb.setText(pass.getSitioWeb());
            etNota.setText(pass.getNota());

        }else{
            //Nuevo registro
        }
    }

    private void GuardarPassword(){
        String titulo = etTitulo.getText().toString().trim();
        String cuenta = etCuenta.getText().toString().trim();
        String nombreUsuario = etNombreUsuario.getText().toString().trim();
        String contrasenna = etContrasena.getText().toString().trim();
        String sitioWeb = etSitioWeb.getText().toString().trim();
        String nota = etNota.getText().toString().trim();
        String tiempo = System.currentTimeMillis()+"";

        //Recupero la llave
        String llave = sp.getString(KEY, null);
        //Encripto la contraseña que voy a guardar en el Objeto pass
        String contrasennaEncriptada = encriptacion.Encriptar(contrasenna, llave);



        if(editar){
            //Actualizar registro
            pass.setTitulo(titulo);
            pass.setCuenta(cuenta);
            pass.setUsuario(nombreUsuario);
            pass.setContrasena(contrasennaEncriptada);
            pass.setSitioWeb(sitioWeb);
            pass.setNota(nota);
            pass.setT_actualizacion(tiempo);

            if(titulo.equals("")){
                etTitulo.setError("Campo obligatorio");
                etTitulo.setFocusable(true);
                if(contrasenna.equals("")){
                    etContrasena.setError("Campo obligatorio");
                }
            }else if(contrasenna.equals("")){
                etContrasena.setError("Campo obligatorio");
                etContrasena.setFocusable(true);
            }
            else{
                long actualizacion = dbHelper.ActualizarRegistro(pass);
                if(actualizacion != 0){
                    Toast.makeText(this, "Registro actualizado", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AgregarActualizarPassword.this, MainActivity.class));
                    finish();
                }
            }




        }else{
            //Nuevo registro
            pass = new PasswordModel(titulo,cuenta,nombreUsuario,contrasennaEncriptada,sitioWeb,nota, tiempo, tiempo);
            //Compruebo el titulo y la contraseña
            if(titulo.equals("")){
                etTitulo.setError("Campo obligatorio");
                etTitulo.setFocusable(true);
                if(contrasenna.equals("")){
                    etContrasena.setError("Campo obligatorio");
                }
            }else if(contrasenna.equals("")){
                etContrasena.setError("Campo obligatorio");
                etContrasena.setFocusable(true);
            }
            else{    //Si todo es correcto guardo la contraseña en la base de datos


                //Se inserta en la base de datos
                long id = dbHelper.InsertarRegistro(pass);

                Toast.makeText(this, "Se ha guardado con éxito ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AgregarActualizarPassword.this, MainActivity.class));
                finish();
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_guardar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.Guardar_Password){
            GuardarPassword();
        }
        return super.onOptionsItemSelected(item);
    }


    private void DialogPassAleatoria() {

        Button btnGenerar, btnAceptar, btnCancelar;
        RadioButton rbSoloLetras, rbLetrasNumeros, rbTodo;
        TextView longitud, tvPassAleatoria;


        dialogAleatoria.setContentView(R.layout.cuadro_dialogo_pass_aleatoria);

        btnGenerar = dialogAleatoria.findViewById(R.id.btnPassAleatoriaGenerar);
        btnAceptar = dialogAleatoria.findViewById(R.id.btnPassAleatoriaAceptar);
        btnCancelar = dialogAleatoria.findViewById(R.id.btnPassAleatoriaCancelar);
        rbSoloLetras = dialogAleatoria.findViewById(R.id.rdbPassAleatoriaLetras);
        rbLetrasNumeros = dialogAleatoria.findViewById(R.id.rdbPassAleatoriaSoloLetrasyNumeros);
        rbTodo = dialogAleatoria.findViewById(R.id.rdbPassAleatoriaTodosTipo);
        longitud = dialogAleatoria.findViewById(R.id.tvPassAleatoriaLongitud);
        tvPassAleatoria = dialogAleatoria.findViewById(R.id.tvPassAleatoria);

        btnAceptar.setEnabled(false);

        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int longitudInt = Integer.parseInt(longitud.getText().toString());
                if(longitudInt < 4 || longitudInt > 30){
                    Toast.makeText(AgregarActualizarPassword.this, "La longitud debe ser entre 4 y 30", Toast.LENGTH_SHORT).show();
                }else{

                    btnAceptar.setEnabled(true);

                    if (rbSoloLetras.isChecked()) {
                        passGenerada = GeneradorPassword.conLetras(longitudInt);
                        tvPassAleatoria.setText(passGenerada);
                    } else if (rbLetrasNumeros.isChecked()) {
                        passGenerada = GeneradorPassword.conLetrasNumeros(longitudInt);
                        tvPassAleatoria.setText(passGenerada);
                    } else if(rbTodo.isChecked()){
                        passGenerada = GeneradorPassword.conTodo(longitudInt);
                        tvPassAleatoria.setText(passGenerada);
                    }
                }
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AgregarActualizarPassword.this, "Nueva contraseña generada", Toast.LENGTH_SHORT).show();
                dialogAleatoria.dismiss();
                etContrasena.setText(passGenerada);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AgregarActualizarPassword.this, "Generacion de contraseña cancelada", Toast.LENGTH_SHORT).show();
                dialogAleatoria.dismiss();
            }
        });

        dialogAleatoria.show();
        dialogAleatoria.setCancelable(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}