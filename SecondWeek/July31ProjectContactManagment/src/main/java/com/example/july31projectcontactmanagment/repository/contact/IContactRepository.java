package com.example.july31projectcontactmanagment.repository.contact;

import com.example.july31projectcontactmanagment.entities.Contact;
import com.example.july31projectcontactmanagment.repository.base.IBaseRepository;

import java.util.List;

public interface IContactRepository extends IBaseRepository<Contact> {
    List<Contact> findByUserId(int userId);
    List<Contact> searchByName(String name, int userId);
}
