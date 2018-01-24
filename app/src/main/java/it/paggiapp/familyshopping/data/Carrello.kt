package it.paggiapp.familyshopping.data

/**
 * Created by nicola on 21/01/18.
 */
class Carrello : Iterable<CarrelloItem> {
    private val list : ArrayList<CarrelloItem> = ArrayList()

    /**
     * Funzione per aggiungere un elemento alla lista
     */
    fun addCarelloItem(carrelloItem: CarrelloItem) {
        list.add(carrelloItem)
    }

    /**
     * Funzione per togliere un elemento dalla lista
     */
    fun removeCarrelloItem(carrelloItem: CarrelloItem) {
        list.remove(carrelloItem)
    }

    /**
     * Funzione che restituisce la lunghezza del Carrello
     */
    fun size() : Int {
        return list.size
    }

    /**
     * Funzione per il funzionamento dell'interfaccia Iterable
     */
    override fun iterator(): Iterator<CarrelloItem> {
        return list.iterator()
    }

}