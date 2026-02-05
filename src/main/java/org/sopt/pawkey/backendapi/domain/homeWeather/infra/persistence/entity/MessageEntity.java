package org.sopt.pawkey.backendapi.domain.homeWeather.infra.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
	name = "message",
	uniqueConstraints = {
		@UniqueConstraint(
			name = "uk_message_temp_rainy",
			columnNames = {"temp_id", "rainy_id"}
		)
	}
)
public class MessageEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long messageId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "temp_id")
	private TempRangeEntity tempRange;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rainy_id")
	private RainyRangeEntity rainyRange;

	private String mainMessage;

	private String subMessage;
}