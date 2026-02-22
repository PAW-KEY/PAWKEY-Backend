package org.sopt.pawkey.backendapi.fixtures;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;

import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

import java.util.List;

public class PostFixture {

    public static PostEntity createPost(UserEntity user, RouteEntity route, PetEntity pet) {
        return PostEntity.builder()
                .user(user)
                .route(route)
                .pet(pet)
                .title("제목")
                .description("본문")
                .isPublic(true)
                .postSelectedCategoryOptionEntityList(List.of())
                .build();
    }
}