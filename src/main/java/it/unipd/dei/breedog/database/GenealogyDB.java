package it.unipd.dei.breedog.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.unipd.dei.breedog.resource.Dog;
import it.unipd.dei.breedog.resource.Genealogy;
import it.unipd.dei.breedog.resource.Message;

/**
 * Creates or deletes successors/predecessors related to a specific dog into the database.
 * 
 */
public class GenealogyDB {

    /**
     * The SQL statement to be executed to create a new genealogy
     */
    private static final String INSERT_SUCCEED = "INSERT INTO Succeed (Predecessor, Successor, DateBirth) VALUES (?, ?, ?) RETURNING *";

    /**
     * The SQL statement to be executed to delete an existing genealogy
     */
    private static final String STMT_DELETE = "DELETE FROM Succeed WHERE Predecessor = ? AND Successor = ? RETURNING *";

    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * Creates a new object for storing or modifying a genealogy into the database.
     * 
     * @param con the connection to the database.
     */
    public GenealogyDB(final Connection con) {
        this.con = con;
    }

    /**
     * Stores a new genealogy into the database
     * 
     * @param g the genealogy to be stored into the database.
     * 
     * @throws SQLException if any error occurs while storing the genealogy.
     */
    public void createGenelaogy(Genealogy g) throws SQLException {
        PreparedStatement pstmt_succ = null;

        Message m = null;

        try {

            Dog d = getDog(g.getSuccessor());

            pstmt_succ = con.prepareStatement(INSERT_SUCCEED);
            pstmt_succ.setString(1, g.getPredecessor());
            pstmt_succ.setString(2, g.getSuccessor());
            pstmt_succ.setString(3, d.getBirth());

            pstmt_succ.execute();

        } catch (SQLException e) {
            m = new Message("SQL EXCEPTION", "E123", e.getMessage());
        } finally {

            if (pstmt_succ != null)
                pstmt_succ.close();

            con.close();

        }
    }

     /**
     * Deletes an existing genealogy into the database
     * 
     * @param g the genealogy to be deleted into the database.
     * 
     * @throws SQLException if any error occurs while storing the genealogy.
     */
    public void deleteGenealogy(Genealogy g) throws SQLException {
        PreparedStatement pstmt = null;

        Message m = null;

        try {
            pstmt = con.prepareStatement(STMT_DELETE);
            pstmt.setString(1, g.getPredecessor());
            pstmt.setString(2, g.getSuccessor());

            pstmt.execute();

        } catch (SQLException e) {
            m = new Message("SQL EXCEPTION", "E123", e.getMessage());
        } finally {

            if (pstmt != null)
                pstmt.close();

            con.close();
        }
    }

    /**
     * Search dog from microchip into the database
     * 
     * @return the {@code Dog} object matching the dog's microchip.
     * 
     * @throws SQLException if any error occurs while getting the dog.
     */
    Dog getDog(String microchip) throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Dog d = null;

        try {
            pstmt = con.prepareStatement("SELECT * FROM dog WHERE microchip = ?;");
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

            // con.close();
        }

        return d;
    }
}
