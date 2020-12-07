package it.unipd.dei.breedog.database;

import it.unipd.dei.breedog.resource.Dog;
import it.unipd.dei.breedog.resource.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Creates a new dog into the database.
 * 
 */
public final class CreateDogDatabase {

    /**
     * The SQL statement to be executed to create a new dog
     */
    private static final String INSERT_DOG = "INSERT INTO Dog (Microchip, Tattoo, Name, Birth, Sex, Height, Weight, Coat, Character, Dna, Teeth, Signs, Photo, Owner, Status, FCI, BreederFC, Kennel) VALUES (?, ?, ?, ?, ?::gender, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?::dogstatus, ?, ?, ?) RETURNING *";

    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * Creates a new object for storing or modifying a pathology into the database.
     * 
     * @param con the connection to the database.
     */
    public CreateDogDatabase(final Connection con) {
        this.con = con;
    }

    /**
     * Stores a new dog into the database
     * 
     * @param dog the dog to be stored into the database.
     * 
     * @throws SQLException if any error occurs while storing the pathology.
     */
    public void createDog(Dog dog) throws SQLException {
        PreparedStatement pstmt = null;

        Message m = null;

        try {
            pstmt = con.prepareStatement(INSERT_DOG);
            pstmt.setString(1, dog.getMicrochip());
            pstmt.setString(2, dog.getTattoo());
            pstmt.setString(3, dog.getName());
            pstmt.setString(4, dog.getBirth());
            pstmt.setString(5, dog.getSex());
            pstmt.setDouble(6, dog.getHeight());
            pstmt.setDouble(7, dog.getWeight());
            pstmt.setString(8, dog.getCoat());
            pstmt.setString(9, dog.getCharacter());
            pstmt.setString(10, dog.getDna());
            pstmt.setString(11, dog.getTeeth());
            pstmt.setString(12, dog.getSigns());
            pstmt.setString(13, dog.getPhoto());
            pstmt.setString(14, dog.getOwner());
            pstmt.setString(15, dog.getStatus());
            pstmt.setInt(16, dog.getFci());
            pstmt.setString(17, dog.getBreederFc());
            pstmt.setString(18, dog.getKennel());


            pstmt.execute();
            
        } catch (SQLException ex)
        {
            m = new Message("SQL EXCEPTION", "E123", ex.getMessage());
        } finally {

			if (pstmt != null) 
                pstmt.close();

            con.close();
            
        }

    }

}