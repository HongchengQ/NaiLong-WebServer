// 独立于根模块 不被编译
// 需要用到时直接运行
// java .\GenerateStressMeasurementData.java

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateStressMeasurementData {
    private static final String[] NICKNAME_PREFIXES = {"user", "player", "tester", "demo", "test"};
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random random = new Random();

    public static void main(String[] args) {
        // 从命令行参数获取要生成的记录数，默认为1000
        int recordCount = 1000;
        if (args.length > 0) {
            try {
                recordCount = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid record count provided, using default: " + recordCount);
            }
        }

        String fileName = "user_data.csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            // 写入CSV头部
            writer.write("\"id\",\"open_id\",\"password\",\"nick_name\",\"login_token\",\"created_time\"\n");
            
            for (int i = 1; i <= recordCount; i++) {
                String openId = generateOpenId();
                String password = generatePassword(); // 可以为null，表示为空值
                String nickName = generateNickName();
                String loginToken = generateLoginToken();
                long createdTime = generateCreatedTime();

                // 根据是否为null来格式化password字段
                String passwordField = (password == null) ? "" : "\"" + password + "\"";
                
                writer.write(String.format("\"%d\",\"%s\",%s,\"%s\",\"%s\",\"%d\"", 
                    i, openId, passwordField, nickName, loginToken, createdTime));
                
                if (i < recordCount) {
                    writer.write("\n");
                }
            }

            System.out.println("Generated " + recordCount + " records in " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // 生成open_id - 模拟手机号码
    private static String generateOpenId() {
        // 生成11位手机号码，以1开头
        StringBuilder sb = new StringBuilder();
        sb.append("1"); // 手机号以1开头
        for (int i = 1; i < 11; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    // 生成password - 随机字符串或null
    private static String generatePassword() {
        // 有一定概率返回null
        if (random.nextBoolean()) {
            return null;
        }
        return generateRandomString(8, 16);
    }

    // 生成nick_name - 随机昵称
    private static String generateNickName() {
        String prefix = NICKNAME_PREFIXES[random.nextInt(NICKNAME_PREFIXES.length)];
        int suffix = random.nextInt(100000); // 0-99999的后缀
        return prefix + suffix;
    }

    // 生成login_token - 40字符的十六进制字符串
    private static String generateLoginToken() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 40; i++) {
            sb.append(Integer.toHexString(random.nextInt(16)));
        }
        return sb.toString();
    }

    // 生成created_time - 随机时间戳（最近5年内的随机时间）
    private static long generateCreatedTime() {
        //时间戳范围 (2021年1月1日至今)
        long startTimestamp = 1609459200L; // 2021-01-01 00:00:00
        long endTimestamp = System.currentTimeMillis() / 1000; // 当前时间戳
        return startTimestamp + (long) (random.nextDouble() * (endTimestamp - startTimestamp));
    }

    // 生成随机字符串
    private static String generateRandomString(int minLength, int maxLength) {
        int length = minLength + random.nextInt(maxLength - minLength + 1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}