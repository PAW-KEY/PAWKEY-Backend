package org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	@Column(nullable = false)
	private Long petId;

	@Column(nullable = false, length = 3)
	private String dbtiType;

	private int eiScore;
	private int psScore;
	private int rfScore;

	@Builder
	public DbtiResultEntity(Long petId, String dbtiType, int eiScore, int psScore, int rfScore) {
		this.petId = petId;
		this.dbtiType = dbtiType;
		this.eiScore = eiScore;
		this.psScore = psScore;
		this.rfScore = rfScore;
	}
}