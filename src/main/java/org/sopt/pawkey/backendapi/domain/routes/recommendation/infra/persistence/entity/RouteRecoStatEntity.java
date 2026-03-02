package org.sopt.pawkey.backendapi.domain.routes.recommendation.infra.persistence.entity;


import jakarta.persistence.*;
import lombok.*;
import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;
import org.sopt.pawkey.backendapi.global.infra.persistence.entity.BaseEntity;

@Entity
@Table(
        name = "route_reco_stats",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_reco_context",
                columnNames = {"regionId", "dbtiType", "routeId"}
        ),
        indexes = {
                @Index(name = "idx_reco_context",
                        columnList = "regionId, dbtiType, score DESC")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RouteRecoStatEntity extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long regionId;

    @Enumerated(EnumType.STRING)
    private DbtiType dbtiType;

    @Column(nullable = false)
    private Long routeId;

    @Column(nullable = false)
    private Long score;
}
