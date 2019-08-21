//package dao;
//
//import dto.UserDTO;
//import junit.framework.TestCase;
//import org.junit.Before;
//import utDao.PoolConnection;
//
//import java.sql.Connection;
//
//public class UserDAOTest extends TestCase {
//
//    public void testFindEntityById() throws Exception {
//
//    }
//
//    public void testCreate() throws Exception {
//
//        Connection connection = null;
//        connection = PoolConnection.getInstance().getConnection();
//        UserDAO userDAO = new UserDAO(connection);
//        assertEquals("findEntityById true", getUserDTO().getLogin(), userDAO.findEntityById(1).getLogin());
//        assertEquals("findUserByLogin true",getUserDTO().getId(),userDAO.findEntityByLogin("testik").getId());
//    }
//    @Before
//    public UserDTO getUserDTO(){
//        UserDTO equalsUser=new UserDTO(1, "testik", "$2a$10$jdIXZpQZMiGNvi4M/CkJju0kCs5zfjAezTrGok3MDpnAK6xjENwDC", 1);
//        return equalsUser;
//    }
//    }
