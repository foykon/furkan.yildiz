package com.furkan.project.user.dto.request;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFilterRequest {

    private String username;
    private String email;
    private Boolean enabled;
    private Boolean locked;
    private Boolean deleted;

    private String sortBy;
    private String sortDirection;
    private Integer page;
    private Integer size;
}
