package sample.pidevjava.interfaces;

import java.util.ArrayList;

public interface IServices <T>{
    void add ();

    ArrayList<T> getAll();
    void update (T t);

    boolean delete(T t);

}