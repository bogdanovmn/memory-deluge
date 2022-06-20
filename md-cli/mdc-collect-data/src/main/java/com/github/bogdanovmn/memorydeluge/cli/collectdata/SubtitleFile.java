package com.github.bogdanovmn.memorydeluge.cli.collectdata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        SubtitleFileRecord previous = null;
        while (i < lines.size()) {
            SubtitleFileRecord.SubtitleFileRecordBuilder currentRecord = SubtitleFileRecord.builder();
            currentRecord.id(
                Integer.parseInt(lines.get(i++).replaceAll("\\D", ""))
            );

            Matcher matcher = TIME_FRAME_PATTERN.matcher(lines.get(i++));
            if (matcher.matches()) {
                LocalTime beginTime = LocalTime.parse(matcher.group(1), TIME_FRAME_FORMAT);
                currentRecord.begin(beginTime);
                currentRecord.end(LocalTime.parse(matcher.group(2), TIME_FRAME_FORMAT));
                currentRecord.interval(
                    Optional.ofNullable(previous)
                        .map(SubtitleFileRecord::end)
                        .map(prevEndTime -> beginTime.toSecondOfDay() - prevEndTime.toSecondOfDay())
                        .orElse(0)
                );
            } else {
                throw new IllegalStateException(
                    String.format("Time frame expected: %s", lines.get(i - 1))
                );
            }

            String textLine = lines.get(i++);
            StringBuilder text = new StringBuilder();
            int l = 1;
            while (!textLine.isEmpty()) {
                if (l > 1) {
                    text.append("\n");
                }
                text.append(textLine);
                textLine = lines.get(i++);
                l++;
            }
            currentRecord.text(text.toString());
            result.add(currentRecord.build());
            previous = result.get(result.size() - 1);
        }
        return result;
    }
}
