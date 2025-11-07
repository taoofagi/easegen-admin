<p align="center">
  <a href="./README.md">English</a> |
  <a href="./README_cn.md">简体中文</a> 
</p>

我 全栈工程师，AGI时代超级个体践行者，希望通过开源为AI发展做贡献。

如果这个项目让你有所收获，记得 Star 关注哦，这对我是非常不错的鼓励与支持。

## 🐶 新手必读

* 演示地址【Vue3 + element-plus】：<http://1.95.87.0:48083> 请自行注册试用
* 启动文档、操作手册：请加入飞书群获取
![飞书](.image%2Fdigitalcourse%2Ffeishu.png)
* 项目合作&技术交流加微信，备注easegen：
![微信](.image%2Fdigitalcourse%2Fwechat.png)
## 🐯 平台简介

**easegen**，开源数字人课程制作平台。

* 前端基于 [yudao-ui-admin-vue3](https://gitee.com/yudaocode/yudao-ui-admin-vue3) 实现
* 后端基于 [ruoyi-vue-pro](https://gitee.com/zhijiantianya/ruoyi-vue-pro) 实现
* 智能课件基于[文多多AIPPT](https://easegen.docmee.cn)实现

* 课程制作页

![digitalhuman_course.png](.image%2Fdigitalcourse%2Fdigitalhuman_course.gif)

* 智能课件

![aippt.png](.image%2Fdigitalcourse%2Faippt.gif)
* 智能出题

![ai_gen_test.png](.image%2Fdigitalcourse%2Fai_gen_test.gif)

*  [样课展示](https://www.bilibili.com/video/av113088116297160/)

[![B站视频](.image%2Fdigitalcourse%2Fdemo_course.png)](https://www.bilibili.com/video/av113088116297160/)

## ✨ 核心功能

### 2D/3D双平台数字人视频合成

**easegen** 现已支持2D和3D数字人视频合成双平台，提供更加多样化的内容创作选择：

#### 2D数字人（Easegen平台）
- 传统2D数字人视频合成
- 处理速度快
- 适合批量生产场景

#### 3D数字人（魔珐星云平台）
- 高品质3D数字人视频生成
- 更加逼真的数字人形象
- 支持自定义数字人形象、声音、演播室
- 两种合成方式：
  - **Segment模式**：基于文本片段+背景图片创建视频
  - **PPT模式**：自动解析PPT文件生成课程视频
- 完整的工作流支持：
  - 创建渲染任务
  - 实时状态查询
  - 任务取消
  - 自动视频下载存储

#### 技术实现
- **策略模式**：优雅支持多平台扩展
- **统一接口**：无缝切换2D/3D模式
- **定时任务**：自动轮询任务状态
- **OSS集成**：自动视频文件上传管理

**配置说明**：只需配置App ID和App Secret即可启用3D数字人功能

## 🗺️ 开发路线图

以下是我们计划在未来实现的主要功能和改进：
- [x] 支持课程模板
- [x] 支持讲稿智能生成
- [x] 支持数字人形象声音定制
- [x] 支持docker快速部署
- [x] 支持AI生成口播稿
- [x] **2D/3D双平台数字人视频合成**
- [x] **魔珐星云3D数字人集成**
- [ ] 声音支持SSML语法
- [ ] 增加教案生成
- [ ] 增加教案转课件，生成可控PPT
- [ ] 支持实时数字人讲课功能
- [ ] 增加智能助理
## 架构图

![easegen_diagram.png](.image%2Fdigitalcourse%2Feasegen_diagram.png)

## 技术栈

参考 [yudao-framework](https://gitee.com/zhijiantianya/ruoyi-vue-pro)
基于master-jdk17 分支开发

## 部署手册
https://ozij45g3ts.feishu.cn/docx/EgS3dm1HtoKOPkxReEQcn3MCncg

## 🔥 前端代码


① easegen-front：<https://github.com/taoofagi/easegen-front>


### 若依其他系统功能
请参考[ruoyi-vue-pro](https://gitee.com/zhijiantianya/ruoyi-vue-pro#-%E5%86%85%E7%BD%AE%E5%8A%9F%E8%83%BD)

## 🌟 Star History

[![Star History Chart](https://api.star-history.com/svg?repos=taoofagi/easegen-admin&type=Date)](https://star-history.com/#taoofagi/easegen-admin&Date)

## 🤝 获奖信息
1. [2024年华彩杯算力大赛总决赛二等奖，项目编号L01610474065](https://mp.weixin.qq.com/s/SE10-cxLVurf0BfAMaegmw)]

## 🧾 免责声明/许可

1. `代码`：`easegen-admin` 的代码采用 `Apache` 许可证发布，学术用途和商业用途都可以。
2. `AIGC`：本项目旨在积极影响基于人工智能的文字、语音、视频生成领域。用户被授予使用此工具创建文字、语音、视频的自由，但他们应该遵守当地法律，并负责任地使用。开发人员不对用户可能的不当使用承担任何责任。


