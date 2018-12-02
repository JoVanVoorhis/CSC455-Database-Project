/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc455_wpac;

import com.sun.rowset.CachedRowSetImpl;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JordanKayleeVanVoorhis
 */
public class CSC455_DatabaseProject{


    public static void loadDriver(){
        try {
            //Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver instance okay...");
        }
        catch (Exception e){
            System.err.println("Unable to load driver!");
            e.printStackTrace();
        }
    }
    
    public static Connection establish_connection(){
        Connection connection = null;
        String database = "CSC455FALL18_01";
        String username = "jkv4623";
        String password = "XJ8c1XHbm";
        try{
            System.out.println("Establishing connection with MySql server on satoshi...");
            connection = DriverManager.getConnection("jdbc:mysql://152.20.12.152/"+database+"?noAccessToProcedureBodies=true&useSSL=false"
                    +"&useJDBCCompliantTimezoneShift=trueuseLegacyDatetimeCode=false&serverTimezone=UTC"
                    +"&user="+username+"&password="+password);
            System.out.println("Connection with MySql server on satoshi.cis.uncw.edu established.");
        }
        catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState:     " + e.getSQLState());
            System.out.println("VendorError:  " + e.getErrorCode());
        }
        return connection;
    }
    
    public static void disconnect_connection(Connection connection) throws Exception{
        try{
            if (connection != null && !connection.isClosed()){
                connection.close();
            }
        }catch(Exception e){
            throw e;
        }
    }
    
    private static void use_database(Connection connection) {
        try{
            Statement stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery("SELECT * from Pricing");
            while (rset.next()){
                System.out.println(rset.getString(1) + "\t" + rset.getString("SEC_A"));
            }
            stmt.executeUpdate("update Pricing set SEC_A = SEC_A*1.1 where PEVENT_ID = 1006");
            System.out.println("\n**********SEC_A Updated***********\n");
            rset = stmt.executeQuery("SELECT * from Pricing");
            while (rset.next()){
                System.out.println(rset.getString(1) + "\t" + rset.getString("SEC_A"));
            }
            stmt.close();
            rset.close();
        }
        catch (SQLException e){
            System.out.println("SQLEXception: " + e.getMessage());
            System.out.println("SQLState:     " + e.getSQLState());
            System.out.println("VendorError:  " + e.getErrorCode());
        }
    }
    
    public static void executeQuery(String sqlStmt) throws SQLException, ClassNotFoundException, Exception{
        Statement stmt = null;
        Connection conn = establish_connection();
        try{
            stmt = conn.createStatement();
            stmt.executeQuery(sqlStmt);
        } catch (SQLException e){
            System.out.println("Error occured while executing the query " + sqlStmt);
            throw e;
        }
        finally{
            if(stmt != null){
                stmt.close();
            }
            disconnect_connection(conn);
        }
    }
    
    @SuppressWarnings("null")
    public static ResultSet getResult(String sqlQuery) throws SQLException, Exception {
        Statement stmt = null;
        ResultSet result = null;
        CachedRowSetImpl cached = null;
        Connection conn = establish_connection();
        try{
            stmt = conn.createStatement();
            result = stmt.executeQuery(sqlQuery);
            cached = new CachedRowSetImpl();
            cached.populate(result);
        }catch(SQLException e){
            System.out.println("Error occured while getting result for the query " + sqlQuery);
            throw e;
        }finally{
            if (result != null){
                result.close();
            }
            if (stmt != null){
                stmt.close();
            }
            disconnect_connection(conn);
        }
        return cached;
    }
    
    public static void executeNewCustomer(String first, String last, String pass) throws SQLException, Exception{
        Connection conn = establish_connection();
        CallableStatement call = null;
        try {
            call = conn.prepareCall("{call newCustomer(?,?,?)}");
            call.setString(1, first);
            call.setString(2, last);
            call.setString(3, pass);
            call.execute();
        } catch (SQLException ex) {
            System.out.println("Error occured while adding customer.");
            Logger.getLogger(CSC455_DatabaseProject.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if (call != null){
                call.close();
            }
            disconnect_connection(conn);
        }
    }
        
    public static int executeSecPrice(int eid, String sec) throws SQLException, Exception{
        Connection conn = establish_connection();
        CallableStatement call = null;
        int output = 0;
        try {
            call = conn.prepareCall("{? = call secPrice(?,?)}");
            call.registerOutParameter(1, Types.INTEGER);
            call.setInt(2, eid);
            call.setString(3, sec);
            call.execute();
            output = call.getInt(1);
        } catch (SQLException ex) {
            System.out.println("Error occured while getting section price.");
            Logger.getLogger(CSC455_DatabaseProject.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if (call != null){
                call.close();
            }
            disconnect_connection(conn);
        }
        return output;
    }
    
    public static void executeDeleteCustomer(int cid) throws Exception{
        Connection conn = establish_connection();
        try (CallableStatement call = conn.prepareCall("{call deleteCustomer(?)}")) {
            call.setInt(1, cid);
            call.execute();
        } catch (SQLException ex) {
            System.out.println("Error occured while deleting customer.");
            Logger.getLogger(CSC455_DatabaseProject.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            disconnect_connection(conn);
        }
    }
    
    public static void executeMakeTransaction(int tid, int cid) throws Exception{
        Connection conn = establish_connection();
        try (CallableStatement call = conn.prepareCall("{call makeTransaction(?,?)}")) {
            call.setInt(1, tid);
            call.setInt(2, cid);
            call.execute();
        } catch (SQLException ex) {
            System.out.println("Error occured while making transaction.");
            Logger.getLogger(CSC455_DatabaseProject.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            disconnect_connection(conn);
        }
    }
    
    @SuppressWarnings({"ConvertToTryWithResources", "null"})
    public static void executePurchaseTicket(int tid, String sid, int rid, int sno, int cid) throws SQLException, Exception{
        Connection conn = establish_connection();
        CallableStatement call = null;
        try {
            call = conn.prepareCall("{call purchaseTicket(?,?,?,?,?)}");
            System.out.println(tid);
            System.out.println(call.toString());
            call.setInt(1, tid);
            call.setString(2, sid);
            call.setInt(3, rid);
            call.setInt(4, sno);
            call.setInt(5, cid);
            System.out.println(call.toString());
            call.execute();
        } catch (SQLException ex) {
            System.out.println("Error occured while purchasing ticket.");
            Logger.getLogger(CSC455_DatabaseProject.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if (call != null){
                call.close();
            }
            disconnect_connection(conn);
        }
    }
    
    public static boolean getSpecificSeatAvailability(String sid, int rid, int sno, int eid) throws SQLException, Exception{
        Connection conn = establish_connection();
        PreparedStatement prepared = null;
        boolean result = false;
        try{
            prepared = conn.prepareStatement("SELECT ENAME, SEC_ID, ROW_ID, SEAT_NUMBER, TAVAILABILITY FROM ( SELECT EVENT_ID, ENAME, TICKET_ID, TSEAT_ID, TAVAILABILITY FROM Event, Ticket where EVENT_ID = TEVENT_ID) x JOIN Seat ON x.TSEAT_ID = SEAT_ID WHERE SEC_ID = ? and ROW_ID = ? and SEAT_NUMBER = ? AND EVENT_ID = ?");
            prepared.setString(1, sid);
            prepared.setInt(2, rid);
            prepared.setInt(3, sno);
            prepared.setInt(4, eid);
            result = prepared.execute();
            System.out.println(result);
        } catch(SQLException ex){
            System.out.println("Error occured while getting specific seat availability.");
            Logger.getLogger(CSC455_DatabaseProject.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            if (prepared != null){
                prepared.close();
            }
            disconnect_connection(conn);
        }
        return result;
    }
    
    public static ResultSet getSectionAvailability(int id, String sid) throws SQLException, Exception{
        Connection conn = establish_connection();
        PreparedStatement prepared = null;
        ResultSet result = null;
        CachedRowSetImpl cached = null;
        try{
            prepared = conn.prepareStatement("SELECT count(TAVAILABILITY) AS AVAILABLE FROM Ticket JOIN Seat ON TSEAT_ID = SEAT_ID WHERE TAVAILABILITY = 'Y' AND SEC_ID = ? AND TEVENT_ID = ?");
            prepared.setString(1, sid);
            prepared.setInt(2, id);
            result =  prepared.executeQuery();
            cached = new CachedRowSetImpl();
            cached.populate(result);
        } catch(SQLException ex){
            System.out.println("Error occured while getting section availability.");
            Logger.getLogger(CSC455_DatabaseProject.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            if (result != null){
                result.close();
            }
            if (prepared != null){
                prepared.close();
            }
            disconnect_connection(conn);
        }
        return cached;
    }
    
    public static ResultSet getRowAvailability(int id, String desiredOutput, String extra) throws SQLException, Exception{
        Connection conn = establish_connection();
        PreparedStatement prepared = null;
        ResultSet result = null;
        CachedRowSetImpl cached = null;
        try{
            prepared = conn.prepareStatement("SELECT " + desiredOutput + " FROM Seat join (Event join Ticket on EVENT_ID = TEVENT_ID) on TSEAT_ID = SEAT_ID where EVENT_ID = ? and TAVAILABILITY = 'Y'" + extra);
            System.out.println(prepared.toString());
            prepared.setInt(1, id);
            result =  prepared.executeQuery();
            cached = new CachedRowSetImpl();
            cached.populate(result);
        } catch(SQLException ex){
            System.out.println("Error occured while getting " + desiredOutput );
            Logger.getLogger(CSC455_DatabaseProject.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            if (result != null){
                result.close();
            }
            if (prepared != null){
                prepared.close();
            }
            disconnect_connection(conn);
        }
        return cached;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        loadDriver();
        Connection conn = establish_connection();
        use_database(conn);
        disconnect_connection(conn);
    }
}
