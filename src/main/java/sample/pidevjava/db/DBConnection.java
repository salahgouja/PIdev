package sample.pidevjava.db;


import java.sql.*;

public class DBConnection {

    private static  DBConnection dbConnection;
    private final Connection connection ;

    private   DBConnection(){

        try {
        Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidev","root","");
            System.out.println("connected ");
            PreparedStatement pstm = connection.prepareStatement("SHOW TABLES LIKE 'reservation'");
            ResultSet resultSet = pstm.executeQuery();
            if (!resultSet.next()) {
                String sql = "CREATE TABLE `user` (\n" +
                        "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                        "  `firstname` VARCHAR(15) DEFAULT NULL,\n" +
                        "  `lastname` VARCHAR(20) DEFAULT NULL,\n" +
                        "  `phone` VARCHAR(20) DEFAULT NULL,\n" +
                        "  `email` VARCHAR(20) DEFAULT NULL,\n" +
                        "  `password` VARCHAR(20) DEFAULT NULL,\n" +
                        "  PRIMARY KEY (`id`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n" +
                        "\n";
                      /*  "CREATE TABLE `reservation` (\n" +
                        "  `id_reservation` INT NOT NULL AUTO_INCREMENT,\n" +
                        "  `date_reserve` VARCHAR(255) DEFAULT NULL,\n" +
                        "  `temps_reservation` VARCHAR(20) DEFAULT NULL,\n" +
                        "  `image` VARCHAR(255) DEFAULT NULL,\n" +
                        "  `prix_reservation` FLOAT DEFAULT NULL,\n" +
                        "  `id_user` INT NOT NULL,\n" + // Added id_user column definition
                        "  PRIMARY KEY (`id_reservation`),\n" +
                        "  FOREIGN KEY (`id_user`) REFERENCES `user`(`id`)\n" + // Corrected FOREIGN KEY syntax
                        ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n" */




                pstm = connection.prepareStatement(sql);
                pstm.execute();
            }



        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);

        }
    }
    public static  DBConnection getInstance(){

        if(dbConnection == null){

            dbConnection =new DBConnection();
        }
        return dbConnection ;
    }

    public Connection getConnection(){
        return connection ;
    }



}
