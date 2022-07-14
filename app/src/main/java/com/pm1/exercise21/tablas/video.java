package com.pm1.exercise21.tablas;

public class video {
    private Integer id;
    private byte[] video;

    public video(Integer id, byte[] video) {
        this.id = id;
        this.video = video;
    }
    public video(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getVideo() {
        return video;
    }

    public void setVideo(byte[] video) {
        this.video = video;
    }
}
