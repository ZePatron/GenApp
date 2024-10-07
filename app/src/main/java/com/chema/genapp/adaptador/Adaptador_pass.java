package com.chema.genapp.adaptador;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chema.genapp.MainActivity;
import com.chema.genapp.R;
import com.chema.genapp.basedatos.BDHelper;
import com.chema.genapp.contrasenas.DetalleRegistros;
import com.chema.genapp.modelo.PasswordModel;
import com.chema.genapp.opciones_pass.AgregarActualizarPassword;

import java.util.ArrayList;

public class Adaptador_pass extends RecyclerView.Adapter<Adaptador_pass.HolderPassword>{

    private Context context;
    private ArrayList<PasswordModel> passwordList;
    Dialog dialog;
    BDHelper helper;

    public Adaptador_pass(Context context, ArrayList<PasswordModel> passwordList) {
        this.context = context;
        this.passwordList = passwordList;
        dialog = new Dialog(context);
        helper = new BDHelper(context);
    }

    @NonNull
    @Override
    public HolderPassword onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_password, parent, false);

        return new HolderPassword(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPassword holder, int position) {
        PasswordModel pass = passwordList.get(position);
        String id = pass.getId();
        String titulo = pass.getTitulo();
        String cuenta = pass.getCuenta();
        String nombre_usuario = pass.getUsuario();
        String contrasenna = pass.getContrasena();
        String sitio_web = pass.getSitioWeb();
        String nota = pass.getNota();
        String tiempo_registro = pass.getT_registro();
        String tiempo_actualizacion = pass.getT_actualizacion();

        System.out.println(pass);

        holder.item_titulo.setText(titulo);
        holder.item_cuenta.setText(cuenta);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Acción que se activa cuando el usuario clickea en el item
                Intent i = new Intent(context, DetalleRegistros.class);
                i.putExtra("id_pass",id);
                context.startActivity(i);
            }
        });

        //Evento que se ejecuta al clickar en el icono con 3 puntos
        holder.ib_mas_opciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CuadroEditarEliminar(id);
            }
        });

    }

    @Override
    public int getItemCount() {
        //Devuelve el tamaño de la lista
        return passwordList.size();
    }

    class HolderPassword extends RecyclerView.ViewHolder{

        //Items del item_password.xml
        TextView item_titulo, item_cuenta, item_nombre_usuario, item_contrasenna, item_sitio_web, item_nota;
        ImageButton ib_mas_opciones;

        public HolderPassword(@NonNull View itemView) {
            super(itemView);

            item_titulo = itemView.findViewById(R.id.item_titulo);
            item_cuenta = itemView.findViewById(R.id.item_cuenta);
            item_nombre_usuario = itemView.findViewById(R.id.item_nombre_usuario);
            item_contrasenna = itemView.findViewById(R.id.item_contrasenna);
            item_sitio_web = itemView.findViewById(R.id.item_sitio_web);
            item_nota = itemView.findViewById(R.id.item_nota);

            ib_mas_opciones = itemView.findViewById(R.id.Ib_mas_opciones);
        }
    }

    //Metodo con la funcionalidad del cuadro_dialogo_editar_eliminar
    private void CuadroEditarEliminar(String id){
        Button btn_edit_registro, btn_eliminar_registro;

        dialog.setContentView(R.layout.cuadro_dialogo_editar_eliminar);

        btn_edit_registro = dialog.findViewById(R.id.btn_edit_registro);
        btn_eliminar_registro = dialog.findViewById(R.id.btn_eliminar_registro);

        //Asigno los eventos a los botones del cuadro de dialogo
        btn_edit_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AgregarActualizarPassword.class);
                i.putExtra("id",id);
                i.putExtra("editar", true);
                context.startActivity(i);

                dialog.dismiss();
            }
        });

        btn_eliminar_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int eliminado = helper.EliminarRegistro(id);
                Intent i = new Intent(context, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //Con esta línea elimino la tarea abierta y convierto a MainActivity en principal
                context.startActivity(i);
                if(eliminado == 1){
                    Toast.makeText(context, "Se ha eliminado correctamente el registro", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        dialog.show(); //Muestra el cuadro
        dialog.setCancelable(true); //Si presionamos fuera del cuadro se cierra

    }
}