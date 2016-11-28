package loc.task.loader;

import loc.task.db.BaseDao;
import loc.task.db.PersonalDataDao;
import loc.task.db.TaskDao;
import loc.task.db.UserDao;
import loc.task.db.exceptions.DaoException;
import loc.task.entity.PersonalData;
import loc.task.entity.Task;
import loc.task.entity.TaskContent;
import loc.task.entity.User;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;

import java.util.*;

public class MenuLoader {
    private static Logger log = Logger.getLogger(MenuLoader.class);
    public static Boolean needMenu = true;
    private static UserDao personDao = null;
    private static TaskDao taskDao = null;
    private static PersonalDataDao personalDataDao = null;
    //    private static TaskMetaDao taskMetaDao= null;
    public static User currentUser;

    private static Random random = new Random();
    private static BaseDao baseDao = null;


    public static void menu() throws DaoException {
        User user = null;
        HashSet<Integer> includeStatus = null;
        while (needMenu) {
            printMenu();
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    System.exit(0);
                    break;
                case 1:
                    currentUser = createUser();
                    PersonalData personalData = createPersonalData(currentUser);
                    personalData.setUser(currentUser);
                    currentUser.setPersonalData(personalData);
//                    getPersonalDataDao().saveOrUpdate(personalData);
                    getUserDao().saveOrUpdate(currentUser);
                    break;
                case 2:
                    user = findUser();
                    MenuLoader.currentUser = user;
                    break;
                case 3:
                    user = loadPerson();
                    System.out.println(user);
//                    MenuLoader.currentUser = user;
                    break;
                case 4:
                    delUser();
                    break;
                case 5:
                    user = findUser();
                    System.out.println(user);
                    Task task = createTask(user);
//                    getBaseDao().saveOrUpdate(task);
                    getTaskDao().saveOrUpdate(task);
                    break;
                case 6:
                    delTask();
                    break;
                case 7:
                    testCashSecondLevel();
                    break;
                case 8:
                    includeStatus = new HashSet<Integer>();
                    includeStatus.add(2);
                    includeStatus.add(4);
                    includeStatus.add(5);
                    getTaskDao().getCountTask(includeStatus);

                    break;
                case 9:
                    userGenerator();
                    break;
                case 10:
                    taskGenerator();
                    break;
                case 11:
                    getTasks();
                    break;
                case 12:
                    getTaskByFilter();
                    break;
                case 13:
                    long taskId = 34L;
                    int newStatus= 3;
                    int result =getTaskDao().setStatus(taskId,newStatus);
                    System.out.println("Update Task "+ taskId + " to status:" + newStatus + "res:" + result);
                    break;
                default:
                    needMenu = true;
                    break;
            }
            needMenu = true;
        }

    }

    private static void printMenu() {
        Date time = new Date();
        time.getTime();
        System.out.println(" Options:");
        System.out.println("        0. Exit");
        System.out.println("        1. Save User & Personal Data (one to one)");
        System.out.println("        2. Get User by Id");
        System.out.println("        3. Load User by Id");
        System.out.println("        4. Delete User");
        System.out.println("        5. Save New Task (many to many)");
        System.out.println("        6. Delete Task (many to many)");
        System.out.println("        7. Test 2lvl cash");
        System.out.println("        8. Get count Task");
        System.out.println("        9. Add Users From String");
        System.out.println("       10. TaskGenerator");
        System.out.println("       11. Get Tasks");
        System.out.println("       12. Get Task by Filter");
        System.out.println("       13. Set Task status");


//        System.out.println("        5. Load Person");
//        System.out.println("        6. Save or Update TaskDTO");

    }

    public static void testCashSecondLevel() {
        System.out.println("Please enter users Id:");
        System.out.print("first id  - ");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();
        System.out.print("Second id  - ");
        int secondId = scanner.nextInt();
        testCashTwoUser(id, secondId);

//        testCashTwoUser
    }
//    getTaskByFilter
    private static void getTaskByFilter(){
        System.out.println("Enter Task status Id (1-7 ,delimiter ','):");
        Scanner scanner = new Scanner(System.in);
        String inTasksId = scanner.nextLine();
        HashSet<Integer> includeStatus= new HashSet<Integer>();
        for (String str: inTasksId.split(",")){
            Integer id = Integer.parseInt(str);
            includeStatus.add(id);
        }
        long totalCount = getTaskDao().getCountTask(includeStatus);
        System.out.println("totalCount:" + totalCount);
        System.out.println("Enter count result per Page:");
        int tasksPerPage = scanner.nextInt();
        System.out.println("Enter number Page:");
        int page = scanner.nextInt();
        System.out.println("Enter column Sorting (1 - date , 2 - id , 3 - statusId, 4- user):");
        int sort = scanner.nextInt();
        System.out.println("Type sorting (1 - ASC , 2 - DESK):");
        int sortType = scanner.nextInt();
        boolean ask;
        if (sortType==1) {ask = true;}
        else ask = false;
        List<Object[]> result = getTaskDao().getCurrentTask(page,tasksPerPage,totalCount,includeStatus,sort,ask);
        int i=0;
        for (Object[] t : result) {
            System.out.print(++i + ":");
            System.out.print(" date " + t[0]);
            System.out.print(" taskID " + t[1]);
            System.out.print(" statusId " + t[2]);
            System.out.print(" user " + t[3]);
            System.out.println();
        }
    }



    private static void getTasks() {
        HashSet<Integer> includeStatus= new HashSet<Integer>();
//        includeStatus.add(2);
//        includeStatus.add(4);
//        includeStatus.add(5);
        List<Object[]> result = getTaskDao().getCurrentTask(includeStatus);
        for (Object[] t : result) {
            System.out.print(" date " + t[0]);
            System.out.print(" taskID " + t[1]);
            System.out.print(" statusId " + t[2]);
            System.out.print(" user " + t[3]);
//            T.taskId,T.statusId,U.userId
            System.out.println();
        }
    }

    public static User createUser() {
        System.out.println("Please enter user description:");
        System.out.print("login - ");
        User user = new User();
        Scanner scanner = new Scanner(System.in);
        String parameter = scanner.nextLine();
        user.setLogin(parameter);
        System.out.print("Personnel Number - ");
        user.setPersonnelNumber(scanner.nextInt());
        user.setAccountStatus(1);
        user.setPasswordHash("hashHardCode");
        return user;

//        ALTER TABLE <имя таблицы> AUTO_INCREMENT=0 #обнуление
    }

    public static void userGenerator() throws DaoException {
        //            26 шт
        String names = "Большой Ух~Бонифаций~Варежка~Вифсла~Волк~Домовёнок Кузя~Дюдюка Барбидокская~Ёжик в тумане~Кот Леопольд~Котенок по имени Гав~Котенок с улицы Лизюкова~Крошка Енот~Лисенок~Львенок~Мамонтенок~Маугли~Паровозик из Ромашкова~Попугай Кеша~Поросенок Фунтик~Пятачок~Тофсла~Умка~Чебурашка~Чертёнок № 13~Чучело-Мяучело~Шопокляк~";
        String[] usersName = names.split("~");
        for (String name : usersName) {
            User user = new User();
            user.setLogin("l ".concat(name));
            user.setAccountStatus(1);
            user.setPasswordHash("hashHardCode");
            PersonalData personalData = new PersonalData();
            personalData.setUserId(user.getUserId());
            personalData.setName("n ".concat(name));
            personalData.setPosition("Дожность HardCode");
            personalData.setSurname("SNameHardCode");
            user.setPersonalData(personalData);
            personalData.setUser(user);

//            getUserDao().saveOrUpdate(user);
        }


//ALTER TABLE `personal_data` AUTO_INCREMENT = 0
//ALTER TABLE `users` AUTO_INCREMENT = 0
//        ALTER TABLE `tasks` AUTO_INCREMENT = 0
//        ALTER TABLE `user_task` AUTO_INCREMENT = 0
//        ALTER TABLE `personal_data` PACK_KEYS =0 CHECKSUM =0 DELAY_KEY_WRITE =0 AUTO_INCREMENT =1;
//        ALTER TABLE `users` PACK_KEYS =0 CHECKSUM =0 DELAY_KEY_WRITE =0 AUTO_INCREMENT =1;
//        ALTER TABLE `tasks` PACK_KEYS =0 CHECKSUM =0 DELAY_KEY_WRITE =0 AUTO_INCREMENT =1;
//        ALTER TABLE `user_task` PACK_KEYS =0 CHECKSUM =0 DELAY_KEY_WRITE =0 AUTO_INCREMENT =1;
    }

    public static void taskGenerator() throws DaoException {
//        int countUser = 26;
        int i = 0;
        while (++i < 400) { //количество тасков
            //номер юзера: макс - 1, 1 не учавствует  2-26
            int randomUserId = random.nextInt(25) + 2; //(2-26)
            User user = getUserDao().get(randomUserId);
            TaskContent content = new TaskContent();
            content.setBody("ContentBody HardCode Content");
            content.setHistory("History HardCode Content");
            Task task = new Task();
            String title = "title Task N" + i;
            task.setTitle(title);
            task.setContent(content);
            task.setStatusId(random.nextInt(7) + 1);
            task.setDateCreation(new Date(2016 - 1900, random.nextInt(7), random.nextInt(27) + 1, 0, 0, 0));
            task.setDeadline(new Date(2016 - 1900, (random.nextInt(2) + 8), (random.nextInt(27) + 1)));
            task.getUserList().add(user);
//            getTaskDao().saveOrUpdate(task);
//            DELETE FROM `java_annotation`.`tasks`;
        }
    }

    public static PersonalData createPersonalData(User user) {
        System.out.println("Please enter Personal Data to user ");
        System.out.print("name - ");
        PersonalData personalData = new PersonalData();
        Scanner scanner = new Scanner(System.in);
        String parameter = scanner.nextLine();
        personalData.setUserId(user.getUserId());
        personalData.setName(parameter);
        personalData.setPosition("Инженер HardCode");
        personalData.setSurname("SNameHardCode");
        return personalData;
    }

    public static Task createTask(User user) {

        System.out.println("Please enter title Task to user " + user.getUserId() + " login " + user.getLogin());
        System.out.print("title - ");
        TaskContent content = new TaskContent();
        content.setBody("ContentBody HardCode   Content");
        content.setHistory("History HardCode   Content");

        Task task = new Task();
        Scanner scanner = new Scanner(System.in);
        String parameter = scanner.nextLine();
        task.setTitle(parameter);
        task.setContent(content);
        task.setDateCreation(new Date());
        task.setDeadline(new Date());
        task.getUserList().add(user);
//        user.getTaskList().add(task);


        return task;
    }

    public static User findUser() {
        System.out.println("Get by Id. Please enter user id:");
        System.out.print("Id - ");

        Scanner scanner = new Scanner(System.in);
        User user = null;
        Integer id = scanner.nextInt();
//        int id = scanner.nextInt();
        try {
            user = getUserDao().get(id);
        } catch (DaoException e) {
            log.error(e, e);
        } catch (NullPointerException e) {
            log.error("Unable find user:", e);
        }
        System.out.print(user);
        return user;
    }

    public static User loadPerson() {
        System.out.println("Loag by Id. Please enter user id:");
        System.out.print("Id - ");

        Scanner scanner = new Scanner(System.in);
        User person = null;
        Integer id = scanner.nextInt();
        try {
            person = getUserDao().load(id); //TODO 1 load = exp
        } catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NullPointerException e) {
            log.error("Unable find person:", e);
        }
        System.out.print("load person " + person);
        return person;
    }

    public static User delUser() {
        System.out.println("delete by Id. Please enter User id:");
        System.out.print("Id - ");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();
        User user = new User();
//        person.setId(id);
        try {
            user = getUserDao().get(id);
            getUserDao().delete(user);
        } catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NullPointerException e) {
            log.error("Unable find user:", e);
        }
        System.out.print(user);
        return user;
    }

    public static Task delTask() {
        System.out.println("delete by Id. Please enter Task id:");
        System.out.print("Id - ");
        Scanner scanner = new Scanner(System.in);
        long id = scanner.nextInt();
        Task task = new Task();
//        person.setId(id);
        try {
            task = getTaskDao().get(id);
            getTaskDao().delete(task);
        } catch (DaoException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            log.error("Unable find task:", e);
        }
        System.out.print(task);
        return task;
    }


    public static UserDao getUserDao() {
        if (personDao == null) {
            personDao = new UserDao();
        }

        return personDao;
    }

    public static PersonalDataDao getPersonalDataDao() {

        if (personalDataDao == null) {
            personalDataDao = new PersonalDataDao();
        }
        return personalDataDao;
    }

    public static TaskDao getTaskDao() {

        if (taskDao == null) {
            taskDao = new TaskDao();
        }
        return taskDao;
    }


    public static BaseDao getBaseDao() {

        if (baseDao == null) {
            baseDao = new BaseDao();
        }

        return baseDao;
    }

    public static void testCashTwoUser(int userId, int otherUserId) {
//        System.out.println("Temp Dir:" + System.getProperty("java.io.tmpdir"));
        Statistics stats = PersonLoader.util.getStats();
        Session session = PersonLoader.util.getSession();
        Session otherSession = PersonLoader.util.getOtherSession();

//        Session otherSession = HibernateUtil.otherSession;
        Transaction transaction = session.beginTransaction();
        Transaction otherTransaction = otherSession.beginTransaction();
        int i = 0;
        printStats(stats, i++);

        System.out.println("load " + userId);
        User user = (User) session.load(User.class, userId);
        printData(user, stats, i++);


        user = (User) session.load(User.class, userId);
        printData(user, stats, i++);

        //clear first level cache, so that second level cache is used
        session.evict(user);
        System.out.println("clear first level cache, so that second level cache is used");
        user = (User) session.load(User.class, userId);
        printData(user, stats, i++);
        System.out.println("load " + otherUserId);
        user = (User) session.load(User.class, otherUserId);
        printData(user, stats, i++);

        user = (User) otherSession.load(User.class, userId);
        printData(user, stats, i++);

        //Release resources
        transaction.commit();
        otherTransaction.commit();
//        sessionFactory.close();
    }


    private static void printStats(Statistics stats, int i) {
        System.out.println("***** " + i + " *****");
        System.out.println("Fetch Count="
                + stats.getEntityFetchCount());
        System.out.println("Second Level Hit Count="
                + stats.getSecondLevelCacheHitCount());
        System.out
                .println("Second Level Miss Count="
                        + stats
                        .getSecondLevelCacheMissCount());
        System.out.println("Second Level Put Count="
                + stats.getSecondLevelCachePutCount());
    }

    private static void printData(User user, Statistics stats, int count) {
        {
            System.out.println(count + ":: UserID=" + user.getUserId() + ", Login=" + user.getLogin());
            printStats(stats, count);
        }

    }
}
