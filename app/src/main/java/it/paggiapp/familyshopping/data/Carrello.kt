package it.paggiapp.familyshopping.data

import java.io.Serializable

/**
 * Data class for the Carrello
 * Created by nicola on 21/01/18.
 */
data class Carrello(
        var id : Int,
        var nome : String,
        var commento : String,
        var categoria : Categoria?,
        var priorita : Int,
        var inLista : Int,
        var dataImmissione : String,
        var timestamp : String,
        var utente: Utente?
) : Serializable {
    companion object {
        val PRIORITA_BASSA = 1
        val PRIORITA_MEDIA = 2
        val PRIORITA_ALTA = 3
        val IN_LISTA = 1
        val NO_IN_LISTA = 0
    }
}