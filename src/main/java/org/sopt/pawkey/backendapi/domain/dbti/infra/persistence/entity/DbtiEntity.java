package org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity;

import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dbti")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DbtiEntity {
	@Id
	@Enumerated(EnumType.STRING)
	@Column(length = 3)
	private DbtiType type;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String imageUrl;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;

	@Column(nullable = false)
	private String keywords;
}