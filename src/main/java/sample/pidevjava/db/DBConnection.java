package sample.pidevjava.db;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class DBConnection {

    private static  DBConnection dbConnection;
    private final Connection connection ;

    public DBConnection(){
        try {
            //properties fih url, username , password
            Properties properties = new Properties();
            InputStream input = new FileInputStream("src/main/resources/db.properties");
            properties.load(input);

            String URL = properties.getProperty("db.url");
            String Username = properties.getProperty("db.username");
            String Password = properties.getProperty("db.password");

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL,Username,Password);
            System.out.println("connected");
            PreparedStatement pstm = connection.prepareStatement("SHOW TABLES");
            ResultSet resultSet = pstm.executeQuery();
            if (!resultSet.next()) {
                String sql = "\n" +
                        "CREATE TABLE `user` (\n" +
                        "  `id` int(10) AUTO_INCREMENT NOT NULL  ,\n" +
                        "  `firstname` varchar(15) DEFAULT NULL,\n" +
                        "  `lastname` varchar(20) DEFAULT NULL,\n" +
                        "  `phone` varchar(20) DEFAULT NULL,\n" +
                        "  `email` varchar(50) DEFAULT NULL,\n" +
                        "  `password` varchar(100) DEFAULT NULL,\n" +
                        "  `role` enum('USER','ADMIN','EMPLOYEE') DEFAULT 'USER',\n" +
                        "  `verification_code` varchar(100) DEFAULT NULL,\n" +

                        "  PRIMARY KEY (`id`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n" +
                        "\n"


                        ;
                pstm = connection.prepareStatement(sql);
                pstm.execute();
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static  DBConnection getInstance(){
        if(dbConnection == null){ dbConnection = new DBConnection();}
        return dbConnection ;
    }

    public Connection getConnection(){
        return connection ;
    }

}
