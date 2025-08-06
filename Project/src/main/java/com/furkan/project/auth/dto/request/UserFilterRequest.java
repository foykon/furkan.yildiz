package com.furkan.project.auth.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFilterRequest {
    private UserRequest userRequest;
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "id";
    private String sortDirection = "asc";
}
