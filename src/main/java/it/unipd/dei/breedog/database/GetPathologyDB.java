package it.unipd.dei.breedog.database;

import it.unipd.dei.breedog.resource.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Get the pathologies related to a dog into the database.
 * 
 */
public class GetPathologyDB {

    /**
     * The SQL statement to be executed to search for a dog pathologies
     */
    private static final String STMT = "SELECT * FROM whichas INNER JOIN pathology ON whichas.pathology = pathology.pathologycode WHERE whichas.dog = ?;";

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
    public GetPathologyDB(final Connection con, final String microchip) {
        this.con = con;
        this.microchip = microchip;
    }

    /**
     * Search dog's pathologies into the database
     * 
     * @return the {@code List<Pathology>} object matching the dog's microchip.
     * 
     * @throws SQLException if any error occurs while getting pathologies of the
     *                      dog.
     */
    public List<Pathology> GetInfo() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        final List<Pathology> pathology = new ArrayList<Pathology>();

        try {
            pstmt = con.prepareStatement(STMT);
            pstmt.setString(1, microchip);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                pathology.add(
                        new Pathology(rs.getString("pathologycode"), rs.getString("name"), rs.getString("severity")));
            }
        } finally {
            if (rs != null)
                rs.close();

            if (pstmt != null)
                pstmt.close();

            con.close();
        }
        return pathology;
    }
}