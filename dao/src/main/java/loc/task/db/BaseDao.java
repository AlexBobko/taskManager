package loc.task.db;


import loc.task.db.exceptions.DaoException;
import loc.task.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;


public class BaseDao<T> implements Dao<T> {
    private static Logger log = Logger.getLogger(BaseDao.class);


    Session session = HibernateUtil.getHibernateUtil().getSession();

//    Session session = PersonLoader.util.getSession();
//    transaction = session.beginTransaction();
//    transaction.commit();
//    transaction.rollback();
//private Transaction transaction = null;
//public Transaction getTransaction() {
//    return transaction;
//}
//public void setTransaction(Transaction transaction) {
//        this.transaction = transaction;
//    }


    public BaseDao() {

    }

    public void saveOrUpdate(T t) throws DaoException {
        try {
            session.saveOrUpdate(t);
            log.info("saveOrUpdate(t):" + t);
            System.out.println("saveOrUpdate(t):" + t);

        } catch (HibernateException e) {
            log.error("Error save or update " + getPersistentClass() + " in Dao" + e);
            throw new DaoException(e);
        }

    }

    public T get(Serializable id) throws DaoException {
        log.info("Get class by id:" + id);
        T t = null;
        try {
            t = (T) session.get(getPersistentClass(), id);
            log.info("get clazz:" + t);
        } catch (HibernateException e) {
            log.error("Error get " + getPersistentClass() + " in Dao" + e);
            throw new DaoException(e);
        }
        return t;
    }

    public T get(T t, Serializable id) throws DaoException {
        log.info("Get class by id:" + id);
//        T t = null;
        try {
            t = (T) session.get(t.getClass(), id);
            log.info("get clazz:" + t);
        } catch (HibernateException e) {
            log.error("Error get " + getPersistentClass() + " in Dao" + e);
            throw new DaoException(e);
        }
        return t;
    }

    public T load(Serializable id) throws DaoException {
        log.info("Load class by id:" + id);
        T t = null;
        try {
            t = (T) session.load(getPersistentClass(), id);
            log.info("load() clazz:" + t);
            session.isDirty();
        } catch (HibernateException e) {
            log.error("Error load() " + getPersistentClass() + " in Dao" + e);
            throw new DaoException(e);
        }
        return t;
    }

    public T load(T t, Serializable id) throws DaoException {
        log.info("Load class by id:" + id);
//        t = null;
        try {
            t = (T) session.load(t.getClass(), id);
            log.info("load() clazz:" + t);
            session.isDirty();
        } catch (HibernateException e) {
            log.error("Error load() " + t.getClass() + " in Dao" + e);
            throw new DaoException(e);
        }
        return t;
    }

    public void refresh(T t) {
    session.refresh(t);
    }

    public void delete(T t) throws DaoException {
        try {
            session.delete(t);
            log.info("Delete:" + t);
        } catch (HibernateException e) {
            log.error("Error save or update PERSON in Dao" + e);
            throw new DaoException(e);
        }
    }

    private Class getPersistentClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }


}
