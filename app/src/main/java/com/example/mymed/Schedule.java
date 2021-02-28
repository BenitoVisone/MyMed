package com.example.mymed;

public class Schedule {
    private int start;
    private int end;
    private String giorno;
    private String user_id;

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    private String indirizzo;

    public int getStart() {
        return start;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Schedule(int start, int end, String giorno, String user_id,String indirizzo) {
        this.start = start;
        this.end = end;
        this.giorno = giorno;
        this.user_id = user_id;
        this.indirizzo = indirizzo;
    }
}
