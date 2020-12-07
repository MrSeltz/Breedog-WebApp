package it.unipd.dei.breedog.resource;

import com.fasterxml.jackson.core.*;

import java.io.*;

/**
 * Represents the data about an event.
 * 
 */
public class Event extends Resource {

    /**
     * The id (identifier) of the event
     */
    private final String event_id;

    /**
     * The location of the event
     */
    private final String event_location;

    /**
     * The beginning of the event
     */
    private final String event_begin;

    /**
     * The ending of the event
     */
    private final String event_finish;

    /**
     * The zip code of the event
     */
    private final String event_zip;

    /**
     * Creates a new event
     * 
     * @param event_id       the id of the event
     * @param event_location the location of the event
     * @param event_begin    the beginning of the event
     * @param event_finish   the ending of the event
     * @param event_zip      the zip code of the event
     */
    public Event(final String event_id, final String event_location, final String event_begin,
            final String event_finish, final String event_zip) {
        this.event_id = event_id;
        this.event_location = event_location;
        this.event_begin = event_begin;
        this.event_finish = event_finish;
        this.event_zip = event_zip;
    }

    /**
     * Returns the id of the event.
     * 
     * @return the id of the event.
     */
    public final String getEventID() {
        return event_id;
    }

    /**
     * Returns the location of the event.
     * 
     * @return the location of the event.
     */
    public final String getLocation() {
        return event_location;
    }

    /**
     * Returns the beginning of the event.
     * 
     * @return the beginning of the event.
     */
    public final String getBegin() {
        return event_begin;
    }

    /**
     * Returns the ending of the event.
     * 
     * @return the ending of the event.
     */
    public final String getFinish() {
        return event_finish;
    }

    /**
     * Returns the zip code of the event.
     * 
     * @return the zip code of the event.
     */
    public final String getZip() {
        return event_zip;
    }

    // Create JSON data from event object
    public final void toJSON(final OutputStream out) throws IOException {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        jg.writeFieldName("event");
        jg.writeStartObject();
        jg.writeStringField("eid", getEventID());
        jg.writeStringField("elocation", getLocation());
        jg.writeStringField("ebegin", getBegin());
        jg.writeStringField("efinish", getFinish());
        jg.writeStringField("ezip", getZip());
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    // Create event from JSON
    public static Event fromJSON(final InputStream in) throws IOException {
        String jevent_id = null;
        String jevent_location = null;
        String jevent_begin = null;
        String jevent_finish = null;
        String jevent_zip = null;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "event".equals(jp.getCurrentName()) == false) {
            // there are no more events
            if (jp.nextToken() == null)
                throw new IOException("Unable to parse JSON: no event object found.");
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {
            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                switch (jp.getCurrentName()) {
                    case "event_id":
                        jp.nextToken();
                        jevent_id = jp.getText();
                        break;
                    case "event_location":
                        jp.nextToken();
                        jevent_location = jp.getText();
                        break;
                    case "event_begin":
                        jp.nextToken();
                        jevent_begin = jp.getText();
                        break;
                    case "event_finish":
                        jp.nextToken();
                        jevent_finish = jp.getText();
                        break;
                    case "event_zip":
                        jp.nextToken();
                        jevent_zip = jp.getText();
                        break;
                }
            }
        }
        return new Event(jevent_id, jevent_location, jevent_begin, jevent_finish, jevent_zip);
    }
}