package com.example.chocolateria.controller;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import java.awt.Desktop;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.Map;

public class JasperReportUtil {

    static {
        JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance())
            .setProperty(
                "net.sf.jasperreports.compiler.xml.parser.factory",
                "com.example.chocolateria.application.NonValidatingSaxParserFactory"
            );
    }

    public static void mostrarReporte(String ruta, Map<String, Object> params, Connection conn) {
        try {
            InputStream jrxmlStream = JasperReportUtil.class.getResourceAsStream(ruta);
            if (jrxmlStream == null) {
                throw new RuntimeException("No se encontró " + ruta);
            }

            String jrxml = new String(jrxmlStream.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
            String cleaned = jrxml.replaceAll("\\suuid=\"[^\"]*\"", "");
            JasperReport report = JasperCompileManager.compileReport(
                new java.io.ByteArrayInputStream(cleaned.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
            System.out.println("[REPORTE] Query: " + report.getQuery().getText());
            System.out.println("[REPORTE] Campos: " + report.getFields().length);
            System.out.println("[REPORTE] Title height: " + (report.getTitle() != null ? report.getTitle().getHeight() : "null"));
            System.out.println("[REPORTE] PH height: " + (report.getPageHeader() != null ? report.getPageHeader().getHeight() : "null"));
            if (report.getTitle() != null) {
                var titleElems = report.getTitle().getElements();
                System.out.println("[REPORTE] Title elements: " + titleElems.length);
                for (int i = 0; i < Math.min(3, titleElems.length); i++) {
                    System.out.println("[REPORTE]   title[" + i + "] class=" + titleElems[i].getClass().getSimpleName()
                        + " w=" + titleElems[i].getWidth() + " h=" + titleElems[i].getHeight());
                }
            }

            JasperPrint print = JasperFillManager.fillReport(report, params, conn);
            System.out.println("[REPORTE] Páginas: " + print.getPages().size());

            var page = print.getPages().get(0);
            var elems = page.getElements();
            System.out.println("[REPORTE] Elementos pág 1: " + elems.size());
            for (int i = 0; i < Math.min(10, elems.size()); i++) {
                var e = elems.get(i);
                System.out.println("[REPORTE]   elem[" + i + "] class=" + e.getClass().getSimpleName()
                    + " x=" + e.getX() + " y=" + e.getY()
                    + " w=" + e.getWidth() + " h=" + e.getHeight());
            }

            Path tmp = Files.createTempFile("reporte_", ".pdf");
            JasperExportManager.exportReportToPdfFile(print, tmp.toString());
            System.out.println("[REPORTE] PDF: " + tmp);
            Desktop.getDesktop().open(tmp.toFile());

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
        }
    }
}
