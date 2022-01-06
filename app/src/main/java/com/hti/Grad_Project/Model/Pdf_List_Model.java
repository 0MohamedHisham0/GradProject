package com.hti.Grad_Project.Model;

import java.util.List;

public class Pdf_List_Model {
    private int count;
    private int next;
    private int previous;
    private List<Pdf_Model> results;

    public Pdf_List_Model(int count, int next, int previous, List<Pdf_Model> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getPrevious() {
        return previous;
    }

    public void setPrevious(int previous) {
        this.previous = previous;
    }

    public List<Pdf_Model> getResults() {
        return results;
    }

    public void setResults(List<Pdf_Model> results) {
        this.results = results;
    }
}
