package com.spring.bank.util;

import com.spring.bank.pojo.Record;

import java.util.List;

public class Page {
    private int pageno;
    private int pageSize=4;
    private int pageCount;
    private int pagetotalCount;
    private List<Record> getList;

    public int getPageno() {
        return pageno;
    }

    public void setPageno(int pageno) {
        this.pageno = pageno;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
        if(pageCount>0){
            pagetotalCount=pageCount%pageSize==0?(pageCount/pageSize):(pageCount/pageSize+1);
        }
    }

    public int getPagetotalCount() {
        return pagetotalCount;
    }

    public void setPagetotalCount(int pagetotalCount) {
        this.pagetotalCount = pagetotalCount;
    }

    public List<Record> getGetList() {
        return getList;
    }

    public void setGetList(List<Record> getAll) {
        this.getList = getAll;
    }
}
