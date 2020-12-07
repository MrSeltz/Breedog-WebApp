package it.unipd.dei.breedog.servlet;

import it.unipd.dei.breedog.resource.*;

import it.unipd.dei.breedog.database.GetDogsDB;
import it.unipd.dei.breedog.database.GetBreedDB;
import it.unipd.dei.breedog.database.GetPathologyDB;
import it.unipd.dei.breedog.database.CompetitionDB;
import it.unipd.dei.breedog.database.GetEventDB;
import it.unipd.dei.breedog.database.GetGenealogyDB;

import it.unipd.dei.breedog.database.UpdateDogDB;

import java.io.IOException;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.sql.SQLException;
import java.io.InputStream;

import java.util.List;

import com.fasterxml.jackson.core.*;

/**
 * Manage a dog into the database.
 */
@SuppressWarnings("serial")
public class DogServlet extends AbstractDatabaseServlet {

    // the dog
    Dog d = null;

    // the breed
    Breed b = null;

    // the list of pathologies of the dog
    List<Pathology> p = null;

    // the list of competitions to which the dog has taken part
    List<Competition> c = null;

    // the list of events to which the dog has taken part
    List<Event> e = null;

    // the list of parents of the dog
    List<Dog> parents = null;

    // the list of sons of the dog
    List<Dog> sons = null;

    // the message
    Message m = null;

    /**
     * Gets a dog from the database.
     * 
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * 
     * @throws ServletException if any error occurs while executing the servlet.
     * @throws IOException      if any error occurs in the client/server
     *                          communication.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Set response type
        res.setContentType("application/json; charset=utf-8");
        final OutputStream out = res.getOutputStream();

        try {
            // parse the URI path to extract BREEDERFC
            String path = req.getRequestURI();
            // PATH = "/user/dog/{microchip}"
            path = path.substring(path.lastIndexOf("dog") + 3);
            // get microchip code
            final String microchip = path.substring(1);

            // get data from DB using the microchip as identificator
            d = new GetDogsDB(getDataSource().getConnection(), null, microchip).GetDog();

            // get breed info from DB
            b = new GetBreedDB(getDataSource().getConnection(), Integer.toString(d.getFci())).GetInfo();

            // get pathology data from DB
            p = new GetPathologyDB(getDataSource().getConnection(), microchip).GetInfo();

            // get competition data from DB
            c = new CompetitionDB(getDataSource().getConnection(), microchip).GetInfo();

            // get event data from DB
            e = new GetEventDB(getDataSource().getConnection(), microchip).GetInfo();

            // get parents
            parents = new GetGenealogyDB(getDataSource().getConnection(), microchip).GetParents();

            // get sons
            sons = new GetGenealogyDB(getDataSource().getConnection(), microchip).GetSuccessors();

            res.setStatus(HttpServletResponse.SC_CREATED);
            toJSON(out, d, b, p, c, e, parents, sons);

        } catch (Throwable t) {
            // CATCH ERROR MESSAGE FOR DEBUG PURPOSE
            m = new Message("Some STRAANGE EXCEPTION", "E123", t.getMessage());
            m.toJSON(out);
        }

        out.flush();
        out.close();
    }

    /**
     * Updates dog information into the database.
     * 
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * 
     * @throws ServletException if any error occurs while executing the servlet.
     * @throws IOException      if any error occurs in the client/server
     *                          communication.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        // Initialize variables
        Message m = null;
        Dog d = null;

        // Set response type
        res.setContentType("application/json; charset=utf-8");
        final OutputStream out = res.getOutputStream();

        final InputStream in = req.getInputStream();

        d = Dog.fromJSON(in);

        // if parse error return
        if (d == null) {
            m = new Message("Error from reading new dog info");
            m.toJSON(out);
            out.flush();
            out.close();
            return;
        }

        // CALL DB TO UPDATE INFO
        try {
            // if the user load a new photo -> execute update photo query
            if (d.getPhoto() != "") {
                UpdateDogDB photo = new UpdateDogDB(getDataSource().getConnection(), d);
                if (photo.updatePhoto())
                    m = new Message("Update PHOTO Successfull.");
                else
                    m = new Message("Update PHOTO or INFO Error.");
            }

            //
            UpdateDogDB info = new UpdateDogDB(getDataSource().getConnection(), d);
            if (info.updateInfo())
                m = new Message("Update Successfull.");
            else
                m = new Message("Update Error.");

            m.toJSON(out);
        } catch (

        SQLException ex) {
            m = new Message("SQLException.", "1234", ex.getMessage());
            m.toJSON(out);
        }

        out.flush();
        out.close();
    }

    // Create a new type of JSON that contains all dog profile informations
    public final void toJSON(final OutputStream out, final Dog dog, final Breed breed, final List<Pathology> pathology,
            final List<Competition> competitions, final List<Event> events, final List<Dog> parents,
            final List<Dog> sons) throws IOException {
        JsonFactory JSON_FACTORY = new JsonFactory();
        JSON_FACTORY.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        JSON_FACTORY.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        // Dog profile
        jg.writeFieldName("dog");
        jg.writeStartObject();
        jg.writeStringField("microchip", dog.getMicrochip());
        jg.writeStringField("tattoo", dog.getTattoo());
        jg.writeStringField("name", dog.getName());
        jg.writeStringField("birth", dog.getBirth());
        jg.writeStringField("sex", dog.getSex());
        jg.writeStringField("height", Double.toString(dog.getHeight()));
        jg.writeStringField("weight", Double.toString(dog.getWeight()));
        jg.writeStringField("coat", dog.getCoat());
        jg.writeStringField("character", dog.getCharacter());
        jg.writeStringField("dna", dog.getDna());
        jg.writeStringField("teeth", dog.getTeeth());
        jg.writeStringField("signs", dog.getSigns());
        jg.writeStringField("photo", dog.getPhoto());
        jg.writeStringField("owner", dog.getOwner());
        jg.writeStringField("status", dog.getStatus());
        jg.writeStringField("fci", Integer.toString(dog.getFci()));
        jg.writeStringField("breederfc", dog.getBreederFc());
        jg.writeStringField("kennel", dog.getKennel());
        jg.writeEndObject();

        // Breed info
        jg.writeFieldName("breed");
        jg.writeStartObject();
        jg.writeStringField("fci", breed.getFci());
        jg.writeStringField("bgroup", breed.getBgroup());
        jg.writeStringField("bname", breed.getBname());
        jg.writeEndObject();

        // Pathology List info
        jg.writeFieldName("pathologies");
        jg.writeStartArray();
        for (final Pathology r : pathology) {
            jg.writeStartObject();
            jg.writeFieldName("pathology");
            jg.writeStartObject();
            jg.writeStringField("pcode", r.getPcode());
            jg.writeStringField("pname", r.getPname());
            jg.writeStringField("pseverity", r.getPseverity());
            jg.writeEndObject();
            jg.writeEndObject();
        }
        jg.writeEndArray();

        // Competitions list info
        jg.writeFieldName("competitions");
        jg.writeStartArray();
        for (final Competition r : competitions) {
            jg.writeStartObject();
            jg.writeFieldName("competition");
            jg.writeStartObject();
            jg.writeStringField("cwin", r.getWin());
            jg.writeStringField("cid", r.getCompID());
            jg.writeStringField("ctype", r.getType());
            jg.writeStringField("cgroup", r.getGroup());
            jg.writeStringField("cclass", r.getCompClass());
            jg.writeStringField("ceventid", r.getEventID());
            jg.writeEndObject();
            jg.writeEndObject();
        }
        jg.writeEndArray();

        // Event list info
        jg.writeFieldName("events");
        jg.writeStartArray();
        for (final Event r : events) {
            jg.writeStartObject();
            jg.writeFieldName("event");
            jg.writeStartObject();
            jg.writeStringField("eid", r.getEventID());
            jg.writeStringField("elocation", r.getLocation());
            jg.writeStringField("ebegin", r.getBegin());
            jg.writeStringField("efinish", r.getFinish());
            jg.writeStringField("ezip", r.getZip());
            jg.writeEndObject();
            jg.writeEndObject();
        }
        jg.writeEndArray();

        // Genealogy list info
        jg.writeFieldName("parents");
        jg.writeStartArray();
        for (final Dog r : parents) {
            jg.writeStartObject();
            jg.writeFieldName("parent");
            jg.writeStartObject();
            jg.writeStringField("microchip", r.getMicrochip());
            jg.writeStringField("name", r.getName());
            jg.writeStringField("sex", r.getSex());
            jg.writeEndObject();
            jg.writeEndObject();
        }
        jg.writeEndArray();

        jg.writeFieldName("sons");
        jg.writeStartArray();
        for (final Dog r : sons) {
            jg.writeStartObject();
            jg.writeFieldName("son");
            jg.writeStartObject();
            jg.writeStringField("microchip", r.getMicrochip());
            jg.writeStringField("name", r.getName());
            jg.writeStringField("birth", r.getBirth());
            jg.writeEndObject();
            jg.writeEndObject();
        }
        jg.writeEndArray();

        // END JSON RESPONSE
        jg.writeEndObject();
        jg.flush();
    }

}