package org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity;

import java.time.LocalDate;

import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiResultEntity;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.global.enums.Gender;
import org.sopt.pawkey.backendapi.global.infra.persistence.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pets")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pet_id")
	private Long petId;

	//User 연관관계 추가
	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender", nullable = false, length = 10)
	private Gender gender;

	@Column(name = "birth", nullable = false)
	@PastOrPresent(message = "생년월일은 현재 또는 과거 날짜여야 합니다")
	private LocalDate birth;

	//Image 연관관계 추가
	@OneToOne
	@JoinColumn(name = "image_id")
	private ImageEntity profileImage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;

	private boolean isNeutered;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "breed_id")
	private BreedEntity breed;

	@OneToOne(mappedBy = "pet", fetch = FetchType.LAZY)
	private DbtiResultEntity dbtiResult;

	@Builder
	public PetEntity(Long petId,
		String name,
		Gender gender,
		LocalDate birth,
		ImageEntity profileImage,
		UserEntity user,
		boolean isNeutered,
		BreedEntity breed) {

		this.petId = petId;
		this.name = name;
		this.gender = gender;
		this.birth = birth;
		this.profileImage = profileImage;
		this.user = user;
		this.isNeutered = isNeutered;
		this.breed = breed;
	}

	public void updateProfile(String name, LocalDate birth, Gender gender, boolean isNeutered, BreedEntity breed,
		ImageEntity profileImage) {
		this.name = name;
		this.birth = birth;
		this.gender = gender;
		this.isNeutered = isNeutered;
		this.breed = breed;
		this.profileImage = profileImage;
	}

	public void setDbtiResult(DbtiResultEntity dbtiResult) {
		this.dbtiResult = dbtiResult;
	}
}
