package example.nicola.giallozafferanoparser;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe che rappresenta la ricetta di GialloZafferano
 */
public class Ricetta implements Serializable{
    private String titolo;
    private String descrizione;
    private String link;
    private String difficolta;
    private String tempoPreparazione;
    private String tempoCottura;
    private String immagine;
    private ArrayList<Ingrediente> ingredienti;

    /**
     * Costruttore vuoto
     */
    public Ricetta() {
    }

    /**
     * Costruttore con tutto
     * @param titolo titolo
     * @param descrizione descrizione
     * @param link link alla pagina completa
     * @param difficolta difficolta
     * @param tempoPreparazione tempo di preparazione
     * @param tempoCottura tempo di cottura
     * @param immagine immagine
     */
    public Ricetta(String titolo, String descrizione, String link, String difficolta, String tempoPreparazione, String tempoCottura, String immagine) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.link = link;
        this.difficolta = difficolta;
        this.tempoPreparazione = tempoPreparazione;
        this.tempoCottura = tempoCottura;
        this.immagine = immagine;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDifficolta() {
        return difficolta;
    }

    public void setDifficolta(String difficolta) {
        this.difficolta = difficolta;
    }

    public String getTempoPreparazione() {
        return tempoPreparazione;
    }

    public void setTempoPreparazione(String tempoPreparazione) {
        this.tempoPreparazione = tempoPreparazione;
    }

    public String getTempoCottura() {
        return tempoCottura;
    }

    public void setTempoCottura(String tempoCottura) {
        this.tempoCottura = tempoCottura;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public ArrayList<Ingrediente> getIngredienti() {
        return ingredienti;
    }

    public void setIngredienti(ArrayList<Ingrediente> ingredienti) {
        this.ingredienti = ingredienti;
    }

    @Override
    public String toString() {
        return "Ricetta{" +
                "titolo='" + titolo + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", link='" + link + '\'' +
                ", difficolta='" + difficolta + '\'' +
                ", tempoPreparazione='" + tempoPreparazione + '\'' +
                ", tempoCottura='" + tempoCottura + '\'' +
                ", immagine='" + immagine + '\'' +
                ", ingredienti=" + ingredienti +
                '}';
    }
}
