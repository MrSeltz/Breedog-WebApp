package it.unipd.dei.breedog.database;

import it.unipd.dei.breedog.resource.Account;
import it.unipd.dei.breedog.resource.Breeder;
import it.unipd.dei.breedog.resource.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Creates a new user/breeder into the database.
 * 
 */
public class SignupDB
{
    /**
     * The SQL statement to be executed to create a new user
     */
    private static final String STMT_USER = "INSERT INTO Users (Username, Password, BreederFC) VALUES (?, ?, ?) RETURNING *";

    /**
     * The SQL statement to be executed to create a new breeder
     */
    private static final String STMT_BREEDER = "INSERT INTO Breeder (BreederFC, Name, Surname, Birth, Address, Telephone, Email) VALUES (?,?,?,?,?,?,?) RETURNING *";

    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The user to be created into the database
     */
    private final Account account;

    /**
     * The breeder to be created into the database
     */
    private final Breeder breeder;

    /**
     * Creates a new object for creating a user/breeder into the database.
     * 
     * @param con the connection to the database.
     * 
     * @param account the user to be created in the database.
     * 
     * @param breeder the breeder to be created in the database.
     */
    public SignupDB(final Connection con, final Account account, final Breeder breeder)
    {
        this.con = con;
        this.account = account;
        this.breeder = breeder;
    }
        
    /**
     * Creates the user/breeder into the database
	 * 
	 * @return true if the update was successfull, false if some error occurred
     * 
     * @throws SQLException if any error occurs while storing the user/breeder.
     */
    public boolean registration() throws SQLException
    {
        PreparedStatement pstmt_acc = null;
        PreparedStatement pstmt_breeder = null;

        ResultSet rsa = null;
        ResultSet rsb = null;
        Account a  = null;
        Breeder b = null;
        Message m = null;

        try
        {
            pstmt_acc = con.prepareStatement(STMT_USER);
            pstmt_acc.setString(1, account.getUsername());
            pstmt_acc.setString(2, account.getPassword());
            pstmt_acc.setString(3, account.getBreederfc());

            pstmt_breeder = con.prepareStatement(STMT_BREEDER);
            pstmt_breeder.setString(1, breeder.getBreederfc());
            pstmt_breeder.setString(2, breeder.getName());
            pstmt_breeder.setString(3, breeder.getSurname());
            pstmt_breeder.setString(4, breeder.getBirth());
            pstmt_breeder.setString(5, breeder.getAddress());
            pstmt_breeder.setString(6, breeder.getTelephone());
            pstmt_breeder.setString(7, breeder.getEmail());
            
            // According to the database structure we need to add breeder first, and after the account associated to the breederfc
            rsb = pstmt_breeder.executeQuery();
                if (rsb.next())
                    b = new Breeder(rsb.getString("breederfc"), rsb.getString("name"), rsb.getString("surname"), rsb.getString("birth"), rsb.getString("address"), rsb.getString("telephone"), rsb.getString("email"), rsb.getString("vat"), rsb.getString("photo"), rsb.getString("description"));
            
            if (b != null)
            {
                rsa = pstmt_acc.executeQuery();
                if (rsa.next())
                    a = new Account(rsa.getString("username"), rsa.getString("password"), rsa.getString("breederfc"));
            }
        }
        catch (SQLException ex)
        {
            m = new Message("SQL EXCEPTION", "E123", ex.getMessage());
        }
        finally
        {
            if (rsa != null) 
				rsa.close();
            
            if (rsb != null) 
                rsb.close();
                
			if (pstmt_acc != null) 
                pstmt_acc.close();

            if (pstmt_breeder != null) 
                pstmt_breeder.close();

			con.close();
        }

        if (a == null || b == null)
        {
            return false;
        }
        
        m = new Message("Registration successfull.");
        return true;
    }
    
}