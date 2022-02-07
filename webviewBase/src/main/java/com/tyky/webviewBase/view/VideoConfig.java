package com.tyky.webviewBase.view;

public class VideoConfig {
    /**
     * 视频的大小和时长限制
     */
    private static boolean isVideoSizeLimit, isVideoDurationLimit;
    private static int videoSize = 10;
    private static int videoDuration = 30;

    public static void setVideoMaxSize(int videoSize) {
        isVideoSizeLimit = true;
        isVideoDurationLimit = false;
        videoSize = videoSize;
    }

    public static void setVideoMaxDuration(int videoDuration) {
        isVideoDurationLimit = true;
        isVideoSizeLimit = false;
        videoDuration = videoDuration;
    }
}
