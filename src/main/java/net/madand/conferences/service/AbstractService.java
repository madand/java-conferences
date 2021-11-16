package net.madand.conferences.service;

import net.madand.conferences.db.dao.ConferenceDao;
import net.madand.conferences.db.dao.ConferenceTranslationDao;
import net.madand.conferences.entity.ConferenceTranslation;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractService {
    private DataSource dataSource;

    public AbstractService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * @param operation the a
     * @param handler
     * @throws ServiceException
     */
    protected void runWithinTransaction(TransactionalOperation operation, TransactionalExceptionHandler handler) throws ServiceException {
        Connection connection = null;
        try {
            try {
                connection = getConnection();
                connection.setAutoCommit(false);

                operation.run(connection);

                connection.commit();
            } catch (SQLException e) {
                if (connection != null) {
                    connection.rollback();
                }
                throw e;
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            handler.handle(e);
        }
    }

    /**
     * Return exception handler that logs the given message along with the caught exception, and then throws
     * {@link ServiceException} with the given message and the caught exception as the cause.
     *
     * @param message the message for logging and for the newly thrown exception.
     * @param logger the logger instance.
     * @return the exception handler.
     */
    protected TransactionalExceptionHandler makeDefaultHandler(String message, Logger logger) {
        return exception -> logAndThrowWrapped(message, logger, exception);
    }

    public static void logAndThrowWrapped(String message, Logger logger, SQLException exception) throws ServiceException {
        logger.error(message, exception);
        throw new ServiceException(message, exception);
    }

    /**
     * Functional interface that designates a group of database operations that are to be run within a transaction.
     */
    @FunctionalInterface
    public interface TransactionalOperation {
        /**
         * @param connection the connection with auto commit disabled.
         */
        void run(Connection connection) throws SQLException;
    }

    /**
     * Functional interface that designates an SQL exception handler.
     */
    @FunctionalInterface
    public interface TransactionalExceptionHandler {
        /**
         * @param exception
         * @throws ServiceException
         */
        void handle(SQLException exception) throws ServiceException;
    }
}
