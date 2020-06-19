package com.example.quixapp.Model;

public class Questions {

    String id,question,answer,option_a,option_b,option_c;
    long time;

    public Questions() {
    }

    public Questions(String id, String question, String answer, String option_a, String option_b, String option_c, long time) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.option_a = option_a;
        this.option_b = option_b;
        this.option_c = option_c;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getOption_a() {
        return option_a;
    }

    public void setOption_a(String option_a) {
        this.option_a = option_a;
    }

    public String getOption_b() {
        return option_b;
    }

    public void setOption_b(String option_b) {
        this.option_b = option_b;
    }

    public String getOption_c() {
        return option_c;
    }

    public void setOption_c(String option_c) {
        this.option_c = option_c;
    }
}
