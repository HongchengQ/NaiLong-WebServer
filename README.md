# NaiLong-WEB-SDK

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