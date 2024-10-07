package com.chema.genapp.basedatos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.chema.genapp.modelo.PasswordModel;

import java.util.ArrayList;

public class BDHelper extends SQLiteOpenHelper {

    public BDHelper(@Nullable Context context) {
        super(context, Constantes.BD_NAME, null, Constantes.BD_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constantes.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ Constantes.TABLE_NAME);
        onCreate(db);
    }

    public long InsertarRegistro(PasswordModel pass){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Constantes.C_TITULO, pass.getTitulo());
        values.put(Constantes.C_CUENTA, pass.getCuenta());
        values.put(Constantes.C_NOMBRE_USUARIO, pass.getUsuario());
        values.put(Constantes.C_PASSWORD, pass.getContrasena());
        values.put(Constantes.C_SITIO_WEB, pass.getSitioWeb());
        values.put(Constantes.C_NOTA, pass.getNota());
        values.put(Constantes.C_TIEMPO_REGISTRO, pass.getT_registro());
        values.put(Constantes.C_TIEMPO_ACTUALIZACION, pass.getT_actualizacion());

        long id = db.insert(Constantes.TABLE_NAME, "", values);

        db.close();

        return id;
    }

    public long ActualizarRegistro(PasswordModel pass){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Constantes.C_TITULO, pass.getTitulo());
        values.put(Constantes.C_CUENTA, pass.getCuenta());
        values.put(Constantes.C_NOMBRE_USUARIO, pass.getUsuario());
        values.put(Constantes.C_PASSWORD, pass.getContrasena());
        values.put(Constantes.C_SITIO_WEB, pass.getSitioWeb());
        values.put(Constantes.C_NOTA, pass.getNota());
        values.put(Constantes.C_TIEMPO_REGISTRO, pass.getT_registro());
        values.put(Constantes.C_TIEMPO_ACTUALIZACION, pass.getT_actualizacion());

        long id = db.update(Constantes.TABLE_NAME, values, Constantes.C_ID + " = "+pass.getId(), null );

        db.close();

        return id;
    }

    public int EliminarRegistro(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        int resultado = db.delete(Constantes.TABLE_NAME,Constantes.C_ID + " = "+id, null);

        db.close();

        return resultado;
    }

    public ArrayList<PasswordModel> ObtenerRegistros(String orderBy){
        ArrayList<PasswordModel> passwordList = new ArrayList<>();

        String query = "SELECT * FROM "+Constantes.TABLE_NAME + " ORDER BY "+ orderBy;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                @SuppressLint("Range") PasswordModel pass = new PasswordModel(
                        cursor.getInt(cursor.getColumnIndex(Constantes.C_ID))+"",
                        ""+cursor.getString(cursor.getColumnIndex(Constantes.C_TITULO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constantes.C_CUENTA)),
                        ""+cursor.getString(cursor.getColumnIndex(Constantes.C_NOMBRE_USUARIO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constantes.C_PASSWORD)),
                        ""+cursor.getString(cursor.getColumnIndex(Constantes.C_SITIO_WEB)),
                        ""+cursor.getString(cursor.getColumnIndex(Constantes.C_NOTA)),
                        ""+cursor.getString(cursor.getColumnIndex(Constantes.C_TIEMPO_REGISTRO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constantes.C_TIEMPO_ACTUALIZACION))
                );

                passwordList.add(pass);
            }while (cursor.moveToNext());
        }

        return passwordList;
    }

    public int ObtenerNumeroRegistros(){
        String countQuery = "SELECT * FROM " + Constantes.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        int contador = cursor.getCount();

        cursor.close();

        return contador;
    }

    public ArrayList<PasswordModel> FiltrarRegistros(String titulo){
        ArrayList<PasswordModel> passwordList = new ArrayList<>();

        String query = "SELECT * FROM "+Constantes.TABLE_NAME + " WHERE "+Constantes.C_TITULO+" LIKE '%"+titulo+"%'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                @SuppressLint("Range") PasswordModel pass = new PasswordModel(
                        cursor.getInt(cursor.getColumnIndex(Constantes.C_ID))+"",
                        ""+cursor.getString(cursor.getColumnIndex(Constantes.C_TITULO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constantes.C_CUENTA)),
                        ""+cursor.getString(cursor.getColumnIndex(Constantes.C_NOMBRE_USUARIO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constantes.C_PASSWORD)),
                        ""+cursor.getString(cursor.getColumnIndex(Constantes.C_SITIO_WEB)),
                        ""+cursor.getString(cursor.getColumnIndex(Constantes.C_NOTA)),
                        ""+cursor.getString(cursor.getColumnIndex(Constantes.C_TIEMPO_REGISTRO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constantes.C_TIEMPO_ACTUALIZACION))
                );

                passwordList.add(pass);
            }while (cursor.moveToNext());
        }

        return passwordList;
    }

    @SuppressLint("Range")
    public PasswordModel BuscarRegistrosId(String id) {
        PasswordModel pass = null;

        String query = "SELECT * FROM " + Constantes.TABLE_NAME + " WHERE " + Constantes.C_ID + " = " + Integer.parseInt(id);

        try {

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    pass = new PasswordModel(
                            cursor.getInt(cursor.getColumnIndex(Constantes.C_ID)) + "",
                            "" + cursor.getString(cursor.getColumnIndex(Constantes.C_TITULO)),
                            "" + cursor.getString(cursor.getColumnIndex(Constantes.C_CUENTA)),
                            "" + cursor.getString(cursor.getColumnIndex(Constantes.C_NOMBRE_USUARIO)),
                            "" + cursor.getString(cursor.getColumnIndex(Constantes.C_PASSWORD)),
                            "" + cursor.getString(cursor.getColumnIndex(Constantes.C_SITIO_WEB)),
                            "" + cursor.getString(cursor.getColumnIndex(Constantes.C_NOTA)),
                            "" + cursor.getString(cursor.getColumnIndex(Constantes.C_TIEMPO_REGISTRO)),
                            "" + cursor.getString(cursor.getColumnIndex(Constantes.C_TIEMPO_ACTUALIZACION))
                    );


                } while (cursor.moveToNext());
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

        return pass;
    }

    public void EliminarTodo(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+Constantes.TABLE_NAME);
        db.close();
    }
}