package org.sopt.pawkey.backendapi.domain.image.infra.persistence;

import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataImageRepository extends JpaRepository<ImageEntity, Long> {
}
