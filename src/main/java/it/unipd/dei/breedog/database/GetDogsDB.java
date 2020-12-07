package it.unipd.dei.breedog.database;

import it.unipd.dei.breedog.resource.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Get the dogs related to a breeder into the database.
 * 
 * Get the dog with a specific microchip code into the database.
 */
public class GetDogsDB {

    /**
     * The SQL statement to be executed to search for dogs owned by a breeer
     */
    private static final String STATEMENT = "SELECT * FROM dog WHERE breederfc = ?;";

    /**
     * The SQL statement to be executed to search for a dog
     */
    private static final String STMT2 = "SELECT * FROM dog WHERE microchip = ?;";

    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The breederfc of the breeder
     */
    private final String breederfc;

    /**
     * The microchip code of the dog
     */
    private final String microchip;

    /**
     * Creates a new object for searching a dog pathologies into the database.
     * 
     * @param con       the connection to the database.
     * 
     * @param breederfc the breeder fc of the breeder in the database.
     * 
     * @param microchip the microchip of the dog in the database.
     */
    public GetDogsDB(final Connection con, final String breederfc, final String microchip) {
        this.con = con;
        this.breederfc = breederfc;
        this.microchip = microchip;
    }

    /**
     * Search dog from microchip into the database
     * 
     * @return the {@code Dog} object matching the dog's microchip.
     * 
     * @throws SQLException if any error occurs while getting the dog.
     */
    public final Dog GetDog() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Dog d = null;

        try {
            pstmt = con.prepareStatement(STMT2);
            pstmt.setString(1, microchip);

            rs = pstmt.executeQuery();

            if (rs.next())
                d = new Dog(rs.getString("microchip"), rs.getString("tattoo"), rs.getString("name"),
                        rs.getString("birth"), rs.getString("sex"), rs.getDouble("height"), rs.getDouble("weight"),
                        rs.getString("coat"), rs.getString("character"), rs.getString("dna"), rs.getString("teeth"),
                        rs.getString("signs"), rs.getString("photo"), rs.getString("owner"), rs.getString("status"),
                        rs.getInt("fci"), rs.getString("breederfc"), rs.getString("kennel"));

        } finally {
            if (rs != null)
                rs.close();

            if (pstmt != null)
                pstmt.close();

            con.close();
        }
        return d;
    }

    /**
     * Search dog from microchip into the database
     * 
     * @return the {@code List<Dog>} object matching the breeder's breeder fc.
     * 
     * @throws SQLException if any error occurs while getting the dogs.
     */
    public final List<Dog> GetDogs() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        final List<Dog> dogs = new ArrayList<Dog>();

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, breederfc);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                dogs.add(new Dog(rs.getString("microchip"), rs.getString("tattoo"), rs.getString("name"),
                        rs.getString("birth"), rs.getString("sex"), rs.getDouble("height"), rs.getDouble("weight"),
                        rs.getString("coat"), rs.getString("character"), rs.getString("dna"), rs.getString("teeth"),
                        rs.getString("signs"), rs.getString("photo"), rs.getString("owner"), rs.getString("status"),
                        rs.getInt("fci"), rs.getString("breederfc"), rs.getString("kennel")));
            }
        } finally {
            if (rs != null)
                rs.close();

            if (pstmt != null)
                pstmt.close();

            con.close();
        }
        return dogs;
    }
}