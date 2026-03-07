package org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.locationtech.jts.geom.LineString;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewCategoryOptionTop3Entity;
import org.sopt.pawkey.backendapi.domain.routes.exception.RouteBusinessException;
import org.sopt.pawkey.backendapi.domain.routes.exception.RouteErrorCode;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.global.infra.persistence.entity.BaseEntity;
import org.sopt.pawkey.backendapi.global.util.GeoJsonUtil;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "routes", indexes = {
	@Index(name = "idx_route_user_created", columnList = "user_id, created_at")
	// 홈 화면 산책 정보 조회를 위해 사용자별 작성일 기준 복합 인덱스 설정
})
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
	private int distance;

	@Column(name = "duration", nullable = false)
	private Integer duration;

	@Column(name = "step_count", nullable = false)
	private Integer stepCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id", nullable = false)
	private RegionEntity region;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "image_id")
	private ImageEntity trackingImage;

	@OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ReviewCategoryOptionTop3Entity> reviewCategoryOptionTop3EntityList = new ArrayList<>();

	@Column(name = "started_at", nullable = false)
	private LocalDateTime startedAt;

	@Column(name = "ended_at", nullable = false)
	private LocalDateTime endedAt;

	public Map<String, Object> getGeoJson() {
		return GeoJsonUtil.toGeoJson(coordinates);
	}

	public void validateOwnership(UserEntity user) {
		if (!getUser().equals(user)) {
			throw new RouteBusinessException(RouteErrorCode.ROUTE_SHOW_FORBIDDEN);
		}
	}

	public int getDurationMinutes() {
		return (int)(this.duration / 60);
	}
}
