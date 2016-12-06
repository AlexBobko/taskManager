package loc.task.dao;

import loc.task.entity.PersonalData;
import lombok.extern.log4j.Log4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Log4j
@Repository()
public class PersonalDataDao extends BaseDao<PersonalData> implements IPersonalDataDao{
    @Autowired
    public PersonalDataDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

}