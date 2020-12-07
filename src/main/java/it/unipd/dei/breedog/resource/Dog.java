package it.unipd.dei.breedog.resource;

import com.fasterxml.jackson.core.*;

import java.io.*;

/**
 * Represents the data about a dog.
 * 
 */
public class Dog extends Resource {

    /**
     * The microchip (identifier) of the dog
     */
    private final String microchip;

    /**
     * The tattoo of the dog
     */
    private final String tattoo;

    /**
     * The name of the dog
     */
    private final String name;

    /**
     * The birth of the dog
     */
    private final String birth;

    /**
     * The sex of the dog
     */
    private final String sex;

    /**
     * The height of the dog
     */
    private final double height;

    /**
     * The weight of the dog
     */
    private final double weight;

    /**
     * The coat of the dog
     */
    private final String coat;

    /**
     * The character of the dog
     */
    private final String character;

    /**
     * The dna of the dog
     */
    private final String dna;

    /**
     * The teeth structure of the dog
     */
    private final String teeth;

    /**
     * The signs of the dog
     */
    private final String signs;

    /**
     * The photo of the dog
     */
    private final String photo;

    /**
     * The owner of the dog
     */
    private final String owner;

    /**
     * The status of the dog
     */
    private final String status;

    /**
     * The fci of the breed
     */
    private final int fci;

    /**
     * The breeder fc of the breeder
     */
    private final String breederFc;

    /**
     * The name of the kennel
     */
    private final String kennel;

    /**
     * Creates a new dog
     * 
     * @param microchip the microchip of the dog
     * @param tattoo    the tattoo of the dog
     * @param name      the name of the dog
     * @param birth     the birth of the dog
     * @param sex       the sex of the dog
     * @param height    the height of the dog
     * @param weight    the weight of the dog
     * @param coat      the coat of the dog
     * @param character the character of the dog
     * @param dna       the dna of the dog
     * @param teeth     the teeth structure of the dog
     * @param signs     the signs of the dog
     * @param photo     the photo of the dog
     * @param owner     the owner of the dog
     * @param status    the status of the dog
     * @param fci       the fci of the breed
     * @param breederFc the breeder fc of the breeder
     * @param kennel    the name of the kennel
     */
    public Dog(final String microchip, final String tattoo, final String name, final String birth, final String sex,
            final double height, final double weight, final String coat, final String character, final String dna,
            final String teeth, final String signs, final String photo, final String owner, final String status,
            final int fci, final String breederFc, final String kennel) {

        this.microchip = microchip;
        this.tattoo = tattoo;
        this.name = name;
        this.birth = birth;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
        this.coat = coat;
        this.character = character;
        this.dna = dna;
        this.teeth = teeth;
        this.signs = signs;
        this.photo = photo;
        this.owner = owner;
        this.status = status;
        this.fci = fci;
        this.breederFc = breederFc;
        this.kennel = kennel;

    }

    /**
     * Returns the microchip of the dog.
     * 
     * @return the microchip of the dog.
     */
    public final String getMicrochip() {
        return microchip;
    }

    /**
     * Returns the tattoo of the dog.
     * 
     * @return the tattoo of the dog.
     */
    public final String getTattoo() {
        return tattoo;
    }

    /**
     * Returns the name of the dog.
     * 
     * @return the name of the dog.
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the birth of the dog.
     * 
     * @return the birth of the dog.
     */
    public final String getBirth() {
        return birth;
    }

    /**
     * Returns the sex of the dog.
     * 
     * @return the sex of the dog.
     */
    public final String getSex() {
        return sex;
    }

    /**
     * Returns the height of the dog.
     * 
     * @return the height of the dog.
     */
    public final double getHeight() {
        return height;
    }

    /**
     * Returns the weight of the dog.
     * 
     * @return the weight of the dog.
     */
    public final double getWeight() {
        return weight;
    }

    /**
     * Returns the coat of the dog.
     * 
     * @return the coat of the dog.
     */
    public final String getCoat() {
        return coat;
    }

    /**
     * Returns the character of the dog.
     * 
     * @return the character of the dog.
     */
    public final String getCharacter() {
        return character;
    }

    /**
     * Returns the dna of the dog.
     * 
     * @return the dna of the dog.
     */
    public final String getDna() {
        return dna;
    }

    /**
     * Returns the teeth structure of the dog.
     * 
     * @return the teeth structure of the dog.
     */
    public final String getTeeth() {
        return teeth;
    }

    /**
     * Returns the signs of the dog.
     * 
     * @return the signs of the dog.
     */
    public final String getSigns() {
        return signs;
    }

    /**
     * Returns the photo of the dog.
     * 
     * @return the photo of the dog.
     */
    public final String getPhoto() {
        return photo;
    }

    /**
     * Returns the owner of the dog.
     * 
     * @return the owner of the dog.
     */
    public final String getOwner() {
        return owner;
    }

    /**
     * Returns the status of the dog.
     * 
     * @return the status of the dog.
     */
    public final String getStatus() {
        return status;
    }

    /**
     * Returns the fci code of the breed.
     * 
     * @return the fci code of the breed.
     */
    public final int getFci() {
        return fci;
    }

    /**
     * Returns the breeder fc of the breeder.
     * 
     * @return the breeder fc of the breeder.
     */
    public final String getBreederFc() {
        return breederFc;
    }

    /**
     * Returns the name of the kennel.
     * 
     * @return the name of the kennel.
     */
    public final String getKennel() {
        return kennel;
    }

    // Create JSON data from Account Object
    @Override
    public final void toJSON(final OutputStream out) throws IOException {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        jg.writeFieldName("dog");
        jg.writeStartObject();
        jg.writeStringField("microchip", microchip);
        jg.writeStringField("tattoo", tattoo);
        jg.writeStringField("name", name);
        jg.writeStringField("birth", birth);
        jg.writeStringField("sex", sex);
        jg.writeStringField("height", Double.toString(height));
        jg.writeStringField("weight", Double.toString(weight));
        jg.writeStringField("coat", coat);
        jg.writeStringField("character", character);
        jg.writeStringField("dna", dna);
        jg.writeStringField("teeth", teeth);
        jg.writeStringField("signs", signs);
        jg.writeStringField("photo", photo);
        jg.writeStringField("owner", owner);
        jg.writeStringField("status", status);
        jg.writeStringField("fci", Integer.toString(fci));
        jg.writeStringField("breederfc", breederFc);
        jg.writeStringField("kennel", kennel);
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    // Create Account Object from JSON data
    public static Dog fromJSON(final InputStream in) throws IOException {
        String jmicrochip = null;
        String jtattoo = null;
        String jname = null;
        String jbirth = null;
        String jsex = null;
        double jheight = -1;
        double jweight = -1;
        String jcoat = null;
        String jcharacter = null;
        String jdna = null;
        String jteeth = null;
        String jsigns = null;
        String jphoto = null;
        String jowner = null;
        String jstatus = null;
        int jfci = -1;
        String jbreederfc = null;
        String jkennel = null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        // while we are not on the start of an element or the element is not
        // a token element, advance to the next element (if any)
        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "dog".equals(jp.getCurrentName()) == false) {
            // there are no more events
            if (jp.nextToken() == null)
                throw new IOException("Unable to parse JSON: no dog object found.");
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {
            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                switch (jp.getCurrentName()) {
                    case "microchip":
                        jp.nextToken();
                        jmicrochip = jp.getText();
                        break;
                    case "tattoo":
                        jp.nextToken();
                        jtattoo = jp.getText();
                        break;
                    case "name":
                        jp.nextToken();
                        jname = jp.getText();
                        break;
                    case "birth":
                        jp.nextToken();
                        jbirth = jp.getText();
                        break;
                    case "sex":
                        jp.nextToken();
                        jsex = jp.getText();
                        break;
                    case "height":
                        jp.nextToken();
                        jheight = Double.parseDouble(jp.getText());
                        break;
                    case "weight":
                        jp.nextToken();
                        jweight = Double.parseDouble(jp.getText());
                        break;
                    case "coat":
                        jp.nextToken();
                        jcoat = jp.getText();
                        break;
                    case "character":
                        jp.nextToken();
                        jcharacter = jp.getText();
                        break;
                    case "dna":
                        jp.nextToken();
                        jdna = jp.getText();
                        break;
                    case "teeth":
                        jp.nextToken();
                        jteeth = jp.getText();
                        break;
                    case "signs":
                        jp.nextToken();
                        jsigns = jp.getText();
                        break;
                    case "photo":
                        jp.nextToken();
                        jphoto = jp.getText();
                        break;
                    case "owner":
                        jp.nextToken();
                        jowner = jp.getText();
                        break;
                    case "status":
                        jp.nextToken();
                        jstatus = jp.getText();
                        break;
                    case "fci":
                        jp.nextToken();
                        jfci = Integer.parseInt(jp.getText());
                        break;
                    case "breederfc":
                        jp.nextToken();
                        jbreederfc = jp.getText();
                        break;
                    case "kennel":
                        jp.nextToken();
                        jkennel = jp.getText();
                        break;
                }
            }
        }
        return new Dog(jmicrochip, jtattoo, jname, jbirth, jsex, jheight, jweight, jcoat, jcharacter, jdna, jteeth,
                jsigns, jphoto, jowner, jstatus, jfci, jbreederfc, jkennel);
    }

}