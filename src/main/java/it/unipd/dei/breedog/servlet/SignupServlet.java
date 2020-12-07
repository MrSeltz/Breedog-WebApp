package it.unipd.dei.breedog.servlet;

import it.unipd.dei.breedog.resource.Message;
import it.unipd.dei.breedog.resource.Breeder;
import it.unipd.dei.breedog.resource.Account;

import it.unipd.dei.breedog.database.SignupDB;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;

/**
 * Creates a new user into the database.
 * 
 */

@SuppressWarnings("serial")
public class SignupServlet extends AbstractDatabaseServlet {

    /**
     * Creates a new user into the database.
     * 
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * 
     * @throws ServletException if any error occurs while executing the servlet.
     * @throws IOException      if any error occurs in the client/server
     *                          communication.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Message m = null;
        Account a = null;
        Breeder b = null;

        InputStream stream = req.getInputStream();
        BufferedInputStream buffer = new BufferedInputStream(stream);

        if (buffer.markSupported()) {
            buffer.mark(0);
        }

        a = Account.fromJSON(buffer);

        buffer.reset();

        b = Breeder.fromJSON(buffer);

        // Set response type
        res.setContentType("application/json; charset=utf-8");
        final OutputStream out = res.getOutputStream();

        // if parse error return
        if (a == null || b == null) {
            m = new Message("Registration Error.");
            m.toJSON(out);
            out.flush();
            out.close();
            return;
        }

        try {
            SignupDB s = new SignupDB(getDataSource().getConnection(), a, b);

            if (s.registration())
                m = new Message("Registration Successfull.");
            else
                m = new Message("Registration Error.");
            m.toJSON(out);
        } catch (SQLException ex) {
            m = new Message("SQLException.");
            m.toJSON(out);
        }

        /*
         * m = new Message("Registration Successfull."); m.toJSON(out); a.toJSON(out);
         * b.toJSON(out);
         */
        out.flush();
        out.close();

    }
}