package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataPostRepository extends JpaRepository<PostEntity, Long> {

	@EntityGraph(attributePaths = {
		"user",
		"pet",
		"route",
		"route.region",
		"route.trackingImage",
		"pet.profileImage"
	})
	Optional<PostEntity> getByPostId(Long postId);

	@Query("SELECT DISTINCT p FROM PostEntity p " +
		"LEFT JOIN FETCH p.user u " +
		"LEFT JOIN FETCH p.pet pt " +
		"LEFT JOIN FETCH pt.profileImage pi " +
		"LEFT JOIN FETCH p.route r " +
		"LEFT JOIN FETCH r.trackingImage rt " +
		"LEFT JOIN FETCH r.region rg " +
		"WHERE p.user = :user")
	List<PostEntity> findAllByUser(@Param("user") UserEntity user);

	@Query("""
   SELECT DISTINCT p FROM PostEntity p
   LEFT JOIN FETCH p.user u
   LEFT JOIN FETCH p.pet pt
   LEFT JOIN FETCH pt.profileImage pi
   LEFT JOIN FETCH p.route r
   LEFT JOIN FETCH r.trackingImage rt
   LEFT JOIN FETCH r.region rg
   WHERE p.postId = :postId
""")
	Optional<PostEntity> getPostWithAllDetails(@Param("postId") Long postId);

	boolean existsByRouteRouteId(Long routeId);

	@Modifying
	@Query("UPDATE PostEntity p SET p.likeCount = p.likeCount + 1 WHERE p.postId = :postId")
	void increaseLikeCount(@Param("postId") Long postId);

	@Modifying
	@Query("UPDATE PostEntity p SET p.likeCount = p.likeCount - 1 WHERE p.postId = :postId AND p.likeCount > 0")
	void decreaseLikeCount(@Param("postId") Long postId);

	Optional<PostEntity> findByRoute(RouteEntity route);

	List<PostEntity> findByRouteRouteIdIn(List<Long> routeIds);

	List<PostEntity> findByRouteRouteIdInAndIsPublicTrue(List<Long> routeIds);

	@Modifying(clearAutomatically = true)
	void deleteByUser_UserId(Long userId);


	List<PostEntity> findByRoute_User_UserId(Long userId);

	@Modifying(clearAutomatically = true)
	void deleteByRoute_User_UserId(Long userId);

	@Modifying
	@Query("DELETE FROM PostEntity p WHERE p.pet.user.userId = :userId")
	void deleteByPetUserId(@Param("userId") Long userId);

}
