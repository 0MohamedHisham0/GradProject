package com.hti.Grad_Project.Model;

import java.util.List;

public class AnswerList_Model {
    private int count;
    private List<Answer_Model> result;

    public AnswerList_Model(int count, List<Answer_Model> result) {
        this.count = count;
        this.result = result;
    }

    public AnswerList_Model() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Answer_Model> getResult() {
        return result;
    }

    public void setResult(List<Answer_Model> result) {
        this.result = result;
    }

}
