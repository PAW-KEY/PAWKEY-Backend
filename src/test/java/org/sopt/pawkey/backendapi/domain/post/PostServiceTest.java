package org.sopt.pawkey.backendapi.domain.post;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostRegisterCommand;
import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostUpdateCommand;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostService;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostRepository;
import org.sopt.pawkey.backendapi.domain.post.exception.PostBusinessException;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void 게시물_저장_성공() {
        // given
        UserEntity user = mock(UserEntity.class);
        RouteEntity route = mock(RouteEntity.class);
        PetEntity pet = mock(PetEntity.class);

        given(route.getUser()).willReturn(user);
        given(user.getPetOrThrow()).willReturn(pet);

        PostRegisterCommand command = PostRegisterCommand.builder()
                .title("제목")
                .description("본문")
                .isPublic(true)
                .build();

        // when
        PostEntity post = postService.savePost(user, command, route);

        // then
        assertThat(post).isNotNull();
        verify(postRepository).save(any(PostEntity.class));
    }

    @Test
    void 게시물_저장_실패_다른_사람_루트() {
        // given
        UserEntity writer = mock(UserEntity.class);
        UserEntity otherUser = mock(UserEntity.class);
        RouteEntity route = mock(RouteEntity.class);

        given(route.getUser()).willReturn(otherUser);

        PostRegisterCommand command = PostRegisterCommand.builder()
                .title("제목")
                .description("본문")
                .isPublic(true)
                .build();

        // when & then
        assertThatThrownBy(() -> postService.savePost(writer, command, route))
                .isInstanceOf(PostBusinessException.class);
    }

    @Test
    void 게시물_수정_성공_본인_게시글() {
        // given
        UserEntity user = mock(UserEntity.class);
        PostEntity post = mock(PostEntity.class);

        given(post.getUser()).willReturn(user);

        PostUpdateCommand command = PostUpdateCommand.builder()
                .title("수정 제목")
                .description("수정 본문")
                .isPublic(false)
                .build();

        // when
        postService.updatePost(post, user, command);

        // then
        verify(post).update("수정 제목", "수정 본문", false);
    }

    @Test
    void 게시물_수정_실패_타인_게시글() {
        // given
        UserEntity user = mock(UserEntity.class);
        UserEntity other = mock(UserEntity.class);
        PostEntity post = mock(PostEntity.class);

        given(post.getUser()).willReturn(other);

        PostUpdateCommand command = PostUpdateCommand.builder()
                .title("수정 제목")
                .description("수정 본문")
                .isPublic(true)
                .build();

        // when & then
        assertThatThrownBy(() -> postService.updatePost(post, user, command))
                .isInstanceOf(PostBusinessException.class);
    }
}