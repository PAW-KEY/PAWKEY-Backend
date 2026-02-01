package org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dbti_type")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DbtiTypeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 10)
	private String code;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String leftLabel;
	@Column(nullable = false)
	private String rightLabel;
}