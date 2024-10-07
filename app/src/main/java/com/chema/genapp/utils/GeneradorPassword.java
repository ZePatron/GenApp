package com.chema.genapp.utils;

import java.util.ArrayList;
import java.util.Random;

public class GeneradorPassword {

    public static String conTodo(int longitud){
        String pass = "";
        Random rand = new Random();
        for(int i = 0; i < longitud; i++){
            pass += (char)(rand.nextInt(93)+32);
        }
        return pass;
    }

    public static String conLetrasNumeros(int longitud){
        //Primero creo un arrayList con los codigos ascii de los caracteres admitidos
        ArrayList<Integer> caracteresAscii = new ArrayList<>();

        for(int i = 48;i < 123;i++){
            if(i<58  || (i > 64 && i<91) ||(i>96 && i<123)){
                caracteresAscii.add(i);
            }
        }

        String pass = "";
        Random rand = new Random();
        for(int i = 0; i < longitud; i++){
            int caracter = caracteresAscii.get(rand.nextInt(caracteresAscii.size()));
            pass += (char)caracter;
        }
        return pass;
    }

    public static String conLetras(int longitud){
        //Primero creo un arrayList con los codigos ascii de los caracteres admitidos
        ArrayList<Integer> caracteresAscii = new ArrayList<>();

        for(int i = 65;i < 123;i++){
            if(i<58  || (i > 64 && i<91) ||(i>96 && i<123)){
                caracteresAscii.add(i);
            }
        }

        String pass = "";
        Random rand = new Random();
        for(int i = 0; i < longitud; i++){
            int caracter = caracteresAscii.get(rand.nextInt(caracteresAscii.size()));
            pass += (char)caracter;
        }
        return pass;
    }

}
