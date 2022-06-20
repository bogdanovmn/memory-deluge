package com.github.bogdanovmn.memorydeluge.cli.collectdata;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Builder
@RequiredArgsConstructor
@Slf4j
class VideoFootage {
    private final String videoFileName;
    private final VlcCommand vlcCommand;
    private final SubtitleFile subtitleFile;
    private final int threadPoolSize;

    private volatile boolean hasError;
    private AtomicInteger processed = new AtomicInteger(0);

    void save() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
        SubtitleFileRecord previous = null;
        int recordsToProcess = 0;
        for (SubtitleFileRecord record : subtitleFile.records()) {
            recordsToProcess++;
            if (!record.isMusic() && !record.isSound()) {
                if (hasError) {
                    break;
                }
                log.info("interval: {} duration: {} --> {}",
                    previous == null
                        ? 0
                        : record.begin().toSecondOfDay() - previous.end().toSecondOfDay(),
                    record.duration(),
                    record
                );
                executorService.submit(() -> makeScreenshot(record));
                previous = record;
            }
        }
        executorService.shutdown();
        while (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
            log.info("Record to process left: {}", recordsToProcess - processed.get());
        }

    }

    private void makeScreenshot(SubtitleFileRecord record) {
        int screenshotTimeOffset = record.begin().toSecondOfDay() + (
            record.duration() < 2
                ? 0
                : record.duration() / 2
        );
        log.debug("make a screenshot at {} second", screenshotTimeOffset);
        boolean result = vlcCommand.makeScreenshot(
            videoFileName, screenshotTimeOffset, record.id()
        );
        if (!result) {
            log.error("Error during a screenshot making: {}", record);
            hasError = true;
        }
        processed.incrementAndGet();
    }
}
