package it.paggiapp.familyshopping.data

/**
 * Data class for the Carrello
 * Created by nicola on 21/01/18.
 */
data class Carrello(
        val id : Int,
        val nome : String,
        val commento : String,
        val categoria : Categoria?,
        val priorita : Int,
        val inLista : Int,
        val dataImmissione : String,
        val oraImmissione : String,
        val timestamp : String,
        val utente: Utente?
) {
    companion object {
        val PRIORITA_BASSA = 1
        val PRIORITA_MEDIA = 2
        val PRIORITA_ALTA = 3
        val IN_LISTA = 1
        val NO_IN_LISTA = 0
    }
}