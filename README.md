
<p align="center">
  <a href="./README.md">English</a> |
  <a href="./README_cn.md">简体中文</a> 
</p>

I am a full-stack engineer, a practitioner of the AGI era super individual, hoping to contribute to AI development through open source.

If this project has helped you, remember to Star and follow, which would be great encouragement and support for me.

## 🐶 Beginner's Guide

* Demo URL 【Vue3 + element-plus】: <http://1.95.87.0:48083> Please register to try it yourself.
* Startup documentation, operation manual: Please join the discard group to get it.
discard:https://discord.gg/q2RK3sEQwW
* Project collaboration & technical exchanges: add WeChat, note easegen:
![WeChat](.image%2Fdigitalcourse%2Fwechat.png)

## 🐯 Platform Introduction

**easegen**, an open-source digital human course creation platform.

* Frontend is implemented based on [yudao-ui-admin-vue3](https://gitee.com/yudaocode/yudao-ui-admin-vue3).
* Backend is implemented based on [ruoyi-vue-pro](https://gitee.com/zhijiantianya/ruoyi-vue-pro).
* Intelligent courseware is realized based on [Wenduoduo AIPPT](https://easegen.docmee.cn).

* Course creation page

![digitalhuman_course.png](.image%2Fdigitalcourse%2Fdigitalhuman_course.gif)

* Intelligent Courseware

![aippt.png](.image%2Fdigitalcourse%2Faippt.gif)
* Intelligent Test Creation

![ai_gen_test.png](.image%2Fdigitalcourse%2Fai_gen_test.gif)

*  [Sample Course](https://www.bilibili.com/video/av113088116297160/)

[![Bilibili Video](.image%2Fdigitalcourse%2Fdemo_course.png)](https://www.bilibili.com/video/av113088116297160/)

## ✨ Key Features

### 2D/3D Dual-Platform Digital Human Video Synthesis

**easegen** now supports both 2D and 3D digital human video synthesis platforms, providing more diverse content creation options:

#### 2D Digital Human (Easegen Platform)
- Traditional 2D digital human video synthesis
- Fast processing speed
- Suitable for mass production scenarios

#### 3D Digital Human (Mofa Xingyun Platform)
- High-quality 3D digital human video generation
- More realistic digital human images
- Supports custom digital human appearance, voice, and studio
- Two synthesis methods:
  - **Segment Mode**: Create videos from text segments with background images
  - **PPT Mode**: Automatically parse PPT files to generate course videos
- Complete workflow support:
  - Create rendering tasks
  - Real-time status query
  - Task cancellation
  - Automatic video download and storage

#### Technical Implementation
- **Strategy Pattern**: Elegant support for multiple platforms
- **Unified Interface**: Switch between 2D/3D modes seamlessly
- **Scheduled Tasks**: Automatic task status polling
- **OSS Integration**: Automatic video file upload and management

**Configuration**: Simple setup with App ID and App Secret to enable 3D digital human functionality

## 🗺️ Development Roadmap

Here are the major features and improvements we plan to implement in the future:
- [x] Support for course templates
- [x] Support for intelligent script generation
- [x] Support for digital human image and voice customization
- [x] Support for docker quick deployment
- [x] Support for AI-generated scriptwriting
- [x] **2D/3D dual-platform digital human video synthesis**
- [x] **Mofa Xingyun 3D digital human integration**
- [ ] SSML syntax support for voice
- [ ] Adding lesson plan generation
- [ ] Convert lesson plans to courseware and generate controllable PPTs
- [ ] Support for real-time digital human teaching
- [ ] Adding an intelligent assistant

## Architecture Diagram

![easegen_diagram.png](.image%2Fdigitalcourse%2Feasegen_diagram.png)

## Technology Stack

Refer to [yudao-framework](https://gitee.com/zhijiantianya/ruoyi-vue-pro)
Developed based on the master-jdk17 branch.

## Deployment Manual
https://ozij45g3ts.feishu.cn/docx/EgS3dm1HtoKOPkxReEQcn3MCncg

## 🔥 Frontend Code

① easegen-front：<https://github.com/taoofagi/easegen-front>

### Additional Features
Refer to [ruoyi-vue-pro](https://gitee.com/zhijiantianya/ruoyi-vue-pro#-%E5%86%85%E7%BD%AE%E5%8A%9F%E8%83%BD)

## 🌟 Star History

[![Star History Chart](https://api.star-history.com/svg?repos=taoofagi/easegen-admin&type=Date)](https://star-history.com/#taoofagi/easegen-admin&Date)

## 🤝 Awards
1. [2024 Huacai Cup Compute Power Competition Finals Second Prize, Project No. L01610474065](https://mp.weixin.qq.com/s/SE10-cxLVurf0BfAMaegmw)]

## 🧾 Disclaimer/License

1. `Code`: The `easegen-admin` code is released under the `Apache` license for academic and commercial use.
2. `AIGC`: This project aims to positively impact the AI-generated text, speech, and video field. Users are granted the freedom to use this tool to create text, speech, and videos, but they should comply with local laws and use it responsibly. Developers bear no responsibility for any misuse of the tool by users.
