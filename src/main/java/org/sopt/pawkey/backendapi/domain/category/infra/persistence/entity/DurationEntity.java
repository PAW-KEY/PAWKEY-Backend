package org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.domain.model.CategorySelectionType;
import org.sopt.pawkey.backendapi.domain.category.exception.CategoryBusinessException;
import org.sopt.pawkey.backendapi.domain.category.exception.CategoryErrorCode;
import org.sopt.pawkey.backendapi.global.infra.persistence.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category_duration")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DurationEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "duration_id")
	private Long durationId;

	@Column(name = "duration_name", nullable = false)
	private String durationName;

	@Column(name = "display_order", nullable = false)
	private Integer displayOrder;

	@Enumerated(EnumType.STRING)
	@Column(name = "selection_type", nullable = false)
	private CategorySelectionType selectionType;

	@OneToMany(mappedBy = "duration", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("id ASC")
	private List<DurationOptionEntity> options = new ArrayList<>();

	public void validateSelection(List<DurationOptionEntity> selectedOptions) {
		if (selectedOptions == null || selectedOptions.isEmpty()) {
			throw new CategoryBusinessException(CategoryErrorCode.CATEGORY_SELECTION_REQUIRED);
		}
		if (this.selectionType == CategorySelectionType.SINGLE && selectedOptions.size() != 1) {
			throw new CategoryBusinessException(CategoryErrorCode.INVALID_CATEGORY_SELECTION);
		}
	}
}
