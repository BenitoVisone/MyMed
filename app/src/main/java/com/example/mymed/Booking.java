package com.example.mymed;

public class Booking {
    String data_prenotazione;
    String stato;
    String id_utente;

    public Booking(){}
    public Booking(String data_prenotazione, String stato, String id_utente) {
        this.data_prenotazione = data_prenotazione;
        this.stato = stato;
        this.id_utente = id_utente;
    }

    public String getData_prenotazione() {
        return data_prenotazione;
    }

    public void setData_prenotazione(String data_prenotazione) {
        this.data_prenotazione = data_prenotazione;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getId_utente() {
        return id_utente;
    }

    public void setId_utente(String id_utente) {
        this.id_utente = id_utente;
    }
}
