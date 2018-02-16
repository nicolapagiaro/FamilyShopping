package it.paggiapp.familyshopping.listaspesa

import android.annotation.SuppressLint
import android.app.Dialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import it.paggiapp.familyshopping.R
import it.paggiapp.familyshopping.data.Categoria
import it.paggiapp.familyshopping.database.DataStore
import kotlinx.android.synthetic.main.categor_sheet_item.view.*

/**
 * Bottom sheet modal for display the Category
 * Created by Aula on 13/02/2018.
 */
class CategoryModal : BottomSheetDialogFragment() {
    var mDialogResult: DialogCategoryResult? = null // the callback

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.category_sheet, null)
        dialog.setContentView(contentView)

        DataStore.execute {
            val categorie = DataStore.getDB().getCategorie()
            for (i in (0 until categorie.size)) {
                val v : View = LayoutInflater.from(context).inflate(R.layout.categor_sheet_item, null, false)
                v.tv_category_name.text = categorie[i].nome
                v.setOnClickListener{
                    if (mDialogResult != null) {
                        mDialogResult!!.finish(categorie[i])
                    }
                    this@CategoryModal.dismiss()
                }
                (contentView as LinearLayout).addView(v)
            }
        }
    }

    /**
     * Interface for the callback at the activity under
     */
    interface DialogCategoryResult {
        fun finish(result: Categoria)
    }
}