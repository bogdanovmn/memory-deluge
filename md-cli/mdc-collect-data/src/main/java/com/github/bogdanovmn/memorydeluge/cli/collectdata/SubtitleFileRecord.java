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
    int interval;

    boolean isMusic() {
        return text.contains("♪");
    }

    boolean isSound() {
        return text.matches("^[\\[(].*[\\])]$");
    }

    boolean isText() {
        return !isMusic() && !isSound();
    }

    int duration() {
        return end.toSecondOfDay() - begin.toSecondOfDay();
    }
}
