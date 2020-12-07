package it.unipd.dei.breedog.servlet;

import it.unipd.dei.breedog.resource.*;

import it.unipd.dei.breedog.database.GetBreederDB;
import it.unipd.dei.breedog.database.UpdateBreederDB;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.*;

/**
 * Manage a user/breeder into the database.
 */
@SuppressWarnings("serial")
public class LoadUserServlet extends AbstractDatabaseServlet {

    /**
     * Gets a user from the database.
     * 
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * 
     * @throws ServletException if any error occurs while executing the servlet.
     * @throws IOException      if any error occurs in the client/server
     *                          communication.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        // Initialize variables
        Breeder b = null;
        List<Breed> breeds = new ArrayList<Breed>();
        Message m = null;

        // set response type
        res.setContentType("application/json; charset=utf-8");
        final OutputStream out = res.getOutputStream();

        try {
            // parse the URI path to extract the badge
            String path = req.getRequestURI();
            path = path.substring(path.lastIndexOf("breeder") + 7);

            // parse from position 1 i order to bypass '/'
            final String breederfc = path.substring(1);

            // Get data from db
            b = new GetBreederDB(getDataSource().getConnection(), breederfc).GetInfo();

            breeds = new GetBreederDB(getDataSource().getConnection(), breederfc).GetBreeds();

            if (b != null) {
                res.setStatus(HttpServletResponse.SC_CREATED);

                // Generate JSON from basic info
                toJSON(out, b, breeds);
                /*
                 * Convert Breeder and Breeds to JSON with standard method b.toJSON(out); new
                 * ResourceList(breeds).toJSON(out);
                 */

            } else {
                // it should not happen
                m = new Message("Cannot load breeder profile: unexpected error.");
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(out);
            }
        } catch (Throwable t) {
            m = new Message("Some STRAANGE EXCEPTION", "E123", t.getMessage());
            m.toJSON(out);
        }

        out.flush();
        out.close();
    }

    /**
     * Updates breeder information into the database.
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
        Breeder b = null;

        // Set response type
        res.setContentType("application/json; charset=utf-8");
        final OutputStream out = res.getOutputStream();

        final InputStream in = req.getInputStream();

        b = Breeder.fromJSON(in);

        // if parse error return
        if (b == null) {
            m = new Message("Error from reading new user info");
            m.toJSON(out);
            out.flush();
            out.close();
            return;
        }

        // CALL DB TO UPDATE INFO
            try {
            // if the user load a new photo -> execute update photo query
            if (b.getPhoto() != "") {
                UpdateBreederDB photo = new UpdateBreederDB(getDataSource().getConnection(), b);
                if (photo.updatePhoto())
                    m = new Message("Update PHOTO Successfull.");
                else
                    m = new Message("Update PHOTO or INFO Error.");
            }

            //
            UpdateBreederDB u = new UpdateBreederDB(getDataSource().getConnection(), b);
            if (u.update())
                m = new Message("Update Successfull.");
            else
                m = new Message("Update Error.");

            m.toJSON(out);
            } 
            
            catch (SQLException ex) {
            m = new Message("SQLException.", "1234", ex.getMessage());
            m.toJSON(out);
            }

        out.flush();
        out.close();
    }
    
    // Create a new type of JSON that contains all breeder profile informations
    public final void toJSON(final OutputStream out, final Breeder breeder_profile, final List<Breed> breeds)
            throws IOException {
        JsonFactory JSON_FACTORY = new JsonFactory();
        JSON_FACTORY.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        JSON_FACTORY.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        // Breeder profile
        jg.writeFieldName("breeder");
        jg.writeStartObject();
        jg.writeStringField("breederfc", breeder_profile.getBreederfc());
        jg.writeStringField("name", breeder_profile.getName());
        jg.writeStringField("surname", breeder_profile.getSurname());
        jg.writeStringField("birth", breeder_profile.getBirth());
        jg.writeStringField("address", breeder_profile.getAddress());
        jg.writeStringField("telephone", breeder_profile.getTelephone());
        jg.writeStringField("email", breeder_profile.getEmail());
        jg.writeStringField("vat", breeder_profile.getVat());
        jg.writeStringField("photo", breeder_profile.getPhoto());
        jg.writeStringField("description", breeder_profile.getDescription());
        jg.writeEndObject();

        // Breed-list
        jg.writeFieldName("breeds");
        jg.writeStartArray();
        for (final Breed r : breeds) {
            // Check if the breed is null ---> jump to the next iteration
            if (r != null) {
                jg.writeStartObject();
                jg.writeFieldName("breed");
                jg.writeStartObject();
                jg.writeStringField("fci", r.getFci());
                jg.writeStringField("bgroup", r.getBgroup());
                jg.writeStringField("bname", r.getBname());
                jg.writeStringField("bcount", r.getCount());
                jg.writeEndObject();
                jg.writeEndObject();
            } else
                continue;
        }

        jg.writeEndArray();
        jg.writeEndObject();
        jg.flush();
    }
}
