package it.unipd.dei.breedog.resource;

import com.fasterxml.jackson.core.*;

import java.io.*;

/**
 * Represents the data about a pathology.
 * 
 */
public class Pathology extends Resource {

    /**
     * The code (identifier) of the pathology
     */
    private final String pcode;

    /**
     * The name of the pathology
     */
    private final String pname;

    /**
     * The severity of the pathology
     */
    private final String pseverity;

    /**
     * Creates a new genealogy
     * 
     * @param pathology_code the code of the pathology
     * @param pathology_name   the name of the pathology
     * @param pathology_severity   the severity of the pathology
     */
    public Pathology(final String pathology_code, final String pathology_name, final String pathology_severity)
    {
        this.pcode = pathology_code;
        this.pname = pathology_name;
        this.pseverity = pathology_severity;
    }

    /**
     * Returns the code of the pathology.
     * 
     * @return the code of the pathology.
     */
    public final String getPcode()
    {
        return pcode;
    }

    /**
     * Returns the name of the pathology.
     * 
     * @return the name of the pathology.
     */
    public final String getPname()
    {
        return pname;
    }

    /**
     * Returns the severity of the pathology.
     * 
     * @return the severity of the pathology.
     */
    public final String getPseverity()
    {
        return pseverity;
    }


    // Create JSON data from pathology object
    public final void toJSON(final OutputStream out) throws IOException
    {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        jg.writeFieldName("pathology");
        jg.writeStartObject();
        jg.writeStringField("pcode", getPcode());
        jg.writeStringField("pname", getPname());
        jg.writeStringField("pseverity", getPseverity());
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    // Create pathology from JSON
    public  static Pathology fromJSON(final InputStream in) throws IOException
    {
        String jpcode = null;
        String jpname = null;
        String jpseverity = null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "pathology".equals(jp.getCurrentName()) == false) {
            // there are no more events
            if (jp.nextToken() == null)
                throw new IOException("Unable to parse JSON: no pathology object found.");
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {
            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                switch (jp.getCurrentName()) {
                    case "code":
                        jp.nextToken();
                        jpcode = jp.getText();
                        break;
                    case "name":
                        jp.nextToken();
                        jpname = jp.getText();
                        break;
                    case "severity":
                        jp.nextToken();
                        jpseverity = jp.getText();
                        break;
                }
            }
        }
        return new Pathology(jpcode, jpname, jpseverity);
    }
}