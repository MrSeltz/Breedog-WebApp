package it.unipd.dei.breedog.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.unipd.dei.breedog.resource.Message;
import it.unipd.dei.breedog.resource.Whichas;

/**
 * Creates or deletes a pathology related to a specific dog into the database.
 * 
 */
public class WhichasDB {

    /**
     * The SQL statement to be executed to create a new pathology
     */
    private static final String STMT_CREATE = "INSERT INTO Whichas (Dog, Pathology, Severity) VALUES (?, ?, ?) RETURNING *";

    /**
     * The SQL statement to be executed to delete an existing pathology
     */
    private static final String STMT_DELETE = "DELETE FROM Whichas WHERE dog = ? AND pathology = ? RETURNING *";

    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * Creates a new object for storing or modifying a pathology into the database.
     * 
     * @param con the connection to the database.
     */
    public WhichasDB(final Connection con) {
        this.con = con;
    }

    /**
     * Stores a new pathology into the database
     * 
     * @param w the pathology to be stored into the database.
     * 
     * @throws SQLException if any error occurs while storing the pathology.
     */
    public void createWhichas(Whichas w) throws SQLException {
        PreparedStatement pstmt_succ = null;

        Message m = null;

        try {

            pstmt_succ = con.prepareStatement(STMT_CREATE);
            pstmt_succ.setString(1, w.getDog());
            pstmt_succ.setString(2, w.getPathology());
            pstmt_succ.setString(3, w.getSeverity());

            pstmt_succ.execute();

        } catch (SQLException e) {
            m = new Message("SQL EXCEPTION", "E123", e.getMessage());
        } finally {

            if (pstmt_succ != null)
                pstmt_succ.close();

            con.close();
        }
    }

    /**
     * Deletes an existing pathology into the database
     * 
     * @param w the pathology to be deleted into the database.
     * 
     * @throws SQLException if any error occurs while storing the pathology.
     */
    public void deleteWhichas(Whichas w) throws SQLException {
        PreparedStatement pstmt = null;

        Message m = null;

        try {
            pstmt = con.prepareStatement(STMT_DELETE);
            pstmt.setString(1, w.getDog());
            pstmt.setString(2, w.getPathology());

            pstmt.execute();

        } catch (SQLException e) {
            m = new Message("SQL EXCEPTION", "E123", e.getMessage());
        } finally {

            if (pstmt != null)
                pstmt.close();

            con.close();
        }
    }
}