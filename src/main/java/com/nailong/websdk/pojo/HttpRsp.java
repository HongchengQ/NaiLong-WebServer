package com.nailong.websdk.pojo;

import com.nailong.websdk.exception.CommonException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HttpRsp {
    private int Code;
    private Object Data;
    private String Msg;

    public static HttpRsp ok() {
        return ok("{}");
    }

    public static HttpRsp ok(Object data) {
        return new HttpRsp(200, data, "OK");
    }

    public static HttpRsp error(String msg) {
        return new HttpRsp(500, "{}", msg);
    }

    public static HttpRsp error(int code, String msg) {
        return new HttpRsp(code, "{}", msg);
    }

    public static HttpRsp error(CommonException e) {
        return new HttpRsp(e.getCode(), "{}", e.getMessage());
    }

    public boolean success() {
        return this.Code == 200;
    }
}
