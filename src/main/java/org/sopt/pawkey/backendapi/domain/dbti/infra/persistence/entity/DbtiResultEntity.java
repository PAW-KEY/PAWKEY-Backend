package org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity;

import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 3)
	private DbtiType dbtiType;

	private int eiScore;
	private int psScore;
	private int rfScore;

	@Builder
	public DbtiResultEntity(Long petId, DbtiType dbtiType, int eiScore, int psScore, int rfScore) {
		this.petId = petId;
		this.dbtiType = dbtiType;
		this.eiScore = eiScore;
		this.psScore = psScore;
		this.rfScore = rfScore;
	}
}