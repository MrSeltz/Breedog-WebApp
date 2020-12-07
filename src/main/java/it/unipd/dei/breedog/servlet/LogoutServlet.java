package it.unipd.dei.breedog.servlet;

import it.unipd.dei.breedog.resource.*;

import java.io.IOException;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import java.io.OutputStream;

/**
 * Manage the logout of a user
 */
@SuppressWarnings("serial")
public class LogoutServlet extends AbstractDatabaseServlet {
    
    // name of the user of the session
    private static final String USER_ATTRIBUTE = "user";
    
    //the message
    Message m = null;

    /**
     * Logs out a user.
     * 
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * 
     * @throws ServletException if any error occurs while executing the servlet.
     * @throws IOException      if any error occurs in the client/server
     *                          communication.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
       
        final OutputStream out = res.getOutputStream();
        res.setContentType("application/json; charset=utf-8");

        // get session
        final HttpSession session = req.getSession(false);

        if (session == null) {
            m = new Message("Session already expired");
            m.toJSON(out);
            return;
        }

        session.removeAttribute(USER_ATTRIBUTE);
        // invalidate session
        session.invalidate();
        m = new Message("Successfully logout");
        m.toJSON(out);
        return;
    }
}