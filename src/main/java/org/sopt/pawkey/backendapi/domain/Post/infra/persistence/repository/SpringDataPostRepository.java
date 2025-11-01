package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
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
		// [MultipleBagFetchException] 회피: Hibernate 제약으로 인해 두 개 이상의 List(Bag)를 동시에 Fetch Join할 수 없음 -> application.yml에 Batch Size 설정을 통해 N+1을 우회함.
		// "LEFT JOIN FETCH p.postSelectedCategoryOptionEntityList sel " +
		// "LEFT JOIN FETCH sel.categoryOption co " +
		"LEFT JOIN FETCH p.postImageEntityList pil " +
		"LEFT JOIN FETCH pil.image pi2 " +
		"WHERE p.user = :user")
	List<PostEntity> findAllByUser(@Param("user") UserEntity user);

	@Query("SELECT p FROM PostEntity p " +
		// 단일 조회 시 MultipleBagFetchException을 피하고 @BatchSize에 의존하도록 쿼리 수정
		// "LEFT JOIN FETCH p.postLikeEntityList " +
		// "LEFT JOIN FETCH p.postImageEntityList pi " +
		// "LEFT JOIN FETCH p.postSelectedCategoryOptionEntityList sel " +
		"LEFT JOIN FETCH p.user u " +
		"LEFT JOIN FETCH p.pet pt " +
		"LEFT JOIN FETCH pt.profileImage pi " +
		"LEFT JOIN FETCH p.route r " +
		"LEFT JOIN FETCH r.trackingImage rt " +
		"LEFT JOIN FETCH r.region rg " +
		// "LEFT JOIN FETCH sel.categoryOption co " +
		"WHERE p.postId = :postId")
	Optional<PostEntity> getPostWithLikesAndImages(@Param("postId") Long postId);

	boolean existsByRouteRouteId(Long routeId);
}
