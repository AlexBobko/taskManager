package loc.task.db;

import loc.task.db.exceptions.DaoException;
import loc.task.entity.PersonalData;
import loc.task.entity.User;
import loc.task.util.HibernateUtil;
import lombok.extern.log4j.Log4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
@Log4j
public class PersonalDataDao extends BaseDao<PersonalData> {

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

}