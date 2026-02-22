package org.sopt.pawkey.backendapi.domain.image;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.pawkey.backendapi.domain.image.application.service.command.ImageService;
import org.sopt.pawkey.backendapi.domain.image.domain.repository.ImageRepository;
import org.sopt.pawkey.backendapi.domain.image.exception.ImageBusinessException;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;


import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;

    @Test
    void imageId로_이미지_조회_성공() {
        Long imageId = 100L;
        ImageEntity image = mock(ImageEntity.class);

        given(imageRepository.findById(imageId))
                .willReturn(Optional.of(image));

        ImageEntity result = imageService.getImageById(imageId);

        assertThat(result).isNotNull();
    }

    @Test
    void imageId로_이미지_조회_실패() {
        Long imageId = 999L;

        given(imageRepository.findById(imageId))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> imageService.getImageById(imageId))
                .isInstanceOf(ImageBusinessException.class);
    }
}
