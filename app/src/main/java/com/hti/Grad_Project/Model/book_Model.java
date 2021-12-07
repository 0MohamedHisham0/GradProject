package com.hti.Grad_Project.Model;

import java.io.Serializable;
import java.util.List;

public class book_Model implements Serializable {
    private String bookName;
    private List<book_page_Model> pagesList;

    public book_Model(String bookName, List<book_page_Model> list) {
        this.bookName = bookName;
        this.pagesList = list;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public List<book_page_Model> getPagesList() {
        return pagesList;
    }

    public void setPagesList(List<book_page_Model> pagesList) {
        this.pagesList = pagesList;
    }
}
