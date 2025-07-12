package org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity;

import java.time.LocalDateTime;

import org.locationtech.jts.geom.LineString;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.RouteRegisterCommand;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.global.infra.persistence.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "routes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class RouteEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "route_id")
	private Long routeId;

	@Column(name = "coordinates", columnDefinition = "geometry(LineString, 4326)", nullable = false)
	private LineString coordinates;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@Column(name = "distance", nullable = false)
	private Double distance;

	@Column(name = "duration", nullable = false)
	private Integer duration;

	@Column(name = "step_count", nullable = false)
	private Integer stepCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id", nullable = false)
	private RegionEntity region;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "image_id", nullable = true)
	private ImageEntity trackingImage;

	@Column(name = "started_at", nullable = false)
	private LocalDateTime startedAt;

	@Column(name = "ended_at", nullable = false)
	private LocalDateTime endedAt;

	public static RouteEntity createRoute(UserEntity user, RouteRegisterCommand command, ImageEntity trackingImage) {
		return RouteEntity.builder()
			.user(user)
			.distance(command.distance())
			.duration(command.duration())
			.stepCount(command.stepCount())
			.region(user.getRegion())
			.trackingImage(trackingImage)
			.startedAt(command.startedAt())
			.endedAt(command.endedAt())
			.build();
	}
}
