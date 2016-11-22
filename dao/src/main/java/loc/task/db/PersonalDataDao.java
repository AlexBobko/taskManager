package loc.task.db;

import loc.task.db.exceptions.DaoException;
import loc.task.entity.PersonalData;
import loc.task.entity.User;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import static loc.task.loader.PersonLoader.util;

public class PersonalDataDao extends BaseDao<PersonalData> {

    private static Logger log = Logger.getLogger(PersonalDataDao.class);

    public void flush(Integer id, String newName) throws DaoException {
        try {
            Session session = util.getSession();
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

}