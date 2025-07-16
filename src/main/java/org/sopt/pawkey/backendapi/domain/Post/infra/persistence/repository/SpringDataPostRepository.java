package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataPostRepository extends JpaRepository<PostEntity, Long> {
	Optional<PostEntity> getByPostId(Long postId);

	List<PostEntity> findAllByUser(UserEntity user);

	@Query("SELECT p FROM PostEntity p " +
		"LEFT JOIN FETCH p.postLikeEntityList " +
		"LEFT JOIN FETCH p.postImageEntityList pi " +
		"LEFT JOIN FETCH pi.image " +
		"LEFT JOIN FETCH p.user u " +
		"LEFT JOIN FETCH p.pet pt " +
		"LEFT JOIN FETCH pt.profileImage " +
		"LEFT JOIN FETCH p.route r " +
		"LEFT JOIN FETCH r.trackingImage " +
		"LEFT JOIN FETCH r.region " +
		"LEFT JOIN FETCH p.postSelectedCategoryOptionEntityList sel " +
		"LEFT JOIN FETCH sel.categoryOption co " +
		"WHERE p.postId = :postId")
	Optional<PostEntity> getPostWithLikesAndImages(@Param("postId") Long postId);

	boolean existsByRouteRouteId(Long routeId);
}
