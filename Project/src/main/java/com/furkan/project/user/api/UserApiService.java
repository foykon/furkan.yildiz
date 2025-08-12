package com.furkan.project.user.api;

public interface UserApiService {
    boolean existsById(Long userId);
    Long findIdByUsername(String username);
    String getUserNameById(Long id);
}