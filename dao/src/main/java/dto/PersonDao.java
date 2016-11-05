package dto;

import dao.BaseDao;
import dao.exceptions.DaoException;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class PersonDao extends BaseDao<Person> {

    private static Logger log = Logger.getLogger(PersonDao.class);

    public void flush(Integer id, String newName) throws DaoException {
        try {
            Session session = util.getSession();
            Person p = (Person)session.get(Person.class, id);
            System.out.println(p);
            p.setName(newName);
            System.out.println(p);
            session.flush();
            System.out.println(p);
        } catch (HibernateException e) {
            log.error("Error Flush person" + e);
            throw new DaoException(e);
        }

    }

}