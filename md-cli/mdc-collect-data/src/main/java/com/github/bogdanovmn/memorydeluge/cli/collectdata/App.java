package com.github.bogdanovmn.memorydeluge.cli.collectdata;

import com.github.bogdanovmn.cmdline.CmdLineAppBuilder;

class App {

    public static final String ARG_SUBTITLES_FILE = "subtitles-file";
    public static final String ARG_VIDEO_FILE = "video-file";
    public static final String ARG_OUTPUT_DIR = "output-dir";
    public static final String ARG_VLC_PATH = "vlc-path";

    public static void main(String[] args) throws Exception {

        new CmdLineAppBuilder(args)
            .withJarName("collect-data")
            .withDescription("Collect screenshots according to subtitles")

            .withRequiredArg(ARG_SUBTITLES_FILE, "file name with video's subtitles")
            .withRequiredArg(ARG_VIDEO_FILE,     "video file name")
            .withRequiredArg(ARG_VLC_PATH,       "VLC player path")
            .withRequiredArg(ARG_OUTPUT_DIR,     "where results have to be created")

            .withEntryPoint(
                cmdLine -> {
                    new VideoFootage(
                        new VlcClient(
                            cmdLine.getOptionValue(ARG_VLC_PATH),
                            cmdLine.getOptionValue(ARG_VIDEO_FILE)
                        ),
                        new SubtitleFile(
                            cmdLine.getOptionValue(ARG_SUBTITLES_FILE)
                        )
                    ).save(
                        cmdLine.getOptionValue(ARG_OUTPUT_DIR)
                    );
                }
            ).build().run();
    }
}