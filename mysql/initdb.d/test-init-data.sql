INSERT INTO member(`member-id`, email, evaluation_alarm, password, recommendation_alarm)
VALUES (default, 'test@email.com', true, '{noop}password', true),
       (default, 'test2@email.com', true, '{noop}password', true);


INSERT INTO category(category_id, name)
VALUES (default, '식비'),
       (default, '쇼핑'),
       (default, '교통'),
       (default, '오락'),
       (default, '자기계발'),
       (default, '의료'),
       (default, '경조사'),
       (default, '투자'),
       (default, '여행'),
       (default, '기타');


INSERT INTO member_category(member_category_id, category_id, member_id)
VALUES (default, 1, 1),
       (default, 2, 1),
       (default, 3, 1),
       (default, 4, 1),
       (default, 5, 1),
       (default, 6, 1),
       (default, 7, 1),
       (default, 8, 1),
       (default, 9, 1),
       (default, 10, 1),
       (default, 1, 2),
       (default, 2, 2),
       (default, 3, 2),
       (default, 4, 2),
       (default, 5, 2),
       (default, 6, 2),
       (default, 7, 2),
       (default, 8, 2),
       (default, 9, 2),
       (default, 10, 2);

INSERT INTO budget(budget_id, amount, month, member_category_id)
VALUES (default, 850000, '2023-10-1', 1),
       (default, 300000, '2023-10-1', 2),
       (default, 60000, '2023-10-1', 3),
       (default, 150000, '2023-10-1', 4),
       (default, 217000, '2023-10-1', 5),
       (default, 50000, '2023-10-1', 6),
       (default, 100000, '2023-10-1', 7),
       (default, 1500000, '2023-10-1', 8),
       (default, 300000, '2023-10-1', 9),
       (default, 473000, '2023-10-1', 10),
       (default, 400000, '2023-10-1', 11),
       (default, 50000, '2023-10-1', 12),
       (default, 150000, '2023-10-1', 13),
       (default, 100000, '2023-10-1', 14),
       (default, 100000, '2023-10-1', 15),
       (default, 100000, '2023-10-1', 16),
       (default, 100000, '2023-10-1', 17),
       (default, 2500000, '2023-10-1', 18),
       (default, 100000, '2023-10-1', 19),
       (default, 400000, '2023-10-1', 20),
       (default, 850000, '2023-11-1', 1),
       (default, 300000, '2023-11-1', 2),
       (default, 60000, '2023-11-1', 3),
       (default, 150000, '2023-11-1', 4),
       (default, 217000, '2023-11-1', 5),
       (default, 50000, '2023-11-1', 6),
       (default, 100000, '2023-11-1', 7),
       (default, 1500000, '2023-11-1', 8),
       (default, 300000, '2023-11-1', 9),
       (default, 473000, '2023-11-1', 10),
       (default, 400000, '2023-11-1', 11),
       (default, 50000, '2023-11-1', 12),
       (default, 150000, '2023-11-1', 13),
       (default, 100000, '2023-11-1', 14),
       (default, 100000, '2023-11-1', 15),
       (default, 100000, '2023-11-1', 16),
       (default, 100000, '2023-11-1', 17),
       (default, 2500000, '2023-11-1', 18),
       (default, 100000, '2023-11-1', 19),
       (default, 400000, '2023-11-1', 20);


INSERT INTO expense(expense_id, amount, expense_at, memo, member_category_id)
VALUES (default, 1250, '2023-10-1 07:32:32', '출근 교통비', 3),
       (default, 10000, '2023-10-1 12:30:12', '점심: 제육볶음', 1),
       (default, 1250, '2023-10-1 18:05:15', '퇴근: 강남역', 3),
       (default, 53000, '2023-10-1 18:42:20', '후드티', 2),
       (default, 15000, '2023-10-1 19:11:42', '닭갈비', 1),
       (default, 12000, '2023-10-1 19:11:42', '아메리카노, 치즈케이크', 4),
       (default, 1250, '2023-10-1 20:25:19', '집으로', 3),
       (default, 1250, '2023-10-2 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-2 12:30:12', '점심: 된장찌개', 1),
       (default, 1250, '2023-10-2 18:25:19', '집으로', 3),
       (default, 1250, '2023-10-3 07:25:32', '출근 교통비', 3),
       (default, 10000, '2023-10-3 12:40:12', '점심: 보리밥', 1),
       (default, 1250, '2023-10-3 18:25:19', '집으로', 3),
       (default, 17000, '2023-10-3 21:09:19', '야식: 치킨', 1),
       (default, 22570, '2023-10-4 11:15:20', '장보기', 1),
       (default, 60000, '2023-10-5 19:20:32', '병원', 6),
       (default, 1250, '2023-10-6 07:15:32', '출근 교통비', 3),
       (default, 10000, '2023-10-6 12:42:12', '점심: 카레', 1),
       (default, 1250, '2023-10-6 18:23:19', '집으로', 3),
       (default, 60000, '2023-10-6 19:20:32', '병원', 6),
       (default, 1250, '2023-10-7 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-7 12:42:12', '점심: 카레', 1),
       (default, 1250, '2023-10-7 18:15:19', '집으로', 3),
       (default, 1250, '2023-10-8 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-8 12:42:12', '점심', 1),
       (default, 1250, '2023-10-8 18:15:19', '집으로', 3),
       (default, 1250, '2023-10-9 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-9 12:42:12', '점심', 1),
       (default, 1250, '2023-10-9 18:15:19', '집으로', 3),
       (default, 1250, '2023-10-10 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-10 12:42:12', '점심', 1),
       (default, 1250, '2023-10-10 18:15:19', '집으로', 3),
       (default, 1250, '2023-10-11 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-11 12:42:12', '점심', 1),
       (default, 1250, '2023-10-11 18:15:19', '집으로', 3),
       (default, 1250, '2023-10-12 07:20:32', '집에서 왕십리로', 3),
       (default, 12000, '2023-10-12 12:42:12', '점심: 중식', 1),
       (default, 5000, '2023-10-12 13:10:12', '카페', 4),
       (default, 99000, '2023-10-12 14:42:12', '신발', 2),
       (default, 22000, '2023-10-12 18:22:12', '고기', 1),
       (default, 1250, '2023-10-12 20:10:19', '집으로', 3),
       (default, 1250, '2023-10-13 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-13 12:42:12', '점심', 1),
       (default, 1250, '2023-10-13 18:15:19', '집으로', 3),
       (default, 1250, '2023-10-14 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-14 12:42:12', '점심', 1),
       (default, 1250, '2023-10-14 18:15:19', '집으로', 3),
       (default, 1250, '2023-10-15 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-15 12:42:12', '점심', 1),
       (default, 1250, '2023-10-15 18:15:19', '집으로', 3),
       (default, 1250, '2023-10-16 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-16 12:42:12', '점심', 1),
       (default, 1250, '2023-10-16 18:15:19', '집으로', 3),
       (default, 1250, '2023-10-17 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-17 12:42:12', '점심', 1),
       (default, 1250, '2023-10-17 18:15:19', '집으로', 3),
       (default, 1250, '2023-10-18 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-18 12:42:12', '점심', 1),
       (default, 1250, '2023-10-18 18:15:19', '집으로', 3),
       (default, 32000, '2023-10-19 11:10:12', '장보기', 1),
       (default, 1250, '2023-10-21 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-21 12:42:12', '점심', 1),
       (default, 1250, '2023-10-21 18:15:19', '집으로', 3),
       (default, 1250, '2023-10-22 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-22 12:42:12', '점심', 1),
       (default, 1250, '2023-10-22 18:15:19', '집으로', 3),
       (default, 1250, '2023-10-23 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-23 12:42:12', '점심', 1),
       (default, 1250, '2023-10-23 18:15:19', '집으로', 3),
       (default, 1250, '2023-10-24 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-24 12:42:12', '점심', 1),
       (default, 1250, '2023-10-24 18:15:19', '집으로', 3),
       (default, 1250, '2023-10-25 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-25 12:42:12', '점심', 1),
       (default, 1250, '2023-10-25 18:15:19', '집으로', 3),
       (default, 1250, '2023-10-26 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-26 12:42:12', '점심', 1),
       (default, 1250, '2023-10-26 18:15:19', '집으로', 3),
       (default, 1250, '2023-10-27 11:20:32', '친구 결혼식장', 3),
       (default, 200000, '2023-10-27 12:42:12', '축의금', 7),
       (default, 5000, '2023-10-27 12:42:12', '카페', 4),
       (default, 1250, '2023-10-27 18:15:19', '집으로', 3),
       (default, 1250, '2023-10-28 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-28 12:42:12', '점심', 1),
       (default, 1250, '2023-10-28 18:15:19', '집으로', 3),
       (default, 1250, '2023-10-29 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-29 12:42:12', '점심', 1),
       (default, 1250, '2023-10-29 18:15:19', '집으로', 3),
       (default, 1250, '2023-10-30 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-10-30 12:42:12', '점심', 1),
       (default, 1250, '2023-10-30 18:15:19', '집으로', 3),
       (default, 1250, '2023-10-1 07:20:32', '출근', 13),
       (default, 10000, '2023-10-1 12:42:12', '점심', 11),
       (default, 1250, '2023-10-1 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-2 07:20:32', '출근', 13),
       (default, 10000, '2023-10-2 12:42:12', '점심', 11),
       (default, 1250, '2023-10-2 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-3 07:20:32', '출근', 13),
       (default, 10000, '2023-10-3 12:42:12', '점심', 11),
       (default, 1250, '2023-10-3 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-4 07:20:32', '출근', 13),
       (default, 10000, '2023-10-4 12:42:12', '점심', 11),
       (default, 1250, '2023-10-4 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-5 07:20:32', '출근', 13),
       (default, 10000, '2023-10-5 12:42:12', '점심', 11),
       (default, 1250, '2023-10-5 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-6 07:20:32', '출근', 13),
       (default, 10000, '2023-10-6 12:42:12', '점심', 11),
       (default, 1250, '2023-10-6 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-7 07:20:32', '출근', 13),
       (default, 10000, '2023-10-7 12:42:12', '점심', 11),
       (default, 1250, '2023-10-7 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-8 07:20:32', '출근', 13),
       (default, 10000, '2023-10-8 12:42:12', '점심', 11),
       (default, 1250, '2023-10-8 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-9 07:20:32', '출근', 13),
       (default, 10000, '2023-10-9 12:42:12', '점심', 11),
       (default, 1250, '2023-10-9 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-10 07:20:32', '출근', 13),
       (default, 12000, '2023-10-15 20:20:32', '병원', 16),
       (default, 12000, '2023-10-16 20:20:32', '병원', 16),
       (default, 10000, '2023-10-10 12:42:12', '점심', 11),
       (default, 1250, '2023-10-10 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-11 07:20:32', '출근', 13),
       (default, 10000, '2023-10-11 12:42:12', '점심', 11),
       (default, 1250, '2023-10-11 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-12 07:20:32', '출근', 13),
       (default, 10000, '2023-10-12 12:42:12', '점심', 11),
       (default, 1250, '2023-10-12 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-13 07:20:32', '출근', 13),
       (default, 10000, '2023-10-13 12:42:12', '점심', 11),
       (default, 1250, '2023-10-13 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-14 07:20:32', '출근', 13),
       (default, 10000, '2023-10-14 12:42:12', '점심', 11),
       (default, 1250, '2023-10-14 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-15 07:20:32', '출근', 13),
       (default, 10000, '2023-10-15 12:42:12', '점심', 11),
       (default, 1250, '2023-10-15 18:15:19', '퇴근', 13),
       (default, 12000, '2023-10-15 20:20:32', '병원', 16),
       (default, 1250, '2023-10-16 07:20:32', '출근', 13),
       (default, 10000, '2023-10-16 12:42:12', '점심', 11),
       (default, 1250, '2023-10-16 18:15:19', '퇴근', 13),
       (default, 12000, '2023-10-16 20:20:32', '병원', 16),
       (default, 1250, '2023-10-17 07:20:32', '출근', 13),
       (default, 10000, '2023-10-17 12:42:12', '점심', 11),
       (default, 1250, '2023-10-17 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-18 07:20:32', '출근', 13),
       (default, 10000, '2023-10-18 12:42:12', '점심', 11),
       (default, 1250, '2023-10-18 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-19 07:20:32', '출근', 13),
       (default, 10000, '2023-10-19 12:42:12', '점심', 11),
       (default, 1250, '2023-10-19 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-20 07:20:32', '출근', 13),
       (default, 10000, '2023-10-20 12:42:12', '점심', 11),
       (default, 1250, '2023-10-20 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-21 07:20:32', '출근', 13),
       (default, 10000, '2023-10-21 12:42:12', '점심', 11),
       (default, 1250, '2023-10-21 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-22 07:20:32', '출근', 13),
       (default, 10000, '2023-10-22 12:42:12', '점심', 11),
       (default, 1250, '2023-10-22 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-23 07:20:32', '출근', 13),
       (default, 10000, '2023-10-23 12:42:12', '점심', 11),
       (default, 1250, '2023-10-23 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-24 07:20:32', '출근', 13),
       (default, 10000, '2023-10-24 12:42:12', '점심', 11),
       (default, 1250, '2023-10-24 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-25 07:20:32', '출근', 13),
       (default, 10000, '2023-10-25 12:42:12', '점심', 11),
       (default, 1250, '2023-10-25 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-26 07:20:32', '출근', 13),
       (default, 10000, '2023-10-26 12:42:12', '점심', 11),
       (default, 1250, '2023-10-26 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-27 07:20:32', '출근', 13),
       (default, 10000, '2023-10-27 12:42:12', '점심', 11),
       (default, 1250, '2023-10-27 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-28 07:20:32', '출근', 13),
       (default, 10000, '2023-10-28 12:42:12', '점심', 11),
       (default, 1250, '2023-10-28 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-29 07:20:32', '출근', 13),
       (default, 10000, '2023-10-29 12:42:12', '점심', 11),
       (default, 1250, '2023-10-29 18:15:19', '퇴근', 13),
       (default, 1250, '2023-10-30 07:20:32', '출근', 13),
       (default, 10000, '2023-10-30 12:42:12', '점심', 11),
       (default, 1250, '2023-10-30 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-1 07:32:32', '출근 교통비', 3),
       (default, 10000, '2023-11-1 12:30:12', '점심: 제육볶음', 1),
       (default, 1250, '2023-11-1 18:05:15', '퇴근: 강남역', 3),
       (default, 53000, '2023-11-1 18:42:20', '후드티', 2),
       (default, 15000, '2023-11-1 19:11:42', '닭갈비', 1),
       (default, 12000, '2023-11-1 19:11:42', '아메리카노, 치즈케이크', 4),
       (default, 1250, '2023-11-1 20:25:19', '집으로', 3),
       (default, 1250, '2023-11-2 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-2 12:30:12', '점심: 된장찌개', 1),
       (default, 1250, '2023-11-2 18:25:19', '집으로', 3),
       (default, 1250, '2023-11-3 07:25:32', '출근 교통비', 3),
       (default, 10000, '2023-11-3 12:40:12', '점심: 보리밥', 1),
       (default, 1250, '2023-11-3 18:25:19', '집으로', 3),
       (default, 17000, '2023-11-3 21:09:19', '야식: 치킨', 1),
       (default, 22570, '2023-11-4 11:15:20', '장보기', 1),
       (default, 1250, '2023-11-6 07:15:32', '출근 교통비', 3),
       (default, 10000, '2023-11-6 12:42:12', '점심: 카레', 1),
       (default, 1250, '2023-11-6 18:23:19', '집으로', 3),
       (default, 1250, '2023-11-7 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-7 12:42:12', '점심: 카레', 1),
       (default, 1250, '2023-11-7 18:15:19', '집으로', 3),
       (default, 1250, '2023-11-8 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-8 12:42:12', '점심', 1),
       (default, 1250, '2023-11-8 18:15:19', '집으로', 3),
       (default, 1250, '2023-11-9 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-9 12:42:12', '점심', 1),
       (default, 1250, '2023-11-9 18:15:19', '집으로', 3),
       (default, 1250, '2023-11-10 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-10 12:42:12', '점심', 1),
       (default, 1250, '2023-11-10 18:15:19', '집으로', 3),
       (default, 1250, '2023-11-11 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-11 12:42:12', '점심', 1),
       (default, 1250, '2023-11-11 18:15:19', '집으로', 3),
       (default, 1250, '2023-11-12 07:20:32', '집에서 왕십리로', 3),
       (default, 12000, '2023-11-12 12:42:12', '점심: 중식', 1),
       (default, 5000, '2023-11-12 13:10:12', '카페', 4),
       (default, 99000, '2023-11-12 14:42:12', '신발', 2),
       (default, 22000, '2023-11-12 18:22:12', '고기', 1),
       (default, 1250, '2023-11-12 20:10:19', '집으로', 3),
       (default, 1250, '2023-11-13 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-13 12:42:12', '점심', 1),
       (default, 1250, '2023-11-13 18:15:19', '집으로', 3),
       (default, 1250, '2023-11-14 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-14 12:42:12', '점심', 1),
       (default, 1250, '2023-11-14 18:15:19', '집으로', 3),
       (default, 1250, '2023-11-15 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-15 12:42:12', '점심', 1),
       (default, 1250, '2023-11-15 18:15:19', '집으로', 3),
       (default, 1250, '2023-11-16 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-16 12:42:12', '점심', 1),
       (default, 1250, '2023-11-16 18:15:19', '집으로', 3),
       (default, 1250, '2023-11-17 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-17 12:42:12', '점심', 1),
       (default, 1250, '2023-11-17 18:15:19', '집으로', 3),
       (default, 1250, '2023-11-18 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-18 12:42:12', '점심', 1),
       (default, 1250, '2023-11-18 18:15:19', '집으로', 3),
       (default, 32000, '2023-11-19 11:10:12', '장보기', 1),
       (default, 1250, '2023-11-21 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-21 12:42:12', '점심', 1),
       (default, 1250, '2023-11-21 18:15:19', '집으로', 3),
       (default, 1250, '2023-11-22 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-22 12:42:12', '점심', 1),
       (default, 1250, '2023-11-22 18:15:19', '집으로', 3),
       (default, 1250, '2023-11-23 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-23 12:42:12', '점심', 1),
       (default, 1250, '2023-11-23 18:15:19', '집으로', 3),
       (default, 1250, '2023-11-24 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-24 12:42:12', '점심', 1),
       (default, 1250, '2023-11-24 18:15:19', '집으로', 3),
       (default, 1250, '2023-11-25 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-25 12:42:12', '점심', 1),
       (default, 1250, '2023-11-25 18:15:19', '집으로', 3),
       (default, 1250, '2023-11-26 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-26 12:42:12', '점심', 1),
       (default, 1250, '2023-11-26 18:15:19', '집으로', 3),
       (default, 1250, '2023-11-27 11:20:32', '친구 결혼식장', 3),
       (default, 200000, '2023-11-27 12:42:12', '축의금', 7),
       (default, 5000, '2023-11-27 12:42:12', '카페', 4),
       (default, 1250, '2023-11-27 18:15:19', '집으로', 3),
       (default, 1250, '2023-11-28 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-28 12:42:12', '점심', 1),
       (default, 1250, '2023-11-28 18:15:19', '집으로', 3),
       (default, 1250, '2023-11-29 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-29 12:42:12', '점심', 1),
       (default, 1250, '2023-11-29 18:15:19', '집으로', 3),
       (default, 1250, '2023-11-30 07:20:32', '출근 교통비', 3),
       (default, 10000, '2023-11-30 12:42:12', '점심', 1),
       (default, 1250, '2023-11-30 18:15:19', '집으로', 3),
       (default, 1250, '2023-11-1 07:20:32', '출근', 13),
       (default, 10000, '2023-11-1 12:42:12', '점심', 11),
       (default, 1250, '2023-11-1 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-2 07:20:32', '출근', 13),
       (default, 10000, '2023-11-2 12:42:12', '점심', 11),
       (default, 1250, '2023-11-2 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-3 07:20:32', '출근', 13),
       (default, 10000, '2023-11-3 12:42:12', '점심', 11),
       (default, 1250, '2023-11-3 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-4 07:20:32', '출근', 13),
       (default, 10000, '2023-11-4 12:42:12', '점심', 11),
       (default, 1250, '2023-11-4 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-5 07:20:32', '출근', 13),
       (default, 10000, '2023-11-5 12:42:12', '점심', 11),
       (default, 1250, '2023-11-5 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-6 07:20:32', '출근', 13),
       (default, 10000, '2023-11-6 12:42:12', '점심', 11),
       (default, 1250, '2023-11-6 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-7 07:20:32', '출근', 13),
       (default, 10000, '2023-11-7 12:42:12', '점심', 11),
       (default, 1250, '2023-11-7 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-8 07:20:32', '출근', 13),
       (default, 10000, '2023-11-8 12:42:12', '점심', 11),
       (default, 1250, '2023-11-8 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-9 07:20:32', '출근', 13),
       (default, 10000, '2023-11-9 12:42:12', '점심', 11),
       (default, 1250, '2023-11-9 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-10 07:20:32', '출근', 13),
       (default, 10000, '2023-11-10 12:42:12', '점심', 11),
       (default, 1250, '2023-11-10 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-11 07:20:32', '출근', 13),
       (default, 10000, '2023-11-11 12:42:12', '점심', 11),
       (default, 1250, '2023-11-11 18:15:19', '퇴근', 13),
       (default, 500000, '2023-11-11 21:15:19', '퇴근', 4),
       (default, 1250, '2023-11-12 07:20:32', '출근', 13),
       (default, 10000, '2023-11-12 12:42:12', '점심', 11),
       (default, 1250, '2023-11-12 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-13 07:20:32', '출근', 13),
       (default, 10000, '2023-11-13 12:42:12', '점심', 11),
       (default, 1250, '2023-11-13 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-14 07:20:32', '출근', 13),
       (default, 10000, '2023-11-14 12:42:12', '점심', 11),
       (default, 1250, '2023-11-14 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-15 07:20:32', '출근', 13),
       (default, 10000, '2023-11-15 12:42:12', '점심', 11),
       (default, 1250, '2023-11-15 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-16 07:20:32', '출근', 13),
       (default, 10000, '2023-11-16 12:42:12', '점심', 11),
       (default, 1250, '2023-11-16 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-17 07:20:32', '출근', 13),
       (default, 10000, '2023-11-17 12:42:12', '점심', 11),
       (default, 1250, '2023-11-17 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-18 07:20:32', '출근', 13),
       (default, 10000, '2023-11-18 12:42:12', '점심', 11),
       (default, 1250, '2023-11-18 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-19 07:20:32', '출근', 13),
       (default, 10000, '2023-11-19 12:42:12', '점심', 11),
       (default, 1250, '2023-11-19 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-20 07:20:32', '출근', 13),
       (default, 10000, '2023-11-20 12:42:12', '점심', 11),
       (default, 1250, '2023-11-20 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-21 07:20:32', '출근', 13),
       (default, 10000, '2023-11-21 12:42:12', '점심', 11),
       (default, 1250, '2023-11-21 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-22 07:20:32', '출근', 13),
       (default, 10000, '2023-11-22 12:42:12', '점심', 11),
       (default, 1250, '2023-11-22 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-23 07:20:32', '출근', 13),
       (default, 10000, '2023-11-23 12:42:12', '점심', 11),
       (default, 1250, '2023-11-23 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-24 07:20:32', '출근', 13),
       (default, 10000, '2023-11-24 12:42:12', '점심', 11),
       (default, 1250, '2023-11-24 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-25 07:20:32', '출근', 13),
       (default, 10000, '2023-11-25 12:42:12', '점심', 11),
       (default, 1250, '2023-11-25 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-26 07:20:32', '출근', 13),
       (default, 10000, '2023-11-26 12:42:12', '점심', 11),
       (default, 1250, '2023-11-26 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-27 07:20:32', '출근', 13),
       (default, 10000, '2023-11-27 12:42:12', '점심', 11),
       (default, 1250, '2023-11-27 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-28 07:20:32', '출근', 13),
       (default, 10000, '2023-11-28 12:42:12', '점심', 11),
       (default, 1250, '2023-11-28 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-29 07:20:32', '출근', 13),
       (default, 10000, '2023-11-29 12:42:12', '점심', 11),
       (default, 1250, '2023-11-29 18:15:19', '퇴근', 13),
       (default, 1250, '2023-11-30 07:20:32', '출근', 13),
       (default, 10000, '2023-11-30 12:42:12', '점심', 11),
       (default, 1250, '2023-11-30 18:15:19', '퇴근', 13);


INSERT
INTO notification_url(notification_url_id, discord_url, member_id)
VALUES (default,
        '/1174610833930715186/nmvSqHXrK2U6VRcfUTcMHAbLqnvU66BClUjatCOag-szUV2LIF-IR6XwQMw6quQg3x6S',
        1),
       (default,
        '/1177551768641601607/qW0plHZ5247igdQ5_kpOAt6Ty7aHBgzKOLmPh-lpIkvSiEnT9dfa1oN3JA6mo6MYiU0P',
        2);