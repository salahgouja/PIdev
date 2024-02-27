package sample.pidevjava.interfaces;

import sample.pidevjava.model.User;

import java.util.ArrayList;

public interface IService<T> {
    void add (T t);
    ArrayList<T> getAllUsers();
    User getUserById(int id);
    void update(T t);
    void delete (T t);

    User getUserByEmail(String email);
}
