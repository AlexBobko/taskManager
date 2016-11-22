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
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;


public class UserDao extends BaseDao<User> {

    private static Logger log = Logger.getLogger(UserDao.class);

    public void flush(Integer id, String newName) throws DaoException {
        try {
//            Session session = util.getSession();
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
//        Session session = util.getSession();
        String hql = "SELECT User FROM User U WHERE U.login IN (:login)";
        Query query = session.createQuery(hql);
        query.setParameter("login", userLogin);
        return (User)query.uniqueResult();
    }

}
