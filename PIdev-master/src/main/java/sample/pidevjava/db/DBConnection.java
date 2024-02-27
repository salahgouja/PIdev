package sample.pidevjava.db;


import java.sql.*;

public class DBConnection {
    private final String URL ="jdbc:mysql://127.0.0.1:/pidev";
    private final String Username ="root";
    private final String Password ="";

    private static  DBConnection dbConnection;
    private final Connection connection ;

    public DBConnection(){

        try {
        Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL,Username,Password);
            System.out.println("connected ");
            PreparedStatement pstm = connection.prepareStatement("SHOW TABLES");
            ResultSet resultSet = pstm.executeQuery();
            if (!resultSet.next()) {
                String sql = "\n" +
                        "CREATE TABLE `user` (\n" +
                        "  `id` int(10) NOT NULL,\n" +
                        "  `firstname` varchar(15) DEFAULT NULL,\n" +
                        "  `lastname` varchar(20) DEFAULT NULL,\n" +
                        "  `phone` varchar(20) DEFAULT NULL,\n" +
                        "  `email` varchar(20) DEFAULT NULL,\n" +
                        "  `password` varchar(20) DEFAULT NULL,\n" +
                        "  PRIMARY KEY (`id`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n" +
                        "\n"
                        ;



                try (PreparedStatement userTablePstm = connection.prepareStatement(sql);
                     ) {

                    userTablePstm.execute();

                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);

        }
    }
    public static  DBConnection getInstance(){
        if(dbConnection == null){ dbConnection =new DBConnection();}
        return dbConnection ;
    }

    public Connection getConnection(){
        return connection ;
    }



}
