package it.unipd.dei.breedog.servlet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unipd.dei.breedog.database.CreateDogDatabase;
import it.unipd.dei.breedog.resource.Dog;
import it.unipd.dei.breedog.resource.Message;

/**
 * Creates a new dog into the database.
 */
@SuppressWarnings("serial")
public final class CreateDogServlet extends AbstractDatabaseServlet {

    /**
     * Creates a new dog into the database.
     * 
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * 
     * @throws ServletException if any error occurs while executing the servlet.
     * @throws IOException      if any error occurs in the client/server
     *                          communication.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        Dog d = null;
        Message m = null;

        InputStream stream = req.getInputStream();
        BufferedInputStream buffer = new BufferedInputStream(stream);

        if (buffer.markSupported()) {
            buffer.mark(0);
        }

        d = Dog.fromJSON(buffer);

        // Set response type
        res.setContentType("application/json; charset=utf-8");
        final OutputStream out = res.getOutputStream();

        // if parse error return
        if (d == null) {
            m = new Message("Insertion Error.");
            m.toJSON(out);
            out.flush();
            out.close();
            return;
        }

        try {

            // New object for accessing dababase
            CreateDogDatabase dogDB = new CreateDogDatabase(getDataSource().getConnection());
            dogDB.createDog(d);

            m = new Message(String.format("Dog %s successfully added", d.getMicrochip()));

        } catch (SQLException ex) {
            if (ex.getSQLState().equals("23505")) {
                m = new Message(
                        String.format("Cannot create the dog. Dog with microchip %s already exists", d.getMicrochip()),
                        "E300", ex.getMessage());
            } else {
                m = new Message("Cannot create the dog: unexpected error", " E200", ex.getMessage());
            }
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