package org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity;

import java.util.ArrayList;
import java.util.List;

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
import org.sopt.pawkey.backendapi.domain.category.domain.model.CategorySelectionType;


@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long categoryId;

	@Column(name = "category_name", nullable = false)
	private String categoryName;

	@Column(name = "display_order", nullable = false)
	private Integer displayOrder;

	@Enumerated(EnumType.STRING)
	@Column(name = "selection_type", nullable = false)
	private CategorySelectionType selectionType;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("id ASC")
	private List<CategoryOptionEntity> options = new ArrayList<>();


	public void validateSelection(List<CategoryOptionEntity> selectedOptions) { //카테고리 선택방식에 대한 책임 -> Entity

		if (selectedOptions == null || selectedOptions.isEmpty()) {
			throw new CategoryBusinessException(
				CategoryErrorCode.CATEGORY_SELECTION_REQUIRED
			);
		}

		if (this.selectionType == CategorySelectionType.SINGLE &&
			selectedOptions.size() != 1) {
			throw new CategoryBusinessException(
				CategoryErrorCode.INVALID_CATEGORY_SELECTION
			);
		}
	}

}

