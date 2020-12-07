package it.unipd.dei.breedog.servlet;

import it.unipd.dei.breedog.resource.*;

import it.unipd.dei.breedog.database.GetDogsDB;

import java.io.IOException;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;

import java.util.ArrayList;
import java.util.List;

/**
 * Get user's dog from the database.
 */
@SuppressWarnings("serial")
public class DogListServlet extends AbstractDatabaseServlet {
    
    //the breeder
    Breeder b = null;
    
    //the breeder's dogs
    List<Dog> dogs = new ArrayList<Dog>();
    
    //the message
    Message m = null;

    /**
     * Gets the breeder's list of dogs from the database.
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
            // PATH = "/user/breeder/dog/{breederfc}"
            path = path.substring(path.lastIndexOf("dog") + 3);
            // get breederfc
            final String breederfc = path.substring(1);

            // get data from DB
            dogs = new GetDogsDB(getDataSource().getConnection(), breederfc, null).GetDogs();

            if (dogs != null) {
                res.setStatus(HttpServletResponse.SC_CREATED);
                new ResourceList(dogs).toJSON(out);
                // convert the data to JSON and append to the request
            } else {
                // it should not happen
                m = new Message("NO DOGS FOUND.");
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(out);
            }
        } catch (Throwable t) {
            // CATCH ERROR MESSAGE FOR DEBUG PURPOSE
            m = new Message("Some STRAANGE EXCEPTION", "E123", t.getMessage());
            m.toJSON(out);
        }

        out.flush();
        out.close();
    }
}