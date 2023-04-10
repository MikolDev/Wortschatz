package com.example.wortschatz.Model;

public class Phrase {
    private int id;
    private String singular;
    private String plural;
    private String translation;
    private boolean isHard;
    private String chapter;

    public Phrase() {
    }

    public Phrase(int id, String singular, String plural, String translation, boolean isHard, String chapter) {
        this.id = id;
        this.singular = singular;
        this.plural = plural;
        this.translation = translation;
        this.isHard = isHard;
        this.chapter = chapter;
    }

    public Phrase(String singular, String plural, String translation, boolean isHard, String chapter) {
        this.id = 0;
        this.singular = singular;
        this.plural = plural;
        this.translation = translation;
        this.isHard = isHard;
        this.chapter = chapter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSingular() {
        return singular;
    }

    public void setSingular(String singular) {
        this.singular = singular;
    }

    public String getPlural() {
        return plural;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public boolean isHard() {
        return isHard;
    }

    public void setHard(boolean hard) {
        isHard = hard;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    @Override
    public String toString() {
        return "Phrase{" +
                "id=" + id +
                ", singular='" + singular + '\'' +
                ", plural='" + plural + '\'' +
                ", translation='" + translation + '\'' +
                ", isHard=" + isHard +
                ", chapter='" + chapter + '\'' +
                '}';
    }
}
