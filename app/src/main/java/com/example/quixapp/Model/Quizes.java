package com.example.quixapp.Model;

public class Quizes {


    String quiz_id,quiz_title,quiz_description,quiz_level,quiz_image,quiz_visibility;
    long quiz_questions;

    public Quizes() {
    }

    public Quizes(String quiz_id, String quiz_title, String quiz_description, String quiz_level, String quiz_image, String quiz_visibility, long quiz_questions) {
        this.quiz_id = quiz_id;
        this.quiz_title = quiz_title;
        this.quiz_description = quiz_description;
        this.quiz_level = quiz_level;
        this.quiz_image = quiz_image;
        this.quiz_visibility = quiz_visibility;
        this.quiz_questions = quiz_questions;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getQuiz_title() {
        return quiz_title;
    }

    public void setQuiz_title(String quiz_title) {
        this.quiz_title = quiz_title;
    }

    public String getQuiz_description() {
        return quiz_description;
    }

    public void setQuiz_description(String quiz_description) {
        this.quiz_description = quiz_description;
    }

    public String getQuiz_level() {
        return quiz_level;
    }

    public void setQuiz_level(String quiz_level) {
        this.quiz_level = quiz_level;
    }

    public String getQuiz_image() {
        return quiz_image;
    }

    public void setQuiz_image(String quiz_image) {
        this.quiz_image = quiz_image;
    }

    public String getQuiz_visibility() {
        return quiz_visibility;
    }

    public void setQuiz_visibility(String quiz_visibility) {
        this.quiz_visibility = quiz_visibility;
    }

    public long getQuiz_questions() {
        return quiz_questions;
    }

    public void setQuiz_questions(long quiz_questions) {
        this.quiz_questions = quiz_questions;
    }
}
