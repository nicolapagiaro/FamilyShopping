package it.paggiapp.familyshopping

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        // metto il button back
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // carico l'immagine
        Picasso.get()
                .load("http://quizapp.000webhostapp.com/familyshopping/images/ico.png")
                .into(ivIco)
    }

    /**
     * onBack button pressed
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
