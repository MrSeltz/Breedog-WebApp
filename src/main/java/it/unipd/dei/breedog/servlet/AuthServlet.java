package it.unipd.dei.breedog.servlet;

import it.unipd.dei.breedog.database.AuthAccountDB;
import it.unipd.dei.breedog.resource.Account;

import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
import java.sql.SQLException;
//import java.util.List;
import it.unipd.dei.breedog.resource.Message;

//import javax.naming.InitialContext;
//import javax.naming.NamingException;
import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.sql.DataSource;
import java.io.OutputStream;
import java.util.Base64;
import javax.servlet.http.HttpSession;

import java.io.BufferedInputStream;

import java.io.InputStream;

/**
 * Manage the authentication of a user.
 * 
 */

@SuppressWarnings("serial")
public class AuthServlet extends AbstractDatabaseServlet {

    // base64 decoder
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    // name of the user of the session
    private static final String USER_ATTRIBUTE = "user";

    /**
     * Manage the authentication of a user.
     * 
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * 
     * @throws ServletException if any error occurs while executing the servlet.
     * @throws IOException      if any error occurs in the client/server
     *                          communication.
     */

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String username = null;
        String password = null;
        String new_password = null;
        Account a = null;
        Message m = null;
        boolean b = false;

        /*
         * ----- OLD WAY TO LOGIN ----- try { username = req.getParameter("uname");
         * password = req.getParameter("psw");
         */

        // ----- HEADER SESSION LOGIN

        // Getting servlet request query string.
        String queryString = req.getQueryString();
        queryString = queryString.substring(queryString.lastIndexOf("req") + 3);
        // get the action request
        String action = queryString.substring(1);

        // get the authorization information
        final String auth = req.getHeader("Authorization");

        if (action.equals("login")) {

            // perform Base64 decoding
            final String pair = new String(DECODER.decode(auth.substring(6)));

            // userDetails[0] is the username; userDetails[1] is the password
            final String[] userDetails = pair.split(":", 2);

            try {
                username = userDetails[0];
                password = userDetails[1];

                a = new AuthAccountDB(getDataSource().getConnection(), username, password).AuthCheck();
                m = new Message("Account authenticated.");

            } catch (SQLException ex) {
                m = new Message("Unexpected error while accessing the database. ", "E200", ex.getMessage());
            }

            // Manage the case where inserted data are wrong
            if (a == null) {
                m = new Message("Authentication Error.");
                res.setContentType("application/json; charset=utf-8");
                final OutputStream out = res.getOutputStream();
                m.toJSON(out);
                out.flush();
                out.close();
                return;
            }

            // Here we authenticate success -> start new session and return infor as JSON
            HttpSession session = req.getSession(true);
            // set session valid time to 3 hours -> need to change javascript local storage
            // data behaviour if implemented
            session.setMaxInactiveInterval(3 * 3600);
            session.setAttribute(USER_ATTRIBUTE, userDetails[0]);
            res.setContentType("application/json; charset=utf-8");
            final OutputStream out = res.getOutputStream();
            a.toJSON(out);
            out.flush();
            out.close();
        }

        /////////// CHANGE PASSWORD

        if (action.equals("changepw")) {

            // perform Base64 decoding
            final String pair = new String(DECODER.decode(auth.substring(6)));

            // userDetails[0] is the username; userDetails[1] is the password
            final String[] userDetails = pair.split(":", 3);

            try {
                username = userDetails[0];
                password = userDetails[1];
                new_password = userDetails[2];

                a = new AuthAccountDB(getDataSource().getConnection(), username, password).AuthCheck();
                m = new Message("Account authenticated.");

            }

            catch (SQLException ex) {
                m = new Message("Unexpected error while accessing the database. ", "E200", ex.getMessage());
            }

            // Manage the case where inserted data are wrong
            if (a == null) {
                m = new Message("Current password incorrect, please retry.");
                res.setContentType("application/json; charset=utf-8");
                final OutputStream out = res.getOutputStream();
                m.toJSON(out);
                out.flush();
                out.close();
                return;
            }

            // Here we authenticate success -> change password in new password
            final OutputStream out = res.getOutputStream();

            try {

                // AuthAccountDB a = new AuthAccountDB(getDataSource().getConnection(),
                // username, password);

                b = new AuthAccountDB(getDataSource().getConnection(), username, password).changePassword(new_password);

                if (b)
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

    }
}
