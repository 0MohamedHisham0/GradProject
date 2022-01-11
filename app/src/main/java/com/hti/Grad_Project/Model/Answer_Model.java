package com.hti.Grad_Project.Model;

public class Answer_Model {
    private String query;
    private String answer;
    private String title;
    private String paragraph;
    private String accuracy;

    public Answer_Model(String query, String answer, String title, String paragraph, String accuracy) {
        this.query = query;
        this.answer = answer;
        this.title = title;
        this.paragraph = paragraph;
        this.accuracy = accuracy;
    }

    public Answer_Model() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getQuestion() {
        return query;
    }

    public void setQuestion(String question) {
        this.query = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

}
