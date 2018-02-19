package it.paggiapp.familyshopping.listaspesa

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.util.DiffUtil
import it.paggiapp.familyshopping.data.Carrello
import it.paggiapp.familyshopping.database.FamilyContract

/**
 * Class that implements the DiffUtil callback for improve
 * performance on recyclerview updates
 * Created by nicola on 17/02/18.
 */
class CarrelloDiffCallback(
        private var mNewCarrello: List<Carrello>,
        private var mOldCarrello: List<Carrello>) : DiffUtil.Callback() {


    /**
     * Method that checks if the items are the same
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mNewCarrello[newItemPosition].id == mOldCarrello[oldItemPosition].id
    }

    override fun getOldListSize() = mOldCarrello.size

    override fun getNewListSize() = mNewCarrello.size

    /**
     * Method that checks if the items content are the same
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if(newListSize == 0 || oldListSize == 0)
            return false

        val newItem = mNewCarrello[newItemPosition]
        val oldItem = mOldCarrello[oldItemPosition]

        return (newItem.nome == oldItem.nome) &&
                (newItem.commento == oldItem.commento) &&
                (newItem.priorita == oldItem.priorita)
    }

    /**
     * Method that returns the changed payload
     */
    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        if(newListSize == 0 || oldListSize == 0)
            return null

        val newItem = mNewCarrello[newItemPosition]
        val oldItem = mOldCarrello[oldItemPosition]

        val bundle = Bundle()
        if(newItem.nome != oldItem.nome) {
            bundle.putString(FamilyContract.Carrello.NOME, newItem.nome)
        }

        if(newItem.commento != oldItem.commento) {
            bundle.putString(FamilyContract.Carrello.COMMENTO, newItem.commento)
        }

        if(newItem.priorita != oldItem.priorita) {
            bundle.putInt(FamilyContract.Carrello.PRIORITA, newItem.priorita)
        }

        return bundle
    }

}