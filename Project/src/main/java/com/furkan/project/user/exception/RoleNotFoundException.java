package com.furkan.project.user.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) { super(message); }
}