package com.example.chocolateria.application;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperCompileManager;
import java.io.File;

public class CompileReports {
    public static void main(String[] args) throws Exception {
        JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance())
            .setProperty(
                "net.sf.jasperreports.compiler.xml.parser.factory",
                "com.example.chocolateria.application.NonValidatingSaxParserFactory"
            );
        String outputDir = args.length > 0 ? args[0] : "target/classes/reportes";
        new File(outputDir).mkdirs();
        File reportDir = new File("src/main/resources/reportes");
        File[] files = reportDir.listFiles((dir, name) -> name.endsWith(".jrxml"));
        if (files != null) {
            for (File f : files) {
                String jasperPath = outputDir + "/" + f.getName().replace(".jrxml", ".jasper");
                JasperCompileManager.compileReportToFile(f.getAbsolutePath(), jasperPath);
                System.out.println("Compiled: " + f.getName() + " -> " + jasperPath);
            }
        }
    }
}
