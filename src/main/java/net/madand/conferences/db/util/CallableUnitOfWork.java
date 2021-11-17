package net.madand.conferences.db.util;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Functional interface that designates a database operation(s) and returns a value.
 * Naming is in analogy with the core's Callable interface.
 */
@FunctionalInterface
public interface CallableUnitOfWork<R> {
    /**
     * @param connection the connection with auto commit set to false.
     */
    R run(Connection connection) throws SQLException;
}
