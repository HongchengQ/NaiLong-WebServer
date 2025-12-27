package com.nailong.websdk.controller;

import com.nailong.websdk.Pb;
import com.nailong.websdk.utils.AeadHelper;
import com.nailong.websdk.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin", method = {RequestMethod.GET, RequestMethod.POST})
@RequiredArgsConstructor
public class AdminController {

    @RequestMapping(path = "/decode_server_list")
    public Object decodeServerList(@RequestParam String region, @RequestBody byte[] body) throws Exception {
        IO.println(Utils.bytesToHex(body));
        byte[] serverListBytes = AeadHelper.decryptCBC(body, region);

        return Pb.ServerListMeta.parseFrom(serverListBytes).toString();
    }
}
