package com.myblog9.payload;

import lombok.*;

import java.util.List;



public class CommentResponse {

    private List<CommentDto> content;
    private int pageno;
    private int pagesize;
    private int totalelement;
    private int totalpages;
    private boolean last;

    public List<CommentDto> getContent() {
        return content;
    }

    public void setContent(List<CommentDto> content) {
        this.content = content;
    }

    public int getPageno() {
        return pageno;
    }

    public void setPageno(int pageno) {
        this.pageno = pageno;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getTotalelement() {
        return totalelement;
    }

    public void setTotalelement(int totalelement) {
        this.totalelement = totalelement;
    }

    public int getTotalpages() {
        return totalpages;
    }

    public void setTotalpages(int totalpages) {
        this.totalpages = totalpages;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}

