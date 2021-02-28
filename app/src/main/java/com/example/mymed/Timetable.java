package com.example.mymed;

public class Timetable {
    private int start;
    private int end;
    private String giorno;

    public int getStart() {
        return start;
    }

    public Timetable(int start, int end, String giorno) {
        this.start = start;
        this.end = end;
        this.giorno = giorno;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getGiorno() {
        return giorno;
    }

    public void setGiorno(String giorno) {
        this.giorno = giorno;
    }
}
