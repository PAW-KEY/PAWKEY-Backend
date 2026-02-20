INSERT INTO category (category_id, category_name, display_order, selection_type, created_at, updated_at)
VALUES (1, '혼잡도', 1, 'SINGLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, '강아지 교류 빈도', 2, 'SINGLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, '안전', 3, 'MULTIPLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (4, '편의성', 4, 'MULTIPLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (5, '환경', 5, 'MULTIPLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO category_option (category_option_id, category_id, option_value, created_at, updated_at)
VALUES (1, 1, '적음', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 1, '평범', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 1, '많음', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (4, 2, '교류 없음', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (5, 2, '보통', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (6, 2, '교류 활발', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (7, 3, '차량 적음', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (8, 3, '보도/차도 분리', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (9, 3, '보도 넓음', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (10, 3, '킥보드/자전거 적음', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (11, 3, '야간 밝음', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (12, 4, '벤치', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (13, 4, '배변 봉투 쓰레기통', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (14, 4, '편의점', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (15, 4, '반려견 동반 카페', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (16, 5, '잔디길', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (17, 5, '흙길', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (18, 5, '포장길', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (19, 5, '놀이터/공터', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO category_duration (duration_id, duration_name, display_order, selection_type, created_at, updated_at)
VALUES (6, '산책 소요 시간', 6, 'SINGLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO duration_option (duration_option_id, duration_id, duration_text, created_at, updated_at)
VALUES (21, 6, '시간 무관', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (22, 6, '30분 미만', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (23, 6, '30~60분', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (24, 6, '1시간 이상', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);