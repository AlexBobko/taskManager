package main;

import dao.UserMetaDao;
import dao.exc.DaoException;
import entity.UserMeta;
import org.apache.log4j.Logger;
import utils.HibernateUtil;

import java.util.Scanner;

public class MainLoader {
    private static Logger log = Logger.getLogger(MainLoader.class);
    public static HibernateUtil util = null;

    private static UserMetaDao userMetaDao= null;



    public static void main(String[] args) throws Exception {


        util = HibernateUtil.getHibernateUtil();
        System.out.println("Start Menu");



//        menu();
        System.out.print(findPerson(1));
//        findPerson(1);

        UserMeta userMetaNew= new UserMeta (5, "FifeUser", "Кузнецов Виталий Владимирович", "dffff@mail.com", "Конструктор");
//        System.out.print(createUserMeta(userMetaNew));
        userMetaDao.saveOrUpdate(userMetaNew);
        System.out.print(loadUserMeta(5));



    }

    
    
    

    public static UserMeta findPerson(Integer id) {
        System.out.println("Get by Id. Please enter userMeta id:");
        System.out.print("Id - ");

        Scanner scanner = new Scanner(System.in);
        UserMeta userMeta = null;
        try {
            userMeta = getUserMetaDao().get(id);
        } catch (DaoException e) {
            log.error(e, e);
        } catch (NullPointerException e) {
            log.error("Unable find userMeta:", e);
        }
        System.out.print(userMeta);
        return userMeta;
    }



    public static UserMeta findUserMeta() {
        System.out.println("Get by Id. Please enter userMeta id:");
        System.out.print("Id - ");

        Scanner scanner = new Scanner(System.in);
        UserMeta userMeta = null;
        Integer id = scanner.nextInt();
        try {
            userMeta = getUserMetaDao().get(id);
        } catch (DaoException e) {
            log.error(e, e);
        } catch (NullPointerException e) {
            log.error("Unable find userMeta:", e);
        }
        System.out.print(userMeta);
        return userMeta;
    }

    public static UserMeta loadUserMeta(Integer id) {
        System.out.println("Loag by Id. Please enter userMeta id:");
        System.out.print("Id - ");

        Scanner scanner = new Scanner(System.in);
        UserMeta userMeta = null;
//        Integer id = scanner.nextInt();

        try {
            userMeta = getUserMetaDao().get(id);
        } catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NullPointerException e) {
            log.error("Unable find userMeta:", e);
        }
        System.out.print(userMeta);
        return userMeta;
    }

    public static void flushSession() {
        System.out.println("Please enter userMeta id:");
        System.out.print("Id - ");
        Scanner scanner = new Scanner(System.in);
        UserMeta userMeta = null;
        Integer id = scanner.nextInt();
        System.out.println("Please enter new Name:");
        System.out.print("New Name - ");
        scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        try {
            getUserMetaDao().flush(id, name);
        } catch (DaoException e) {
            log.error("Unable run flush example");
        }
    }

    public static UserMetaDao getUserMetaDao() {
        if (userMetaDao == null) {
            userMetaDao = new UserMetaDao();
        }

        return userMetaDao;
    }

//    public static UserMeta createUserMeta(UserMeta userMeta) {
//        System.out.println("Please enter UserMeta description:");
//        System.out.print("Name - ");
//
//        if(userMeta == null) {userMeta = new UserMeta();}
//        Scanner scanner = new Scanner(System.in);
//        String parameter = scanner.nextLine();
//        userMeta.setName(parameter);
//        System.out.print("Surname - ");
//        parameter = scanner.nextLine();
//        userMeta.setSurname(parameter);
//        System.out.print("Age - ");
//        userMeta.setAge(scanner.nextInt());
////        userMeta.setUserMetaAddress(new Address());
////        userMeta.getUserMetaAddress().setCity("Minsk");
////        userMeta.getUserMetaAddress().setStreet("Gaya");
//        return userMeta;
//    }
    
    
    
}
