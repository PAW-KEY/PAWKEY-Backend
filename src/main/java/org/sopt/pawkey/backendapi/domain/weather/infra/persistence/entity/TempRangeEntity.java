package org.sopt.pawkey.backendapi.domain.weather.infra.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "temp_range")
public class TempRangeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tempId;
	private String code;

	private Integer minTemp;
	private Integer maxTemp;
	private String judgment;
	private String icon;
}