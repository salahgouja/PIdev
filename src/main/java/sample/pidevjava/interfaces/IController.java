package sample.pidevjava.interfaces;

import sample.pidevjava.model.Terrain;

import java.util.ArrayList;

public interface IController  <T> {
    void add (T t);


    ArrayList<T> getAll();
    void update(T t);
    boolean delete (T t);



}
