package com.furkan.project.auth.repository;

import com.furkan.project.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    @Query("select rt from RefreshToken rt where rt.user.id = :userId")
    Optional<RefreshToken> findByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("delete from RefreshToken rt where rt.user.id = :userId")
    int deleteByUserId(@Param("userId") Long userId);
}
