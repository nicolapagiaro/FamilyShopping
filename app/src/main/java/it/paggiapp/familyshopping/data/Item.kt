package it.paggiapp.familyshopping.data

/**
 * Created by nicola on 21/01/18.
 */
data class Item(
        val id : Int,
        val nome : String,
        val categoria : String,
        val priorita : Int
) {
    companion object {
        var PRIORITA_BASSA = 1
        var PRIORITA_MEDIA = 2
        var PRIORITA_ALTA = 3
    }
}