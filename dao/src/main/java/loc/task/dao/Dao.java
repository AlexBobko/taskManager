package loc.task.dao;

import org.hibernate.Session;

import java.io.Serializable;

public interface Dao<T> {
    void saveOrUpdate(T t);

    T get(Serializable id);

    T get(T t, Serializable id);

    T load(Serializable id);

    void delete(T t);

    //TODO перенести в таски
//    void replicate(T t);

    void refresh(T t);
    Session getSession();
}




