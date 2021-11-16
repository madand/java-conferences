package net.madand.conferences.db.util;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Functional interface that designates a database operation (presumably consisting of multiple statements), that are
 * to be run within a transaction.
 */
@FunctionalInterface
public interface TransactionalOperation {
    /**
     * @param connection the connection with auto commit set to false.
     */
    void run(Connection connection) throws SQLException;
}
