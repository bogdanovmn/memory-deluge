package com.github.bogdanovmn.memorydeluge.cli.collectdata;

import com.github.bogdanovmn.common.concurrent.ConcurrentConsuming;
import com.github.bogdanovmn.memorydeluge.videoplayer.ScreenshotCommand;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;

@Slf4j
@Builder
class VideoFootage {
    private final Path videoFileName;
    private final ScreenshotCommand screenshotCommand;
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
        if (log.isDebugEnabled() || dryRun) {
            log.info("make a screenshot at {} second for {}", screenshotTimeOffset, record);
        }
        boolean result = dryRun || screenshotCommand.execute(
            videoFileName, screenshotTimeOffset, String.valueOf(record.id())
        );
        if (!result) {
            throw new RuntimeException(
                "Error during a screenshot making: " + record
            );
        }
    }
}
