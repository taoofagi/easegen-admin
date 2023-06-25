CREATE TABLE IF NOT EXISTS "market_activity" (
    "id" bigint(20) NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    "title" varchar(50) NOT NULL,
    "activity_type" tinyint(4) NOT NULL,
    "status" tinyint(4) NOT NULL,
    "start_time" datetime NOT NULL,
    "end_time" datetime NOT NULL,
    "invalid_time" datetime,
    "delete_time" datetime,
    "time_limited_discount" varchar(2000),
    "full_privilege" varchar(2000),
    "creator" varchar(64) DEFAULT '',
    "create_time" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updater" varchar(64) DEFAULT '',
    "update_time" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    "deleted" bit NOT NULL DEFAULT FALSE,
    "tenant_id" bigint(20) NOT NULL,
    PRIMARY KEY ("id")
    ) COMMENT '促销活动';

CREATE TABLE IF NOT EXISTS "promotion_coupon_template" (
   "id" bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
   "name" varchar NOT NULL,
   "status" int NOT NULL,
   "total_count" int NOT NULL,
   "take_limit_count" int NOT NULL,
   "take_type" int NOT NULL,
   "use_price" int NOT NULL,
   "product_scope" int NOT NULL,
   "product_spu_ids" varchar,
   "validity_type" int NOT NULL,
   "valid_start_time" datetime,
   "valid_end_time" datetime,
   "fixed_start_term" int,
   "fixed_end_term" int,
   "discount_type" int NOT NULL,
   "discount_percent" int,
   "discount_price" int,
   "discount_limit_price" int,
   "take_count" int NOT NULL DEFAULT 0,
   "use_count" int NOT NULL DEFAULT 0,
   "creator" varchar DEFAULT '',
   "create_time" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
   "updater" varchar DEFAULT '',
   "update_time" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   "deleted" bit NOT NULL DEFAULT FALSE,
   PRIMARY KEY ("id")
) COMMENT '优惠劵模板';

CREATE TABLE IF NOT EXISTS "promotion_coupon" (
    "id" bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    "template_id" bigint NOT NULL,
    "name" varchar NOT NULL,
    "status" int NOT NULL,
    "user_id" bigint NOT NULL,
    "take_type" int NOT NULL,
    "useprice" int NOT NULL,
    "valid_start_time" datetime NOT NULL,
    "valid_end_time" datetime NOT NULL,
    "product_scope" int NOT NULL,
    "product_spu_ids" varchar,
    "discount_type" int NOT NULL,
    "discount_percent" int,
    "discount_price" int,
    "discount_limit_price" int,
    "use_order_id" bigint,
    "use_time" datetime,
    "creator" varchar DEFAULT '',
    "create_time" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updater" varchar DEFAULT '',
    "update_time" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    "deleted" bit NOT NULL DEFAULT FALSE,
    PRIMARY KEY ("id")
) COMMENT '优惠劵';

CREATE TABLE IF NOT EXISTS "promotion_reward_activity" (
   "id" bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
   "name" varchar NOT NULL,
   "status" int NOT NULL,
   "start_time" datetime NOT NULL,
   "end_time" datetime NOT NULL,
   "remark" varchar,
   "condition_type" int NOT NULL,
   "product_scope" int NOT NULL,
   "product_spu_ids" varchar,
   "rules" varchar,
   "creator" varchar DEFAULT '',
   "create_time" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
   "updater" varchar DEFAULT '',
   "update_time" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   "deleted" bit NOT NULL DEFAULT FALSE,
   PRIMARY KEY ("id")
) COMMENT '满减送活动';

CREATE TABLE IF NOT EXISTS "promotion_discount_activity" (
     "id" bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
     "name" varchar NOT NULL,
     "status" int NOT NULL,
     "start_time" datetime NOT NULL,
     "end_time" datetime NOT NULL,
     "remark" varchar,
     "creator" varchar DEFAULT '',
     "create_time" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
     "updater" varchar DEFAULT '',
     "update_time" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
     "deleted" bit NOT NULL DEFAULT FALSE,
     PRIMARY KEY ("id")
) COMMENT '限时折扣活动';

-- 将该建表 SQL 语句，添加到 yudao-module-promotion-biz 模块的 test/resources/sql/create_tables.sql 文件里
CREATE TABLE IF NOT EXISTS "promotion_seckill_activity" (
    "id" bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    "spu_id" bigint NOT NULL,
    "name" varchar NOT NULL,
    "status" int NOT NULL,
    "remark" varchar,
    "start_time" varchar NOT NULL,
    "end_time" varchar NOT NULL,
    "sort" int NOT NULL,
    "config_ids" varchar NOT NULL,
    "order_count" int NOT NULL,
    "user_count" int NOT NULL,
    "total_price" int NOT NULL,
    "total_limit_count" int,
    "single_limit_count" int,
    "stock" int,
    "total_stock" int,
    "creator" varchar DEFAULT '',
    "create_time" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updater" varchar DEFAULT '',
    "update_time" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    "deleted" bit NOT NULL DEFAULT FALSE,
    "tenant_id" bigint NOT NULL,
    PRIMARY KEY ("id")
) COMMENT '秒杀活动';

CREATE TABLE IF NOT EXISTS "promotion_seckill_config" (
    "id" bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    "name" varchar NOT NULL,
    "start_time" varchar NOT NULL,
    "end_time" varchar NOT NULL,
    "pic_url" varchar NOT NULL,
    "status" int NOT NULL,
    "creator" varchar DEFAULT '',
    "create_time" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updater" varchar DEFAULT '',
    "update_time" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    "deleted" bit NOT NULL DEFAULT FALSE,
    "tenant_id" bigint NOT NULL,
    PRIMARY KEY ("id")
) COMMENT '秒杀时段配置';
