-- =============================================
-- 魔珐星云3D数字人API配置 - 系统配置表数据
-- =============================================
-- 使用说明：
-- 1. 将 'YOUR_APP_ID_HERE' 替换为魔珐星云提供的实际 APP ID
-- 2. 将 'YOUR_APP_SECRET_HERE' 替换为魔珐星云提供的实际 APP SECRET
-- 3. 网关地址如需修改，替换默认值即可（通常不需要修改）
-- 4. 执行此SQL文件，将配置插入到 infra_config 表中
-- =============================================

-- 1. 魔珐星云 APP ID 配置
INSERT INTO `infra_config` (
    `category`,
    `name`,
    `config_key`,
    `value`,
    `type`,
    `visible`,
    `remark`,
    `creator`,
    `create_time`,
    `updater`,
    `update_time`,
    `deleted`
) VALUES (
    '数字人',                                      -- category: 参数分类
    '魔珐星云APP_ID',                              -- name: 参数名称
    'xingyun3d.app.id',                           -- config_key: 参数键名
    'YOUR_APP_ID_HERE',                           -- value: 参数键值（需要替换为实际值）
    2,                                            -- type: 2-自定义配置
    0,                                            -- visible: 0-不可见（敏感信息）
    '魔珐星云3D数字人API的应用访问密钥ID',           -- remark: 备注
    '',                                           -- creator: 创建者
    NOW(),                                        -- create_time: 创建时间
    '',                                           -- updater: 更新者
    NOW(),                                        -- update_time: 更新时间
    0                                             -- deleted: 0-未删除
);

-- 2. 魔珐星云 APP SECRET 配置
INSERT INTO `infra_config` (
    `category`,
    `name`,
    `config_key`,
    `value`,
    `type`,
    `visible`,
    `remark`,
    `creator`,
    `create_time`,
    `updater`,
    `update_time`,
    `deleted`
) VALUES (
    '数字人',                                      -- category: 参数分类
    '魔珐星云APP_SECRET',                          -- name: 参数名称
    'xingyun3d.app.secret',                       -- config_key: 参数键名
    'YOUR_APP_SECRET_HERE',                       -- value: 参数键值（需要替换为实际值）
    2,                                            -- type: 2-自定义配置
    0,                                            -- visible: 0-不可见（敏感信息）
    '魔珐星云3D数字人API的应用访问密钥SECRET，用于签名计算',  -- remark: 备注
    '',                                           -- creator: 创建者
    NOW(),                                        -- create_time: 创建时间
    '',                                           -- updater: 更新者
    NOW(),                                        -- update_time: 更新时间
    0                                             -- deleted: 0-未删除
);

-- 3. 魔珐星云网关地址配置（可选，有默认值）
INSERT INTO `infra_config` (
    `category`,
    `name`,
    `config_key`,
    `value`,
    `type`,
    `visible`,
    `remark`,
    `creator`,
    `create_time`,
    `updater`,
    `update_time`,
    `deleted`
) VALUES (
    '数字人',                                      -- category: 参数分类
    '魔珐星云网关地址',                             -- name: 参数名称
    'xingyun3d.gateway.server',                   -- config_key: 参数键名
    'https://nebula-agent.xingyun3d.com',         -- value: 参数键值（默认值，通常不需要修改）
    2,                                            -- type: 2-自定义配置
    1,                                            -- visible: 1-可见（非敏感信息）
    '魔珐星云3D数字人API的网关服务器地址，默认为官方地址，通常不需要修改',  -- remark: 备注
    '',                                           -- creator: 创建者
    NOW(),                                        -- create_time: 创建时间
    '',                                           -- updater: 更新者
    NOW(),                                        -- update_time: 更新时间
    0                                             -- deleted: 0-未删除
);

-- =============================================
-- 验证配置是否插入成功
-- =============================================
-- 执行以下查询验证配置：
-- SELECT * FROM `infra_config` WHERE `config_key` LIKE 'xingyun3d.%';
-- =============================================
