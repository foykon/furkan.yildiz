package com.example.july31projectcontactmanagment.service.contact;

import com.example.july31projectcontactmanagment.entities.Contact;

import java.util.List;

public interface IContactService {
    Contact getContactById(int id);
    List<Contact> getAllContacts();
    void addContact(Contact contact);
    void updateContact(Contact contact);
    void deleteContact(int id);
    List<Contact> getContactsByUserId(int userId);
    List<Contact> searchByName(String name, int userId);
}
