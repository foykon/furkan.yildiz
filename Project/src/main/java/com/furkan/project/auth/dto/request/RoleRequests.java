package com.furkan.project.auth.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class RoleRequests {
    private Set<RoleRequest> roleRequests;
}
