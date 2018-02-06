package it.paggiapp.familyshopping

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import it.paggiapp.familyshopping.backend.Comunication
import it.paggiapp.familyshopping.data.Utente
import it.paggiapp.familyshopping.database.DataStore
import it.paggiapp.familyshopping.intro.IntroActivity
import it.paggiapp.familyshopping.util.Util
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    companion object {
        val REQUEST_CODE_INTRO : Int = 300
        val DELAY_SNACKBAR : Long = 1500
    }

    /**
     * onCreate method
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // INIT the database and get reference of it for next uses
        DataStore.init(applicationContext)

        // if user is not logged then IntroActivity
        if(!Util.isUserLogged(applicationContext)) {
            startActivityForResult(Intent(applicationContext, IntroActivity::class.java), REQUEST_CODE_INTRO)
        }
        else {
            setUpMain()
        }
    }

    /**
     * onActiviy result for result by the IntroActivity
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_INTRO) {
            if (resultCode != Activity.RESULT_OK) {
                finish()
            }
            else {
                setUpMain()
            }
        }
    }

    /**
     * Creo il menu
     *
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Listener per il menu
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item == null) return false
        val id = item.itemId
        if(id == R.id.orderby_lista) {
            // filtra
            val fragmentModalBottomSheet = ModalOrderBy()
            fragmentModalBottomSheet.show(supportFragmentManager, "BottomSheet Fragment")
        }

        return super.onOptionsItemSelected(item)
    }*/

    /**
     * Function that download all the database data for the first time
     */
    fun updateUtente() {
        val utente = Util.getUser(applicationContext)

        val requestParams = RequestParams()
        requestParams.put(Comunication.UpdateUtente.ID_LABEL, utente.id)
        requestParams.put(Comunication.UpdateUtente.CODE_LABEL, utente.codiceFamiglia)
        requestParams.put(Comunication.UpdateUtente.IDS_LABEL, Gson().toJson(
                DataStore.getDB().getUtentiId()
        ))

        val client = AsyncHttpClient()

        client.post(applicationContext, Comunication.UpdateUtente.URL, requestParams, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: JSONObject?) {
                // prendo la lista degli UTENTI (membri della famiglia)
                val list = ArrayList<Utente>()
                val userInfo : JSONArray = responseString!!.getJSONArray(Comunication.UpdateUtente.ARRAY_USER)
                for (i in 0..(userInfo.length() - 1)) {
                    val u = Utente(userInfo.getJSONObject(i).getInt("id"),
                            userInfo.getJSONObject(i).getString("nome"),
                            userInfo.getJSONObject(i).getString("email"),
                            userInfo.getJSONObject(i).getInt("codiceFamiglia"))
                    list.add(u)
                }

                // eseguo le operazioni sul database locale
                DataStore.execute{
                    DataStore.getDB().salvaUtenti(list)
                }

            }
        })
    }

    /**
     * Function thats set up all thing in the main activity
     */
    fun setUpMain() {
        // set up the bottom navigation bar
        val item1 = AHBottomNavigationItem(R.string.bn_item1, R.drawable.ic_shopping_cart_black_24dp, R.color.bn_default_color)
        val item2 = AHBottomNavigationItem(R.string.bn_item2, R.drawable.ic_folder_black_24dp, R.color.bn_default_color)
        val item3 = AHBottomNavigationItem(R.string.bn_item3, R.drawable.ic_person_black_24dp, R.color.bn_default_color)
        bottom_navigation.addItem(item1)
        bottom_navigation.addItem(item2)
        bottom_navigation.addItem(item3)

        bottom_navigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW)
        bottom_navigation.setCurrentItem(0)
        bottom_navigation.accentColor = ContextCompat.getColor(applicationContext,R.color.colorPrimary)
        bottom_navigation.manageFloatingActionButtonBehavior(fab_main)
        bottom_navigation.isBehaviorTranslationEnabled = true

        // listener for tab click
        bottom_navigation.setOnTabSelectedListener { position, wasSelected ->
            Log.d("Tab selected", "$position")
            true
        }

        // display the first fragment
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, ListaFragment.newInstance())
                .commit()

        if(Util.isOnline(applicationContext)) {
            // Start the process that updates the UTENTI
            updateUtente()
        }
        else {
            Handler().postDelayed(Runnable {
                Snackbar.make(bottom_navigation, R.string.no_internet, Snackbar.LENGTH_SHORT).show()
            }, DELAY_SNACKBAR)
        }
    }
}
