package com.example.july31projectcontactmanagment.repository.user;

import com.example.july31projectcontactmanagment.entities.User;
import com.example.july31projectcontactmanagment.repository.base.BaseRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository extends BaseRepository<User> implements IUserRepository {

    @Override
    protected String getTableName() {
        return "user";
    }
    public UserRepository() {
        super();
    }
    @Override
    protected User mapResultSetToEntity(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        return user;
    }

    @Override
    protected PreparedStatement prepareInsertStatement(Connection conn, User user) throws SQLException {
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        return stmt;
    }

    @Override
    protected PreparedStatement prepareUpdateStatement(Connection conn, User user) throws SQLException {
        String sql = "UPDATE user SET username = ?, password = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        stmt.setInt(3, user.getId());
        return stmt;
    }

    @Override
    public User findByUsername(String username) {
        String query = "SELECT * FROM user WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEntity(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
