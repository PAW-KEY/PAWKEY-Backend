package org.sopt.pawkey.backendapi.domain.homeWeather.infra.persistence.entity;

import java.time.LocalDateTime;

import org.sopt.pawkey.backendapi.global.infra.persistence.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "weather")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class WeatherEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "weather_id")
	private Long id;

	@Column(name = "region_id", nullable = false, unique = true)
	private Long regionId;

	@Column(name = "temperature")
	private Integer temperature;

	@Column(name = "rainy_mm")
	private Integer rainyMm;

	@Column(name = "rainy_prob")
	private Integer rainyProb; // 강수 확률

	@Column(name = "weather_code")
	private Integer weatherCode;

	/**
	 * 데이터가 1시간 이상 경과했는지 확인합니다.
	 * 데이터가 없거나(null) 1시간이 지났다면 true를 반환하여 갱신을 유도합니다.
	 */
	public boolean isStale() {
		if (this.getUpdatedAt() == null) {
			return true;
		}
		return this.getUpdatedAt().isBefore(LocalDateTime.now().minusHours(1));
	}

	public static WeatherEntity create(
		Long regionId,
		Integer temperature,
		Integer rainyMm,
		Integer rainyProb,
		Integer weatherCode
	) {
		return WeatherEntity.builder()
			.regionId(regionId)
			.temperature(temperature)
			.rainyMm(rainyMm)
			.rainyProb(rainyProb)
			.weatherCode(weatherCode)
			.build();
	}

	/**
	 * 외부 API로부터 받은 최신 날씨 정보로 데이터를 업데이트합니다.
	 */
	public void updateWeather(Integer temperature, Integer rainyMm, Integer rainyProb, Integer weatherCode) {
		this.temperature = temperature;
		this.rainyMm = rainyMm;
		this.rainyProb = rainyProb;
		this.weatherCode = weatherCode;
	}
}