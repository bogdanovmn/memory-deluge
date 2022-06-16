package com.github.bogdanovmn.memorydeluge.cli.collectdata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
class VideoFootage {
    private final VlcClient vlcClient;
    private final SubtitleFile subtitleFile;

    void save(String location) throws IOException {
        for (SubtitleFileRecord record : subtitleFile.records()) {
            log.info(record.toString());
        }


    }
}
