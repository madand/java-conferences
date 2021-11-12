package net.madand.conferences.db.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementParametersSetter {
    void setStatementParameters(PreparedStatement stmt) throws SQLException;
}
