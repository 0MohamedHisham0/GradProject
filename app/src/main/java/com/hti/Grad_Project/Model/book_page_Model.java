package com.hti.Grad_Project.Model;

import java.io.Serializable;

public class book_page_Model implements Serializable {
    private String mainText;
    private String pageNum;

    public book_page_Model(String mainText, String pageNum) {
        this.mainText = mainText;
        this.pageNum = pageNum;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }
}
