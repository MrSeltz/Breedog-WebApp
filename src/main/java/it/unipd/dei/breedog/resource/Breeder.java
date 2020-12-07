package it.unipd.dei.breedog.resource;

import com.fasterxml.jackson.core.*;

import java.io.*;

/**
 * Represents the data about a breeder.
 * 
 */
public class Breeder extends Resource {

    /**
     * The breederfc (identifier) of the breeder
     */
    private final String breederfc;

    /**
     * The name of the breeder
     */
    private final String name;

    /**
     * The surname of the breeder
     */
    private final String surname;

    /**
     * The birth of the breeder
     */
    private final String birth;

    /**
     * The address of the breeder
     */
    private final String address;

    /**
     * The telephone of the breeder
     */
    private final String telephone;

    /**
     * The email of the breeder
     */
    private final String email;

    /**
     * The vat of the breeder
     */
    private final String vat;

    /**
     * The photo of the breeder
     */
    private final String photo;

    /**
     * The description of the breeder
     */
    private final String description;

    /**
     * Creates a new breeder
     * 
     * @param breederfc   the fc code of the breeder
     * @param name        the name of the breeder
     * @param surname     the surname of the breeder
     * @param birth       the birth of the breeder
     * @param address     the address of the breeder
     * @param telephone   the telephone of the breeder
     * @param email       the email of the breeder
     * @param vat         the vat of the breeder
     * @param photo       the photo of the breeder
     * @param description the description of the breeder
     */
    public Breeder(final String breederfc, final String name, final String surname, final String birth,
            final String address, final String telephone, final String email, final String vat, final String photo,
            final String description) {
        this.breederfc = breederfc;
        this.name = name;
        this.surname = surname;
        this.birth = birth;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
        this.vat = vat;
        this.photo = photo;
        this.description = description;

    }

    /**
     * Returns the fc code of the breeder.
     * 
     * @return the fc of the breeder.
     */
    public final String getBreederfc() {
        return breederfc;
    }

    /**
     * Returns the name of the breeder.
     * 
     * @return the name of the breeder.
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the surname of the breeder.
     * 
     * @return the surname of the breeder.
     */
    public final String getSurname() {
        return surname;
    }

    /**
     * Returns the birth of the breeder.
     * 
     * @return the birth of the breeder.
     */
    public final String getBirth() {
        return birth;
    }

    /**
     * Returns the address of the breeder.
     * 
     * @return the address of the breeder.
     */
    public final String getAddress() {
        return address;
    }

    /**
     * Returns the telephone of the breeder.
     * 
     * @return the telephone of the breeder.
     */
    public final String getTelephone() {
        return telephone;
    }

    /**
     * Returns the email of the breeder.
     * 
     * @return the email of the breeder.
     */
    public final String getEmail() {
        return email;
    }

    /**
     * Returns the vat of the breeder.
     * 
     * @return the vat of the breeder.
     */
    public final String getVat() {
        return vat;
    }

    /**
     * Returns the photo of the breeder.
     * 
     * @return the photo of the breeder.
     */
    public final String getPhoto() {
        return photo;
    }

    /**
     * Returns the description of the breeder.
     * 
     * @return the description of the breeder.
     */
    public final String getDescription() {
        return description;
    }

    // Create JSON data from Breeder Object
    public final void toJSON(final OutputStream out) throws IOException {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        jg.writeFieldName("breeder");
        jg.writeStartObject();
        jg.writeStringField("breederfc", breederfc);
        jg.writeStringField("name", name);
        jg.writeStringField("surname", surname);
        jg.writeStringField("birth", birth);
        jg.writeStringField("address", address);
        jg.writeStringField("telephone", telephone);
        jg.writeStringField("email", email);
        jg.writeStringField("vat", vat);
        jg.writeStringField("photo", photo);
        jg.writeStringField("description", description);
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /* NEED TO IMPLEMENT fromJSON function */
    public static Breeder fromJSON(final InputStream in) throws IOException {
        String jbreederfc = null;
        String jname = null;
        String jsurname = null;
        String jbirth = null;
        String jaddress = null;
        String jtelephone = null;
        String jemail = null;
        String jvat = null;
        String jphoto = null;
        String jdescription = null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        // while we are not on the start of an element or the element is not
        // a token element, advance to the next element (if any)
        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "breeder".equals(jp.getCurrentName()) == false) {
            // there are no more events
            if (jp.nextToken() == null)
                throw new IOException("Unable to parse JSON: no breeder object found.");
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {
            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                switch (jp.getCurrentName()) {
                    case "breederfc":
                        jp.nextToken();
                        jbreederfc = jp.getText();
                        break;
                    case "name":
                        jp.nextToken();
                        jname = jp.getText();
                        break;
                    case "surname":
                        jp.nextToken();
                        jsurname = jp.getText();
                        break;
                    case "birth":
                        jp.nextToken();
                        jbirth = jp.getText();
                        break;
                    case "address":
                        jp.nextToken();
                        jaddress = jp.getText();
                        break;
                    case "telephone":
                        jp.nextToken();
                        jtelephone = jp.getText();
                        break;
                    case "email":
                        jp.nextToken();
                        jemail = jp.getText();
                        break;
                    case "vat":
                        jp.nextToken();
                        jvat = jp.getText();
                        break;
                    case "photo":
                        jp.nextToken();
                        jphoto = jp.getText();
                        break;
                    case "description":
                        jp.nextToken();
                        jdescription = jp.getText();
                        break;
                }
            }
        }

        return new Breeder(jbreederfc, jname, jsurname, jbirth, jaddress, jtelephone, jemail, jvat, jphoto,
                jdescription);
    }
}