package sample.pidevjava.interfaces;

import java.util.ArrayList;

public interface IHoraire <T> {
    void add (T t);
    void update(T t);
    ArrayList<T> getByID();
}
