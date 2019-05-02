package com.higgschain.trust.consensus.sofajraft.snapshot;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Slf4j
public class SofajraftSnapshotFile {

    private String              path;

    public SofajraftSnapshotFile(String path) {
        super();
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    /**
     * Save value to snapshot file.
     */
    public boolean save(byte[] data) {
        try {
            FileUtils.writeByteArrayToFile(new File(path), data);
            return true;
        } catch (IOException e) {
            log.error("Fail to save snapshot", e);
            return false;
        }
    }

    public byte[] load() throws IOException {
        final byte[] data = FileUtils.readFileToByteArray(new File(path));
        if (data != null) {
            return data;
        }
        throw new IOException("Fail to load snapshot from " + path + ",content: " + data);
    }
}
