package it.unipd.dei.breedog.database;

import it.unipd.dei.breedog.resource.Account;
import it.unipd.dei.breedog.resource.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Get a user from the database.
 * 
 */
public class AuthAccountDB
{
    /**
     * The SQL statement to be executed to search for a user by its username
     */
    private static final String STATEMENT1 = "SELECT * FROM users WHERE username = ? AND password = ?;";

    /**
     * The SQL statement to be executed to update the user password
     */
    private static final String STATEMENT2 = "UPDATE users SET password = ? WHERE username = ?;";

    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user's username
     */
    private final String username;

    /**
     * The user's password
     */
    private final String password;
    

    /**
     * Creates a new object for searching a breeder/breed into the database.
     * 
     * @param con       the connection to the database.
     * 
     * @param username the user's username in the database.
     * 
     * @param password the user's password in the database.
     */
    public AuthAccountDB(final Connection con, final String username, final String password)
    {
        this.con = con;
        this.username = username;
        this.password = password;
    }

    /**
     * Search breeder from breederfc into the database
     * 
     * @return the {@code Account} object matching the user's username.
     * 
     * @throws SQLException if any error occurs while getting the breeder.
     */
    public Account AuthCheck() throws SQLException
    {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Account a  = null;

        try
        {
            pstmt = con.prepareStatement(STATEMENT1);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            rs =  pstmt.executeQuery();
            
            if (rs.next())
                a = new Account(rs.getString("username"), rs.getString("password"), rs.getString("breederfc"));
        }
        finally
        {
            if (rs != null) 
				rs.close();

			if (pstmt != null) 
                pstmt.close();
                
			con.close();
        }
        return a;
    }

    /**
     * Updates the user password into the database
	 * 
	 * @return true if the update was successfull, false if some error occurred
     * 
     *  
     * @param new_password the new password to be updated in the database.
     * 
     * @throws SQLException if any error occurs while updating the password.
     */
    public boolean changePassword(final String new_password) throws SQLException {
        
        PreparedStatement pstmt = null;
        Message m = null;
        int rs = -1;
        try {
            pstmt = con.prepareStatement(STATEMENT2);
            pstmt.setString(1, new_password);
            pstmt.setString(2, username);

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