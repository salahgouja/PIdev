package sample.pidevjava.db;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class DBConnection {

    private static DBConnection dbConnection;
    private Connection connection;

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
                        "CREATE TABLE user (\n" +
                        "  id int(10) AUTO_INCREMENT NOT NULL  ,\n" +
                        "  firstname varchar(15) DEFAULT NULL,\n" +
                        "  lastname varchar(20) DEFAULT NULL,\n" +
                        "  phone varchar(20) DEFAULT NULL,\n" +
                        "  email varchar(50) DEFAULT NULL,\n" +
                        "  password varchar(100) DEFAULT NULL,\n" +
                        "  role enum('USER','ADMIN','EMPLOYEE') DEFAULT 'USER',\n" +
                        "  verification_code varchar(100) DEFAULT NULL,\n" +
                        "  image varchar(10000000) DEFAULT NULL,\n" +
                        "  qrcode varchar(10000000) DEFAULT NULL,\n" +


                        "  PRIMARY KEY (id)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n" +
                        "\n" ;


                String articleTable = "CREATE TABLE article (\n" +
                        "  id INT NOT NULL AUTO_INCREMENT,\n" +
                        "  title VARCHAR(255) NOT NULL,\n" +
                        "  description TEXT,\n" +
                        "  form_link VARCHAR(255),\n" +
                        "  PRIMARY KEY (id)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;\n";
                String terrainTable = "CREATE TABLE terrain (\n" +
                        "  id int(10) NOT NULL AUTO_INCREMENT,\n" +
                        "  nom varchar(50) DEFAULT NULL,\n" +
                        "  active boolean NOT NULL,\n" +
                        "  capaciteTerrain int(10) DEFAULT NULL,\n" +
                        "  type ENUM('FOOTBALL', 'BASKETBALL', 'TENNIS', 'HANDBALL') NOT NULL,\n" +
                        "  PRIMARY KEY (id)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n";

                String appelTable = "CREATE TABLE appeloffre (\n" +
                        "  id int(10) NOT NULL AUTO_INCREMENT,\n" +
                        "  nom varchar(50) DEFAULT NULL,\n" +
                        "  prenom varchar(50) DEFAULT NULL,\n" +
                        "  prix float NOT NULL,\n" +
                        "  numero varchar(20) DEFAULT NULL,\n" +
                        "  cv varchar(255) DEFAULT NULL,\n" +
                        "  article_id int(10),\n" +
                        "  PRIMARY KEY (id),\n" +
                        "  FOREIGN KEY (article_id) REFERENCES article`(id`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n";

                String alterTableQuery = "ALTER TABLE appeloffre " +
                        "ADD COLUMN article_id INT, " +
                        "ADD CONSTRAINT fk_article_id FOREIGN KEY (article_id) REFERENCES article(id);";

                try (PreparedStatement userTablePstm = connection.prepareStatement(sql);
                     PreparedStatement terrainTablePstm = connection.prepareStatement(terrainTable);
                     PreparedStatement articleTablePstm = connection.prepareStatement(articleTable);
                     PreparedStatement appelTablePstm = connection.prepareStatement(appelTable);
                     PreparedStatement alterTablePstm = connection.prepareStatement(alterTableQuery)) {

                    userTablePstm.execute();
                    terrainTablePstm.execute();
                    articleTablePstm.execute(); // Ajout de la table article
                    appelTablePstm.execute();
                    alterTablePstm.execute();

                }
            }

        } catch (ClassNotFoundException | SQLException e) {

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
