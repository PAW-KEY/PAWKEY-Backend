package org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewEntity;
import org.sopt.pawkey.backendapi.global.infra.persistence.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	private String loginId;
	private String password;

	private String name;
	private String gender;
	private int age;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id")
	private RegionEntity region;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PetEntity> petEntityList = new ArrayList<>();
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReviewEntity> reviewEntityList = new ArrayList<>();
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostLikeEntity> postLikeEntityList = new ArrayList<>();

	// @Builder
	// public UserEntity(Long userId,
	// 	String name,
	// 	String gender,
	// 	int age,
	// 	RegionEntity region) {
	// 	this.userId = userId;
	// 	this.name = name;
	// 	this.gender = gender;
	// 	this.age = age;
	// 	this.region = region;
	/**
	 * Retrieves the first pet associated with the user, or returns {@code null} if none exist.
	 *
	 * @return the first {@link PetEntity} in the user's pet list, or {@code null} if the list is empty
	 */

	public PetEntity getPet() {
		return petEntityList.stream()
			.findFirst()
			.orElse(null);
	} // Optional<PetEntity>을 반환하도록 변경하여 null 처리를 명시적으로 만드는 것을 고려

}

