package it.unipd.dei.breedog.servlet;

import it.unipd.dei.breedog.database.CompetitionDB;
import it.unipd.dei.breedog.resource.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Add or remove a competition into the database.
 */
@SuppressWarnings("serial")
public class CompetitionServlet extends AbstractDatabaseServlet {

    /**
     * Add or remove a competition into the database.
     * 
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * 
     * @throws ServletException if any error occurs while executing the servlet.
     * @throws IOException      if any error occurs in the client/server
     *                          communication.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Competition c = null;
        Event e = null;
        Dog d = null;
        Message m = null;
        boolean check_tpi = false;
        boolean check_comp = false;
        boolean check_event = false;

        InputStream stream = req.getInputStream();
        BufferedInputStream buffer = new BufferedInputStream(stream);

        // Buffer the input stream in order to read twice from the same stream
        if (buffer.markSupported())
            buffer.mark(0);

        d = Dog.fromJSON(buffer);
        buffer.reset();
        c = Competition.fromJSON(buffer);
        buffer.reset();

        // Set response type
        res.setContentType("application/json; charset=utf-8");
        final OutputStream out = res.getOutputStream();

        // Check input data

        try {
            // Getting servlet request query string.
            String queryString = req.getQueryString();
            queryString = queryString.substring(queryString.lastIndexOf("req") + 3);
            // get the action request
            String action = queryString.substring(1);

            // check if takepartin already exist
            CompetitionDB compdb_tpi = new CompetitionDB(getDataSource().getConnection(), d.getMicrochip());
            check_tpi = compdb_tpi.CheckTPI(c.getCompID());

            // check if competition already exist
            CompetitionDB compdb_comp = new CompetitionDB(getDataSource().getConnection(), d.getMicrochip());
            check_comp = compdb_comp.CheckComp(c.getCompID());

            if (action.equals("add")) {
                // Parse also the event, otherwise no need
                e = Event.fromJSON(buffer);
                // check if event already exist
                CompetitionDB compdb_event = new CompetitionDB(getDataSource().getConnection(), d.getMicrochip());
                check_event = compdb_event.CheckEvent(e.getEventID());

                // check if takepartin already exist
                if (check_tpi == true) {
                    m = new Message("This competition for your dog already exist.");
                    m.toJSON(out);
                } else {
                    if (check_comp == true && check_event == true) {
                        // Add only the new takepartin row
                        CompetitionDB add_tpi = new CompetitionDB(getDataSource().getConnection(), d.getMicrochip());
                        boolean tmp = add_tpi.AddTPI(c.getCompID(), c.getWin());

                        if (tmp) {
                            m = new Message("Competition Added :)");
                            m.toJSON(out);
                        } else {
                            m = new Message(
                                    "We incurred in some problems during the adding operation, try again later :(");
                            m.toJSON(out);
                        }
                    } else {
                        // Add the new event, competition, and takepartin row
                        CompetitionDB add_event = new CompetitionDB(getDataSource().getConnection(), d.getMicrochip());
                        boolean tmp_check_e = add_event.AddE(e);

                        CompetitionDB add_comp = new CompetitionDB(getDataSource().getConnection(), d.getMicrochip());
                        boolean tmp_check_c = add_comp.AddC(c);

                        CompetitionDB add_tpi = new CompetitionDB(getDataSource().getConnection(), d.getMicrochip());
                        boolean tmp_check_tpi = add_tpi.AddTPI(c.getCompID(), c.getWin());

                        if (tmp_check_e && tmp_check_c && tmp_check_tpi) {
                            m = new Message("Competition and Event Added :)");
                            m.toJSON(out);
                        } else {
                            m = new Message(
                                    "We incurred in some problems during the adding operation, try again later :(");
                            m.toJSON(out);
                        }
                    }
                }
            }

            if (action.equals("remove")) {
                // check if takepartin already exist
                if (check_tpi == true) // proceed with deletion
                {
                    CompetitionDB del_tpi = new CompetitionDB(getDataSource().getConnection(), d.getMicrochip());
                    boolean tmp = del_tpi.DelTPI(c.getCompID());

                    if (tmp)
                        m = new Message("Deletion Complete :)");
                    else
                        m = new Message(
                                "We incurred in some problems during the deletion operation, try again later :(");
                    m.toJSON(out);
                } else// RETURN
                {
                    m = new Message("Your dog never registered the selected competition :(");
                    m.toJSON(out);
                }
            }
        } catch (SQLException ex) {
            m = new Message("Some STRAANGE EXCEPTION", "E123", ex.getMessage());
            m.toJSON(out);
        }
        out.flush();
        out.close();
    }
}
