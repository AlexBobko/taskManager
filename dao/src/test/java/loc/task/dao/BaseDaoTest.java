package loc.task.dao;

import loc.task.entity.PersonalData;
import loc.task.entity.User;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

@Ignore
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/beans-dao.xml")
@Transactional(transactionManager = "txManager", propagation = Propagation.SUPPORTS)
public class BaseDaoTest {
    User user = new User();
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private UserDao userDao;
    @Before
    public void setUp() throws Exception {
//        userDao.getSession().beginTransaction();
//        String names = "Большой Ух~Бонифаций~Варежка~Вифсла~Волк~Домовёнок Кузя~Дюдюка Барбидокская~Ёжик в тумане~Кот Леопольд~Котенок по имени Гав~Котенок с улицы Лизюкова~Крошка Енот~Лисенок~Львенок~Мамонтенок~Маугли~Паровозик из Ромашкова~Попугай Кеша~Поросенок Фунтик~Пятачок~Тофсла~Умка~Чебурашка~Чертёнок № 13~Чучело-Мяучело~Шопокляк~";
        String name = "Бонифаций";
        user = new User();
        user.setLogin("log ".concat(name));
        user.setLogin("log ".concat(name));
        user.setAccountStatus(1);
        user.setPasswordHash("hashHardCode");
        PersonalData personalData = new PersonalData();
        personalData.setUserId(user.getUserId());
        personalData.setName("n ".concat(name));
        personalData.setPosition("Дожность HardCode");
        personalData.setSurname("SNameHardCode");
        user.setPersonalData(personalData);
        personalData.setUser(user);
        userDao.saveOrUpdate(user);
        sessionFactory.getCurrentSession().flush();
//        userDao.getSession().getTransaction().commit();
    }
    @After
    public void delete() throws Exception {
//        userDao.getSession().beginTransaction();
        userDao.delete(user);
//            userDao.delete(users.remove());
        sessionFactory.getCurrentSession().flush();
//        userDao.getSession().getTransaction().commit();
    }

    @Test
    public void get() throws Exception {
//        userDao.getSession().beginTransaction();
        User userTest = (User) userDao.get(user.getUserId());
        assertNotNull(userTest);
        assertEquals(userTest.getClass(), user.getClass());
        assertEquals(userTest.getUserId(), user.getUserId());
        assertEquals(userTest.getLogin(), user.getLogin());
        assertEquals(userTest.getRole(), user.getRole());
//        userDao.getSession().getTransaction().commit();
    }

//    @Test
//    public void load() throws Exception {
//        User userTest = (User) userDao.load(user.getUserId());
//        assertNotNull(userTest);
//        assertEquals(userTest.getClass(), user.getClass());
//        assertEquals(userTest.getUserId(), user.getUserId());
//        assertEquals(userTest.getLogin(), user.getLogin());
//        assertEquals(userTest.getRole(), user.getRole());
//    }
//
//    @Test
//    public void getAllEmployee() throws Exception {
//        userDao.saveOrUpdate(user);
//        sessionFactory.getCurrentSession().flush();
//        List<User> results = userDao.getAllEmployee();
//        assertNotNull(results);
//        assertEquals(results.get(0).getUserId(), user.getUserId());
//    }
}