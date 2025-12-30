# 奶龙 WEB-SDK

## 简介
可以用于某三百石头一抽大头小人游戏的 web sdk

## 功能特性
基于目前已知的官方接口的增删改查用户

同时支持一对多所有区服 (需要使用 网关/代理 将客户端流量转发至 sdk，并添加对应区域的请求头；推荐使用nginx)

客户端热更新 - sdk只下发清单(大约2.3Mb) 其余全走官方对象存储

可与 [Nebula](https://github.com/Melledy/Nebula) 搭配使用

## 使用说明
### 配置文件
第一次使用请将 [src/main/resources/application.yaml](src/main/resources/application.yaml) 复制到程序运行目录

在运行目录配置文件不存在时会使用默认的配置文件

### 客户端热更新
也叫热修复，说人话就是启动游戏后让你下载资源的那部分

自带的清单来自于 [xt_hotfix_diff](https://github.com/HongchengQ/xt_hotfix_diff.git)

程序支持运行时接收热更清单文件的变化，只需要在运行目录创建`hot_fix`文件夹 并将类似于上面库包含的文件放入即可

### 一对多区服
因为客户端天生不支持多区服 —— 区服数据是加密的，每个客户端类型使用的密钥都不一样，并且客户端向服务端拉取区服数据时不会附加任何数据；\
所以我们只能使用网关，从客户端访问的网络地址来识别用户使用的客户端

请准备五个 `域名 或 ip + 端口`，按照以下规则为转发的请求设置请求头
```
第1个(域名/端口) -> cn
第2个(域名/端口) -> tw
第3个(域名/端口) -> jp
第4个(域名/端口) -> kr
第5个(域名/端口) -> global
```
`需要添加的请求头 key 是 "X-Region" value 使用对应的规则区域`

这里就不教具体怎么设置了，实在不会就去问 ai

### 与 Nebula 搭配使用
因为本项目数据库使用 mysql，Nebula 使用 mongodb\
如果要适配 Nebula 需要加入以下代码
- `src/main/java/emu/nebula/command/commands/DbAccountCommand.java`
```java
package emu.nebula.command.commands;

import emu.nebula.Nebula;
import emu.nebula.command.Command;
import emu.nebula.command.CommandArgs;
import emu.nebula.command.CommandHandler;
import emu.nebula.game.account.Account;
import emu.nebula.util.JsonUtils;
import emu.nebula.util.Utils;

import java.nio.charset.StandardCharsets;

@Command(label = "dbaccount", permission = "admin.account", desc = "dbaccount {hex str} - 接受传来的 account 对象(JSON)，将其保存在 mongodb 数据库中")
public class DbAccountCommand implements CommandHandler {
    @Override
    public String execute(CommandArgs args) {
        if (args.size() > 1) {
            return "Invalid amount of args";
        }

        byte[] accountBytes = Utils.hexToBytes(args.get(0));
        String accountHexStr = new String(accountBytes, StandardCharsets.UTF_8);

        Account account = JsonUtils.decode(accountHexStr, Account.class);

        Nebula.getLogger().info("开始保存命令传来的 account 对象 - {}", account.getEmail());

        account.save();

        return "成功保存 account 对象 - " + account.getEmail();
    }
}
```
SDK 在操作自身数据库时会向 Nebula command server 发送指令以保存经过转换的对象


## 运行与编译
通过源码构建：
-  Java 25
-  maven

```bash
# 运行应用
mvn spring-boot:run
```

```bash
# 构建 jar 文件
mvn package
```