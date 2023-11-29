create table budget
(
    budget_id          bigint not null auto_increment,
    amount             bigint,
    month              date,
    member_category_id bigint,
    primary key (budget_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table category
(
    category_id bigint not null auto_increment,
    name        varchar(255) CHARACTER SET utf8mb4,
    primary key (category_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table expense
(
    expense_id         bigint not null auto_increment,
    amount             bigint,
    expense_at         datetime(6),
    memo               varchar(255),
    member_category_id bigint,
    primary key (expense_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table member
(
    `member-id`          bigint       not null auto_increment,
    email                varchar(255) not null,
    evaluation_alarm     bit          not null,
    password             varchar(255) not null,
    recommendation_alarm bit          not null,
    primary key (`member-id`)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table member_category
(
    member_category_id bigint not null auto_increment,
    category_id        bigint,
    member_id          bigint,
    primary key (member_category_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table notification_url
(
    notification_url_id bigint not null auto_increment,
    discord_url         varchar(255),
    member_id           bigint,
    primary key (notification_url_id)
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


alter table category
    add constraint UK_46ccwnsi9409t36lurvtyljak unique (name);

alter table member
    add constraint UK_mbmcqelty0fbrvxp1q58dn57t unique (email);

alter table notification_url
    add constraint UK_4ffysbqqi10040v6dptptu3pf unique (member_id);

alter table budget
    add constraint FK97sbxq00lgdaf2ae6ro0pwmq8
        foreign key (member_category_id)
            references member_category (member_category_id);

alter table expense
    add constraint FKj96fg5153mdk8wekd2b53oyp0
        foreign key (member_category_id)
            references member_category (member_category_id);

alter table member_category
    add constraint FK87r65a4xna6uray30n79f9ar4
        foreign key (category_id)
            references category (category_id);

alter table member_category
    add constraint FK8hm1bokubb1b6412fgb7jd77f
        foreign key (member_id)
            references member (`member-id`);

alter table notification_url
    add constraint FK2s8il1jnbu4i606s3dy4pqc7k
        foreign key (member_id)
            references member (`member-id`);