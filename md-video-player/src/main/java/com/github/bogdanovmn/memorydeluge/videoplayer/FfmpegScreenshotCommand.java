package com.github.bogdanovmn.memorydeluge.videoplayer;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;


@Builder
@Slf4j
class FfmpegScreenshotCommand implements ScreenshotCommand {
    private final Path ffmpegPath;
    private final Path outputDir;
    private final OutputFormat format;

    /**
     * Example: "ffmpeg -ss 00:44:27 -i /path/to/file.mkv -vframes 1 output.png",
     */
    @Override
    public boolean execute(Path videoFile, long timestamp, String outputName) {
        String outputFileName = String.format("%s.%s", outputName, format);
        String command = String.format(
            "%s -ss %s -i %s -vframes 1 %s",
                ffmpegPath,
                timestamp,
                videoFile,
                outputDir.resolve(outputFileName)
        );
        return SystemCommand.builder()
            .command(command)
            .maxExecutionTimeInSeconds(5)
        .build().execute();
    }

}
