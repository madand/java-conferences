package net.madand.conferences.service.impl;

import net.madand.conferences.auth.Role;
import net.madand.conferences.db.dao.UserDao;
import net.madand.conferences.db.web.QueryOptions;
import net.madand.conferences.entity.Talk;
import net.madand.conferences.entity.User;
import net.madand.conferences.service.AbstractService;
import net.madand.conferences.service.ServiceException;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class UserService extends AbstractService {
    public UserService(DataSource dataSource) {
        super(dataSource);
    }

    public List<User> findAllExceptGiven(User excludeUser, QueryOptions queryOptions) throws ServiceException {
        return callNoTransaction(
                connection -> UserDao.findAllExceptGiven(connection, excludeUser, queryOptions),
                "Failed to the list of users"
        );
    }

    public Optional<User> findOneByEmail(String email) throws ServiceException {
        return callNoTransaction(
                connection -> UserDao.findOneByEmail(connection, email),
                "Failed to find the user"
        );
    }

    public Optional<User> findOneById(int id) throws ServiceException {
        return callNoTransaction(
                connection -> UserDao.findOneById(connection, id),
                "Failed to find the user"
        );
    }

    public void create(User user) throws ServiceException {
        runWithinTransaction(
                connection -> UserDao.insert(connection, user),
                "Failed to save the user into the database");
    }

    public void update(User user) throws ServiceException {
        runWithinTransaction(
                connection -> UserDao.update(connection, user),
                "Failed to update the user in the database");
    }

    public void delete(User user) throws ServiceException {
        runWithinTransaction(
                connection -> UserDao.delete(connection, user),
                "Failed to delete the user from the database");
    }

    public List<User> speakersList() throws ServiceException {
        return callNoTransaction(
                connection -> UserDao.findAllByRole(connection, Role.SPEAKER),
                "Failed to fetch the speakers list"
        );
    }

    public List<User> unproposedSpeakersForTalk(Talk talk) throws ServiceException {
        return callNoTransaction(
                connection -> UserDao.findAllUnproposedSpeakersForTalk(connection, talk),
                "Failed to fetch the speakers list"
        );
    }
}
