package it.unipd.dei.breedog.servlet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unipd.dei.breedog.database.WhichasDB;
import it.unipd.dei.breedog.resource.Message;
import it.unipd.dei.breedog.resource.Whichas;

/**
 * Manage pathologies into the database.
 */
@SuppressWarnings("serial")
public class PathologyServlet extends AbstractDatabaseServlet {

    /**
     * Add or remove a pathology into the database.
     * 
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * 
     * @throws ServletException if any error occurs while executing the servlet.
     * @throws IOException      if any error occurs in the client/server
     *                          communication.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        Whichas w = null;
        Message m = null;

        InputStream stream = req.getInputStream();
        BufferedInputStream buffer = new BufferedInputStream(stream);

        if (buffer.markSupported()) {
            buffer.mark(0);
        }

        w = Whichas.fromJSON(buffer);

        // Set response type
        res.setContentType("application/json; charset=utf-8");
        final OutputStream out = res.getOutputStream();

        // if parse error return
        if (w == null) {
            m = new Message("Insertion Error.");
            m.toJSON(out);
            out.flush();
            out.close();
            return;
        }

        try {
            // Getting servlet request query string.
            String queryString = req.getQueryString();
            queryString = queryString.substring(queryString.lastIndexOf("req") + 3);
            // get the action request
            String action = queryString.substring(1);

            if (action.equals("add")) {
                // New object for accessing dababase
                WhichasDB whichDB = new WhichasDB(getDataSource().getConnection());
                whichDB.createWhichas(w);

                m = new Message(String.format("Pathology correlated to the dog %s successfully added", w.getDog()));
                m.toJSON(out);
            }

            if (action.equals("remove")) {
                // New object for accessing dababase
                WhichasDB whichDB = new WhichasDB(getDataSource().getConnection());
                whichDB.deleteWhichas(w);

                m = new Message(String.format("Pathology correlated to the dog %s successfully removed", w.getDog()));
                m.toJSON(out);
            }

        } catch (SQLException ex) {
            if (ex.getSQLState().equals("23505"))
                m = new Message(String.format("Cannot create the pathology correlation for the dog: ", w.getDog()),
                        "E300", ex.getMessage());
            else
                m = new Message("Cannot create the correlation of pathology: unexpected error", " E200",
                        ex.getMessage());
            m.toJSON(out);
        } catch (NumberFormatException ex) {
            m = new Message("Cannot create the correlation of pathology.", "E100", ex.getMessage());
            m.toJSON(out);
        }

        out.flush();
        out.close();
    }
}
