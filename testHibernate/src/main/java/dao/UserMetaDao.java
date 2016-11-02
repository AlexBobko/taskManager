package dao;

import dao.exc.DaoException;
import entity.UserMeta;
import main.MainLoader;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class UserMetaDao extends BaseDao<UserMeta> {
    private static Logger log = Logger.getLogger(UserMetaDao.class);

    public void flush(Integer id, String newName) throws DaoException {
        try {
            //TODO
            Session session = MainLoader.util.getSession();
            UserMeta userMeta = (UserMeta)session.get(UserMeta.class, id);
            System.out.println(userMeta);
            userMeta.setName(newName);
            System.out.println(userMeta);
            session.flush();
            System.out.println(userMeta);
        } catch (HibernateException e) {
            log.error("Error Flush person" + e);
            throw new DaoException(e);
        }

    }
}
