package it.unipd.dei.breedog.database;

import it.unipd.dei.breedog.resource.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Get the parents/sons related to a dog into the database.
 * 
 */
public class GetGenealogyDB {

    /**
     * The SQL statement to be executed to search for a dog parents
     */
    private static final String STMT_pred = "SELECT dog.microchip, dog.name, dog.sex FROM succeed INNER JOIN dog ON succeed.predecessor = dog.microchip WHERE  successor = ?;";

    /**
     * The SQL statement to be executed to search for a dog sons
     */
    private static final String STMT_succ = "SELECT dog.microchip, dog.name, dog.birth FROM succeed INNER JOIN dog ON succeed.successor = dog.microchip WHERE predecessor = ?;";

    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The microchip code of the dog
     */
    private final String microchip;

    /**
     * Creates a new object for searching a dog predecessor/successor into the
     * database.
     * 
     * @param con       the connection to the database.
     * 
     * @param microchip the microchip of the dog in the database.
     */
    public GetGenealogyDB(final Connection con, final String microchip) {
        this.con = con;
        this.microchip = microchip;
    }

    /**
     * Search dog's parents into the database
     * 
     * @return the {@code List<Dog>} object matching the dog's microchip.
     * 
     * @throws SQLException if any error occurs while getting predecessors of the
     *                      dog.
     */
    public List<Dog> GetParents() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        final List<Dog> parents = new ArrayList<Dog>();

        try {
            pstmt = con.prepareStatement(STMT_pred);
            pstmt.setString(1, microchip);

            rs = pstmt.executeQuery();

            while (rs.next()) {

                if (rs.getString("name") != null) {
                    parents.add(
                            new Dog(rs.getString("microchip"), null, rs.getString("name"), null, rs.getString("sex"),
                                    -1, -1, null, null, null, null, null, null, null, null, -1, null, null));
                } else {
                    // If theparent is not in the db, but we have the microchip code
                    parents.add(new Dog(rs.getString("microchip"), null, null, null, null, -1, -1, null, null, null,
                            null, null, null, null, null, -1, null, null));
                }
            }
        } finally {
            if (rs != null)
                rs.close();

            if (pstmt != null)
                pstmt.close();

            con.close();
        }
        return parents;
    }

    /**
     * Search dog's sons into the database
     * 
     * @return the {@code List<Dog>} object matching the dog's microchip.
     * 
     * @throws SQLException if any error occurs while getting successors of the dog.
     */
    public List<Dog> GetSuccessors() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        final List<Dog> successors = new ArrayList<Dog>();

        try {
            pstmt = con.prepareStatement(STMT_succ);
            pstmt.setString(1, microchip);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                successors.add(new Dog(rs.getString("microchip"), null, rs.getString("name"), rs.getString("birth"),
                        null, -1, -1, null, null, null, null, null, null, null, null, -1, null, null));
            }
        } finally {
            if (rs != null)
                rs.close();

            if (pstmt != null)
                pstmt.close();

            con.close();
        }
        return successors;
    }
}