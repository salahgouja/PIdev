package sample.pidevjava.interfaces;

import sample.pidevjava.model.User;

import java.util.ArrayList;

public interface IController  <T> {
    void add (T t);
    ArrayList<T> getAll();
    void update(T t);
    boolean delete (T t);


}
