package com.nailong.websdk.pojo;

import com.nailong.websdk.Pb;
import lombok.Data;

import java.util.List;

@Data
public class HotfixPatchList {
    public List<PatchListFile> diff;

    @Data
    public static class PatchListFile {
        private String fileName;
        private String hash;
        private long version;
        private String additionalPath;
    }

    // Proto
    public Pb.ClientDiff toProto() {
        var proto = Pb.ClientDiff.newInstance();

        for (var file : diff) {
            var diff = Pb.FileDiff.newInstance()
                    .setFileName(file.getFileName())
                    .setHash(file.getHash())
                    .setVersion(file.getVersion())
                    .setAdditionalPath(file.getAdditionalPath());

            proto.addDiff(diff);
        }

        return proto;
    }
}
