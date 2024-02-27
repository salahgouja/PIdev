package sample.pidevjava.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.interfaces.IService;
import sample.pidevjava.model.User;
import sample.pidevjava.model.UserRole;


import java.sql.*;
import java.util.ArrayList;


public class UserController implements IService<User> {

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
            preparedStatement.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }


    @Override
    public void delete(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM USER WHERE id = ?");
            statement.setInt(1, user.getId()); // Set the id parameter for the prepared statement
            statement.executeUpdate();
            statement.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }
    @Override
    public ArrayList<User> getAllUsers() {
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
                user.setRole(String.valueOf(UserRole.valueOf(resultSet.getString("role")))); //  role is a string
                userList.add(user);
            }
            statement.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return userList;
    }


    @Override
    public User getUserById(int id) {
        User user = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USER WHERE id = ?");
            statement.setInt(1, id); // Set the id parameter for the prepared statement
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // Move the cursor to the first row
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setFirstname(resultSet.getString("firstname"));
                user.setLastname(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(String.valueOf(UserRole.valueOf(resultSet.getString("role")))); //  role is a string
            }

            statement.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return user;
    }


    @Override
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
                user.setRole(String.valueOf(UserRole.valueOf(resultSet.getString("role"))));
            }

            statement.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return user;
    }
    public ObservableList<User> searchUsers(String query) {
        ObservableList<User> searchResults = FXCollections.observableArrayList();
        for (User user : getAllUsers()) {
            if (user.getFirstname().toLowerCase().contains(query.toLowerCase()) ||
                    user.getLastname().toLowerCase().contains(query.toLowerCase()) ||
                    user.getEmail().toLowerCase().contains(query.toLowerCase()) ||
                    user.getPhone().toLowerCase().contains(query.toLowerCase()) ||
                    user.getRole().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(user);
            }
        }
        return searchResults;
    }

}
