package loc.task.db;

import loc.task.db.exceptions.DaoException;
import loc.task.entity.User;
import loc.task.util.HibernateUtil;
import lombok.extern.log4j.Log4j;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;

@Log4j
public class UserDao extends BaseDao<User> {

    private static UserDao userDao = null;

    private UserDao() {
        log.info("SINGLE TONE: create new UserDao()");
    }

    private static synchronized UserDao getInstance() {
        if (userDao == null) {
            userDao = new UserDao();
        }
        return userDao;
    }

    public static UserDao getUserDao() {
        if (userDao == null) {
            return getInstance();
        }
        return userDao;
    }

    public User findEntityByLogin(String userLogin) throws DaoException {

        try {
            Session session = HibernateUtil.getHibernateUtil().getSession();
            String hql = "SELECT U FROM User U WHERE U.login =:login";
//        System.out.println(hql);
//        System.out.println(session.getStatistics());
            Query query = session.createQuery(hql);

            query.setParameter("login", "l Бонифаций");
            //        query.setParameter("login", userLogin); //TODO ХАРДКОР ЛОГИНА (лень)
            return (User) query.uniqueResult();
        } catch (NonUniqueResultException e) {
            log.error("Error findEntityByLogin" + " in Dao" + e);
            throw new DaoException(e);
        }
    }
}
