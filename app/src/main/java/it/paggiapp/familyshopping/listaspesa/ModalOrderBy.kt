package it.paggiapp.familyshopping.listaspesa

import android.annotation.SuppressLint
import android.app.Dialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.RecyclerView
import android.view.View
import it.paggiapp.familyshopping.R
import it.paggiapp.familyshopping.database.DataStore
import kotlinx.android.synthetic.main.orderby_stylesheet.view.*
import it.paggiapp.familyshopping.util.Util

/**
 * Fragment modal bottom sheet
 * Created by Aula on 06/02/2018.
 */
class ModalOrderBy : BottomSheetDialogFragment() {
    companion object {
        val CHOICE_LABEL = "choice"
        val CHOICE_PRIORITY = 1
        val CHOICE_LAST = 2
    }

    var currentChoice : Int = -1
    lateinit var recyclerView : RecyclerView

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.orderby_stylesheet, null)
        dialog.setContentView(contentView)

        currentChoice = Util.getCurrentOrder(context)
        if(currentChoice == CHOICE_PRIORITY)
            contentView.img_choice_priority.visibility = View.VISIBLE
        else
            contentView.img_choice_last.visibility = View.VISIBLE

        // CHOICE PRIORITY listener
        contentView.bottom_choice_priority.setOnClickListener{
            if(currentChoice == CHOICE_PRIORITY) return@setOnClickListener

            contentView.img_choice_priority.visibility = View.VISIBLE
            contentView.img_choice_last.visibility = View.INVISIBLE
            Util.setOrder(context, CHOICE_PRIORITY)
            DataStore.getDB().currentOrder = CHOICE_PRIORITY
            (recyclerView.adapter as ListaFragment.ListaAdapter).refresh()
            dismiss()
        }

        // CHOICE LAST UPDATED listener
        contentView.bottom_choice_last.setOnClickListener{
            if(currentChoice == CHOICE_LAST) return@setOnClickListener

            contentView.img_choice_priority.visibility = View.INVISIBLE
            contentView.img_choice_last.visibility = View.VISIBLE
            Util.setOrder(context, CHOICE_LAST)
            DataStore.getDB().currentOrder = CHOICE_LAST
            (recyclerView.adapter as ListaFragment.ListaAdapter).refresh()
            dismiss()
        }
    }
}