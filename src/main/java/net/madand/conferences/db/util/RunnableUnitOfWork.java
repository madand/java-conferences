package net.madand.conferences.db.util;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Functional interface that designates a database operation(s), with no value returned.
 * Naming is in analogy with the core's Runnable interface.
 */
@FunctionalInterface
public interface RunnableUnitOfWork {
    /**
     * @param connection the connection with auto commit set to false.
     */
    void run(Connection connection) throws SQLException;
}
