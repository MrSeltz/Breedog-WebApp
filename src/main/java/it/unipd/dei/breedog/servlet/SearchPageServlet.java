package it.unipd.dei.breedog.servlet;

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

@SuppressWarnings("serial")
public class SearchPageServlet extends AbstractDatabaseServlet {
    
    /**
	* Searches employees by their salary.
	* 
	* @param req
	*            the HTTP request from the client.
	* @param res
	*            the HTTP response from the server.
	* 
	* @throws ServletException
	*             if any error occurs while executing the servlet.
	* @throws IOException
	*             if any error occurs in the client/server communication.
	*/
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        // request parameter
		String query = "";
        Message m = null;

        try {
            
            query = req.getParameter("query");
            
            
        } catch (Throwable t) {
            // error message debug
            m = new Message("Some STRAANGE EXCEPTION", "E123", t.getMessage());
        }

        req.setAttribute("query", query);
		req.setAttribute("message", m);
		
		// forwards the control to the search-employee-result JSP
		req.getRequestDispatcher("/jsp/search.jsp").forward(req, res);

    }

    
}