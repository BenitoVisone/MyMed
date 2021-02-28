package com.example.mymed;

public class Timetable {
    private int start_hour;
    private int start_min;
    private int end_hour;
    private int end_min;

    public int getStart_min() {
        return start_min;
    }

    public void setStart_min(int start_min) {
        this.start_min = start_min;
    }

    public int getEnd_min() {
        return end_min;
    }

    public void setEnd_min(int end_min) {
        this.end_min = end_min;
    }

    private String giorno;

    public int getStart() {
        return start_hour;
    }

    public Timetable(int start, int start_min, int end, int end_min, String giorno) {
        this.start_hour = start;
        this.start_min = start_min;
        this.end_hour = end;
        this.end_min = end_min;
        this.giorno = giorno;
    }

    public void setStartHour(int start) {
        this.start_hour = start;
    }

    public int getEndHour() {
        return end_hour;
    }

    public void setEndHour(int end) {
        this.end_hour = end;
    }

    public String getGiorno() {
        return giorno;
    }

    public void setGiorno(String giorno) {
        this.giorno = giorno;
    }
}
