package it.paggiapp.familyshopping

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_add_listaitem.*

class AddListaitem : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_listaitem)

        // set up the toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
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
