package it.unipd.dei.breedog.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * Represents the data about a genealogy.
 * 
 */
public class Genealogy extends Resource {

    /**
     * The microchip (identifier) of the predecessor
     */
    private final String predecessor;

    /**
     * The microchip (identifier) of the successor
     */
    private final String successor;

    /**
     * The date of the birth
     */
    private final String datebirth;

    /**
     * Creates a new genealogy
     * 
     * @param predecessor the microchip of the predecessor
     * @param successor   the microchip of the successor
     * @param datebirth   the date of the birth
     */
    public Genealogy(final String predecessor, final String successor, final String datebirth) {

        this.predecessor = predecessor;
        this.successor = successor;
        this.datebirth = datebirth;

    }

    /**
     * Returns the microchip of the predecessor.
     * 
     * @return the microchip of the predecessor.
     */
    public final String getPredecessor() {
        return predecessor;
    }

    /**
     * Returns the microchip of the successor.
     * 
     * @return the microchip of the successor.
     */
    public final String getSuccessor() {
        return successor;
    }

    /**
     * Returns the date of the birth.
     * 
     * @return the date of the birth.
     */
    public final String getDatebirth() {
        return datebirth;
    }

    // Create JSON data from Genealogy object
    public final void toJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        ;
        jg.writeFieldName("succeed");
        jg.writeStartObject();
        jg.writeStringField("predecessor", predecessor);
        jg.writeStringField("successor", successor);
        jg.writeStringField("datebirth", datebirth);
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    // Create Genealogy from JSON
    public static Genealogy fromJSON(final InputStream in) throws IOException {
        String jpredecessor = null;
        String jsuccessor = null;
        String jdatebirth = null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "succeed".equals(jp.getCurrentName()) == false) {
            // there are no more events
            if (jp.nextToken() == null)
                throw new IOException("Unable to parse JSON: no succeed object found.");
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {
            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                switch (jp.getCurrentName()) {
                    case "predecessor":
                        jp.nextToken();
                        jpredecessor = jp.getText();
                        break;
                    case "successor":
                        jp.nextToken();
                        jsuccessor = jp.getText();
                        break;
                    case "datebirth":
                        jp.nextToken();
                        jdatebirth = jp.getText();
                        break;
                }
            }
        }

        return new Genealogy(jpredecessor, jsuccessor, jdatebirth);

    }

}