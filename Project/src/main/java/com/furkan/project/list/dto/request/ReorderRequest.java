package com.furkan.project.list.dto.request;

import com.furkan.project.list.entity.ListType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ReorderRequest {

    @NotEmpty
    private List<ListReorderItem> items;

    @Getter @Setter
    public static class ListReorderItem {
        @NotNull private Long movieId;
        @NotNull private ListType type;
        @NotNull private Integer orderIndex;
    }
}
