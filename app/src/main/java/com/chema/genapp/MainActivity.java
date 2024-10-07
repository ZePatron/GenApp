package com.chema.genapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Toast;

import com.chema.genapp.fragmentos.F_AcercaDe;
import com.chema.genapp.fragmentos.F_Ajustes;
import com.chema.genapp.fragmentos.F_Todas;
import com.chema.genapp.login.LoginUsuarioRegistrado;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    boolean dobleClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null); //Mantiene el color original de los iconos

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Fragmente que se ejecuta al iniciar la aplicación
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmeent_container, new F_Todas()).commit();
            navigationView.setCheckedItem(R.id.Opcion_Todas);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.Opcion_Todas){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmeent_container, new F_Todas()).commit();
        }
        if (id == R.id.Opcion_Ajustes){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmeent_container, new F_Ajustes()).commit();
        }
        if(id == R.id.Opcion_AcercaDe){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmeent_container, new F_AcercaDe()).commit();
        }
        if(id == R.id.Opcion_Salir){
            CerrarSesion();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        //Compruebo si se ha hecho doble click para salir
        if(dobleClick){
            super.onBackPressed();
            Toast.makeText(this, "Saliste de la aplicación", Toast.LENGTH_SHORT).show();
            return;
        }
        //Acción al hacer un click
        this.dobleClick = true;
        Toast.makeText(this, "Presione dos veces para salir", Toast.LENGTH_SHORT).show();

        //Al pasar dos segundos doble click se vuelve a tener valor false
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                dobleClick = false;
            }
        }, 2000);

    }

    private void CerrarSesion(){
        startActivity(new Intent(MainActivity.this, LoginUsuarioRegistrado.class));
        finish();
        Toast.makeText(this, "Cerraste la sesión", Toast.LENGTH_SHORT).show();
    }
}