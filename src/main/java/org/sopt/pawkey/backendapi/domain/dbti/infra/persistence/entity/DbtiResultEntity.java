package org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity;

import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;

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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dbti_result")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DbtiResultEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dbti_result_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pet_id", nullable = false)
	private PetEntity pet;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 3)
	private DbtiType dbtiType;

	private int eiScore;
	private int psScore;
	private int rfScore;

	@Builder
	public DbtiResultEntity(PetEntity pet, DbtiType dbtiType, int eiScore, int psScore, int rfScore) {
		this.pet = pet;
		this.dbtiType = dbtiType;
		this.eiScore = eiScore;
		this.psScore = psScore;
		this.rfScore = rfScore;
	}

	public void updateResult(DbtiType dbtiType, int eiScore, int psScore, int rfScore) {
		this.dbtiType = dbtiType;
		this.eiScore = eiScore;
		this.psScore = psScore;
		this.rfScore = rfScore;
	}
}