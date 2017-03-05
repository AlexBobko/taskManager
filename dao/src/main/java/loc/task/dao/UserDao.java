package loc.task.dao;

import loc.task.entity.User;
import lombok.extern.log4j.Log4j;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Log4j
@Repository()
public class UserDao extends BaseDao<loc.task.entity.User> implements IUserDao{
    @Autowired
    public UserDao(SessionFactory sessionFactory){
        super(sessionFactory);
    }

    @Override
    public User findUserByLogin(String userLogin){
            String hql = "SELECT U FROM loc.task.entity.User U WHERE U.login =:login";
            Query query = getSession().createQuery(hql);
            query.setParameter("login", userLogin);
            return (User) query.uniqueResult();
    }
    @Override
    public List<User> getAllEmployee(){
        String hql = "FROM loc.task.entity.User U WHERE U.role =1 AND U.accountStatus = 1";
        Query query = getSession().createQuery(hql);
        return query.list();
    }
}
