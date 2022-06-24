package com.github.bogdanovmn.memorydeluge.cli.collectdata;

import com.github.bogdanovmn.common.concurrent.ConcurrentConsuming;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
class VideoFootage {
    private final String videoFileName;
    private final VlcClient vlcClient;
    private final SubtitleFile subtitleFile;
    private final int threadPoolSize;
    private final boolean dryRun;

    void save() throws Exception {
        new ConcurrentConsuming(threadPoolSize)
            .consume(subtitleFile.textRecords(), this::makeScreenshot);
    }

    private void makeScreenshot(SubtitleFileRecord record) {
        int screenshotTimeOffset = record.begin().toSecondOfDay() + (
            record.duration() < 2
                ? 0
                : record.duration() / 2
        );
        log.debug("make a screenshot at {} second for {}", screenshotTimeOffset, record);
        boolean result = dryRun || vlcClient.makeScreenshot(
            videoFileName, screenshotTimeOffset, record.id()
        );
        if (!result) {
            throw new RuntimeException(
                "Error during a screenshot making: " + record
            );
        }
    }
}
