package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import feign.Param;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostSelectedCategoryOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SpringDataPostSelectedCategoryOptionRepository extends JpaRepository<PostSelectedCategoryOptionEntity, Long> {
	void deleteByPost(PostEntity post);


	@Modifying(clearAutomatically = true)
	@Query("""
        delete from PostSelectedCategoryOptionEntity o
        where o.post.user.userId = :userId
    """)
	void deleteByUserId(@Param("userId") Long userId);
}
