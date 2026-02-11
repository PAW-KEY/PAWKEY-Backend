package org.sopt.pawkey.backendapi.domain.post.api.controller;

import static org.sopt.pawkey.backendapi.domain.image.domain.ImageDomain.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.SpringDataImageRepository;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.post.api.dto.request.FilterPostsRequestDto;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostLikeRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository.SpringDataPostRepository;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.SpringDataRegionRepository;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.SpringDataRouteRepository;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.SpringDataUserRepository;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.fixtures.RegionFixture;
import org.sopt.pawkey.backendapi.fixtures.UserFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class PostFilterControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private SpringDataPostRepository postRepository;
	@Autowired
	private SpringDataUserRepository userRepository;
	@Autowired
	private SpringDataRegionRepository regionRepository;
	@Autowired
	private SpringDataRouteRepository routeRepository;
	@Autowired
	private SpringDataImageRepository imageRepository;
	@Autowired
	private PostLikeRepository postLikeRepository;

	private UserEntity testUser;

	@BeforeEach
	void setUp() {
		RegionEntity region = regionRepository.save(RegionFixture.createRegion());
		testUser = userRepository.save(UserFixture.createUser(region));

		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(testUser.getUserId(), null, List.of())
		);

		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

		for (int i = 1; i <= 11; i++) {
			ImageEntity routeImage = imageRepository.save(ImageEntity.builder()
				.imageUrl("https://pawkey.com/test" + i)
				.width(1080).height(720).extension("png")
				.domain(ROUTE)
				.build());

			RouteEntity uniqueRoute = routeRepository.save(RouteEntity.builder()
				.distance(1500 + i)
				.duration(1200)
				.region(region)
				.trackingImage(routeImage)
				.startedAt(LocalDateTime.now().minusMinutes(20))
				.endedAt(LocalDateTime.now())
				.stepCount(2000)
				.coordinates(geometryFactory.createLineString(new Coordinate[] {
					new Coordinate(126.8576, 37.5193),
					new Coordinate(126.8580, 37.5200)
				}))
				.build());

			postRepository.save(PostEntity.builder()
				.user(testUser)
				.route(uniqueRoute)
				.title("테스트 산책 게시물 " + i)
				.isPublic(true)
				.build());
		}
	}

	@Test
	@DisplayName("선택된 옵션이 없을 때 전체 리스트를 최신순으로 페이징 조회")
	void filterPosts_All_Success() throws Exception {
		FilterPostsRequestDto request = new FilterPostsRequestDto(List.of());

		mockMvc.perform(post("/api/v1/posts/filter")
				.param("sortBy", "latest")
				.param("size", "10")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpectAll(
				status().isOk(),
				jsonPath("$.data.posts.length()").value(10),
				jsonPath("$.data.hasNext").value(true)
			);
	}

	@Test
	@DisplayName("30분 미만(22) 시간 필터 적용 시 데이터가 정상 조회")
	void filterPosts_WithDuration_Success() throws Exception {

		String jsonRequest = """
			{
			  "selectedOptions": [
			    {
			      "categoryId": null,
			      "durationId": 6,
			      "optionsIds": [22]
			    }
			  ]
			}
			""";

		mockMvc.perform(post("/api/v1/posts/filter")
				.param("sortBy", "latest")
				.param("size", "10")
				.content(jsonRequest)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpectAll(
				status().isOk(),
				jsonPath("$.code").value("S000")
			);
	}

	@Test
	@DisplayName("Case 1: 선택된 옵션이 아예 없을 때 (빈 배열)")
	void filterPosts_EmptyOptions_Success() throws Exception {
		String jsonRequest = "{\"selectedOptions\": []}";

		mockMvc.perform(post("/api/v1/posts/filter")
				.param("sortBy", "latest")
				.param("size", "10")
				.content(jsonRequest)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Case 2: 카테고리 1번(옵션 3)과 4번(옵션 12) 필터 적용")
	void filterPosts_MultipleCategories_Success() throws Exception {
		String jsonRequest = """
			{
			  "selectedOptions": [
			    { "categoryId": 1, "optionsIds": [3] },
			    { "categoryId": 4, "optionsIds": [12] }
			  ]
			}
			""";

		mockMvc.perform(post("/api/v1/posts/filter")
				.param("sortBy", "latest")
				.param("size", "10")
				.content(jsonRequest)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Case 3: 시간 필터 + 모든 카테고리 풀 세트 필터 적용")
	void filterPosts_FullSetOptions_Success() throws Exception {
		String jsonRequest = """
			{
			  "selectedOptions": [
			    { "durationId": 6, "optionsIds": [22] },
			    { "categoryId": 1, "optionsIds": [3] },
			    { "categoryId": 2, "optionsIds": [4] },
			    { "categoryId": 3, "optionsIds": [7, 9] },
			    { "categoryId": 4, "optionsIds": [12] },
			    { "categoryId": 5, "optionsIds": [17, 18] }
			  ]
			}
			""";

		mockMvc.perform(post("/api/v1/posts/filter")
				.param("sortBy", "latest")
				.param("size", "10")
				.content(jsonRequest)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("인기순 정렬 조회 시 좋아요가 많은 게시글이 상단에 노출")
	void filterPosts_SortByPopular_Success() throws Exception {
		List<PostEntity> allPosts = postRepository.findAll();
		PostEntity targetPost = allPosts.stream()
			.min(Comparator.comparing(PostEntity::getPostId))
			.orElseThrow();

		postLikeRepository.save(org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity.builder()
			.post(targetPost)
			.user(testUser)
			.build());

		FilterPostsRequestDto request = new FilterPostsRequestDto(List.of());

		mockMvc.perform(post("/api/v1/posts/filter")
				.param("sortBy", "popular")
				.param("size", "10")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpectAll(
				status().isOk(),
				jsonPath("$.data.posts[0].postId").value(targetPost.getPostId()),
				jsonPath("$.data.posts[0].isLiked").value(true)
			);
	}
}