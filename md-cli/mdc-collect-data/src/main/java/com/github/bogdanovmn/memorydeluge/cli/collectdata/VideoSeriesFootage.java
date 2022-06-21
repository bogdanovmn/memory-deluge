package com.github.bogdanovmn.memorydeluge.cli.collectdata;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Builder
class VideoSeriesFootage {
    private final String videoSeriesSeasonDir;
    private final String seriesSeasonSubtitlesDir;
    private final VlcClient vlcClient;
    private final int threadPoolSize;
    private final boolean dryRun;

    private static final Pattern EPISODE_PATTERN = Pattern.compile(".*(S\\d{1,2}E\\d{1,2})\\D.*", Pattern.CASE_INSENSITIVE);

    void save() throws Exception {
        Map<String, Path> episodeSubtitle = groupByEpisode(
            new Directory(seriesSeasonSubtitlesDir).files()
        );
        Map<String, Path> episodeVideo = groupByEpisode(
            new Directory(videoSeriesSeasonDir).files()
        );
        if (episodeSubtitle.size() != episodeVideo.size() || !episodeSubtitle.keySet().containsAll(episodeVideo.keySet())) {
            throw new IllegalStateException(
                String.format(
                    "Subtitles and videos don't match each other: %n%s%n%s",
                        episodeVideo.values(),
                        episodeSubtitle.values()
                )
            );
        }
        for (String episode : episodeVideo.keySet()) {
            log.info("Process {}: video={} subtitles={}", episode, episodeVideo.get(episode), episodeSubtitle.get(episode));
            VideoFootage.builder()
                .vlcClient(vlcClient)
                .threadPoolSize(threadPoolSize)
                .dryRun(dryRun)
                .videoFileName(episodeVideo.get(episode).toString())
                .subtitleFile(
                    new SubtitleFile(episodeSubtitle.get(episode).toString())
                )
            .build().save();
        }
    }

    private Map<String, Path> groupByEpisode(Set<Path> files) {
        Map<String, Path> result = new HashMap<>();
        for (Path file : files) {
            Matcher matcher = EPISODE_PATTERN.matcher(file.toString());
            if (matcher.matches()) {
                result.put(matcher.group(1).toLowerCase(), file);
            }
        }
        return result;
    }

}
