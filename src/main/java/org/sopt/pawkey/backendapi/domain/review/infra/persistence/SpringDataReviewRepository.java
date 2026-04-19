package org.sopt.pawkey.backendapi.domain.review.infra.persistence;

import feign.Param;
import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SpringDataReviewRepository extends JpaRepository<ReviewEntity, Long> {
    @Modifying
    @Query("DELETE FROM ReviewEntity r WHERE r.user.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);


    @Modifying
    @Query("DELETE FROM ReviewEntity r WHERE r.route.user.userId = :userId")
    void deleteByRouteUserId(@Param("userId") Long userId);

    boolean existsByUserUserIdAndRouteRouteId(Long userId, Long routeId);
}
