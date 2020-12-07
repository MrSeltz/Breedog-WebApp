package it.unipd.dei.breedog.database;

import it.unipd.dei.breedog.resource.Breeder;
import it.unipd.dei.breedog.resource.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Updates information related to a specific breeder into the database.
 * 
 */
public class UpdateBreederDB {

        /**
     * The SQL statement to be executed to update the breeder photo
     */
    private static final String STMT_photo = "UPDATE breeder SET photo = ? WHERE breederfc = ?;";
    
    /**
     * The SQL statement to be executed to update the breeder informations
     */
    private static final String STMT = "UPDATE breeder SET email = ?, telephone = ?, address = ?, vat = ?, description = ? WHERE breederfc = ?;";

    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The breeder to be updated into the database
     */
    private final Breeder breeder;
    
    /**
     * Creates a new object for modifying a breeder into the database.
     * 
     * @param con the connection to the database.
     * 
     * @param breeder the breeder to be updated in the database.
     */
    public UpdateBreederDB(final Connection con, final Breeder breeder)
    {
        this.con = con;
        this.breeder = breeder;
    }
 
    /**
     * Updates the breeder photo into the database
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
            pstmt.setString(1, breeder.getPhoto());
            pstmt.setString(2, breeder.getBreederfc());

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
     * Updates the breeder informations into the database
	 * 
	 * @return true if the update was successfull, false if some error occurred
     * 
     * @throws SQLException if any error occurs while storing the informations.
     */
    public boolean update() throws SQLException
    {
        PreparedStatement pstmt= null;
        int rs = -1;
        Breeder b = null;
        Message m = null;

        try
        {
            pstmt = con.prepareStatement(STMT);
            pstmt.setString(1, breeder.getEmail());
            pstmt.setString(2, breeder.getTelephone());
            pstmt.setString(3, breeder.getAddress());
            pstmt.setString(4, breeder.getVat());
            pstmt.setString(5, breeder.getDescription());
            pstmt.setString(6, breeder.getBreederfc());

            rs = pstmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            m = new Message("SQL EXCEPTION", "E123", ex.getMessage());
        }
        finally
        {
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