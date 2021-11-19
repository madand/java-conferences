package net.madand.conferences.service;

import net.madand.conferences.db.util.RunnableUnitOfWork;
import net.madand.conferences.db.util.CallableUnitOfWork;
import net.madand.conferences.web.controller.impl.TalkController;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractService {
    private static final Logger logger = Logger.getLogger(AbstractService.class);

    private final DataSource dataSource;

    protected AbstractService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Run the given unit of work without a database transaction (with autoCommit=true), then return the result.
     * Should an exception be thrown during the operation, {@link ServiceException} will be thrown with the given
     * error message.
     *
     * @param unitOfWork the database operation(s) to be performed.
     * @param errorMessage the error message of the exception, thrown by this method.
     * @param <R> the type of the value returned by the {@code unitOfWork}.
     * @return the value returned by the {@code unitOfWork}.
     * @throws ServiceException
     */
    protected <R> R callNoTransaction(CallableUnitOfWork<R> unitOfWork, String errorMessage) throws ServiceException {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(true);
            return unitOfWork.run(connection);
        } catch (SQLException e) {
            logger.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    /**
     * Run the given unit of work within a database transaction. Should an exception be thrown during the operation,
     * the transaction will be rolled back, and {@link ServiceException} will be thrown with the given error message.
     *
     * @param unitOfWork the database operation(s) to be performed.
     * @param errorMessage the error message of the exception, thrown by this method.
     * @throws ServiceException
     */
    protected void runWithinTransaction(RunnableUnitOfWork unitOfWork, String errorMessage) throws ServiceException {
        try (Connection connection = getConnection()) {
            runDMLWithinTransactionInternal(connection, unitOfWork);
        } catch (SQLException e) {
            logger.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    private void runDMLWithinTransactionInternal(Connection connection, RunnableUnitOfWork unitOfWork) throws SQLException {
        try {
            connection.setAutoCommit(false);
            unitOfWork.run(connection);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }
}
