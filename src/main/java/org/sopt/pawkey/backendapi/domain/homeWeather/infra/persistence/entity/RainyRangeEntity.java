package org.sopt.pawkey.backendapi.domain.homeWeather.infra.persistence.entity;

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
@Table(name = "rainy_range")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RainyRangeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rainyId;
	private String code;
	private Integer minProb;
	private Integer maxProb;
	private String judgment;
}