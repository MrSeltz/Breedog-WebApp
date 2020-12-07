package it.unipd.dei.breedog.database;

import it.unipd.dei.breedog.resource.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Get the events related to a dog into the database.
 * 
 */
public class GetEventDB {

    /**
     * The SQL statement to be executed to search for events to which a dog
     * participated
     */
    private static final String STMT = "SELECT event.eventid, event.location, event.begin, event.finish, event.origincode FROM takepartin INNER JOIN competition ON takepartin.competition = competition.competitionid INNER JOIN event ON competition.eventid = event.eventid WHERE takepartin.dog = ?;";

    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The microchip code of the dog
     */
    private final String microchip;

    /**
     * Creates a new object for searching a dog pathologies into the database.
     * 
     * @param con       the connection to the database.
     * 
     * @param microchip the microchip of the dog in the database.
     */
    public GetEventDB(final Connection con, final String microchip) {
        this.con = con;
        this.microchip = microchip;
    }

    /**
     * Search events to which the dog participated into the database
     * 
     * @return the {@code List<Event>} object matching the dog's microchip.
     * 
     * @throws SQLException if any error occurs while getting events related to the
     *                      dog.
     */
    public List<Event> GetInfo() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        final List<Event> events = new ArrayList<Event>();

        try {
            pstmt = con.prepareStatement(STMT);
            pstmt.setString(1, microchip);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                events.add(new Event(rs.getString("eventid"), rs.getString("location"), rs.getString("begin"),
                        rs.getString("finish"), rs.getString("origincode")));
            }
        } finally {
            if (rs != null)
                rs.close();

            if (pstmt != null)
                pstmt.close();

            con.close();
        }
        return events;
    }

}