package org.sopt.pawkey.backendapi.domain.user.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.user.application.dto.request.OnboardingInfoCommand;
import org.sopt.pawkey.backendapi.domain.user.application.dto.request.UpdateUserInfoCommand;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.UserRepository;
import org.sopt.pawkey.backendapi.domain.user.exception.UserBusinessException;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.global.enums.Gender;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	private static final Long USER_ID = 1L;

	@Mock private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Test
	void 온보딩_중복닉네임이면_예외() {
		// given
		OnboardingInfoCommand command = new OnboardingInfoCommand("중복닉네임", LocalDate.of(1999, 1, 1), Gender.F, 1L);
		given(userRepository.existsByName("중복닉네임")).willReturn(true);

		// when & then
		assertThatThrownBy(() -> userService.completeOnboarding(USER_ID, command, mock(RegionEntity.class)))
			.isInstanceOf(UserBusinessException.class);
	}

	@Test
	void 닉네임_변경시_중복이면_예외() {
		// given
		UserEntity user = userWithName("기존닉네임");
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
		given(userRepository.existsByName("중복닉네임")).willReturn(true);

		// when & then
		assertThatThrownBy(() -> userService.updateUserInfo(USER_ID, new UpdateUserInfoCommand("중복닉네임", null, null)))
			.isInstanceOf(UserBusinessException.class);
	}

	@Test
	void 현재_닉네임과_같으면_중복검사_생략() {
		// given
		UserEntity user = userWithName("기존닉네임");
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));

		// when
		userService.updateUserInfo(USER_ID, new UpdateUserInfoCommand("기존닉네임", null, null));

		// then
		verify(userRepository, never()).existsByName("기존닉네임");
	}

	@Test
	void 닉네임이_null이면_변경_의도_없는것으로_간주한다() {
		// given
		UserEntity user = mock(UserEntity.class);
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));

		// when
		userService.updateUserInfo(USER_ID, new UpdateUserInfoCommand(null, null, null));

		// then
		verify(userRepository, never()).existsByName(any());
	}

	@Test
	void 유저정보_수정_정상_저장() {
		// given
		UserEntity user = userWithName("기존닉네임");
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
		given(userRepository.existsByName("새닉네임")).willReturn(false);

		// when
		userService.updateUserInfo(USER_ID, new UpdateUserInfoCommand("새닉네임", null, null));

		// then
		verify(userRepository).saveAndFlush(user);
	}

	private UserEntity userWithName(String name) {
		UserEntity user = mock(UserEntity.class);
		given(user.getName()).willReturn(name);
		return user;
	}
}
