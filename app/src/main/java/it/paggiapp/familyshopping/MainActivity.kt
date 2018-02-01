package it.paggiapp.familyshopping

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import it.paggiapp.familyshopping.database.DataStore
import it.paggiapp.familyshopping.util.Util
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val REQUEST_CODE_INTRO : Int = 300
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // if user is not logged then IntroActivity
        if(!Util.isUserLogged(applicationContext)) {
            startActivityForResult(Intent(applicationContext, IntroActivity::class.java), REQUEST_CODE_INTRO)
        }

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

        // listener for tab click
        bottom_navigation.setOnTabSelectedListener(object : AHBottomNavigation.OnTabSelectedListener{
            override fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {
                Log.d("Tab selected", "$position")
                return true
            }
        })
    }

    /**
     * onActiviy result for result by the IntroActivity
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_INTRO) {
            if (resultCode == Activity.RESULT_OK) {
                // Finished the intro, build the database
                DataStore.init(applicationContext)
            } else {
                finish()
            }
        }
    }
}
