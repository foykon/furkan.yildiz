package com.furkan.project.list.repository;

import com.furkan.project.list.dto.response.ListItemResponse;
import com.furkan.project.list.entity.ListType;
import com.furkan.project.list.entity.UserListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserListItemRepository extends JpaRepository<UserListItem, Long>, JpaSpecificationExecutor<UserListItem> {

    boolean existsByUserIdAndMovieIdAndTypeAndDeletedFalse(Long userId, Long movieId, ListType type);

    Optional<UserListItem> findByUserIdAndMovieIdAndTypeAndDeletedFalse(Long userId, Long movieId, ListType type);

    Page<UserListItem> findByUserIdAndTypeAndDeletedFalse(Long userId, ListType type, Pageable pageable);

    @Query("""
      select li.movieId
      from UserListItem li
      where li.userId = :userId and li.type = :type and li.deleted = false
    """)
    List<Long> findMovieIdsByUserAndType(@Param("userId") Long userId, @Param("type") ListType type);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
      update UserListItem li set li.deleted = true
      where li.userId = :userId and li.movieId = :movieId and li.type = :type and li.deleted = false
    """)
    int softDeleteOne(@Param("userId") Long userId,
                      @Param("movieId") Long movieId,
                      @Param("type") ListType type);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
      update UserListItem li set li.deleted = true
      where li.userId = :userId and li.type = :type and li.deleted = false
    """)
    int softDeleteAll(@Param("userId") Long userId, @Param("type") ListType type);
}

