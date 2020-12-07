package it.unipd.dei.breedog.database;

import it.unipd.dei.breedog.resource.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates or deletes events/competitions, to which a dog has taken part, into the database.
 * 
 */

public class CompetitionDB {
    // merge and read TAKEPARTIN + COMPETITION given DOG-MICROCHIP
    private static final String STMT = "SELECT * FROM takepartin INNER JOIN competition ON takepartin.competition = competition.competitionid WHERE takepartin.dog =?;";

    // read COMPETITION given COMPETITIONID
    private static final String STMT_READ_COMP = "SELECT * FROM competition WHERE competitionid = ?;";

    // read EVENT given EVENTID
    private static final String STMT_READ_EVENT = "SELECT * FROM event WHERE eventid = ?;";

    // add row in TAKEPARTIN
    private static final String STMT_READ_TPI = "SELECT * FROM takepartIn WHERE dog = ? AND competition = ?;";

    // add row in TAKEPARTIN
    private static final String STMT_ADD_TPI = "INSERT INTO takePartIn (Dog, Competition, Win) VALUES (?, ?, ?);";

    // delete ROW in TAKEPARTIN given DOG-MICROCHIP + COMPETITIONID
    private static final String STMT_DEL_TPI = "DELETE FROM takePartIn WHERE dog = ? AND competition = ?;";

    // add row in COMPETITION
    private static final String STMT_ADD_C = "INSERT INTO Competition (CompetitionID, Type, CompGroup, Class, EventID) VALUES (?, ?::COMPETITIONTYPE, ?, ?::COMPETITIONCLASS, ?);";

    // add row in EVENT
    private static final String STMT_ADD_E = "INSERT INTO event (EventID, Location, Begin, Finish, OriginCode) VALUES (?, ?, ?, ?, ?);";

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
    public CompetitionDB(final Connection con, final String microchip) {
        this.con = con;
        this.microchip = microchip;
    }

    /**
     * Check if a competition exists into the database
     * 
     * @return true if the competition exist, otherwise it returns false.
     *  
     * @param compid the id of the competition in the database.
     * 
     * @throws SQLException if any error occurs while getting the competition.
     */
    public boolean CheckComp(final String compid) throws SQLException {
        PreparedStatement pstmt = null;
        boolean check = false;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(STMT_READ_COMP);
            pstmt.setString(1, compid);

            rs = pstmt.executeQuery();

            if (rs.next())
                check = true;

        } finally {
            if (rs != null)
                rs.close();

            if (pstmt != null)
                pstmt.close();

            con.close();
        }
        return check;
    }

    /**
     * Check if an event exists into the database
     * 
     * @return true if the event exist, otherwise it returns false.
     *   
     * @param eventid the id of the event in the database.
     * 
     * @throws SQLException if any error occurs while getting the event.
     */
    public boolean CheckEvent(final String eventid) throws SQLException {
        PreparedStatement pstmt = null;
        boolean check = false;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(STMT_READ_EVENT);
            pstmt.setString(1, eventid);

            rs = pstmt.executeQuery();

            if (rs.next())
                check = true;

        } finally {
            if (rs != null)
                rs.close();

            if (pstmt != null)
                pstmt.close();

            con.close();
        }
        return check;
    }

    /**
     * Check if the dog has taken part in the competition
     * 
     * @return true if the dog has taken part in the competition, otherwise it returns false.
     *  
     * @param compid the id of the competition in the database.
     * 
     * @throws SQLException if any error occurs while getting the competition.
     */
    public boolean CheckTPI(final String compid) throws SQLException {
        PreparedStatement pstmt = null;
        boolean check = false;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(STMT_READ_TPI);
            pstmt.setString(1, microchip);
            pstmt.setString(2, compid);

            rs = pstmt.executeQuery();

            if (rs.next())
                check = true;

        } finally {
            if (rs != null)
                rs.close();

            if (pstmt != null)
                pstmt.close();

            con.close();
        }
        return check;
    }

    /**
     * Create the relation between the dog and the competition in which it has taken part 
     * 
     * @return true if the insertion is successfull, otherwise it returns false.
     *  
     * @param compid the id of the competition in the database.
     *  
     * @param w the boolean value which states if the dog has won the competition.
     * 
     * @throws SQLException if any error occurs while storing the dog/competition relation.
     */
    public boolean AddTPI(final String compid, final String w) throws SQLException {
        PreparedStatement pstmt = null;
        boolean check = false;
        int rs = -1;
        boolean input_w = false;
        if(w.equals("T"))
            input_w = true;

        try {
            pstmt = con.prepareStatement(STMT_ADD_TPI);
            pstmt.setString(1, microchip);
            pstmt.setString(2, compid);
            pstmt.setBoolean(3, input_w);

            rs = pstmt.executeUpdate();

            if (rs == 1)
                check = true;

        } finally {

            if (pstmt != null)
                pstmt.close();

            con.close();
        }
        return check;
    }

    /**
     * Delete the relation between the dog and the competition in which it has taken part 
     * 
     * @return true if the deletion is successfull, otherwise it returns false.
     *  
     * @param compid the id of the competition in the database.
     * 
     * @throws SQLException if any error occurs while storing the dog/competition relation.
     */
    public boolean DelTPI(final String compid) throws SQLException {
        PreparedStatement pstmt = null;
        boolean check = false;
        int rs = -1;

        try {
            pstmt = con.prepareStatement(STMT_DEL_TPI);
            pstmt.setString(1, microchip);
            pstmt.setString(2, compid);

            rs = pstmt.executeUpdate();

            if (rs == 1)
                check = true;

        } finally {

            if (pstmt != null)
                pstmt.close();

            con.close();
        }
        return check;
    }


    /**
     * Create a new competition in the database 
     * 
     * @return true if the insertion is successfull, otherwise it returns false.
     *  
     * @param comp_in the competition in the database.
     * 
     * @throws SQLException if any error occurs while storing the competition.
     */
    public boolean AddC(final Competition comp_in) throws SQLException {
        PreparedStatement pstmt = null;
        boolean check = false;
        int rs = -1;

        try {
            pstmt = con.prepareStatement(STMT_ADD_C);
            pstmt.setString(1, comp_in.getCompID());
            pstmt.setString(2, comp_in.getType());
            pstmt.setInt(3, Integer.valueOf(comp_in.getGroup()));
            pstmt.setString(4, comp_in.getCompClass());
            pstmt.setString(5, comp_in.getEventID());

            rs = pstmt.executeUpdate();

            if (rs == 1)
                check = true;

        } finally {

            if (pstmt != null)
                pstmt.close();

            con.close();
        }
        return check;
    }

    /**
     * Create a new event in the database 
     * 
     * @return true if the insertion is successfull, otherwise it returns false.
     *  
     * @param event_in the event in the database.
     * 
     * @throws SQLException if any error occurs while storing the event.
     */
    public boolean AddE(final Event event_in) throws SQLException {
        PreparedStatement pstmt = null;
        boolean check = false;
        int rs = -1;

        try {
            pstmt = con.prepareStatement(STMT_ADD_E);
            pstmt.setString(1, event_in.getEventID());
            pstmt.setString(2, event_in.getLocation());
            pstmt.setString(3, event_in.getBegin());
            pstmt.setString(4, event_in.getFinish());
            pstmt.setString(5, event_in.getZip());

            rs = pstmt.executeUpdate();

            if (rs == 1)
                check = true;

        } finally {

            if (pstmt != null)
                pstmt.close();

            con.close();
        }
        return check;
    }

    /**
     * Search events/competitions to which a dog has taken part
     * 
     * @return the {@code List<Competition>} object matching the dog's microchip.
     * 
     * @throws SQLException if any error occurs while getting the events/competitions.
     */
    public List<Competition> GetInfo() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        final List<Competition> competitions = new ArrayList<Competition>();

        try {
            pstmt = con.prepareStatement(STMT);
            pstmt.setString(1, microchip);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                competitions.add(new Competition(rs.getString("win"), rs.getString("competitionid"),
                        rs.getString("type"), String.valueOf(rs.getInt("compgroup")), rs.getString("class"),
                        rs.getString("eventid")));
            }
        } finally {
            if (rs != null)
                rs.close();

            if (pstmt != null)
                pstmt.close();

            con.close();
        }
        return competitions;
    }
}