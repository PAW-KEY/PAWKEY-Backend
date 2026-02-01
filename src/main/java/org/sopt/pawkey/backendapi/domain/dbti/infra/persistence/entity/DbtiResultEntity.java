package org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity;

import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;
import org.sopt.pawkey.backendapi.domain.dbti.exception.DbtiBusinessException;
import org.sopt.pawkey.backendapi.domain.dbti.exception.DbtiErrorCode;
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
	@JoinColumn(name = "pet_id", nullable = false, unique = true)
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

	public void setPet(PetEntity pet) {
		this.pet = pet;
		if (pet.getDbtiResult() != this) {
			pet.setDbtiResult(this);
		}
	}

	public DbtiAnalysisResult getAnalysisOf(String axisCode) {
		int rawScore = switch (axisCode) {
			case "EI" -> this.eiScore;
			case "PS" -> this.psScore;
			case "RT" -> this.rfScore;
			default -> throw new DbtiBusinessException(DbtiErrorCode.DBTI_NOT_FOUND);
		};

		boolean isLeftDominant = rawScore >= 2;
		String side = isLeftDominant ? "left" : "right";
		int finalScore = isLeftDominant ? rawScore : (3 - rawScore);

		return new DbtiAnalysisResult(side, finalScore);
	}

	public record DbtiAnalysisResult(String side, int score) {
	}

}