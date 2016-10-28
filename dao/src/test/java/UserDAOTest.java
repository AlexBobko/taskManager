import dao.UserDAO;
import dto.UserDTO;
import org.junit.Assert;
import org.junit.Test;
import utDao.PoolConnection;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class UserDAOTest {
//    @Autowired private AbstractDAO<UserDTO> UserDAO;
    @Test
    public void findEntityById() throws PropertyVetoException, SQLException, IOException {
        Connection connection = null;
        connection = PoolConnection.getInstance().getConnection();
        UserDAO userDAO = new UserDAO(connection);
//        UserDTO user1=new UserDTO(1, "testik", "");
//        UserDTO userDTO=new UserDTO(1);
        UserDTO equalsUser=new UserDTO(1, "testik", "$2a$10$jdIXZpQZMiGNvi4M/CkJju0kCs5zfjAezTrGok3MDpnAK6xjENwDC", 1);
        Assert.assertEquals("findEntityById true",equalsUser, userDAO.findEntityById(1));
        Assert.assertEquals("findEntityByLogin true",userDAO.findEntityByLogin("testik"));
    }
}