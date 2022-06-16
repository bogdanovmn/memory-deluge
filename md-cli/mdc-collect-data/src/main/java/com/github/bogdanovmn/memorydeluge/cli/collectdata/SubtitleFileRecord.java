package com.github.bogdanovmn.memorydeluge.cli.collectdata;

import lombok.Builder;
import lombok.Value;

import java.time.LocalTime;

@Value
@Builder
class SubtitleFileRecord {
    int id;
    LocalTime begin;
    LocalTime end;
    String text;

    boolean isMusic() {
        return false;
    }

    boolean isSound() {
        return false;
    }
}
