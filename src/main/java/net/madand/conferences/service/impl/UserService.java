package net.madand.conferences.service.impl;

import net.madand.conferences.db.dao.UserDao;
import net.madand.conferences.entity.User;
import net.madand.conferences.service.AbstractService;
import net.madand.conferences.service.ServiceException;

import javax.sql.DataSource;
import java.util.Optional;


public class UserService extends AbstractService {
    public UserService(DataSource dataSource) {
        super(dataSource);
    }

    public Optional<User> findByEmail(String email) throws ServiceException {
        return callNoTransaction(
                connection -> UserDao.findOneByEmail(connection, email),
                "Failed to find the user"
        );
    }

    public Optional<User> findById(int id) throws ServiceException {
        return callNoTransaction(
                connection -> UserDao.findOneById(connection, id),
                "Failed to find the user"
        );
    }
}
