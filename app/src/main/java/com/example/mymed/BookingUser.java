package com.example.mymed;

public class BookingUser {
    String data_prenotazione;
    String stato;
    String id_utente;
    String ora;

    public BookingUser(){}
    public BookingUser(String data_prenotazione, String stato, String id_utente,String ora) {
        this.data_prenotazione = data_prenotazione;
        this.stato = stato;
        this.id_utente = id_utente;
        this.ora = ora;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String turno_selezionato) {
        this.ora = turno_selezionato;
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
