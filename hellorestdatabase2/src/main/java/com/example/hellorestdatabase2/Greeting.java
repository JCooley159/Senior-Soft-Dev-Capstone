package com.example.hellorestdatabase2;

public class Greeting implements Comparable<Greeting> {
    private final int id;
    private String content;

    public Greeting() {
        this.id = 0;
        this.content = "";
    }

    public Greeting(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String str) {
        this.content = str;
    }

    @Override
    public int compareTo(Greeting o) {
        return (int) (this.id - o.getId());
    }
}
