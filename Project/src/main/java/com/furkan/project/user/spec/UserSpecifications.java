package com.furkan.project.user.spec;

import com.furkan.project.user.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public final class UserSpecifications {

    private UserSpecifications() {}

    private static final Set<String> ALLOWED_SORTS =
            Set.of("id","username","email","createdAt","updatedAt");

    public static Specification<User> alwaysTrue() {
        return (root, query, cb) -> cb.conjunction();
    }

    public static Specification<User> hasUsername(String username) {
        return (root, query, cb) ->
                cb.equal(cb.lower(root.get("username")), username.toLowerCase());
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, cb) ->
                cb.equal(cb.lower(root.get("email")), email.toLowerCase());
    }

    public static Specification<User> isEnabled(boolean enabled) {
        return (root, query, cb) -> cb.equal(root.get("enabled"), enabled);
    }

    public static Specification<User> isLocked(boolean locked) {
        return (root, query, cb) -> cb.equal(root.get("locked"), locked);
    }

    public static Specification<User> isDeleted(boolean deleted) {
        return (root, query, cb) -> cb.equal(root.get("deleted"), deleted);
    }

    public static String resolveSortBy(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) return "id";
        return ALLOWED_SORTS.contains(sortBy) ? sortBy : "id";
    }

    public static Sort.Direction resolveSortDir(String dir) {
        return "DESC".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;
    }
}
