package com.chema.genapp.fragmentos;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.chema.genapp.opciones_pass.AgregarActualizarPassword;
import com.chema.genapp.R;
import com.chema.genapp.adaptador.Adaptador_pass;
import com.chema.genapp.basedatos.BDHelper;
import com.chema.genapp.basedatos.Constantes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class F_Todas extends Fragment {

    FloatingActionButton FAB_AgregarPassword;
    RecyclerView recyclerView_Registros;
    BDHelper helper;

    Dialog dialog, dialogOrdenar;

    String ordenarNuevos = Constantes.C_TIEMPO_REGISTRO + " DESC";
    String ordenarAntiguos = Constantes.C_TIEMPO_REGISTRO + " ASC";
    String ordenarTituloAsc = Constantes.C_TITULO + " ASC";
    String ordenarTituloDesc = Constantes.C_TITULO + " DESC";
    String ordenElegido = ordenarTituloAsc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_f__todas, container, false);

        FAB_AgregarPassword = view.findViewById(R.id.FAB_AgregarPassword);
        recyclerView_Registros = view.findViewById(R.id.recyclerView_Registros);
        helper = new BDHelper(getActivity());

        dialog = new Dialog(getActivity());
        dialogOrdenar = new Dialog(getActivity());

        cargarRegistros(ordenarNuevos);

        FAB_AgregarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AgregarActualizarPassword.class); //Uso getActivity ya que estoy en un fragment
                intent.putExtra("editar", false);
                startActivity(intent);
            }
        });
        return view;
    }

    private void cargarRegistros(String orden) {

        ordenElegido = orden;

        Adaptador_pass adaptador_pass = new Adaptador_pass(getActivity(),helper.ObtenerRegistros(orden));
        recyclerView_Registros.setAdapter(adaptador_pass);

    }

    private void buscarRegistros(String titulo){
        Adaptador_pass adaptador_pass = new Adaptador_pass(getActivity(),helper.FiltrarRegistros(titulo));
        recyclerView_Registros.setAdapter(adaptador_pass);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragmento_todos,menu);
        MenuItem item = menu.findItem(R.id.menu_buscar_registros);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /*Realiza la busqueda de un registro cuando el usaurio presiona el icono de search*/
                buscarRegistros(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                /*Realiza la busqueda de un registro mientras el usuario escribe*/
                buscarRegistros(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        if(id == R.id.menu_numero_registros){
            Visualizar_Total_Registros();
            return true;
        }

        if(id == R.id.menu_ordenar_registros){
            OrdenarRegistros();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        cargarRegistros(ordenElegido);
    }

    private void Visualizar_Total_Registros(){
        TextView total;
        Button btnEntendido;

        dialog.setContentView(R.layout.cuadro_dialogo_total_registros);

        total = dialog.findViewById(R.id.total);
        btnEntendido = dialog.findViewById(R.id.btnEntendidoTotal);

        //Obtengo el número de registros
        String totalRegistros = String.valueOf(helper.ObtenerNumeroRegistros());
        total.setText(totalRegistros);

        btnEntendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss(); //Cierro el cuadro de dialogo
            }
        });

        //Inicamos el cuadro de dialogo
        dialog.show();
        dialog.setCancelable(false);

    }

    //Método que ordena los registros
    private void OrdenarRegistros(){
        Button btnNuevos, btnAntiguos, btnAZ, btnZA;

        dialogOrdenar.setContentView(R.layout.cuadro_dialogo_ordenar_registros);

        btnNuevos = dialogOrdenar.findViewById(R.id.btn_nuevos);
        btnAntiguos = dialogOrdenar.findViewById(R.id.btn_antiguos);
        btnAZ = dialogOrdenar.findViewById(R.id.btn_AZ);
        btnZA = dialogOrdenar.findViewById(R.id.btn_ZA);

        //EVENTOS DE LOS BOTONES
        btnNuevos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarRegistros(ordenarNuevos);
                dialogOrdenar.dismiss();
            }
        });

        btnAntiguos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarRegistros(ordenarAntiguos);
                dialogOrdenar.dismiss();
            }
        });

        btnAZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarRegistros(ordenarTituloAsc);
                dialogOrdenar.dismiss();
            }
        });

        btnZA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarRegistros(ordenarTituloDesc);
                dialogOrdenar.dismiss();
            }
        });

        dialogOrdenar.show();
        dialogOrdenar.setCancelable(true);


    }
}