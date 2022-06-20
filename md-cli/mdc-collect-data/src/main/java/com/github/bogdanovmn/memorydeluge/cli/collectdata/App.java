package com.github.bogdanovmn.memorydeluge.cli.collectdata;

import com.github.bogdanovmn.cmdline.CmdLineAppBuilder;

public class App {

    public static final String ARG_SUBTITLES_FILE = "subtitles-file";
    public static final String ARG_VIDEO_FILE = "video-file";
    public static final String ARG_OUTPUT_DIR = "output-dir";
    public static final String ARG_VLC_PATH = "vlc-path";
    public static final String ARG_THREAD_POOL_SIZE = "thread-pool-size";

    public static void main(String[] args) throws Exception {

        new CmdLineAppBuilder(args)
            .withJarName("collect-data")
            .withDescription("Collect screenshots according to subtitles")

            .withRequiredArg(ARG_SUBTITLES_FILE,   "file name with video's subtitles")
            .withRequiredArg(ARG_VIDEO_FILE,       "video file name")
            .withRequiredArg(ARG_VLC_PATH,         "VLC player path")
            .withRequiredArg(ARG_OUTPUT_DIR,       "where results have to be created")
            .withRequiredArg(ARG_THREAD_POOL_SIZE, "concurrent screenshot making thread pool size (default: 1)")

            .withEntryPoint(
                cmdLine -> {
                    VideoFootage.builder()
                        .videoFileName(
                            cmdLine.getOptionValue(ARG_VIDEO_FILE)
                        )
                        .vlcCommand(
                            new VlcCommand(
                                cmdLine.getOptionValue(ARG_VLC_PATH),
                                cmdLine.getOptionValue(ARG_OUTPUT_DIR)
                            )
                        )
                        .subtitleFile(
                            new SubtitleFile(
                                cmdLine.getOptionValue(ARG_SUBTITLES_FILE)
                            )
                        )
                        .threadPoolSize(
                            Integer.parseInt(
                                cmdLine.getOptionValue(ARG_THREAD_POOL_SIZE, "1")
                            )
                        )
                    .build().save();
                }
            ).build().run();
    }
}
