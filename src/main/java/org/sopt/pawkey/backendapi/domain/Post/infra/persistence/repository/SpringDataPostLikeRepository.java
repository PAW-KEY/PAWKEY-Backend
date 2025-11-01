package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataPostLikeRepository extends JpaRepository<PostLikeEntity, Long> {
	boolean existsByUser_UserIdAndPost_PostId(Long userId, Long postId);

	@Query("""
		    SELECT DISTINCT pl FROM PostLikeEntity pl
		    LEFT JOIN FETCH pl.post p
		    LEFT JOIN FETCH p.user u               
		    LEFT JOIN FETCH p.pet pt              
		    LEFT JOIN FETCH pt.profileImage pi     
		    LEFT JOIN FETCH p.route r             
		    LEFT JOIN FETCH r.trackingImage rt     
		    LEFT JOIN FETCH r.region rg           
		    LEFT JOIN FETCH p.postImageEntityList pil 
		    LEFT JOIN FETCH pil.image pimg   
		    WHERE pl.user.userId = :userId
		""")
	List<PostLikeEntity> findAllByUserWithPostAndImages(@Param("userId") Long userId);

	Optional<PostLikeEntity> findByUser_UserIdAndPost_PostId(Long userId, Long postId);
}