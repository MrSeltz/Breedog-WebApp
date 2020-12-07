package it.unipd.dei.breedog.database;

import it.unipd.dei.breedog.resource.Breeder;
import it.unipd.dei.breedog.resource.Breed;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Get the breeder and its related breeds from the database.
 * 
 */
public class GetBreederDB {

    /**
     * The SQL statement to be executed to search for a breeder from its breeder fc
     */
    private static final String STATEMENT = "SELECT * FROM breeder WHERE breederfc = ?;";

    /**
     * The SQL statement to be executed to search for a breeder's breed
     */
    private static final String STMT2 = "SELECT breed.fci, breed.breedgroup, breed.name, COUNT(*) FROM breed INNER JOIN dog ON breed.fci = dog.fci INNER JOIN breeder ON breeder.breederfc = dog.breederfc WHERE dog.breederfc = ? GROUP BY breed.fci;";

    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The breederfc of the breeder
     */
    private final String breederfc;

    /**
     * Creates a new object for searching a breeder/breed into the database.
     * 
     * @param con       the connection to the database.
     * 
     * @param breederfc the breeder fc of the breeder in the database.
     */
    public GetBreederDB(final Connection con, final String breederfc) {
        this.con = con;
        this.breederfc = breederfc;
    }

    /**
     * Search breeder from breederfc into the database
     * 
     * @return the {@code Breeder} object matching the breeder's breederfc.
     * 
     * @throws SQLException if any error occurs while getting the breeder.
     */
    public Breeder GetInfo() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Breeder b = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, breederfc);

            rs = pstmt.executeQuery();

            if (rs.next())
                b = new Breeder(rs.getString("breederfc"), rs.getString("name"), rs.getString("surname"),
                        rs.getString("birth"), rs.getString("address"), rs.getString("telephone"),
                        rs.getString("email"), rs.getString("vat"), rs.getString("photo"), rs.getString("description"));
        } finally {
            if (rs != null)
                rs.close();

            if (pstmt != null)
                pstmt.close();

            con.close();
        }
        return b;
    }

    /**
     * Search breeds from breederfc into the database
     * 
     * @return the {@code List<Breed>} object matching the breeder's breederfc.
     * 
     * @throws SQLException if any error occurs while getting the breeder's breeds.
     */
    public final List<Breed> GetBreeds() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        final List<Breed> breeds = new ArrayList<Breed>();

        try {
            pstmt = con.prepareStatement(STMT2);
            pstmt.setString(1, breederfc);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                breeds.add(
                        new Breed(rs.getInt("fci"), rs.getInt("breedgroup"), rs.getString("name"), rs.getInt("count")));
            }
        } finally {
            if (rs != null)
                rs.close();

            if (pstmt != null)
                pstmt.close();

            con.close();
        }
        return breeds;
    }
}