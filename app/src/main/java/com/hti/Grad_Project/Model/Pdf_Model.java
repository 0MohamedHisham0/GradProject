package com.hti.Grad_Project.Model;

import java.io.Serializable;

public class Pdf_Model implements Serializable {
    private int pdfIcon;
    private String author;
    private int id;
    private String title;
    private String file;

    public Pdf_Model(int id, String title, String file) {
        this.id = id;
        this.title = title;
        this.file = file;
    }

    public Pdf_Model(int pdfIcon, String author, int id, String title, String file) {
        this.pdfIcon = pdfIcon;
        this.author = author;
        this.id = id;
        this.title = title;
        this.file = file;
    }

    public Pdf_Model() {
    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public int getPdfIcon() {
        return pdfIcon;
    }

    public void setPdfIcon(int pdfIcon) {
        this.pdfIcon = pdfIcon;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
