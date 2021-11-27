package net.madand.conferences.db.dao;

import net.madand.conferences.auth.Role;
import net.madand.conferences.db.util.QueryHelper;
import net.madand.conferences.db.util.StatementParametersSetter;
import net.madand.conferences.entity.Talk;
import net.madand.conferences.entity.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public class UserDao {
    private static final String FIND_ALL = "SELECT * FROM \"user\" ORDER BY real_name";
    private static final String FIND_ALL_BY_ROLE = "SELECT * FROM \"user\" WHERE role = ?::role_type ORDER BY real_name";
    private static final String FIND_ONE_BY_ID = "SELECT * FROM \"user\" WHERE id = ?";
    private static final String FIND_ONE_BY_EMAIL = "SELECT * FROM \"user\" WHERE email = ?";
    private static final String INSERT = "INSERT INTO \"user\" (email, real_name, password_hash, role) VALUES (?,?,?,?::role_type)";
    private static final String UPDATE = "UPDATE \"user\" SET email = ?, real_name = ?, password_hash = ?, role = ?::role_type WHERE id = ?";
    private static final String DELETE = "DELETE FROM \"user\" WHERE id = ?";

    public static List<User> findAllByRole(Connection connection, Role role) throws SQLException {
        return QueryHelper.findAll(connection, FIND_ALL_BY_ROLE,
                stmt -> stmt.setString(1, role.toString()),
                UserDao::mapRow);
    }

    public static Optional<User> findOneByEmail(Connection conn, String email) throws SQLException {
        return QueryHelper.findOne(conn, FIND_ONE_BY_EMAIL,
                stmt -> stmt.setString(1, email),
                UserDao::mapRow);
    }

    public static Optional<User> findOneById(Connection conn, int id) throws SQLException {
        return QueryHelper.findOne(conn, FIND_ONE_BY_ID,
                stmt -> stmt.setInt(1, id),
                UserDao::mapRow);
    }

    public static void insert(Connection conn, User user) throws SQLException {
        Optional<Integer> maybeId = QueryHelper.insert(conn, INSERT, makeStatementParametersSetter(user));
        user.setId(maybeId.get());
    }

    public static void update(Connection conn, User user) throws SQLException {
        QueryHelper.update(conn, UPDATE, stmt -> {
            makeStatementParametersSetter(user).setStatementParameters(stmt);
            stmt.setInt(5, user.getId());
        });
    }

    public static void delete(Connection conn, User user) throws SQLException {
        QueryHelper.delete(conn, DELETE,
                stmt -> stmt.setInt(1, user.getId()));
    }

    public static List<User> findAllUnproposedSpeakersForTalk(Connection connection, Talk talk) throws SQLException {
        final String SQL = "SELECT * FROM \"user\" " +
                "WHERE role = 'SPEAKER' AND id NOT IN (SELECT speaker_id FROM talk_speaker_proposal WHERE talk_id = ?) " +
                "ORDER BY real_name";
        return QueryHelper.findAll(connection, SQL,
                stmt -> stmt.setInt(1, talk.getId()),
                UserDao::mapRow);
    }

    private static StatementParametersSetter makeStatementParametersSetter(User user) {
        return (stmt) -> {
            int i = 0;
            stmt.setString(++i, user.getEmail());
            stmt.setString(++i, user.getRealName());
            stmt.setString(++i, user.getPasswordHash());
            stmt.setString(++i, user.getRole().toString());
        };
    }

    private static User mapRow(ResultSet rs) throws SQLException {
        User user = new User();

        int i = 0;
        user.setId(rs.getInt(++i));
        user.setCreatedAt(rs.getObject(++i, OffsetDateTime.class));
        user.setUpdatedAt(rs.getObject(++i, OffsetDateTime.class));
        user.setEmail(rs.getString(++i));
        user.setRealName(rs.getString(++i));
        user.setPasswordHash(rs.getString(++i));
        user.setRole(Role.valueOf(rs.getString(++i)));

        return user;
    }
}
