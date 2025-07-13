package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataPostLikeRepository extends JpaRepository<PostLikeEntity, Long> {
	boolean existsByUser_UserIdAndPost_PostId(Long userId, Long postId);

	@Query("""
		    select distinct pl
		    from PostLikeEntity pl
		    join fetch pl.post p
		    join fetch pl.user u
		    where pl.user.userId = :userId
		""")
	List<PostLikeEntity> findAllByUserWithPostAndImages(@Param("userId") Long userId);

}