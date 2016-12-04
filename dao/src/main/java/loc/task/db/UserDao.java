/*
 * Copyright (c) 2012 by VeriFone, Inc.
 * All Rights Reserved.
 *
 * THIS FILE CONTAINS PROPRIETARY AND CONFIDENTIAL INFORMATION
 * AND REMAINS THE UNPUBLISHED PROPERTY OF VERIFONE, INC.
 *
 * Use, disclosure, or reproduction is prohibited
 * without prior written approval from VeriFone, Inc.
 */
package loc.task.db;

import loc.task.db.exceptions.DaoException;
import loc.task.entity.User;
import loc.task.util.HibernateUtil;
import lombok.extern.log4j.Log4j;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

@Log4j
public class UserDao extends BaseDao<User> {

    private static UserDao userDao = null;

    private UserDao() {
        log.info("SINGLE TONE: create new UserDao()");
//        super.getBaseDao();
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



    public void flush(Integer id, String newName) throws DaoException {
        try {
            Session session = HibernateUtil.getHibernateUtil().getSession();
            User p = (User)session.get(User.class, id);
            System.out.println(p);
//            p.setName(newName);
            System.out.println(p);
            session.flush();

            System.out.println(p);
        } catch (HibernateException e) {
            log.error("Error Flush person" + e);
            throw new DaoException(e);
        }

    }
    public User findEntityByLogin(String userLogin){
        Session session = HibernateUtil.getHibernateUtil().getSession();
        String hql = "SELECT U FROM User U WHERE U.login =:login";
        System.out.println(hql);
        System.out.println(session.getStatistics());

        Query query = session.createQuery(hql);

        query.setParameter("login", "l Бонифаций");
//        query.setParameter("login", userLogin);
        return (User)query.uniqueResult();
    }

}
