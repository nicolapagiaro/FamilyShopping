package it.paggiapp.familyshopping

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import it.paggiapp.familyshopping.util.Util

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        if(!Util.isUserLogged(applicationContext)) {
            startActivity(Intent(applicationContext, IntroActivity::class.java))
        }
    }
}
