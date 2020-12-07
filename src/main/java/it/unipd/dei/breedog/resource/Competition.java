package it.unipd.dei.breedog.resource;

import com.fasterxml.jackson.core.*;

import java.io.*;

/**
 * Represents the data about a competition.
 * 
 */
public class Competition extends Resource {

    /**
     * The name of the competition
     */
    private final String win;

    /**
     * The id (identifier) of the competition
     */
    private final String comp_id;

    /**
     * The type of the competition
     */
    private final String comp_type;

    /**
     * The group of the competition
     */
    private final String comp_group;

    /**
     * The class of the competition
     */
    private final String comp_class;

    /**
     * The id of the event
     */
    private final String event_id;

    /**
     * Creates a new competition
     * 
     * @param win        the winner of the competition
     * @param comp_id    the id of the competition
     * @param comp_type  the type of the competition
     * @param comp_group the group of the competition
     * @param comp_class the class of the competition
     * @param event_id   the id of the event
     */
    public Competition(final String win, final String comp_id, final String comp_type, final String comp_group,
            final String comp_class, final String event_id) {
        this.win = win;
        this.comp_id = comp_id;
        this.comp_type = comp_type;
        this.comp_group = comp_group;
        this.comp_class = comp_class;
        this.event_id = event_id;
    }

    /**
     * Returns the winner of the competition.
     * 
     * @return the winner of the competition.
     */
    public final String getWin() {
        return win;
    }

    /**
     * Returns the id of the competition.
     * 
     * @return the id of the competition.
     */
    public final String getCompID() {
        return comp_id;
    }

    /**
     * Returns the type of the competition.
     * 
     * @return the type of the competition.
     */
    public final String getType() {
        return comp_type;
    }

    /**
     * Returns the group of the competition.
     * 
     * @return the group of the competition.
     */
    public final String getGroup() {
        return comp_group;
    }

    /**
     * Returns the class of the competition.
     * 
     * @return the class of the competition.
     */
    public final String getCompClass() {
        return comp_class;
    }

    /**
     * Returns the id of the event.
     * 
     * @return the id of the event.
     */
    public final String getEventID() {
        return event_id;
    }

    // Create JSON data from competition object
    public final void toJSON(final OutputStream out) throws IOException {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        jg.writeFieldName("competition");
        jg.writeStartObject();
        jg.writeStringField("cwin", getWin());
        jg.writeStringField("cid", getCompID());
        jg.writeStringField("ctype", getType());
        jg.writeStringField("cgroup", getGroup());
        jg.writeStringField("cclass", getCompClass());
        jg.writeStringField("ceventid", getEventID());
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    // Create competition from JSON
    public static Competition fromJSON(final InputStream in) throws IOException {
        String jwin = null;
        String jcomp_id = null;
        String jcomp_type = null;
        String jcomp_group = null;
        String jcomp_class = null;
        String jevent_id = null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "competition".equals(jp.getCurrentName()) == false) {
            // there are no more events
            if (jp.nextToken() == null)
                throw new IOException("Unable to parse JSON: no competition object found.");
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {
            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                switch (jp.getCurrentName()) {
                    case "win":
                        jp.nextToken();
                        jwin = jp.getText();
                        break;
                    case "comp_id":
                        jp.nextToken();
                        jcomp_id = jp.getText();
                        break;
                    case "comp_type":
                        jp.nextToken();
                        jcomp_type = jp.getText();
                        break;
                    case "comp_group":
                        jp.nextToken();
                        jcomp_group = jp.getText();
                        break;
                    case "comp_class":
                        jp.nextToken();
                        jcomp_class = jp.getText();
                        break;
                    case "event_id":
                        jp.nextToken();
                        jevent_id = jp.getText();
                        break;
                }
            }
        }
        return new Competition(jwin, jcomp_id, jcomp_type, jcomp_group, jcomp_class, jevent_id);
    }
}