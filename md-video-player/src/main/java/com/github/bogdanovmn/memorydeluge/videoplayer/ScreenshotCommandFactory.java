package com.github.bogdanovmn.memorydeluge.videoplayer;


import lombok.Builder;

import java.nio.file.Path;

@Builder
public class ScreenshotCommandFactory {
    private final Path playerPath;
    private final Path outputDir;
    private final ScreenshotCommand.OutputFormat format;

    public ScreenshotCommand of(VideoPlayer player) {
        switch (player) {
            case VCL:    return new VlcScreenshotCommand(playerPath, outputDir, format);
            case FFMPEG: return new FfmpegScreenshotCommand(playerPath, outputDir, format);
            default:     throw new IllegalArgumentException("Unsupported video player type: " + player);
        }
    }
}
