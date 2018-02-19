package it.paggiapp.familyshopping.data

import java.io.Serializable

/**
 * Data class for the Carrello
 * Created by nicola on 21/01/18.
 */
data class Carrello(
        var id : Int?,
        var nome : String,
        var commento : String,
        var categoria : Categoria?,
        var priorita : Int,
        var inLista : Int,
        var dataImmissione : String,
        var timestamp : String,
        var utente: Utente?,
        var inputMode : Int?



) : Serializable {
    override fun equals(other: Any?): Boolean {
        if(this === other) return true

        if(javaClass != other?.javaClass) return false

        other as Carrello

        return (id == other.id) &&
                (nome == other.nome) &&
                (commento == other.commento) &&
                (categoria!!.id == other.categoria!!.id) &&
                (priorita == other.priorita) &&
                (inLista == other.inLista) &&
                (dataImmissione == other.dataImmissione) &&
                (timestamp == other.timestamp) &&
                (utente!!.id == other.utente!!.id) &&
                (inputMode == other.inputMode)
    }

    companion object {
        val PRIORITA_BASSA = 1
        val PRIORITA_MEDIA = 2
        val PRIORITA_ALTA = 3
        val IN_LISTA = 1
        val NO_IN_LISTA = 0
    }
}

