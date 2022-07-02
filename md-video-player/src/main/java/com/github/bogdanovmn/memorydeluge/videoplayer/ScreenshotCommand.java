package com.github.bogdanovmn.memorydeluge.videoplayer;

import java.nio.file.Path;

public interface ScreenshotCommand {
    enum OutputFormat { png, jpg }

    boolean execute(Path videoFile, long timestamp, String outputName);
}
