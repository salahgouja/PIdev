package sample.pidevjava.controller;

import sample.pidevjava.db.DBConnection;
import sample.pidevjava.interfaces.IController;
import sample.pidevjava.model.User;


import java.sql.*;
import java.util.ArrayList;


public class UserController implements IController<User> {

    Connection connection = DBConnection.getInstance().getConnection();

    @Override
    public void add(User user) {

        try {

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO USER (firstname, lastname, phone, email, password, role) VALUES (?, ?, ?, ?, ?, ?)");

            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getPhone());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setString(6, user.getRole().toString());

            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }


    }




    @Override
    public void update(User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user SET firstname = ?, lastname = ?, phone = ?, email = ?, password = ?, role = ? WHERE id = ?");

            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getPhone());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setString(6, user.getRole().toString());
            preparedStatement.setInt(7, user.getId());

            preparedStatement.executeUpdate();
          //  preparedStatement.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }


    @Override
    public boolean delete(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM USER WHERE id = ?");
            statement.setInt(1, user.getId()); // Set the id parameter for the prepared statement
            statement.executeUpdate();
            statement.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return false;
    }
    @Override
    public ArrayList<User> getAll() {
        ArrayList<User> userList = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USER");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setFirstname(resultSet.getString("firstname"));
                user.setLastname(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(resultSet.getString("role"));// badlalt edhi khatar maandich lfichier enum
                userList.add(user);
            }
           // statement.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return userList;
    }



    public User getUserById(int id) {
        User user = null;
        try {
            System.out.println("fff");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USER WHERE id = ?");
            System.out.println("gfggfg");
            statement.setInt(1, id); // Set the id parameter for the prepared statement
            System.out.println("fff");
            ResultSet resultSet = statement.executeQuery();
            System.out.println("ggg");
            System.out.println(resultSet);

            if (resultSet.next()) { // Move the cursor to the first row
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setFirstname(resultSet.getString("firstname"));
                user.setLastname(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(resultSet.getString("role"));// badlalt edhi khatar maandich lfichier enum
            }

           // statement.close();
        } catch (SQLException exception) {
            System.out.println("HERE");
            System.out.println(exception.getMessage());
        }
        return user;
    }



    public User getUserByEmail(String email) {
        User user = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USER WHERE email = ?");
            statement.setString(1,email); // Set the id parameter for the prepared statement
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // Move the cursor to the first row
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setFirstname(resultSet.getString("firstname"));
                user.setLastname(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(resultSet.getString("role"));// badlalt edhi khatar maandich lfichier enum
                System.out.println("Can get user : "+ user);
            } else{
                System.out.println("Cannot get user (getUserByEmail)");
            }

           // statement.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return user;
    }

}