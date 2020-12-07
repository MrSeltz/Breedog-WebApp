package it.unipd.dei.breedog.database;

import it.unipd.dei.breedog.resource.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Get a breed from the database.
 * 
 */
public class GetBreedDB {

    /**
     * The SQL statement to be executed to search for a breed
     */
    private static final String STMT = "SELECT * FROM breed WHERE fci = ?;";

    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The breed fci
     */
    private final String fci;

    /**
     * Creates a new object for searching a breeder/breed into the database.
     * 
     * @param con the connection to the database.
     * 
     * @param fci the fci of the breed in the database.
     */
    public GetBreedDB(final Connection con, final String fci) {
        this.con = con;
        this.fci = fci;
    }

    /**
     * Search breed from breed fci into the database
     * 
     * @return the {@code Breed} object matching the breed's fci.
     * 
     * @throws SQLException if any error occurs while getting the breed.
     */
    public Breed GetInfo() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Breed b = null;
        int count = -1;
        try {
            pstmt = con.prepareStatement(STMT);
            pstmt.setInt(1, Integer.valueOf(fci));

            rs = pstmt.executeQuery();

            if (rs.next())
                b = new Breed(rs.getInt("fci"), rs.getInt("breedgroup"), rs.getString("name"), count);
        } finally {
            if (rs != null)
                rs.close();

            if (pstmt != null)
                pstmt.close();

            con.close();
        }
        return b;
    }

}