package com.example.july31projectcontactmanagment.repository.user;

import com.example.july31projectcontactmanagment.entities.User;
import com.example.july31projectcontactmanagment.repository.base.IBaseRepository;

public interface IUserRepository extends IBaseRepository<User> {
  User findByUsername(String username);

}
