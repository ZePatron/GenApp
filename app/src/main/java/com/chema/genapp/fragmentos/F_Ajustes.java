package com.chema.genapp.fragmentos;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chema.genapp.MainActivity;
import com.chema.genapp.R;
import com.chema.genapp.basedatos.BDHelper;
import com.chema.genapp.basedatos.Constantes;
import com.chema.genapp.login.LoginUsuarioRegistrado;
import com.chema.genapp.modelo.PasswordModel;
import com.chema.genapp.utils.Encriptacion;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class F_Ajustes extends Fragment {

    TextView tvEliminar, tvImportar, tvExportar, tvCambiarPass;
    Dialog dialog, dialogCambiarPass;
    BDHelper helper;
    SharedPreferences sp;
    //Constantes de las SharedPreferences
    private static final String SHARED_PREF = "mi_sp";
    private static final String KEY_PASS = "password";
    private static final String KEY = "key_encriptacion";

    public F_Ajustes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Declaro esta ventana como una ventana segura que no permite capturas de pantalla ni ser visualizada en pantallas no seguras
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_f__ajustes, container, false);

        tvEliminar = view.findViewById(R.id.eliminarTodo);
        tvImportar = view.findViewById(R.id.ajImportar);
        tvExportar = view.findViewById(R.id.ajExportar);
        tvCambiarPass = view.findViewById(R.id.ajCambiarPass);
        dialog = new Dialog(getActivity());
        dialogCambiarPass = new Dialog(getActivity());
        helper = new BDHelper(getActivity());
        sp = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

        tvEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogEliminar();
            }
        });

        tvImportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Creo un alert para avisar si deseamos continuar
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("¿Desea importar datos?");
                alert.setMessage("Al importar los datos se procederá a eliminar los registros actuales");
                alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        importarArchivo();
                    }
                });
                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Importación cancelada", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.create().show();

            }
        });

        tvExportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportarArchivo();
            }
        });

        tvCambiarPass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cuadroDialogCambiarPass();
            }
        });
        

        return view;
    }



    private void DialogEliminar() {

        Button btnSi, btnNo;
        //Se asocia el layout correspondiente
        dialog.setContentView(R.layout.cuadro_dialogo_eliminar_todo);

        btnSi = dialog.findViewById(R.id.btnEliminarSi);
        btnNo = dialog.findViewById(R.id.btnEliminarNo);

        btnSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.EliminarTodo();
                startActivity(new Intent(getActivity(), MainActivity.class)); //Vuelvo al mainActivity que carga el fragment F_Todas
                Toast.makeText(getActivity(), "Registros eliminados", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.setCancelable(false); //Al pulsar fuera no se cerrara el dialogo

    }

    /*
    * Método que restaura la copia de seguridad
    * */
    private void importarArchivo() {
        String rutaFichero = Environment.getExternalStorageDirectory()+"/Documents/"+"/GenApp/"+"/Registros.dat";
        File fichero = new File(rutaFichero);
        if(fichero.exists()){
            try {
                helper.EliminarTodo();


                ObjectInputStream ef = new ObjectInputStream(new FileInputStream(rutaFichero));

                ArrayList<PasswordModel> listaPass = (ArrayList<PasswordModel>) ef.readObject();

                for(PasswordModel a: listaPass){
                    helper.InsertarRegistro(a);
                }

                Toast.makeText(getActivity(), "Copia de seguridad restaurada", Toast.LENGTH_SHORT).show();

            }catch (Exception e){
                System.out.println("Error al leer");
            }
        }else{
            Toast.makeText(getActivity(), "No existe copia de seguridad", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportarArchivo() {
        //Dirección donde se van a guardar los documentos exportados
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"GenApp");

        //Compruebo si tengo los permisos, en caso de que no los tenga los solicito
        int permissionCheck = ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            System.out.println("No tiene persmiso para escribir");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225); //Solicitud de permisos
            Toast.makeText(getActivity(),"Inténtelo de nuevo", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println("Sí tiene permiso");
        }

        boolean nuevaCarpeta = false;
        ArrayList<PasswordModel> listaRegistros = new ArrayList<>();

        if(!folder.exists()){
            nuevaCarpeta = folder.mkdirs();
        }

        //Nombre archivo
        String nombreFichero = "Registros.dat";
        String rutaFichero = folder+"/"+nombreFichero;

        //Obtengo el registro
        listaRegistros = helper.ObtenerRegistros(Constantes.C_TITULO+" ASC");


        try{

            ObjectOutputStream ef = new ObjectOutputStream(new FileOutputStream(rutaFichero));
            ef.writeObject(listaRegistros);
            ef.flush();
            ef.close();

            Toast.makeText(getActivity(), "Datos exportado en la carpeta "+rutaFichero, Toast.LENGTH_SHORT).show();
        }catch(Exception e) {
            Toast.makeText(getActivity(), "Error al exportar archivo", Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());

        }
    }

    private void cuadroDialogCambiarPass(){
        EditText tvPassPrincipal;
        EditText etPass1, etPass2;
        Button btnAceptar, btnCancelar;
        String passSp = sp.getString(KEY_PASS,null);
        String claveEncriptacion = sp.getString(KEY, null);

        dialogCambiarPass.setContentView(R.layout.cuadro_dialogo_cambiar_pass);

        tvPassPrincipal = dialogCambiarPass.findViewById(R.id.passPrincipal);
        etPass1 = dialogCambiarPass.findViewById(R.id.etPassPrincipalNueva);
        etPass2 = dialogCambiarPass.findViewById(R.id.etPassPrincipalNuevaConfirmar);
        btnAceptar = dialogCambiarPass.findViewById(R.id.btnPassPrincipalCambiar);
        btnCancelar = dialogCambiarPass.findViewById(R.id.btnPassPrincipalCancelar);


        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass1 = etPass1.getText().toString();
                String pass2 = etPass2.getText().toString();

                if(!pass1.equals(pass2)){
                    Toast.makeText(getActivity(), "Las nuevas contraseñas escritas no coinciden", Toast.LENGTH_SHORT).show();
                }else if(pass1.isEmpty()){
                    Toast.makeText(getActivity(), "Debes escribir las nueva contraseña", Toast.LENGTH_SHORT).show();
                }else if(pass1.length() < 4){
                    Toast.makeText(getActivity(), "Las contraseña debe contener al menos 4 caracteres", Toast.LENGTH_SHORT).show();
                }else{

                    String key = pass1;
                    if(key.length() < 16){
                        key = key+key+key+key;
                    }

                    //Reescribo los SharedPreferences de la KEY_PASS y de la KEY de encriptacion
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(KEY_PASS,pass1);
                    editor.putString(KEY, key);
                    editor.apply();

                    //Cambio las claves de encriptacion de la base de datos
                    cambiarClaveEncriptacion(claveEncriptacion, key);

                    startActivity(new Intent(getActivity(), LoginUsuarioRegistrado.class));
                    getActivity().finish();
                    Toast.makeText(getActivity(), "La contraseña principal ha sido cambiada con éxito", Toast.LENGTH_SHORT).show();
                    dialogCambiarPass.dismiss();
                }

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCambiarPass.dismiss();
            }
        });

        tvPassPrincipal.setText(passSp);
        tvPassPrincipal.setBackgroundColor(Color.TRANSPARENT);
        tvPassPrincipal.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        dialogCambiarPass.show();
        dialog.setCancelable(false);

    }

    //Método que cambia la clave de encriptacion de los registros de la base de datos
    public void cambiarClaveEncriptacion(String llaveAntigua, String llaveNueva){
        Encriptacion encriptacion = new Encriptacion();
        ArrayList<PasswordModel> listaRegistros = helper.ObtenerRegistros(Constantes.C_TITULO+" ASC");
        String passAux;

        helper.EliminarTodo();

        for(PasswordModel pass: listaRegistros){
            passAux = encriptacion.Desencriptar(pass.getContrasena(),llaveAntigua); //Desencripto la contraseña con la clave que se encripto
            passAux = encriptacion.Encriptar(passAux, llaveNueva); //Estando desencriptada, la vuelvo a encriptar con la nueva clave
            pass.setContrasena(passAux); //La establezco en el modelo
            helper.InsertarRegistro(pass); //La inserto
        }

        /*Reescribo la copia de seguridad para que nos funcione con la nueva clave*/
        //Dirección donde se van a guardar los documentos exportados
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"GenApp");
        boolean nuevaCarpeta = false;

        if(!folder.exists()){
            nuevaCarpeta = folder.mkdirs();
        }

        //Nombre archivo
        String nombreFichero = "Registros.dat";
        String rutaFichero = folder+"/"+nombreFichero;

        //Obtengo el registro
        listaRegistros = helper.ObtenerRegistros(Constantes.C_TITULO+" ASC");

        try{
            ObjectOutputStream ef = new ObjectOutputStream(new FileOutputStream(rutaFichero));
            ef.writeObject(listaRegistros);
            ef.flush();
            ef.close();
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}