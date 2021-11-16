package net.madand.conferences.service.impl;

import net.madand.conferences.db.dao.UserDao;
import net.madand.conferences.entity.User;
import net.madand.conferences.service.AbstractService;
import net.madand.conferences.service.ServiceException;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class UserService extends AbstractService {
    private static final Logger log = Logger.getLogger(UserService.class);

    public UserService(DataSource dataSource) {
        super(dataSource);
    }

    public Optional<User> findByEmail(String email) throws ServiceException {
        log.trace("findByEmail started");

        try (Connection conn = getConnection()) {
            return UserDao.findOneByEmail(conn, email);
        } catch (SQLException throwables) {
            final String message = "Error finding user";
            log.error(message, throwables);
            throw new ServiceException(message, throwables);
        }
    }
}
