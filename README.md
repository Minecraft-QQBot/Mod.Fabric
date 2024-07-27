# McdReforged

与 [BotServer](https://github.com/Minecraft-QQBot/BotServer) 进行对接的 Mcdr 插件。

你可以到 [Releases](https://github.com/Minecraft-QQBot/Fabric/releases) 下载最新版本 Fabric 服务器插件。

## 功能

- 与BotServer项目联动

## 安装插件

- 与安装服务器mod一致,请在游戏目录下的config文件夹中创建配置文件qq_bot.json

```json
{
  "name": "服务器名称",
  "url": "ws://x.x.x.x:xxxx",
  "token": "令牌",
}
```

其中各个字段的含义如下：

|        字段名         | 类型  |             含义              |
|:------------------:|:---:|:---------------------------:|
|        url         | 字符串 |       URL，填写服务器的URL。        |
|        name        | 字符串 |        服务器名称，中英文都可。         |
|       token        | 字符串 | 口令，和服务器配置文件下的 TOKEN 保持一致即可。 |

> [!TIP]
> 若插件遇到问题，或有更好的想法，可以加入 QQ 群 [`962802248`](https://qm.qq.com/q/B3kmvJl2xO) 或者提交 Issues 向作者反馈。若你有能力，欢迎为本项目提供代码贡献！