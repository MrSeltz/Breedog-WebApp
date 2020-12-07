package it.unipd.dei.breedog.database;

import it.unipd.dei.breedog.resource.*;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//import java.util.logging.Level; 
//import java.util.logging.Logger;
/**
 * Search a dog into the database.
 * 
 */
public class SearchDogsDB {

    /**
     * The SQL statement to be executed to search for a dog
     */
    private String STATEMENT = "select * from dog where "
            + " (? IS NULL OR to_tsvector(microchip || ' ' || name|| ' ' || kennel || ' ' || coat || ' ' || character || ' ' || owner || ' ' || breederfc) @@ to_tsquery(?) )"
            + " AND (? IS NULL OR SEX = ?::gender) " 
            + " AND (? IS NULL OR STATUS = ?::dogstatus )"
            + " AND (? IS NULL OR STATUS = 'Adottabile')  "
            + " AND (? IS NULL OR DATE_PART('year', CURRENT_DATE) - DATE_PART('year', birth::date) < ?::INTEGER) ";

    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The query to be executed into the database
     */
    private final String query;

    /**
     * The filters to be used for searching into the database
     */
    private final String sex;
    private final String status;
    private final String maxAge;
    private boolean userIsRegistered;
    /**
     * The order to be used to return the search values into the database
     */
    private final String orderBy;

    /**
     * The limits to be used for pagination
     */
    private int limit;
    private int offset;

    /**
     * Object for the query
     * @param con connection to the db
     * @param query text search query
     * @param sex sex of dog
     * @param status status of dog
     * @param maxAge max age of a dog
     * @param orderBy order to display dogs
     * @param limit limit of dog to diplay per page
     * @param offset offset of dog to display
     * @param userIsRegistered flag to know is a user is registered
     */
    public SearchDogsDB(final Connection con, final String query, final String sex, final String status,
            final String maxAge, final String orderBy, final int limit, final int offset, final boolean userIsRegistered) {
        this.con = con;
        this.query = query;
        this.sex = sex;
        this.status = status;
        this.userIsRegistered = userIsRegistered;
        this.maxAge = getAge(maxAge);
        this.orderBy = orderBy;
        this.limit = limit;
        this.offset = offset;
        
        if (orderBy == "ASC") {
            this.STATEMENT = STATEMENT + " ORDER BY birth ASC";
        } else if (orderBy == "DESC") {
            this.STATEMENT = STATEMENT + " ORDER BY birth DESC";
        }

        this.STATEMENT += " LIMIT ? OFFSET ? ";
        this.STATEMENT += " ;";

    }

    /**
     * Search dogs into the database
     * @return the {@code List<Dog>} object matching the query.
     * @throws SQLException if any error occurs while searching the dog.
     */
    public final List<Dog> GetDogs() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        final List<Dog> dogs = new ArrayList<Dog>();

        try {

            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, query);
            pstmt.setString(2, query);
            pstmt.setString(3, sex);
            pstmt.setString(4, sex);
            pstmt.setString(5, status);
            pstmt.setString(6, status);
            
            if (userIsRegistered){
            pstmt.setString(7, null) ;
            } else {
                pstmt.setString(7, " ");    
            }

            pstmt.setString(8, maxAge);
            pstmt.setString(9, maxAge);
            pstmt.setInt(10, limit);
            pstmt.setInt(11, offset);

            rs = pstmt.executeQuery();
            
            //debug
            //String executedQuery = rs.getStatement().toString();
            //System.out.println(executedQuery);

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

    /**
     * Parse the string of dog age
     * 
     * @return the parsing of age string.
     */
    private String getAge(String age) {
        String maxAge = null;
        try {
            maxAge = Integer.toString( Integer.parseInt(age) ) ;
        } catch (NumberFormatException e) {
            maxAge = null;
        } finally {
            return maxAge;
        }
    }
}