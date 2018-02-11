package it.paggiapp.familyshopping

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import it.paggiapp.familyshopping.data.Carrello
import it.paggiapp.familyshopping.util.Util
import kotlinx.android.synthetic.main.activity_show_lista_item_details.*

class ShowListaItemDetails : AppCompatActivity() {
    lateinit var item : Carrello

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_lista_item_details)

        // set up the toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // take the Carrello item
        item = intent.getSerializableExtra("item") as Carrello

        // show in the layout the things
        collapse_toolbar.title = item.nome
        var nomeUtente = item.utente?.nome
        if(nomeUtente.equals("null") || nomeUtente?.length == 0) {
            nomeUtente = getString(R.string.username_null)
        }
        tv_itemdetails_username.text = nomeUtente
        tv_itemdetails_email.text = item.utente?.email

        var comment = item.commento
        if(comment.equals("null") || comment.length == 0) {
            comment = getString(R.string.comment_null)
        }
        tv_itemdetails_comment.text = comment
        var priorita = ""
        when(item.priorita) {
            1 -> priorita = getString(R.string.item_priorita_value_bassa)
            2 -> priorita = getString(R.string.item_priorita_value_media)
            3 -> priorita = getString(R.string.item_priorita_value_alta)
        }
        tv_itemdetails_priority_value.text = priorita
        tv_itemdetails_added_value.text = Util.timeToText(item.dataImmissione, applicationContext)

        // listener for the FAB edit
        item_details_fab_edit.setOnClickListener {
            // open the view to edit the item (the same used for create a new one)
            val editItem = Intent(applicationContext, AddListaitem::class.java)
            editItem.putExtra("item", item)
            startActivity(editItem)
        }
    }

    /**
     * onBack button pressed
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
