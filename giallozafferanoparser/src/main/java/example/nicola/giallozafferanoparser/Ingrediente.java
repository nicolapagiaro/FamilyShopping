package example.nicola.giallozafferanoparser;

import java.io.Serializable;

/**
 * Classe che rappresenta un ingrediente delle ricette di giallo zafferano
 */
public class Ingrediente implements Serializable {
    private String nome;
    private String qta;

    /**
     * Costruttore parametrico
     * @param nome nome dell'ingrediente
     * @param qta quantità per quella ricetta
     */
    public Ingrediente(String nome, String qta) {
        this.nome = nome;
        this.qta = qta;
    }

    /**
     * Costruttore vuoto
     */
    public Ingrediente() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getQta() {
        return qta;
    }

    public void setQta(String qta) {
        this.qta = qta;
    }

    @Override
    public String toString() {
        return "Ingrediente{" +
                "nome='" + nome + '\'' +
                ", qta='" + qta + '\'' +
                '}';
    }
}
