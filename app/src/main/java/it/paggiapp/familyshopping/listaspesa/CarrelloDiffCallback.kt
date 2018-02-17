package it.paggiapp.familyshopping.listaspesa

import android.support.annotation.Nullable
import android.support.v7.util.DiffUtil
import it.paggiapp.familyshopping.data.Carrello

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

    override fun getOldListSize(): Int {
        return mOldCarrello.size
    }

    override fun getNewListSize(): Int {
        return mNewCarrello.size
    }

    /**
     * Method that checks if the items content are the same
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mNewCarrello[newItemPosition].equals(mOldCarrello[oldItemPosition])
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}