package com.furkan.project.list.service;

import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.PagedDataResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.list.dto.request.AddListItemRequest;
import com.furkan.project.list.dto.request.ListFilterRequest;
import com.furkan.project.list.dto.request.ReorderRequest;
import com.furkan.project.list.dto.response.ListItemResponse;
import com.furkan.project.list.entity.ListType;
import org.springframework.data.domain.Pageable;

public interface ListService {
    DataResult<ListItemResponse> addToList(Long userId, AddListItemRequest req);
    Result removeFromList(Long userId, Long movieId, ListType type);
    PagedDataResult<ListItemResponse> getList(Long userId, ListType type, ListFilterRequest filter, Pageable pageable);
    Result reorder(Long userId, ReorderRequest request);
    Result clear(Long userId, ListType type);
    DataResult<Boolean> contains(Long userId, Long movieId, ListType type);
}
