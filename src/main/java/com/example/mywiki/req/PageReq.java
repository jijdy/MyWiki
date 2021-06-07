package com.example.mywiki.req;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

public class PageReq<E> {

    @NotNull(message = "页码不能为空！")
    private int page;

    @NotNull(message = "列表不能为空！")
    @Max(value = 1000,message = "最大查询数据过大！")
    private int size;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PageReq{" +
                "page=" + page +
                ", size=" + size +
                '}';
    }
}
