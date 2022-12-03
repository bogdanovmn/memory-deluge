package com.github.bogdanovmn.memorydeluge.cli.collectdata;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubtitleFileTest {
    @Test
    void records() throws IOException {
        List<SubtitleFileRecord> records = new SubtitleFile("src/test/resources/subtitles-1.srt").records();
        assertEquals(7, records.size());

        assertFalse(records.get(0).isMusic());
        assertEquals(1784, records.get(1).begin().toSecondOfDay());
        assertTrue(records.get(3).isMusic());
        assertEquals(3, records.get(4).interval());
        assertEquals(4, records.get(4).duration());
        assertEquals(
            "Some phrase after empty string",
            records.get(5).text()
        );
        assertTrue(records.get(0).isSound());
        assertTrue(records.get(6).isSound());
    }
}