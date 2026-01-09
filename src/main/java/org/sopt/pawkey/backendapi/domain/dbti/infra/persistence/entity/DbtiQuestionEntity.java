package org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "dbti_question")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DbtiQuestionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id")
	private DbtiTypeEntity dbtiType;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(nullable = false)
	private Integer sequence; // 질문 노출 순서

	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
	private List<DbtiOptionEntity> options;
}