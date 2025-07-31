package com.example.july31projectcontactmanagment.repository.base;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public abstract class BaseRepository<IEntity> implements IBaseRepository<IEntity> {
    private static final String url = "jdbc:mysql://localhost:3307/contact_management";
    private static final String username = "root";
    private static final String password = "password";

    protected Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  // MySQL driver'ı yükle
            System.out.println("MySQL JDBC Driver yüklendi.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public BaseRepository() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected abstract String getTableName();
    protected abstract IEntity mapResultSetToEntity(ResultSet rs) throws SQLException;
    protected abstract PreparedStatement prepareInsertStatement(Connection conn, IEntity entity) throws SQLException;
    protected abstract PreparedStatement prepareUpdateStatement(Connection conn, IEntity entity) throws SQLException;

    @Override
    public IEntity getById(int id) {
        String query = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEntity(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<IEntity> getAll() {
        List<IEntity> list = new ArrayList<>();
        String query = "SELECT * FROM " + getTableName();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void add(IEntity entity) {
        try (PreparedStatement stmt = prepareInsertStatement(connection, entity)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(IEntity entity) {
        try (PreparedStatement stmt = prepareUpdateStatement(connection, entity)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM " + getTableName() + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

