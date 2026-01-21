package org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.locationtech.jts.geom.MultiPolygon;
import org.sopt.pawkey.backendapi.domain.region.domain.model.RegionType;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.global.infra.persistence.entity.BaseEntity;
import org.sopt.pawkey.backendapi.global.util.GeoJsonUtil;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "regions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class RegionEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "region_id")
	private Long regionId;

	@Enumerated(EnumType.STRING)
	@Column(name = "region_type", nullable = false)
	private RegionType regionType;

	@Column(name = "region_name", nullable = false)
	private String regionName;

	@Column(name = "latitude", nullable = false)
	private Double latitude;

	@Column(name = "longitude", nullable = false)
	private Double longitude;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private RegionEntity parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RegionEntity> childrenRegionList = new ArrayList<>();

	@Column(name = "area_geometry", columnDefinition = "geometry(MultiPolygon, 4326)")
	private MultiPolygon areaGeometry;

	@OneToMany(mappedBy = "region", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<UserEntity> userEntityList = new ArrayList<>();

	@OneToMany(mappedBy = "region", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<RouteEntity> routeEntityList = new ArrayList<>();

	public String getFullRegionName() {
		String parentText = parent == null ? "" : parent.getRegionName() + " ";
		return parentText + regionName;
	}

	public Map<String, Object> getGeoJson() {
		return GeoJsonUtil.toGeoJson(areaGeometry);
	}
}

