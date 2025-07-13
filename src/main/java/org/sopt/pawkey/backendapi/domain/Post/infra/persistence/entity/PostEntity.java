package org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostLikeEntity> postLikeEntityList = new ArrayList<>();

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostSelectedCategoryOptionEntity> postSelectedCategoryOptionEntityList = new ArrayList<>();

	// Hibernate는 fetch join 시 여러 List 컬렉션을 동시에 조회할 수 없으므로, Set으로 바꿔 문제를 회피. Set은 중복을 허용하지 않고 순서가 없으며, Hibernate에서 fetch join 시 안정적
	// 추후에 @OrderColumn(name = "order_idx") 같은 컬럼을 추가해 DB에 순서 정보를 저장할 수도 있음
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PostImageEntity> postImageEntityList = new HashSet<>();

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<PostCategoryOptionTop3Entity> postCategoryOptionTop3EntityList = new ArrayList<>();

	public static PostEntity create(
		Long postId,
		String title,
		String description,
		boolean isPublic,
		UserEntity user,
		RouteEntity route
	) {
		return new PostEntity(
			postId,
			title,
			description,
			isPublic,
			user,
			route,
			new ArrayList<>(),
			new ArrayList<>(),
			new HashSet<>(),
			new ArrayList<>()
		);
	}
}