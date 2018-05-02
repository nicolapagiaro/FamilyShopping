package it.paggiapp.familyshopping.listaspesa

import android.annotation.SuppressLint
import android.app.Dialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import it.paggiapp.familyshopping.R
import it.paggiapp.familyshopping.util.Util
import kotlinx.android.synthetic.main.viewmode_stylesheet.view.*

@SuppressLint("ValidFragment")
/**
 * Fragment modal bottom sheet
 * Created by Aula on 06/02/2018.
 */
class ModalViewMode(var fragment: ListaFragment) : BottomSheetDialogFragment() {
    companion object {
        val CHOICE_LABEL = "viewMode"
        val CHOICE_LIST= 1
        val CHOICE_GRID = 2
    }

    var currentChoice : Int = -1

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.viewmode_stylesheet, null)
        dialog.setContentView(contentView)

        currentChoice = Util.getCurrentViewMode(context!!)
        if(currentChoice == CHOICE_LIST)
            contentView.img_choice_list.visibility = View.VISIBLE
        else
            contentView.img_choice_grid.visibility = View.VISIBLE

        // CHOICE LIST listener
        contentView.bottom_choice_list.setOnClickListener{
            if(currentChoice == CHOICE_LIST) return@setOnClickListener

            contentView.img_choice_list.visibility = View.VISIBLE
            contentView.img_choice_grid.visibility = View.INVISIBLE
            Util.setViewMode(context!!, CHOICE_LIST)
            fragment.setLinearLayoutManager()
            dismiss()
        }

        // CHOICE GRID listener
        contentView.bottom_choice_grid.setOnClickListener{
            if(currentChoice == CHOICE_GRID) return@setOnClickListener

            contentView.img_choice_list.visibility = View.INVISIBLE
            contentView.img_choice_grid.visibility = View.VISIBLE
            Util.setViewMode(context!!, CHOICE_GRID)
            fragment.setGridLayoutManager()
            dismiss()
        }
    }
}