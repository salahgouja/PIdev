package utils;


import java.sql.*;
public class Mydatabase {
    final String URL = "jdbc:mysql://localhost:3306/maram";
    final String USER="root";
    final String PASS="";
    Connection connection;
    private static Mydatabase instance;
    private Mydatabase(){
        try {
            connection = DriverManager.getConnection(URL,USER,PASS);
            System.out.println("connected");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

}

    public  static Mydatabase getInstance() {
        if (instance == null) {
            instance = new Mydatabase();
        }
        return instance;
    }
    public Connection getConnection() {
        return connection;
    }


}
