package it.unipd.dei.breedog.servlet;

import java.util.Base64;
import it.unipd.dei.breedog.database.AuthAccountDB;
import java.sql.SQLException;
import javax.sql.DataSource;

import it.unipd.dei.breedog.resource.*;

import it.unipd.dei.breedog.database.SearchDogsDB;

import java.io.IOException;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;

/**
 * Seraches for dog in the database applying manually chosen filters
 */
@SuppressWarnings("serial")
public class SearchDogsServlet extends AbstractDatabaseServlet {

    // the list of dogs returned by query
    List<Dog> dogs = new ArrayList<Dog>();

    // the message
    Message m = null;

    // decoder for the encoded user data
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    // name of the user of the session
    private static final String USER_ATTRIBUTE = "user";
    private DataSource ds;

    /**
     * Gets a dog from the database applying filters to queries.
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

            // try to get a user session
            final HttpSession session = req.getSession(false);

            // check is the user is registered
            final boolean isRegistered = isUserAuthenticated(session, req, res);

            // get url-encoded parameters
            final String query = formatQuery(req.getParameter("query"));

            final String orderBy = getOrder(req.getParameter("order"));
            final String sex = req.getParameter("sex");
            final String status = req.getParameter("status");
            final String maxAge = req.getParameter("mxage");
            final int limit = 6; // limit of results per page
            final int offset = limit * getPage(req.getParameter("page"));

            // query the db
            dogs = new SearchDogsDB(getDataSource().getConnection(), query, sex, status, maxAge, orderBy, limit, offset,
                    isRegistered).GetDogs();

            if (dogs != null) {
                res.setStatus(HttpServletResponse.SC_CREATED);
                new ResourceList(dogs).toJSON(out);

            } else {

                m = new Message("NO DOGS FOUND.");
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(out);

            }
        } catch (Throwable t) {
            // error message debug
            m = new Message("Some STRAANGE EXCEPTION", "E123", t.getMessage());
            m.toJSON(out);
        }

        out.flush();
        out.close();
    }

    /**
     * Get the order for the query and sanitaze the input
     * 
     * @param order
     * @return
     */
    private String getOrder(String order) {

        HashMap<String, String> orderMap = new HashMap<String, String>();
        orderMap.put("youngest", "DESC");
        orderMap.put("oldest", "ASC");
        if (!orderMap.containsKey(order))
            return null;
        else
            return (String) orderMap.get(order);

    }

    /**
     * Get page of result to display
     * 
     * @param page
     * @return number of the page
     */
    private int getPage(String page) {
        int pageNumber = 0;
        try {
            pageNumber = Integer.parseInt(page) - 1;
        } catch (NumberFormatException e) {
            pageNumber = 0;
        } finally {
            return pageNumber;
        }
    }

    /**
     * Check if user is registered
     * 
     * @param session
     * @param req
     * @param res
     * @return
     * @throws IOException
     * @throws ServletException
     */
    private boolean isUserAuthenticated(HttpSession session, HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        // if we do not have a session, try to authenticate the user

        if (session == null) {
            if (!authenticateUser(req, res))
                return false;
        } else {
            final String user = (String) session.getAttribute("user");
            // there might exist a session but without any user in it
            if (user == null || user.isBlank()) {
                // invalidate the session
                session.invalidate();
                // try to authenticate the user
                if (!authenticateUser(req, res))
                    return false;
            }
            return true;
        }
        return false;
    }

    private boolean authenticateUser(HttpServletRequest req, HttpServletResponse res) throws IOException {

        // get the authorization information
        final String auth = req.getHeader("Authorization");

        // if there is no authentication header return false
        if (auth == null || auth.isBlank()) {
            return false;
        }

        // if it is not HTTP Basic authentication, return false
        if (!auth.toUpperCase().startsWith("BASIC ")) {
            return false;
        }

        // perform Base64 decoding
        final String pair = new String(DECODER.decode(auth.substring(6)));

        // userDetails[0] is the username; userDetails[1] is the password
        final String[] userDetails = pair.split(":", 2);

        // if the user is successfully authenticated, create a Session and store the
        // user
        try {
            if (checkUserCredentials(userDetails[0], userDetails[1])) {
                HttpSession session = req.getSession(true);
                session.setAttribute(USER_ATTRIBUTE, userDetails[0]);
                return true;
            }
        } catch (SQLException ex) {
        }

        return false;
    }

    /**
     * Performs the actual authentication based on the provided user credentials.
     *
     * @param username the username.
     * @param password the password.
     * @return {@code true} if the user has been successfully authenticated;
     *         {@code false} otherwise.
     */
    private boolean checkUserCredentials(String username, String password) throws SQLException {
        Account a = null;

        try {
            a = new AuthAccountDB(ds.getConnection(), username, password).AuthCheck();
        } catch (SQLException ex) {
        }

        if (a == null)
            return false;
        return true;
    }
    
    /**
     * Make the query formatted for a text search query 
     * @param query
     * @return
     */
    private String formatQuery(String query){
        String res =  query.trim().replace(" ", " | ");
        if(res == ""){
            return null;
        }
        return res;
    }

}