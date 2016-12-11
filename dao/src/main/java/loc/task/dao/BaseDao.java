package loc.task.dao;

import lombok.extern.log4j.Log4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

@Log4j
@Repository()
public class BaseDao<T> implements Dao<T> {

    private SessionFactory sessionFactory;

    @Autowired
    public BaseDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void saveOrUpdate(T t) {
        getSession().saveOrUpdate(t);
        log.info("saveOrUpdate(t):" + t.getClass());
//            System.out.println("TASK UPDATE 4 " + getSession().getStatistics() + ": ");
    }

    @Override
    public T get(Serializable id) {
        log.info("Get class " + getPersistentClass() + " by id:" + id);
        return (T) getSession().get(getPersistentClass(), id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public T get(T t, Serializable id) {
        log.info("Get class "+ getPersistentClass() + " by id:" + id);
        return (T) getSession().get(t.getClass(), id);
    }

    @Override
    public T load(Serializable id) {
        log.info("Load class by id:" + id);
        return (T) getSession().load(getPersistentClass(), id);
    }

    @Override
    public void refresh(T t) {
        getSession().refresh(t);
    }

    public void delete(T t) {
        getSession().delete(t);
    }

//    @Override
//    public void replicate(T t) {
//        getSession().replicate(t, ReplicationMode.LATEST_VERSION);
//    }

    private Class getPersistentClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }


}
