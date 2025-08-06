package com.furkan.project.auth.filter;

import com.furkan.project.auth.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<User> hasUsername(String username) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%");
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }
}
