package com.furkan.project.list.dto.request;

import com.furkan.project.list.entity.ListType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddListItemRequest {
    @NotNull
    private Long movieId;

    @NotNull
    private ListType type;

    private Integer orderIndex;
}
