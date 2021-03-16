package com.example.mymed;

public class BookingUser {
    String data_prenotazione;
    String stato;
    String id_utente;
    String turno_selezionato;

    public BookingUser(){}
    public BookingUser(String data_prenotazione, String stato, String id_utente,String turno_selezionato) {
        this.data_prenotazione = data_prenotazione;
        this.stato = stato;
        this.id_utente = id_utente;
        this.turno_selezionato = turno_selezionato;
    }

    public String getTurno_selezionato() {
        return turno_selezionato;
    }

    public void setTurno_selezionato(String turno_selezionato) {
        this.turno_selezionato = turno_selezionato;
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
