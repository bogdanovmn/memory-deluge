package com.github.bogdanovmn.memorydeluge.cli.collectdata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
class VlcClient {
    private final String vlcPath;
    private final String outputDir;

    boolean makeScreenshot(String videoFileName, int offsetInSeconds, int subtitleId) {
        String outputFilePrefix = String.format("snap_%d_", subtitleId);
        String currentCommand = String.format(
            "%sq %s -I rc --rate=1 --video-filter=scene --vout=dummy --start-time=%d --stop-time=%d --scene-format=png --scene-ratio=24 --scene-prefix=%s --scene-path=%s vlc://quit",
                vlcPath, videoFileName, offsetInSeconds, offsetInSeconds + 1, outputFilePrefix, outputDir
        );
        try {
            Process process = Runtime.getRuntime().exec(currentCommand);
            boolean commandIsOk = process.waitFor(5, TimeUnit.SECONDS) && process.exitValue() == 0;
            if (commandIsOk) {
                String garbageFileName = String.format("%s/%s00025.png", outputDir, outputFilePrefix);
                currentCommand = String.format("[[ -e %s ]] && rm %s", garbageFileName, garbageFileName);
                process = Runtime.getRuntime().exec(currentCommand);
                if (!process.waitFor(3, TimeUnit.SECONDS) || process.exitValue() != 0) {
                    log.warn("Can't delete file: {}", currentCommand);
                }
            }
            return commandIsOk;
        } catch (Exception e) {
            log.error("Run command error: {}\nCommand: {}", e.getMessage(), currentCommand);
            return false;
        }
    }

}
