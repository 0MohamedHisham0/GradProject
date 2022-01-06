package com.hti.Grad_Project.Model;

import java.io.Serializable;

public class QuestionAsk_Model implements Serializable {
    private String question;
    private String model;
    private int prediction;
    private String folder;

    public QuestionAsk_Model(String question, String model, int prediction, String folder) {
        this.question = question;
        this.model = model;
        this.prediction = prediction;
        this.folder = folder;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPrediction() {
        return prediction;
    }

    public void setPrediction(int prediction) {
        this.prediction = prediction;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
}
