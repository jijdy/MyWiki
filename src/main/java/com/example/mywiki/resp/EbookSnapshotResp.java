package com.example.mywiki.resp;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class EbookSnapshotResp {

    @JsonFormat(pattern="MM-dd", timezone = "GMT+8")
    private Date data;

    private Integer viewCount;

    private Integer voteCount;

    private Integer viewIncrease;

    private Integer voteIncrease;

    @Override
    public String toString() {
        return "EbookSnapshotResp{" +
                "data=" + data +
                ", viewCount=" + viewCount +
                ", voteCount=" + voteCount +
                ", viewIncrease=" + viewIncrease +
                ", voteIncrease=" + voteIncrease +
                '}';
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Integer getViewIncrease() {
        return viewIncrease;
    }

    public void setViewIncrease(Integer viewIncrease) {
        this.viewIncrease = viewIncrease;
    }

    public Integer getVoteIncrease() {
        return voteIncrease;
    }

    public void setVoteIncrease(Integer voteIncrease) {
        this.voteIncrease = voteIncrease;
    }

}