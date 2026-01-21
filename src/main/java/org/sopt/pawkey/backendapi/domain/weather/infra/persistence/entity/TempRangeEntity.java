package org.sopt.pawkey.backendapi.domain.weather.infra.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "temp_range")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TempRangeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tempId;
	private String code;

	@Column(nullable = false)
	private Integer minTemp;
	@Column(nullable = false)
	private Integer maxTemp;
	private String judgment;
	private String icon;
}