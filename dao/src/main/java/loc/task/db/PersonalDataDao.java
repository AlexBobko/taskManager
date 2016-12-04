package loc.task.db;

import loc.task.entity.PersonalData;
import lombok.extern.log4j.Log4j;
@Log4j
public class PersonalDataDao extends BaseDao<PersonalData> {

    private static PersonalDataDao personalDataDao = null;

    private PersonalDataDao() {
        log.info("SINGLE TONE: create new PersonalDataDao()");
    }

    private static synchronized PersonalDataDao getInstance() {
        if (personalDataDao == null) {
            personalDataDao = new PersonalDataDao();
        }
        return personalDataDao;
    }

    public static PersonalDataDao getPersonalDataDao() {
        if (personalDataDao == null) {
            return getInstance();
        }
        return personalDataDao;
    }
}