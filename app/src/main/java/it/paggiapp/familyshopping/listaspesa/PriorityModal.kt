package it.paggiapp.familyshopping.listaspesa

import android.annotation.SuppressLint
import android.app.Dialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import it.paggiapp.familyshopping.R
import it.paggiapp.familyshopping.data.Carrello.Companion.PRIORITA_ALTA
import it.paggiapp.familyshopping.data.Carrello.Companion.PRIORITA_BASSA
import it.paggiapp.familyshopping.data.Carrello.Companion.PRIORITA_MEDIA
import kotlinx.android.synthetic.main.priority_sheet.view.*

/**
 * Bottom sheet modal to display the Priority selection
 * Created by Aula on 13/02/2018.
 */
class PriorityModal : BottomSheetDialogFragment() {
    var mDialogResult: DialogPriorityResult? = null // the callback

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.priority_sheet, null)
        dialog.setContentView(contentView)

        contentView.bottom_priority_high.setOnClickListener {
            // priorità alta
            if (mDialogResult != null) {
                mDialogResult!!.finish(PRIORITA_ALTA)
            }
            this@PriorityModal.dismiss()
        }

        contentView.bottom_priority_med.setOnClickListener {
            // priorità media
            if (mDialogResult != null) {
                mDialogResult!!.finish(PRIORITA_MEDIA)
            }
            this@PriorityModal.dismiss()
        }

        contentView.bottom_priority_low.setOnClickListener {
            // priorità bassa
            if (mDialogResult != null) {
                mDialogResult!!.finish(PRIORITA_BASSA)
            }
            this@PriorityModal.dismiss()
        }
    }

    /**
     * Interface for the callback at the activity under
     */
    interface DialogPriorityResult {
        fun finish(result: Int)
    }
}