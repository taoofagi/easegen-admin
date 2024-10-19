package cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo;

public class CourseTextRespVO {
    private String audio;
    private String text;
    private long timestamp;
    private String img;
    private int currentNo;
    private int totalNo;

    public CourseTextRespVO(String audio, String text, long timestamp, String img, int currentNo, int totalNo) {
        this.audio = audio;
        this.text = text;
        this.timestamp = timestamp;
        this.img = img;
        this.currentNo = currentNo;
        this.totalNo = totalNo;
    }

    public String getAudio() {
        return audio;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getImg() {
        return img;
    }

    public int getCurrentNo() {
        return currentNo;
    }

    public int getTotalNo() {
        return totalNo;
    }
}
