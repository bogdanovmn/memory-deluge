package com.github.bogdanovmn.memorydeluge.cli.collectdata;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubtitleFile {
    private final String fileName;

    private static final DateTimeFormatter TIME_FRAME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
    private static final Pattern TIME_FRAME_PATTERN = Pattern.compile("^([0-9:,]+) --> ([0-9:,]+)");

    public SubtitleFile(String fileName) {
        this.fileName = fileName;
    }

    public List<SubtitleFileRecord> records() throws IOException {
        List<SubtitleFileRecord> result = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        int i = 0;
        while (i < lines.size()) {
            SubtitleFileRecord.SubtitleFileRecordBuilder currentRecord = SubtitleFileRecord.builder();
            currentRecord.id(
                Integer.parseInt(lines.get(i++).replaceAll("\\D", ""))
            );

            Matcher matcher = TIME_FRAME_PATTERN.matcher(lines.get(i++));
            if (matcher.matches()) {
                currentRecord.begin(LocalTime.parse(matcher.group(1), TIME_FRAME_FORMAT));
                currentRecord.end(LocalTime.parse(matcher.group(2), TIME_FRAME_FORMAT));
            } else {
                throw new IllegalStateException(
                    String.format("Time frame expected: %s", lines.get(i - 1))
                );
            }
            String textLine = lines.get(i++);
            StringBuilder text = new StringBuilder();
            while (!textLine.isEmpty()) {
                text.append(textLine);
                text.append("\n");
                textLine = lines.get(i++);
            }
            currentRecord.text(text.toString());
            result.add(currentRecord.build());
        }
        return result;
    }
}
