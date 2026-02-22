package org.sopt.pawkey.backendapi.controller.post;

import static org.hamcrest.Matchers.*;
import static org.sopt.pawkey.backendapi.domain.image.domain.ImageDomain.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.repository.SpringDataCategoryOptionRepository;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.SpringDataImageRepository;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.post.api.dto.request.FilterPostsRequestDto;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostLikeRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostSelectedCategoryOptionEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository.SpringDataPostRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository.SpringDataPostSelectedCategoryOptionRepository;
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
import org.springframework.test.util.ReflectionTestUtils;
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
	@Autowired
	private SpringDataCategoryOptionRepository categoryOptionRepository;
	@Autowired
	private SpringDataPostSelectedCategoryOptionRepository postSelectedCategoryOptionRepository;

	private UserEntity testUser;

	private RegionEntity region;
	private ImageEntity routeImage;

	@BeforeEach
	void setUp() {
		region = regionRepository.save(RegionFixture.createRegion());
		testUser = userRepository.save(UserFixture.createUser(region));

		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(testUser.getUserId(), null, List.of())
		);

		routeImage = imageRepository.save(ImageEntity.builder()
			.imageUrl("https://pawkey.com/test-route")
			.width(1080).height(720).extension("png")
			.domain(ROUTE)
			.build());

		PostEntity postA = createPost("A: 20분 & 많음 & 벤치", 1200);
		saveMapping(postA, 3L);
		saveMapping(postA, 12L);

		PostEntity postB = createPost("B: 60분 & 많음 & 벤치", 3600);
		saveMapping(postB, 3L);
		saveMapping(postB, 12L);

		PostEntity postC = createPost("C: 10분 & 적음 & 카페", 600);
		saveMapping(postC, 1L);
		saveMapping(postC, 15L);
	}

	private PostEntity createPost(String title, int durationSeconds) {
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

		RouteEntity route = routeRepository.save(RouteEntity.builder()
			.distance(2000)
			.duration(durationSeconds)
			.region(region)
			.trackingImage(routeImage)
			.startedAt(LocalDateTime.now().minusSeconds(durationSeconds))
			.endedAt(LocalDateTime.now())
			.stepCount(3000)
			.coordinates(geometryFactory.createLineString(new Coordinate[] {
				new Coordinate(126.8, 37.5), new Coordinate(126.9, 37.6)
			}))
			.build());

		return postRepository.save(PostEntity.builder()
			.user(testUser)
			.route(route)
			.title(title)
			.isPublic(true)
			.build());
	}

	private void saveMapping(PostEntity post, Long optionId) {
		postSelectedCategoryOptionRepository.save(
			PostSelectedCategoryOptionEntity.builder()
				.post(post)
				.categoryOption(categoryOptionRepository.getReferenceById(optionId))
				.build()
		);
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
				jsonPath("$.data.posts.length()").value(3),
				jsonPath("$.data.hasNext").value(false)
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
				jsonPath("$.code").value("S000"),
				jsonPath("$.data.posts.length()", greaterThan(0))
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
			.andExpectAll(status().isOk(),
				jsonPath("$.data.posts.length()", greaterThan(0))
			);
	}

	@Test
	@DisplayName("Case 3: 시간 필터 +  카테고리 세트 필터 적용")
	void filterPosts_FullSetOptions_Success() throws Exception {
		String jsonRequest = """
			{
			  "selectedOptions": [
			    { "durationId": 6, "optionsIds": [22] }, 
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
			.andExpectAll(
				status().isOk(),
				jsonPath("$.data.posts").isArray(),
				jsonPath("$.data.posts.length()").value(1)
			);
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

		ReflectionTestUtils.setField(targetPost, "likeCount", 1L);
		postRepository.saveAndFlush(targetPost);

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

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
	}
}