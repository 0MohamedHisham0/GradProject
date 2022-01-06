package com.hti.Grad_Project.Model;

public class answerModel {
    String Answer;
    String Paragraph;
    String aqu;

    public answerModel(String answer, String paragraph, String aqu) {
        Answer = answer;
        Paragraph = paragraph;
        this.aqu = aqu;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public String getParagraph() {
        return Paragraph;
    }

    public void setParagraph(String paragraph) {
        Paragraph = paragraph;
    }

    public String getAqu() {
        return aqu;
    }

    public void setAqu(String aqu) {
        this.aqu = aqu;
    }

}
