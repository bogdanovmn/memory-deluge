package com.github.bogdanovmn.memorydeluge.videoplayer;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Builder
@Slf4j
class SystemCommand {
    private final String command;
    private final int maxExecutionTimeInSeconds;

    public boolean execute() {
        try {
            Process process = Runtime.getRuntime().exec(command);
            boolean commandIsOk = process.waitFor(maxExecutionTimeInSeconds, TimeUnit.SECONDS);
            if (!commandIsOk) {
                log.error("Command didn't finish in {} sec. Forced canceling it.", maxExecutionTimeInSeconds);
                process.destroy();
            } else {
                commandIsOk = process.exitValue() == 0;
            }
            if (!commandIsOk) {
                log.error(
                    "Command error: {}\nDetails: {}",
                    command,
                    new BufferedReader(
                        new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8)
                    ).lines()
                        .collect(Collectors.joining("\n"))
                );
            }
            return commandIsOk;
        } catch (Exception e) {
            log.error("Run command error: {}\nCommand: {}", e.getMessage(), command);
            return false;
        }
    }
}
