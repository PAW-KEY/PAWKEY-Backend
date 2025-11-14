package org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.BatchSize;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewEntity;
import org.sopt.pawkey.backendapi.domain.user.exception.UserBusinessException;
import org.sopt.pawkey.backendapi.domain.user.exception.UserErrorCode;
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

	@BatchSize(size = 100)
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PetEntity> petEntityList = new ArrayList<>();

	@BatchSize(size = 100)
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReviewEntity> reviewEntityList = new ArrayList<>();

	@BatchSize(size = 100)
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostLikeEntity> postLikeEntityList = new ArrayList<>();

	@Builder
	public UserEntity(Long userId,
		String name,
		String gender,
		int age,
		RegionEntity region) {
		this.userId = userId;
		this.name = name;
		this.gender = gender;
		this.age = age;

	}

	public PetEntity getPet() {
		return petEntityList.stream()
			.findFirst()
			.orElse(null);
	}

	public PetEntity getPetOrThrow() {
		PetEntity pet = getPet();
		if (pet == null) {
			throw new UserBusinessException(UserErrorCode.USER_PET_NOT_REGISTERED);
		}

		return pet;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		UserEntity that = (UserEntity)o;

		return userId != null && userId.equals(that.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(userId);
	}

	public void updateRegion(RegionEntity region) {
		this.region = region;
	}
}

