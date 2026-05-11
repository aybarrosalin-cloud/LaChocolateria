package com.example.chocolateria.controller;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

// Manejo de contraseñas con SHA-256 + salt
// Las contraseñas viejas en texto plano siguen funcionando y se migran solas al primer login
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

            // isequal compara byte a byte en tiempo constante para no filtrar info por timing
            return MessageDigest.isEqual(hashIngresado, hashAlmacenado);

        } catch (Exception e) {
            return false;
        }
    }

    // Devuelve true si la contraseña guardada ya está hasheada (no es texto plano)
    public static boolean esHashSeguro(String storedValue) {
        return storedValue != null && storedValue.contains(SEPARADOR);
    }
}
