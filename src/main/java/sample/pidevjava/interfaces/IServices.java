package sample.pidevjava.interfaces;

import java.io.IOException;
import java.util.ArrayList;

public interface IServices <T>{
    void add () throws IOException;

    ArrayList<T> getAll();
    void update (T t);

    boolean delete(T t);

}