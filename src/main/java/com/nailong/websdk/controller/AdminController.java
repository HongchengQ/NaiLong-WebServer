package com.nailong.websdk.controller;

import com.nailong.websdk.domain.dto.UserInput;
import com.nailong.websdk.domain.po.User;
import com.nailong.websdk.domain.po.UserDraft;
import com.nailong.websdk.proto.Pb;
import com.nailong.websdk.utils.AeadHelper;
import com.nailong.websdk.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin", method = {RequestMethod.GET, RequestMethod.POST})
@RequiredArgsConstructor
public class AdminController {

    private final JSqlClient sqlClient;

    @RequestMapping(path = "/decode_server_list")
    public Object decodeServerList(@RequestParam String region, @RequestBody byte[] body) throws Exception {
        IO.println(Utils.bytesToHex(body));
        byte[] serverListBytes = AeadHelper.decryptCBC(body, region);

        return Pb.ServerListMeta.parseFrom(serverListBytes).toString();
    }
}
