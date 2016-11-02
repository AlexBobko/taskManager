package dao;


import dao.exc.DaoException;

import java.io.Serializable;

public interface DAO<T> {
    void saveOrUpdate(T t) throws DaoException;

    T get(Serializable id) throws DaoException;

    T load(Serializable id) throws DaoException;

    void delete(T t) throws DaoException;
}




