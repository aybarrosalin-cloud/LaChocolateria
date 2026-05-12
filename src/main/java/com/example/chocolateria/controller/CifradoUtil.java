package com.example.chocolateria.controller;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

// sha256 + salt para las claves, las viejas se migran solas cuando el usuario entra
public class CifradoUtil {

    private static final String SEPARADOR = ":SHA256:";


    public static String hashPassword(String password) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hash = md.digest(password.getBytes("UTF-8"));

            String saltB64 = Base64.getEncoder().encodeToString(salt);
            String hashB64 = Base64.getEncoder().encodeToString(hash);
            return saltB64 + SEPARADOR + hashB64;

        } catch (Exception e) {
            throw new RuntimeException("Error al cifrar la contraseña: " + e.getMessage(), e);
        }
    }


    public static boolean verificarPassword(String passwordIngresado, String storedValue) {
        if (passwordIngresado == null || storedValue == null) return false;

        // Si no tiene el separador es contraseña vieja sin hash, se compara directo
        if (!storedValue.contains(SEPARADOR)) {
            return passwordIngresado.equals(storedValue);
        }

        try {
            String[] partes = storedValue.split(SEPARADOR, 2);
            byte[] salt             = Base64.getDecoder().decode(partes[0]);
            byte[] hashAlmacenado   = Base64.getDecoder().decode(partes[1]);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashIngresado = md.digest(passwordIngresado.getBytes("UTF-8"));

            // isEqual va byte por byte, así tarda lo mismo si falla al principio o al final
            return MessageDigest.isEqual(hashIngresado, hashAlmacenado);

        } catch (Exception e) {
            return false;
        }
    }

    // si tiene el separador es hash, si no es texto plano todavía
    public static boolean esHashSeguro(String storedValue) {
        return storedValue != null && storedValue.contains(SEPARADOR);
    }
}
