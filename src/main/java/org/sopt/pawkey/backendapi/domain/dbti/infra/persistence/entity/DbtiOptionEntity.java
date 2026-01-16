package org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dbti_option")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DbtiOptionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id")
	private DbtiQuestionEntity question;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	private String imageUrl;

	@Column(nullable = false, length = 2)
	private String value;
}