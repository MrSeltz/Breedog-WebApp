package it.unipd.dei.breedog.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * Represents the data about a relation dog/pathology.
 * 
 */
public class Whichas extends Resource {

    /**
     * The microchip (identifier) of the dog
     */
    private final String dog;

    /**
     * The code (identifier) of the pathology
     */
    private final String pathology;

    /**
     * The severity of the pathology
     */
    private final String severity;

    /**
     * Creates a new genealogy
     * 
     * @param dog       the microchip of the dog
     * @param pathology the code of the pathology
     * @param severity  the severity of the pathology
     */
    public Whichas(final String dog, final String pathology, final String severity) {

        this.dog = dog;
        this.pathology = pathology;
        this.severity = severity;

    }

    /**
     * Returns the microchip of the dog.
     * 
     * @return the microchip of the dog.
     */
    public final String getDog() {
        return dog;
    }

    /**
     * Returns the code of the pathology.
     * 
     * @return the code of the pathology.
     */
    public final String getPathology() {
        return pathology;
    }

    /**
     * Returns the severity of the pathology.
     * 
     * @return the severity of the pathology.
     */
    public final String getSeverity() {
        return severity;
    }

    // Create JSON data from dog/pathology object
    public final void toJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        ;
        jg.writeFieldName("whichas");
        jg.writeStartObject();
        jg.writeStringField("dog", dog);
        jg.writeStringField("pathology", pathology);
        jg.writeStringField("severity", severity);
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    // Create dog/pathology from JSON
    public static Whichas fromJSON(final InputStream in) throws IOException {
        String jdog = null;
        String jpathology = null;
        String jseverity = null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "whichas".equals(jp.getCurrentName()) == false) {
            // there are no more events
            if (jp.nextToken() == null)
                throw new IOException("Unable to parse JSON: no succeed object found.");
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {
            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                switch (jp.getCurrentName()) {
                    case "dog":
                        jp.nextToken();
                        jdog = jp.getText();
                        break;
                    case "pathology":
                        jp.nextToken();
                        jpathology = jp.getText();
                        break;
                    case "severity":
                        jp.nextToken();
                        jseverity = jp.getText();
                        break;
                }
            }
        }

        return new Whichas(jdog, jpathology, jseverity);

    }

}