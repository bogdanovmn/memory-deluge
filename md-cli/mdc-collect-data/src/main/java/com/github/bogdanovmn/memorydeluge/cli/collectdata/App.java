package com.github.bogdanovmn.memorydeluge.cli.collectdata;

import com.github.bogdanovmn.cmdline.CmdLineAppBuilder;
import com.github.bogdanovmn.memorydeluge.videoplayer.ScreenshotCommand;
import com.github.bogdanovmn.memorydeluge.videoplayer.ScreenshotCommand.OutputFormat;
import com.github.bogdanovmn.memorydeluge.videoplayer.ScreenshotCommandFactory;
import com.github.bogdanovmn.memorydeluge.videoplayer.VideoPlayer;

import java.nio.file.Paths;
import java.util.Arrays;

public class App {

    public static final String ARG_SUBTITLES_FILE = "subtitles-file";
    public static final String ARG_VIDEO_FILE = "video-file";
    public static final String ARG_OUTPUT_DIR = "output-dir";
    public static final String ARG_OUTPUT_FORMAT = "output-format";
    public static final String ARG_VIDEO_PLAYER_TYPE = "video-player-type";
    public static final String ARG_VIDEO_PLAYER_PATH = "video-player-path";
    public static final String ARG_THREAD_POOL_SIZE = "thread-pool-size";
    public static final String ARG_SERIES_SUBTITLES_DIR = "series-subtitles-dir";
    public static final String ARG_SERIES_VIDEO_DIR = "series-video-dir";
    public static final String ARG_DRY_RUN = "dry-run";

    public static final OutputFormat DEFAULT_OUTPUT_FORMAT = OutputFormat.jpg;

    public static void main(String[] args) throws Exception {

        new CmdLineAppBuilder(args)
            .withJarName("collect-data")
            .withDescription("Collect screenshots according to subtitles")

            .withFlag(ARG_DRY_RUN, "run without any changes")

            .withRequiredArg(
                ARG_VIDEO_PLAYER_TYPE,
                String.format("video player type: %s", Arrays.asList(VideoPlayer.values()))
            )
            .withRequiredArg(ARG_VIDEO_PLAYER_PATH, "video player executable path")
            .withRequiredArg(ARG_OUTPUT_DIR, "where results have to be created")
            .withArg(
                ARG_OUTPUT_FORMAT,
                String.format(
                    "output file format: %s (default: %s)",
                        Arrays.asList(OutputFormat.values()),
                        DEFAULT_OUTPUT_FORMAT
                )
            )
            .withArg(ARG_THREAD_POOL_SIZE, "workers thread pool size (default: 1)")

            .withArg(ARG_VIDEO_FILE,     "video file name")
            .withArg(ARG_SUBTITLES_FILE, "file name with video's subtitles")

            .withArg(ARG_SERIES_VIDEO_DIR,     "dir with video files belong to a single series season")
            .withArg(ARG_SERIES_SUBTITLES_DIR, "dir with subtitles belong to a single series season")

            .withAtLeastOneRequiredOption(ARG_VIDEO_FILE, ARG_SERIES_VIDEO_DIR)
            .withDependencies(ARG_VIDEO_FILE, ARG_SUBTITLES_FILE)
            .withDependencies(ARG_SERIES_VIDEO_DIR, ARG_SERIES_SUBTITLES_DIR)

            .withEntryPoint(
                cmdLine -> {
                    ScreenshotCommand screenshotCommand = ScreenshotCommandFactory.builder()
                        .playerPath(Paths.get(cmdLine.getOptionValue(ARG_VIDEO_PLAYER_PATH)))
                        .outputDir(Paths.get(cmdLine.getOptionValue(ARG_OUTPUT_DIR)))
                        .format(
                            OutputFormat.valueOf(
                                cmdLine.getOptionValue(ARG_OUTPUT_FORMAT, DEFAULT_OUTPUT_FORMAT.name())
                            )
                        )
                    .build().of(
                        VideoPlayer.valueOf(
                            cmdLine.getOptionValue(ARG_VIDEO_PLAYER_TYPE)
                        )
                    );
                    int threadPoolSize = Integer.parseInt(
                        cmdLine.getOptionValue(ARG_THREAD_POOL_SIZE, "1")
                    );
                    if (cmdLine.hasOption(ARG_VIDEO_FILE)) {
                        VideoFootage.builder()
                            .screenshotCommand(screenshotCommand)
                            .threadPoolSize(threadPoolSize)
                            .videoFileName(
                                Paths.get(cmdLine.getOptionValue(ARG_VIDEO_FILE))
                            )
                            .subtitleFile(
                                new SubtitleFile(
                                    cmdLine.getOptionValue(ARG_SUBTITLES_FILE)
                                )
                            )
                            .dryRun(
                                cmdLine.hasOption(ARG_DRY_RUN)
                            )
                        .build().save();
                    } else {
                        VideoSeriesFootage.builder()
                            .screenshotCommand(screenshotCommand)
                            .threadPoolSize(threadPoolSize)
                            .videoSeriesSeasonDir(
                                cmdLine.getOptionValue(ARG_SERIES_VIDEO_DIR)
                            )
                            .seriesSeasonSubtitlesDir(
                                cmdLine.getOptionValue(ARG_SERIES_SUBTITLES_DIR)
                            )
                            .dryRun(
                                cmdLine.hasOption(ARG_DRY_RUN)
                            )
                        .build().save();
                    }
                }
            ).build().run();
    }
}
