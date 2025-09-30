package org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "social_account", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"platform", "platform_user_id"})
})
public class SocialAccountEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@Column(nullable = false, length = 20)
	private String platform; // KAKAO, GOOGLE, APPLE

	@Column(name = "platform_user_id", nullable = false, length = 64)
	private String platformUserId; // 소셜에서 받은 고유 ID (sub, id 등)

	@Column(name = "primary_email", length = 255)
	private String primaryEmail;

	@Builder
	private SocialAccountEntity(UserEntity user, String platform, String platformUserId, String primaryEmail) {
		this.user = user;
		this.platform = platform;
		this.platformUserId = platformUserId;
		this.primaryEmail = primaryEmail;
	}

	/**
	 * SocialAccount 객체 생성 메서드
	 */
	public static SocialAccountEntity create(UserEntity user, String platform, String platformUserId, String primaryEmail) {
		return SocialAccountEntity.builder()
			.user(user)
			.platform(platform)
			.platformUserId(platformUserId)
			.primaryEmail(primaryEmail)
			.build();
	}

}
