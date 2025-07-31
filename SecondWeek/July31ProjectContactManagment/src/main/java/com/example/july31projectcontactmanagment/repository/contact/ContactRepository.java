package com.example.july31projectcontactmanagment.repository.contact;

import com.example.july31projectcontactmanagment.entities.Contact;
import com.example.july31projectcontactmanagment.repository.base.BaseRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactRepository extends BaseRepository<Contact> implements IContactRepository {

    @Override
    protected String getTableName() {
        return "contacts";
    }

    public ContactRepository() {
        super();
    }

    @Override
    protected Contact mapResultSetToEntity(ResultSet rs) throws SQLException {
        Contact contact = new Contact();
        contact.setId(rs.getInt("id"));
        contact.setUserid(rs.getInt("userid"));
        contact.setContactname(rs.getString("contactname"));
        contact.setContactnumber(rs.getString("contactnumber"));
        return contact;
    }

    public List<Contact> searchByName(String name, int userId) {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts WHERE userid = ? AND contactname LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, "%" + name + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                contacts.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contacts;
    }


    @Override
    public List<Contact> findByUserId(int userId) {
        List<Contact> contacts = new ArrayList<>();
        String query = "SELECT * FROM contacts WHERE userid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                contacts.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    @Override
    protected PreparedStatement prepareInsertStatement(Connection conn, Contact contact) throws SQLException {
        String sql = "INSERT INTO contacts (userid, contactname, contactnumber) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, contact.getUserid());
        stmt.setString(2, contact.getContactname());
        stmt.setString(3, contact.getContactnumber());
        return stmt;
    }

    @Override
    protected PreparedStatement prepareUpdateStatement(Connection conn, Contact contact) throws SQLException {
        String sql = "UPDATE contacts SET userid = ?, contactname = ?, contactnumber = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, contact.getUserid());
        stmt.setString(2, contact.getContactname());
        stmt.setString(3, contact.getContactnumber());
        stmt.setInt(4, contact.getId());
        return stmt;
    }
}
