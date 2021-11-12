package net.madand.conferences.db.util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapper creates a new entity instance and populates it with the data from the given result set row.
 *
 * @param <E> the type of the entities this mapper creates instances of.
 */
@FunctionalInterface
public interface Mapper<E> {
    /**
     * Create a new entity instance and populate it with the data in the given {@code resultSet}.
     *
     * @param resultSet the result set with a database row data.
     * @return the new entity populated with data.
     * @throws SQLException
     */
    E mapRow(ResultSet resultSet) throws SQLException;
}
