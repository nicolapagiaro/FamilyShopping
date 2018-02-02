package it.paggiapp.familyshopping.data

/**
 * Created by nicola on 21/01/18.
 */
data class CarrelloItem(
        val item : Item,
        val commento : String,
        val quantita : Int,
        val priorita : Int,
        val dataImmissione : String,
        val oraImmissione : String,
        val timestamp : String,
        val utente: Utente
) {
    companion object {
        var PRIORITA_BASSA = 1
        var PRIORITA_MEDIA = 2
        var PRIORITA_ALTA = 3
    }
}