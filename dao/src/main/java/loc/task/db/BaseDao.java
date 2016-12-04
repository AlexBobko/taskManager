package loc.task.db;

import loc.task.db.exceptions.DaoException;
import loc.task.util.HibernateUtil;
import lombok.extern.log4j.Log4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

//TODO ?? типитизация через дженерики + синглтон? возможные проблемы, например при получении имени класса
@Log4j
public class BaseDao<T> implements Dao<T> {
    private static BaseDao baseDao = null;

    protected BaseDao() {
        log.info("SINGLE TONE: create new BaseDao()");
    }

    private static synchronized BaseDao getInstance() {
        if (baseDao == null) {
            baseDao = new BaseDao();
        }
        return baseDao;
    }

    public static BaseDao getBaseDao() {
        if (baseDao == null) {
            return getInstance();
        }
        return baseDao;
    }

    public void saveOrUpdate(T t) throws DaoException {
        try {
            Session session = HibernateUtil.getHibernateUtil().getSession();
            session.saveOrUpdate(t);
            log.info("saveOrUpdate(t):" + t);
            System.out.println("TASK UPDATE 4 " + session.getStatistics() + ": ");

        } catch (HibernateException e) { //TODO ?? isDirty + нужна ли обработка Ех (ДАО)
            log.error("Error save or update " + getPersistentClass() + " in Dao" + e);
            throw new DaoException(e);
        }

    }

    public T get(Serializable id) throws DaoException {
        Session session = HibernateUtil.getHibernateUtil().getSession();
        log.info("Get class by id:" + id);
        T t = null;
        try {
            t = (T) session.get(getPersistentClass(), id);
//            log.info("get clazz:" + t);
        } catch (HibernateException e) {
            log.error("Error get " + getPersistentClass() + " in Dao" + e);
            throw new DaoException(e);
        }
        return t;
    }

    //TODO ?? юзабельность такого метода (или проще создать профильный ДАО) и сделать поиск по ИД
    public T get(T t, Serializable id) throws DaoException {
        Session session = HibernateUtil.getHibernateUtil().getSession();
        log.info("Get class by id:" + id);
//        T t = null;
        try {
            t = (T) session.get(t.getClass(), id);
        } catch (HibernateException e) {
            log.error("Error get " + getPersistentClass() + " in Dao" + e);
            throw new DaoException(e);
        }
        return t;
    }

    public T load(Serializable id) throws DaoException {
        Session session = HibernateUtil.getHibernateUtil().getSession();
        log.info("Load class by id:" + id);
        T t = null;
        try {
            t = (T) session.load(getPersistentClass(), id);
//            log.info("load() clazz:" + t);
            session.isDirty();
        } catch (HibernateException e) {
            log.error("Error load() " + getPersistentClass() + " in Dao" + e);
            throw new DaoException(e);
        }
        return t;
    }

    public void refresh(T t) throws DaoException {
        try {
            Session session = HibernateUtil.getHibernateUtil().getSession();
            session.refresh(t);
        } catch (HibernateException e) {
            log.error("Error refresh() " + getPersistentClass() + " in Dao" + e);
            throw new DaoException(e);
        }
    }

    public void delete(T t) throws DaoException {
        try {
            Session session = HibernateUtil.getHibernateUtil().getSession();
            session.delete(t);
        } catch (HibernateException e) {
            log.error("Error save or update PERSON in Dao" + e);
            throw new DaoException(e);
        }
    }

    private Class getPersistentClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }


}
