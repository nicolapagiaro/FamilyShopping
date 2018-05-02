package it.paggiapp.familyshopping.listaspesa

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import it.paggiapp.familyshopping.R
import it.paggiapp.familyshopping.data.Carrello
import it.paggiapp.familyshopping.util.Util
import kotlinx.android.synthetic.main.activity_show_lista_item_details.*
import android.view.ViewAnimationUtils
import android.animation.Animator
import android.os.Build
import android.support.v4.view.ViewCompat
import android.view.View
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import it.paggiapp.familyshopping.backend.ServerHelper
import java.lang.Exception

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
        tv_itemdetails_category_value.text = item.categoria?.nome

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

        // load the image if there is internet connection
        Picasso.get()
                .load(ServerHelper.ABSOLUTE_URL + item.categoria!!.immagine)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(show_item_header_img, object : Callback{
                    override fun onError(e: Exception?) {}

                    override fun onSuccess() {
                        if(ViewCompat.isAttachedToWindow(show_item_header_img)) {
                            showImage()
                        }
                    }
                })

        // listener for the FAB edit
        item_details_fab_edit.setOnClickListener {
            // open the view to edit the item (the same used for create a new one)
            val editItem = Intent(applicationContext, AddListaitem::class.java)
            editItem.putExtra("item", item)
            startActivityForResult(editItem, AddListaitem.ADDLISTITEM_CODE)
        }
    }

    /**
     * onBack button pressed
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * onActiviy result for result by the IntroActivity
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == AddListaitem.ADDLISTITEM_CODE){
            if(resultCode == Activity.RESULT_OK) {
                // finish activity
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    /**
     * Funzione per fare l'effetto di circle reveal dell'immagine dell'header
     */
    private fun showImage() {
        // get the center for the clipping circle
        val x = app_bar.getRight() / 2
        val y = app_bar.getBottom() / 2

        val startRadius = 0F
        val endRadius  = Math.hypot(app_bar.getWidth().toDouble(), app_bar.getHeight().toDouble()).toFloat()

        val anim : Animator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewAnimationUtils.createCircularReveal(show_item_header_img, x, y, startRadius, endRadius)
        } else {
            null
        }

        show_item_header_img.visibility = View.VISIBLE
        anim?.start()
    }

    /**
     * Oggetti statici
     */
    companion object {
        val SHOWITEM_CODE = 555
        val WAIT_TIME = 500L
    }
}
