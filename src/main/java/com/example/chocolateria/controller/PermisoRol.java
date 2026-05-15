package com.example.chocolateria.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PermisoRol {

    public enum Pantalla {
        ORDEN_CLIENTE, PAGO_VENTA, GESTION_ENVIOS, GESTION_RECLAMOS,
        SOLICITUD_PRODUCCION, ORDEN_PRODUCCION,
        RECEPCION, SALIDA_MATERIALES, SALIDA_PRODUCTOS,
        ORDEN_PROVEEDOR, PAGO_COMPRA,
        REG_PRODUCTO, REG_EMPLEADO, REG_CLIENTE, REG_SUPLIDOR, REG_MAQUINARIA,
        MANTENIMIENTO,
        CONSULTAS,
        GESTION_USUARIOS
    }

    // consultas individuales dentro de la pantalla de consultas
    public enum Consulta {
        CLIENTES, VENTAS, COMPRAS, PRODUCCION, INVENTARIO, PEDIDOS,
        INGRESOS,       // requiere clave gerencial si no tiene acceso libre
        MAS_VENDIDOS,   // requiere clave gerencial si no tiene acceso libre
        MANTENIMIENTO
    }

    // roles con acceso libre a ingresos y mas vendidos (sin clave)
    private static final Set<String> ROLES_FINANCIEROS = Set.of(
        "Administrador", "Gerente General", "Auditor", "Encargado de Finanzas"
    );

    // roles que pueden ver ingresos/mas vendidos con clave gerencial
    private static final Set<String> ROLES_CON_CLAVE_FINANCIERA = Set.of(
        "Vendedor", "Encargado de Compras"
    );

    private static final Map<String, Set<Pantalla>> PERMISOS        = new HashMap<>();
    private static final Map<String, Set<Consulta>> PERMISOS_CONSULTA = new HashMap<>();

    static {
        // pantallas

        // administrador - todo
        Set<Pantalla> todasLasPantallas = new HashSet<>();
        for (Pantalla p : Pantalla.values()) todasLasPantallas.add(p);
        PERMISOS.put("Administrador", todasLasPantallas);

        // gerente general - todo menos gestion usuarios
        PERMISOS.put("Gerente General", pantallas(
            Pantalla.ORDEN_CLIENTE, Pantalla.PAGO_VENTA, Pantalla.GESTION_ENVIOS, Pantalla.GESTION_RECLAMOS,
            Pantalla.SOLICITUD_PRODUCCION, Pantalla.ORDEN_PRODUCCION,
            Pantalla.RECEPCION,
            Pantalla.ORDEN_PROVEEDOR, Pantalla.PAGO_COMPRA,
            Pantalla.REG_PRODUCTO, Pantalla.REG_EMPLEADO, Pantalla.REG_CLIENTE,
            Pantalla.REG_SUPLIDOR, Pantalla.REG_MAQUINARIA,
            Pantalla.MANTENIMIENTO, Pantalla.CONSULTAS
        ));

        // auditor - solo consultas
        PERMISOS.put("Auditor", pantallas(Pantalla.CONSULTAS));

        // encargado de finanzas
        PERMISOS.put("Encargado de Finanzas", pantallas(
            Pantalla.PAGO_VENTA, Pantalla.PAGO_COMPRA, Pantalla.CONSULTAS
        ));


        // vendedor
        PERMISOS.put("Vendedor", pantallas(
            Pantalla.ORDEN_CLIENTE, Pantalla.PAGO_VENTA, Pantalla.GESTION_RECLAMOS,
            Pantalla.SALIDA_PRODUCTOS,
            Pantalla.SOLICITUD_PRODUCCION, Pantalla.REG_CLIENTE, Pantalla.CONSULTAS
        ));

        // encargado de produccion
        PERMISOS.put("Encargado de Producción", pantallas(
            Pantalla.SOLICITUD_PRODUCCION, Pantalla.ORDEN_PRODUCCION,
            Pantalla.SALIDA_MATERIALES,
            Pantalla.REG_MAQUINARIA, Pantalla.MANTENIMIENTO, Pantalla.CONSULTAS
        ));

        // operario de empaque - solo ver orden de produccion
        PERMISOS.put("Operario de Empaque", pantallas(Pantalla.ORDEN_PRODUCCION));

        // inspector de calidad
        PERMISOS.put("Inspector de Calidad", pantallas(
            Pantalla.GESTION_RECLAMOS, Pantalla.ORDEN_PRODUCCION,
            Pantalla.MANTENIMIENTO, Pantalla.CONSULTAS
        ));

        // encargado de almacen
        PERMISOS.put("Encargado de Almacén", pantallas(
            Pantalla.RECEPCION, Pantalla.REG_PRODUCTO, Pantalla.CONSULTAS
        ));

        // encargado de compras
        PERMISOS.put("Encargado de Compras", pantallas(
            Pantalla.ORDEN_PROVEEDOR, Pantalla.PAGO_COMPRA,
            Pantalla.REG_PRODUCTO, Pantalla.REG_SUPLIDOR, Pantalla.CONSULTAS
        ));

        // encargado de logistica
        PERMISOS.put("Encargado de Logística", pantallas(
            Pantalla.GESTION_ENVIOS, Pantalla.CONSULTAS
        ));

        // tecnico de mantenimiento
        PERMISOS.put("Técnico de Mantenimiento", pantallas(
            Pantalla.REG_MAQUINARIA, Pantalla.MANTENIMIENTO, Pantalla.CONSULTAS
        ));

        // consultas individuales

        Set<Consulta> todasLasConsultas = new HashSet<>();
        for (Consulta c : Consulta.values()) todasLasConsultas.add(c);

        PERMISOS_CONSULTA.put("Administrador",    todasLasConsultas);
        PERMISOS_CONSULTA.put("Gerente General",  todasLasConsultas);
        PERMISOS_CONSULTA.put("Auditor",          todasLasConsultas);

        PERMISOS_CONSULTA.put("Encargado de Finanzas", consultas(
            Consulta.CLIENTES, Consulta.VENTAS, Consulta.COMPRAS,
            Consulta.INGRESOS, Consulta.MAS_VENDIDOS, Consulta.PEDIDOS
        ));


        PERMISOS_CONSULTA.put("Vendedor", consultas(
            Consulta.CLIENTES, Consulta.VENTAS, Consulta.PEDIDOS
            // INGRESOS y MAS_VENDIDOS con clave gerencial
        ));

        PERMISOS_CONSULTA.put("Encargado de Producción", consultas(
            Consulta.PRODUCCION, Consulta.INVENTARIO, Consulta.PEDIDOS, Consulta.MANTENIMIENTO
        ));

        PERMISOS_CONSULTA.put("Operario de Empaque", consultas()); // sin consultas

        PERMISOS_CONSULTA.put("Inspector de Calidad", consultas(
            Consulta.PRODUCCION, Consulta.PEDIDOS, Consulta.MANTENIMIENTO
        ));

        PERMISOS_CONSULTA.put("Encargado de Almacén", consultas(
            Consulta.COMPRAS, Consulta.INVENTARIO
        ));

        PERMISOS_CONSULTA.put("Encargado de Compras", consultas(
            Consulta.COMPRAS, Consulta.INVENTARIO
            // INGRESOS y MAS_VENDIDOS con clave gerencial
        ));

        PERMISOS_CONSULTA.put("Encargado de Logística", consultas(
            Consulta.PEDIDOS
        ));

        PERMISOS_CONSULTA.put("Técnico de Mantenimiento", consultas(
            Consulta.MANTENIMIENTO
        ));
    }

    // api publica

    public static boolean tieneAcceso(String rol, Pantalla pantalla) {
        if (rol == null || rol.isBlank()) return false;
        Set<Pantalla> permisos = PERMISOS.get(rol);
        return permisos != null && permisos.contains(pantalla);
    }

    public static boolean tieneAccesoConsulta(String rol, Consulta consulta) {
        if (rol == null || rol.isBlank()) return false;
        Set<Consulta> permisos = PERMISOS_CONSULTA.get(rol);
        return permisos != null && permisos.contains(consulta);
    }

    // true = puede ver ingresos/masvendidos pero pide clave gerencial
    public static boolean requiereClaveFinanciera(String rol) {
        return ROLES_CON_CLAVE_FINANCIERA.contains(rol);
    }

    // true = acceso libre a datos financieros (sin clave extra)
    public static boolean esRolFinanciero(String rol) {
        return ROLES_FINANCIEROS.contains(rol);
    }

    // helpers privados

    private static Set<Pantalla> pantallas(Pantalla... ps) {
        Set<Pantalla> set = new HashSet<>();
        for (Pantalla p : ps) set.add(p);
        return set;
    }

    private static Set<Consulta> consultas(Consulta... cs) {
        Set<Consulta> set = new HashSet<>();
        for (Consulta c : cs) set.add(c);
        return set;
    }
}
