/*
 * Copyright 2020 University of Padua, Italy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.unipd.dei.breedog.filter;

import it.unipd.dei.breedog.database.AuthAccountDB;

import it.unipd.dei.breedog.resource.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Base64;

import java.sql.SQLException;

public class AuthFilter implements Filter {

    // base64 decoder
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    // name of the user of the session
    private static final String USER_ATTRIBUTE = "user";

    // Configuration for the filter
    private FilterConfig config = null;

    // Connection pool to the database
    private DataSource ds;

    @Override
    public void init(final FilterConfig config) throws ServletException {

        if (config == null) {
            throw new ServletException("Filter configuration cannot be null.");
        }
        this.config = config;

        /*
         * Here we could pass configuration parameters to the filter, if needed.
         */

        // the JNDI lookup context
        InitialContext cxt;

        try {
            cxt = new InitialContext();
            ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/breedog");
        } catch (NamingException e) {
            ds = null;

            throw new ServletException(
                    String.format("Impossible to access the connection pool to the database: %s", e.getMessage()));
        }
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
            final FilterChain chain) throws IOException, ServletException {

        if (!(servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletResponse)) {
            throw new ServletException("Only HTTP requests/responses are allowed.");
        }

        // Safe to downcast at this point.
        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        final HttpServletResponse res = (HttpServletResponse) servletResponse;

        final HttpSession session = req.getSession(false);

        // if we do not have a session, try to authenticate the user
        if (session == null) {

            if (!authenticateUser(req, res))
                return;
        } else {

            final String user = (String) session.getAttribute(USER_ATTRIBUTE);

            // there might exist a session but without any user in it
            if (user == null || user.isBlank()) {
                // invalidate the session
                session.invalidate();

                // try to authenticate the user
                if (!authenticateUser(req, res))
                    return;
            }
        }

        // the user is properly authenticated and in session, continue the processing
        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        config = null;
        ds = null;
    }

    private boolean authenticateUser(HttpServletRequest req, HttpServletResponse res) throws IOException {

        // get the authorization information
        final String auth = req.getHeader("Authorization");

        // if there is no authorization information, send the authentication challenge
        // again
        if (auth == null || auth.isBlank()) {
            // sendAuthenticationChallenge(res);
            res.sendRedirect("../index.html");

            return false;
        }

        // if it is not HTTP Basic authentication, send the authentication challenge
        // again
        if (!auth.toUpperCase().startsWith("BASIC ")) {
            // sendAuthenticationChallenge(res);
            res.sendRedirect("../index.html");
            return false;
        }

        // perform Base64 decoding
        final String pair = new String(DECODER.decode(auth.substring(6)));

        // userDetails[0] is the username; userDetails[1] is the password
        final String[] userDetails = pair.split(":", 2);

        // if the user is successfully authenticated, create a Session and store the
        // user there
        try {
            if (checkUserCredentials(userDetails[0], userDetails[1])) {
                // create a new session
                HttpSession session = req.getSession(true);

                session.setAttribute(USER_ATTRIBUTE, userDetails[0]);

                return true;
            }
        } catch (SQLException ex)
        {

        }

        // as a fallback, always send the authentication challenge again
        // sendAuthenticationChallenge(res);
        res.sendRedirect("../index.html");

        return false;
    }

    /*
     * Sends the authentication challenge.
     *
     * @param res the HTTP servlet response.
     * @throws IOException if anything goes wrong while sending the authentication
     *                     challenge.
     
    private void sendAuthenticationChallenge(HttpServletResponse res) throws IOException {

        res.setHeader("WWW-Authenticate", "Basic realm=Employee");

        res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }*/

    /**
     * Performs the actual authentication based on the provided user credentials.
     *
     * @param username the username.
     * @param password the password.
     * @return {@code true} if the user has been successfully authenticated;
     *         {@code false} otherwise.
     */
    private boolean checkUserCredentials(String username, String password) throws SQLException {
        /*
         * Here there should be some logic to actually authenticate the user, e.g.
         * accessing a database, by using the provided username and password.
         * 
         * The connection pool to the database, if needed, is retrieved in the init
         * method and made available in the ds field.
         * 
         * Not implemented for simplicity. Therefore all the users will be authenticated
         * ;-)
         */
        Account a = null;

        try {
            a = new AuthAccountDB(ds.getConnection(), username, password).AuthCheck();
        } catch (SQLException ex) {
        }

        if (a == null)
            return false;
        return true;
    }

}
