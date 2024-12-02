package Wordle;

import java.sql.*;


public class DbFunctions {
    public Connection connectToDb(String dbname, String user, String password) {
        Connection con = null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, user, password);
            if (con != null){
                System.out.println("Connected to PostgreSQL database");
            }
            else {
                System.out.println("Failed to connect to PostgreSQL database");
            }
        } catch (Exception e){
            System.out.println(e);
        }
        return con;
    }
}