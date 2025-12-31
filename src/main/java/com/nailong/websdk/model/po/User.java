package com.nailong.websdk.model.po;

import org.babyfish.jimmer.sql.*;
import org.jspecify.annotations.Nullable;

/*
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `open_id` varchar(32) NOT NULL,
  `password` varchar(32) DEFAULT NULL,
  `nick_name` varchar(32) DEFAULT NULL,
  `login_token` varchar(88) DEFAULT NULL,
  `created_at` int DEFAULT NULL,
  PRIMARY KEY (`id` DESC,`open_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
 */

@Entity
public interface User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    /* 账号 - 手机号码(cn)或邮箱(os) */
    @Key
    String openId();

    @Nullable
    String password();

    @Key
    @Nullable
    String nickName();

    @Nullable
    String loginToken();

    @Nullable
    Long createdTime();
}
