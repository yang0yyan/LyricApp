package com.example.lyricapp.view.lyric;

public class LyricBean {

    private String timeClock = "";
    private String lyric = "";
    private long timeMillis = 0L;


    public String getTimeClock() {
        return timeClock;
    }

    public void setTimeClock(String timeClock) {
        this.timeClock = timeClock;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public long getTimeMillis() {
        return timeMillis;
    }

    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
    }
}
