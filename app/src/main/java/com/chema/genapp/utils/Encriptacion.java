package com.chema.genapp.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encriptacion {

    public SecretKeySpec CrearClave(String llave){
        try{
            SecretKeySpec secretKey = new SecretKeySpec(llave.getBytes(),  0, 16, "AES");
            return secretKey;
        }catch (Exception e){
            return null;
        }
    }

    public String Encriptar(String encriptar, String llave){
        try {
            SecretKeySpec secretKey = CrearClave(llave);

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] datosEncriptar = encriptar.getBytes("UTF-8");
            byte[] bytesEncriptados = cipher.doFinal(datosEncriptar);
            String encriptado = Base64.encodeToString(bytesEncriptados, Base64.DEFAULT);

            return encriptado;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String Desencriptar(String desencriptar, String llave) {
        try {
            SecretKeySpec secretKey = CrearClave(llave);

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] bytesEncriptados = Base64.decode(desencriptar.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
            byte[] datosDesencriptados = cipher.doFinal(bytesEncriptados);
            String datos = new String(datosDesencriptados);

            return datos;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

}
