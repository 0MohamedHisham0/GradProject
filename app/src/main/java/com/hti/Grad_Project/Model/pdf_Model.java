package com.hti.Grad_Project.Model;

public class pdf_Model {
    private String mainText;
    private String pageNum;

    public pdf_Model(String mainText, String pageNum) {
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
