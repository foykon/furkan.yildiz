package com.furkan.project.list.dto.response;

import com.furkan.project.list.entity.ListType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class ListItemResponse {
    private Long movieId;
    private String title;
    private String posterUrl;
    private Integer releaseYear;
    private ListType type;
    private Instant addedAt;
    private Integer orderIndex;
}
