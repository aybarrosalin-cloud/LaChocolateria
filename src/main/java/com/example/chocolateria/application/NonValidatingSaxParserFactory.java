package com.example.chocolateria.application;

import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.xml.BaseSaxParserFactory;

import javax.xml.parsers.SAXParser;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.map.ReferenceMap;

public class NonValidatingSaxParserFactory extends BaseSaxParserFactory {

    public NonValidatingSaxParserFactory(JasperReportsContext context) {
        super(context);
        System.out.println("[SAX FACTORY] NonValidatingSaxParserFactory created");
    }

    @Override
    protected boolean isValidating() {
        return false;
    }

    @Override
    protected List<String> getSchemaLocations() {
        return Collections.emptyList();
    }

    @Override
    protected void configureParser(SAXParser parser) {
        try {
            var reader = parser.getXMLReader();
            reader.setFeature("http://apache.org/xml/features/validation/schema", false);
            reader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", false);
            reader.setFeature("http://xml.org/sax/features/validation", false);
            reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    protected ThreadLocal<ReferenceMap<Object, Object>> getGrammarPoolCache() {
        return null;
    }
}