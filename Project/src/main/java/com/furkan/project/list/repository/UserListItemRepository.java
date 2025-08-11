package com.furkan.project.list.repository;

import com.furkan.project.list.dto.response.ListItemResponse;
import com.furkan.project.list.entity.ListType;
import com.furkan.project.list.entity.UserListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
public interface UserListItemRepository extends JpaRepository<UserListItem, Long>, JpaSpecificationExecutor<UserListItem> {

    boolean existsByUserIdAndMovieIdAndTypeAndDeletedFalse(Long userId, Long movieId, ListType type);

    Optional<UserListItem> findByUserIdAndMovieIdAndTypeAndDeletedFalse(Long userId, Long movieId, ListType type);

    @Query("""
      select new com.furkan.project.list.dto.response.ListItemResponse(
         m.id, m.title, m.posterUrl, FUNCTION('YEAR', m.releaseDate),
         li.type, li.createdAt, li.orderIndex)
      from UserListItem li
      join li.movie m
      where li.user.id = :userId and li.type = :type
        and (:q is null or lower(m.title) like lower(concat('%', :q, '%')))
      order by coalesce(li.orderIndex, 2147483647), li.createdAt desc
    """)
    Page<ListItemResponse> findList(Long userId, ListType type, String q, Pageable pageable);

    @Query("""
      select new com.furkan.project.list.dto.response.ListItemResponse(
         m.id, m.title, m.posterUrl, FUNCTION('YEAR', m.releaseDate),
         li.type, li.createdAt, li.orderIndex)
      from UserListItem li
      join li.movie m
      where li.user.id = :userId and li.movie.id = :movieId and li.type = :type
    """)
    Optional<ListItemResponse> findOneItem(Long userId, Long movieId, ListType type);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update UserListItem li set li.deleted = true where li.user.id = :userId and li.movie.id = :movieId and li.type = :type and li.deleted = false")
    int softDeleteOne(Long userId, Long movieId, ListType type);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update UserListItem li set li.deleted = true where li.user.id = :userId and li.type = :type and li.deleted = false")
    int softDeleteAll(Long userId, ListType type);
}

