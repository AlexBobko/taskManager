package loc.task.db;

import loc.task.db.exceptions.DaoException;

import java.io.Serializable;

public interface Dao<T> {
    void saveOrUpdate(T t) throws DaoException;

    T get(Serializable id) throws DaoException;

    T load(Serializable id) throws DaoException;

    void delete(T t) throws DaoException;
}




