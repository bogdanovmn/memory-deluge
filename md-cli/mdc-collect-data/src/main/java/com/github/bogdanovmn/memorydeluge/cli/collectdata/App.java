package com.github.bogdanovmn.memorydeluge.cli.collectdata;

import com.github.bogdanovmn.cmdline.CmdLineAppBuilder;

public class App {

    public static final String ARG_SUBTITLES_FILE = "subtitles-file";
    public static final String ARG_VIDEO_FILE = "video-file";
    public static final String ARG_OUTPUT_DIR = "output-dir";
    public static final String ARG_VLC_PATH = "vlc-path";
    public static final String ARG_THREAD_POOL_SIZE = "thread-pool-size";
    public static final String ARG_SERIES_SUBTITLES_DIR = "series-subtitles-dir";
    public static final String ARG_SERIES_VIDEO_DIR = "series-video-dir";
    public static final String ARG_DRY_RUN = "dry-run";

    public static void main(String[] args) throws Exception {

        new CmdLineAppBuilder(args)
            .withJarName("collect-data")
            .withDescription("Collect screenshots according to subtitles")

            .withFlag(ARG_DRY_RUN, "run without any changes")

            .withRequiredArg(ARG_VLC_PATH,   "VLC player path")
            .withRequiredArg(ARG_OUTPUT_DIR, "where results have to be created")
            .withArg(ARG_THREAD_POOL_SIZE,   "concurrent screenshot making thread pool size (default: 1)")

            .withArg(ARG_VIDEO_FILE,     "video file name")
            .withArg(ARG_SUBTITLES_FILE, "file name with video's subtitles")

            .withArg(ARG_SERIES_VIDEO_DIR,     "dir with video files belong to a single series season")
            .withArg(ARG_SERIES_SUBTITLES_DIR, "dir with subtitles belong to a single series season")

            .withAtLeastOneRequiredOption(ARG_VIDEO_FILE, ARG_SERIES_VIDEO_DIR)
            .withDependencies(ARG_VIDEO_FILE, ARG_SUBTITLES_FILE)
            .withDependencies(ARG_SERIES_VIDEO_DIR, ARG_SERIES_SUBTITLES_DIR)

            .withEntryPoint(
                cmdLine -> {
                    VlcClient vlcClient = new VlcClient(
                        cmdLine.getOptionValue(ARG_VLC_PATH),
                        cmdLine.getOptionValue(ARG_OUTPUT_DIR)
                    );
                    int threadPoolSize = Integer.parseInt(
                        cmdLine.getOptionValue(ARG_THREAD_POOL_SIZE, "1")
                    );
                    if (cmdLine.hasOption(ARG_VIDEO_FILE)) {
                        VideoFootage.builder()
                            .vlcClient(vlcClient)
                            .threadPoolSize(threadPoolSize)
                            .videoFileName(
                                cmdLine.getOptionValue(ARG_VIDEO_FILE)
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
                            .vlcClient(vlcClient)
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
