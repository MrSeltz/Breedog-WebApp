package it.unipd.dei.breedog.servlet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unipd.dei.breedog.database.GenealogyDB;
import it.unipd.dei.breedog.resource.Genealogy;
import it.unipd.dei.breedog.resource.Message;

/**
 * Manage genealogies into the database.
 */
@SuppressWarnings("serial")
public final class GenealogyServlet extends AbstractDatabaseServlet {

    /**
     * Add or remove a genealogy into the database.
     * 
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * 
     * @throws ServletException if any error occurs while executing the servlet.
     * @throws IOException      if any error occurs in the client/server
     *                          communication.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        Genealogy g = null;
        Message m = null;

        InputStream stream = req.getInputStream();
        BufferedInputStream buffer = new BufferedInputStream(stream);

        if (buffer.markSupported()) {
            buffer.mark(0);
        }

        g = Genealogy.fromJSON(buffer);

        // Set response type
        res.setContentType("application/json; charset=utf-8");
        final OutputStream out = res.getOutputStream();

        // if parse error or predecessor = successor
        if (g == null || g.getPredecessor().equals(g.getSuccessor())) {
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
                GenealogyDB genDB = new GenealogyDB(getDataSource().getConnection());
                genDB.createGenelaogy(g);

                m = new Message(String.format("Genealogy %s successfully added", g.getPredecessor()));
                m.toJSON(out);
            }

            if (action.equals("remove")) {
                // New object for accessing dababase
                GenealogyDB genDB = new GenealogyDB(getDataSource().getConnection());
                genDB.deleteGenealogy(g);

                m = new Message(String.format("Genealogy %s successfully deleted", g.getPredecessor()));
                m.toJSON(out);

            }

        } catch (SQLException ex) {
            if (ex.getSQLState().equals("23505"))
                m = new Message(String.format("Cannot create the genealogy. Dog with microchip %s already exists",
                        g.getPredecessor()), "E300", ex.getMessage());
            else
                m = new Message("Cannot create the dog: unexpected error", " E200", ex.getMessage());

            m.toJSON(out);
        } catch (NumberFormatException ex) {
            m = new Message("Cannot create the dog. Height and weight must be numbers and fci must be integer", "E100",
                    ex.getMessage());
            m.toJSON(out);
        }

        out.flush();
        out.close();

    }

}