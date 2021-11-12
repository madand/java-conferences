package net.madand.conferences.db.dao;

import net.madand.conferences.auth.Role;
import net.madand.conferences.entity.User;
import net.madand.conferences.test.DbHelper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

public class UserDaoTest {
    private final DbHelper dbHelper;
    private final Connection connection;

    public UserDaoTest() throws SQLException, IOException {
        dbHelper = DbHelper.getInstance();
        connection = dbHelper.getConnection();
    }

    @Before
    public void setUp() throws IOException, SQLException {
        dbHelper.recreateDbTables();
    }

    @Test
    public void findAllByRole() throws SQLException {
        List<User> list1 = UserDao.findAllByRole(connection, Role.MODERATOR);
        assertEquals("No moderators yet", 0, list1.size());

        User user = User.makeInstance("a@b.com", "John Doe", "secret", Role.MODERATOR);
        UserDao.insert(connection, user);
        List<User> list2 = UserDao.findAllByRole(connection, Role.MODERATOR);
        assertEquals("Successfully inserted", 1, list2.size());
        assertTrue("Should have the same fields", compareUsers(user, list2.get(0)));
    }

    @Test
    public void findOne() throws SQLException {
        User user1 = User.makeInstance("a@b.com", "John Doe", "secret", Role.MODERATOR);
        UserDao.insert(connection, user1);
        User user2 = UserDao.findOne(connection, user1.getId()).get();
        assertNotNull("Should successfully find", user2);
        assertTrue("Should have the same fields", compareUsers(user1, user2));
    }

    @Test
    public void insert() throws SQLException {
        User user1 = User.makeInstance("a@b.com", "John Doe", "secret", Role.MODERATOR);
        int oldId = user1.getId();
        UserDao.insert(connection, user1);
        assertNotEquals("Should set generated ID", oldId, user1.getId());
    }

    @Test
    public void update() throws SQLException {
        User user1 = User.makeInstance("a@b.com", "John Doe", "secret", Role.MODERATOR);
        UserDao.insert(connection, user1);

        final String NEW_EMAIL = "new@test.com";
        user1.setEmail(NEW_EMAIL);
        UserDao.update(connection, user1);
        User user2 = UserDao.findOne(connection, user1.getId()).get();
        assertEquals("Updated email in database", NEW_EMAIL, user2.getEmail());
    }

    @Test
    public void delete() throws SQLException {
        List<User> list1 = UserDao.findAllByRole(connection, Role.MODERATOR);
        assertEquals("No moderators yet", 0, list1.size());

        User user = User.makeInstance("a@b.com", "John Doe", "secret", Role.MODERATOR);
        UserDao.insert(connection, user);
        assertNotNull(UserDao.findOne(connection, user.getId()));

        UserDao.delete(connection, user);
        assertFalse(UserDao.findOne(connection, user.getId()).isPresent());
    }

    private boolean compareUsers(User u1, User u2) {
        return Objects.equals(u1.getEmail(), u2.getEmail())
                && Objects.equals(u1.getRealName(), u2.getRealName())
                && Objects.equals(u1.getPasswordHash(), u2.getPasswordHash())
                && Objects.equals(u1.getRole(), u2.getRole());
    }
}
