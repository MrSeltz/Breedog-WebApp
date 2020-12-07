package it.unipd.dei.breedog.resource;

import com.fasterxml.jackson.core.*;

import java.io.*;

/**
 * Represents the data about a breed.
 * 
 */
public class Breed extends Resource {

    /**
     * The fci (identifier) of the breed
     */
    private final int fci;

    /**
     * The group of the breed
     */
    private final int bgroup;

    /**
     * The name of the breed
     */
    private final String bname;

    /**
     * The counter of the breeds
     */
    private final int count;

    /**
     * Creates a new breed
     * 
     * @param fci    the fci code of the breed
     * @param bgroup the group name of the breed
     * @param bname  the name of the breed
     * @param count  the count of the breed
     */
    public Breed(final int fci, final int bgroup, final String bname, final int count) {
        this.fci = fci;
        this.bgroup = bgroup;
        this.bname = bname;

        if (count > 0)
            this.count = count;
        else
            this.count = -1;
    }

    /**
     * Returns the fci code of the breed.
     * 
     * @return the fci of the breed.
     */
    public final String getFci() {
        return String.valueOf(fci);
    }

    /**
     * Returns the group name of the breed.
     * 
     * @return the group name of the breed.
     */
    public final String getBgroup() {
        return String.valueOf(bgroup);
    }

    /**
     * Returns the name of the breed.
     * 
     * @return the name of the breed.
     */
    public final String getBname() {
        return bname;
    }

    /**
     * Returns the counter of the breed.
     * 
     * @return the counter of the breed.
     */
    public final String getCount() {
        return String.valueOf(count);
    }

    // Create JSON data from breeder object
    public final void toJSON(final OutputStream out) throws IOException {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        jg.writeFieldName("breed");
        jg.writeStartObject();
        jg.writeStringField("fci", getFci());
        jg.writeStringField("bgroup", getBgroup());
        jg.writeStringField("bname", getBname());
        jg.writeStringField("bcount", getCount());
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    // Create BREED from JSON
    public static Breed fromJSON(final InputStream in) throws IOException {
        int jfci = -1;
        int jbgroup = -1;
        String jbname = null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        // while we are not on the start of an element or the element is not
        // a token element, advance to the next element (if any)
        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "breed".equals(jp.getCurrentName()) == false) {
            // there are no more events
            if (jp.nextToken() == null)
                throw new IOException("Unable to parse JSON: no breed object found.");
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {
            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                switch (jp.getCurrentName()) {
                    case "fci":
                        jp.nextToken();
                        jfci = Integer.parseInt(jp.getText());
                        break;
                    case "group":
                        jp.nextToken();
                        jbgroup = Integer.parseInt(jp.getText());
                        break;
                    case "name":
                        jp.nextToken();
                        jbname = jp.getText();
                        break;
                }
            }
        }

        // Force count value to -1 because that parameter is used only in clientside
        // after queing the DB
        return new Breed(jfci, jbgroup, jbname, -1);
    }
}