package it.unipd.dei.breedog.database;

import it.unipd.dei.breedog.resource.Dog;
import it.unipd.dei.breedog.resource.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Updates information related to a specific dog into the database.
 * 
 */
public class UpdateDogDB {

    /**
     * The SQL statement to be executed to update the dog photo
     */
    private static final String STMT_photo = "UPDATE dog SET photo = ? WHERE microchip = ?;";

    /**
     * The SQL statement to be executed to update the dog informations
     */
    private static final String STMT_info = "UPDATE dog SET name = ?, birth = ?, owner = ?, status = ?::dogstatus, height = ?, weight = ?, coat = ?, character = ?, teeth = ?, signs = ? WHERE microchip = ?;";

    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The dog to be updated into the database
     */
    private final Dog dog;

    /**
     * Creates a new object for modifying a dog into the database.
     * 
     * @param con the connection to the database.
     * 
     * @param dog the dog to be updated in the database.
     */
    public UpdateDogDB(final Connection con, final Dog dog) {
        this.con = con;
        this.dog = dog;
    }

    /**
     * Updates the dog photo into the database
	 * 
	 * @return true if the update was successfull, false if some error occurred
     * 
     * @throws SQLException if any error occurs while storing the photo.
     */
    public boolean updatePhoto() throws SQLException {
        PreparedStatement pstmt = null;
        Message m = null;
        int rs = -1;
        try {
            pstmt = con.prepareStatement(STMT_photo);
            pstmt.setString(1, dog.getPhoto());
            pstmt.setString(2, dog.getMicrochip());

            rs = pstmt.executeUpdate();
        } catch (SQLException ex) {
            m = new Message("SQL EXCEPTION", "E123", ex.getMessage());
        } finally {
            if (pstmt != null)
                pstmt.close();

            con.close();
        }

        // rs != 1 ---> Update failed
        if (rs != 1)
            return false;
        return true;
    }

    /**
     * Updates the dog informations into the database
	 * 
	 * @return true if the update was successfull, false if some error occurred
     * 
     * @throws SQLException if any error occurs while storing the information.
     */
    public boolean updateInfo() throws SQLException {
        PreparedStatement pstmt = null;
        Message m = null;
        int rs = -1;

        try {
            pstmt = con.prepareStatement(STMT_info);
            pstmt.setString(1, dog.getName());
            pstmt.setString(2, dog.getBirth());
            pstmt.setString(3, dog.getOwner());
            pstmt.setString(4, dog.getStatus());
            pstmt.setDouble(5, dog.getHeight());
            pstmt.setDouble(6, dog.getWeight());
            pstmt.setString(7, dog.getCoat());
            pstmt.setString(8, dog.getCharacter());
            pstmt.setString(9, dog.getTeeth());
            pstmt.setString(10, dog.getSigns());
            pstmt.setString(11, dog.getMicrochip());

            rs = pstmt.executeUpdate();
        } catch (SQLException ex) {
            m = new Message("SQL EXCEPTION", "E123", ex.getMessage());
        } finally {
            if (pstmt != null)
                pstmt.close();

            con.close();
        }

        // rs != 1 ---> Update failed
        if (rs != 1)
            return false;
        return true;
    }
}