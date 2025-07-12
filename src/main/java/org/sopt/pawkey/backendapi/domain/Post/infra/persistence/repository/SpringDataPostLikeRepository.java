package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataPostLikeRepository extends JpaRepository<PostLikeEntity, Long> {
	boolean existsByUser_UserIdAndPost_PostId(Long userId, Long postId);

	// fetch join으로 연관된 Post, User, Pet, PostImage 한 번에 조회
	@Query("""
		    select pl
		    from PostLikeEntity pl
		    where pl.user.userId = :userId
		""")
	List<PostLikeEntity> findAllByUserWithPostAndImages(@Param("userId") Long userId);

}