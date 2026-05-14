package com.example.chocolateria.controller;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;

public class JasperReportUtil {

    public static void mostrarReporte(String jrxmlPath, Map<String, Object> params, Connection conn) {
        try {
            InputStream jrxmlStream = JasperReportUtil.class.getResourceAsStream(jrxmlPath);
            if (jrxmlStream == null) {
                throw new RuntimeException("No se encontró el archivo: " + jrxmlPath);
            }
            JasperPrint print = JasperFillManager.fillReport(
                    JasperCompileManager.compileReport(jrxmlStream),
                    params,
                    conn
            );
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setVisible(true);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte: " + e.getMessage(), e);
        }
    }
}
