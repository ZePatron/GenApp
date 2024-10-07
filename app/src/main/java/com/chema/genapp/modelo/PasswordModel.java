package com.chema.genapp.modelo;

import java.io.Serializable;

public class PasswordModel implements Serializable {

    private String id;
    private String titulo, cuenta, usuario, contrasena, sitioWeb, nota, t_registro, t_actualizacion;

    public PasswordModel() {
    }

    //Contructor sin ID
    public PasswordModel(String titulo, String cuenta, String usuario, String contrasena, String sitioWeb, String nota,String t_registro, String t_actualizacion) {
        this.titulo = titulo;
        this.cuenta = cuenta;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.sitioWeb = sitioWeb;
        this.nota = nota;
        this.t_registro = t_registro;
        this.t_actualizacion = t_actualizacion;
    }

    //Contructor con ID
    public PasswordModel(String id, String titulo, String cuenta, String usuario, String contrasena, String sitioWeb, String nota, String t_registro, String t_actualizacion) {
        this.id = id;
        this.titulo = titulo;
        this.cuenta = cuenta;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.sitioWeb = sitioWeb;
        this.nota = nota;
        this.t_registro = t_registro;
        this.t_actualizacion = t_actualizacion;
    }

    //Getters & Setters


    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getT_registro() {
        return t_registro;
    }

    public void setT_registro(String t_registro) {
        this.t_registro = t_registro;
    }

    public String getT_actualizacion() {
        return t_actualizacion;
    }

    public void setT_actualizacion(String t_actualizacion) {
        this.t_actualizacion = t_actualizacion;
    }

    @Override //String con el formato para el csv
    public String toString() {
        return this.id+","+this.titulo+","+this.cuenta+","+this.usuario+","+this.contrasena+","
                +this.sitioWeb+","+this.nota+","+this.t_registro+","+this.t_actualizacion;
    }
}