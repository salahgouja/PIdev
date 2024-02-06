package sample.pidevjava.controller;

import sample.pidevjava.interfaces.IController;
import sample.pidevjava.model.User;

import java.util.ArrayList;

public class UserController implements IController<User> {

    @Override
    public void add(User user) {

    }

    @Override
    public ArrayList<User> getAll() {
        return null;
    }

    @Override
    public void update(User user) {

    }

    @Override
    public boolean delete(User user) {
        return false;
    }
}
