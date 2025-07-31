package com.example.july31projectcontactmanagment.service.contact;

import com.example.july31projectcontactmanagment.entities.Contact;
import com.example.july31projectcontactmanagment.repository.contact.ContactRepository;
import com.example.july31projectcontactmanagment.repository.contact.IContactRepository;

import java.util.List;

public class ContactService implements IContactService {

    private final IContactRepository contactRepository;

    public ContactService(IContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public Contact getContactById(int id) {
        return contactRepository.getById(id);
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepository.getAll();
    }

    @Override
    public void addContact(Contact contact) {
        contactRepository.add(contact);
    }

    @Override
    public void updateContact(Contact contact) {
        contactRepository.update(contact);
    }

    @Override
    public void deleteContact(int id) {
        contactRepository.delete(id);
    }

    @Override
    public List<Contact> getContactsByUserId(int userId) {
        return contactRepository.findByUserId(userId);
    }
    @Override
    public List<Contact> searchByName(String name, int userId) {
        return contactRepository.searchByName(name, userId);
    }
}