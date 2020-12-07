package it.unipd.dei.breedog.resource;

import com.fasterxml.jackson.core.*;

import java.io.*;

/**
 * Represents the data about a user.
 * 
 */
public class Account extends Resource {

    /**
     * The username (identifier) of the user
     */
    private final String username;

    /**
     * The password of the user
     */
    private final String password;

    /**
     * The breederfc of the user
     */
    private final String breederfc;

    /**
     * Creates a new user
     * 
     * @param username  the username of the user
     * @param password  the password of the user
     * @param breederfc the breederfc of the user
     */
    public Account(final String username, final String password, final String breederfc) {
        this.username = username;
        this.password = password;
        this.breederfc = breederfc;
    }

    /**
     * Returns the username of the user.
     * 
     * @return the username of the user.
     */
    public final String getUsername() {
        return username;
    }

    /**
     * Returns the password of the user.
     * 
     * @return the password of the user.
     */
    public final String getPassword() {
        return password;
    }

    /**
     * Returns the breederfc of the user.
     * 
     * @return the breederfc of the user.
     */
    public final String getBreederfc() {
        return breederfc;
    }

    // Create JSON data from Account Object
    @Override
    public final void toJSON(final OutputStream out) throws IOException {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        jg.writeFieldName("account");
        jg.writeStartObject();
        jg.writeStringField("username", username);
        jg.writeStringField("password", password);
        jg.writeStringField("breederfc", breederfc);
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    // Create Account Object from JSON data
    public static Account fromJSON(final InputStream in) throws IOException {
        String jusername = null;
        String jpassword = null;
        String jbreederfc = null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        // while we are not on the start of an element or the element is not
        // a token element, advance to the next element (if any)
        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "account".equals(jp.getCurrentName()) == false) {
            // there are no more events
            if (jp.nextToken() == null)
                throw new IOException("Unable to parse JSON: no account object found.");
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {
            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                switch (jp.getCurrentName()) {
                    case "username":
                        jp.nextToken();
                        jusername = jp.getText();
                        break;
                    case "password":
                        jp.nextToken();
                        jpassword = jp.getText();
                        break;
                    case "breederfc":
                        jp.nextToken();
                        jbreederfc = jp.getText();
                        break;
                }
            }
        }
        return new Account(jusername, jpassword, jbreederfc);
    }
}