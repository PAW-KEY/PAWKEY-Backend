package org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.global.infra.persistence.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long postId;

	@Column(name = "title", columnDefinition = "TEXT")
	private String title;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "is_public", nullable = false)
	private boolean isPublic;

	//User연관관계
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@OneToOne
	@JoinColumn(name = "route_id", nullable = false)
	private RouteEntity route;

	@ManyToOne
	@JoinColumn(name = "pet_id", nullable = true)
	private PetEntity pet;

	@BatchSize(size = 100)
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostLikeEntity> postLikeEntityList = new ArrayList<>();

	@BatchSize(size = 100)
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostSelectedCategoryOptionEntity> postSelectedCategoryOptionEntityList = new ArrayList<>();

	@BatchSize(size = 100)
	@Builder.Default
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostImageEntity> postImageEntityList = new ArrayList<>();

	@BatchSize(size = 100)
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<PostCategoryOptionTop3Entity> postCategoryOptionTop3EntityList = new ArrayList<>();


	public void addImages(List<ImageEntity> images) {
		for (ImageEntity image : images) {
			PostImageEntity postImage = PostImageEntity.builder()
				.post(this)
				.image(image)
				.build();
			this.postImageEntityList.add(postImage);
		}
	}


}