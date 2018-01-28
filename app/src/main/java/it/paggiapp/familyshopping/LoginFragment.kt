package it.paggiapp.familyshopping

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heinrichreimersoftware.materialintro.app.SlideFragment
import com.loopj.android.http.*
import kotlinx.android.synthetic.main.fragment_intro_login.*
import cz.msebera.android.httpclient.Header
import it.paggiapp.familyshopping.util.EmailValidator
import it.paggiapp.familyshopping.util.Util
import org.json.JSONObject
import android.net.ConnectivityManager
import android.support.design.widget.Snackbar

/**
 * Fragment per il login dell'utente
 * Created by nicola on 23/01/18.
 */
class LoginFragment : SlideFragment() {
    var email : String = ""
    var password : String = ""

    companion object {
        /**
         * Gives a new instance of the fragment
         */
        fun newInstance() : LoginFragment {
            return LoginFragment()
        }
    }


    /**
     * onCreate function
     */
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_intro_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // listener for the login button
        btn_login.setOnClickListener {

            // check for internet connection
            if(!isOnline()) {
                Snackbar.make(introActivity.contentView, R.string.no_internet , Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // check if the login is valid
            if(isLoginValid()) {
                Log.d("Login", "started")
                btn_login.text = getString(R.string.action_login_fetching)
                btn_login.isClickable = false

                val requestParams = RequestParams()
                requestParams.put("login_step", Util.LOGIN_FIRST_STEP)
                requestParams.put("email", email)
                requestParams.put("pssw", password)

                val client = AsyncHttpClient()

                client.post(context, Util.LOGIN_URL, requestParams, object : JsonHttpResponseHandler() {

                    override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: JSONObject?) {
                        Log.d("Login ok", responseString?.toString())
                        if(responseString?.getInt("mode") == 0) {
                            // new user
                            if(responseString.getInt("result") == 0) {
                                btn_login.text = getString(R.string.action_login_done)
                                (introActivity as IntroActivity).setIsNewUser(true)
                                Util.loginUser(context)
                            }
                            else {
                                btn_login.text = getString(R.string.action_login)
                            }
                        }
                        else {
                            // old user
                            if(responseString?.getInt("result") == 0) {
                                // memorizzo tutti i dati
                                btn_login.text = getString(R.string.action_login_done)
                                (introActivity as IntroActivity).setIsNewUser(false)
                                Util.loginUser(context)
                            }
                            else {
                                btn_login.text = getString(R.string.action_login)
                            }
                        }

                    }

                    override fun onFailure(statusCode: Int, headers: Array<out Header>?, errorResponse: String?, throwable: Throwable?) {
                        Log.d("Login no", errorResponse)
                        btn_login.text = getString(R.string.action_login)
                    }
                })
            }

        }
    }

    /**
     * Function to check if the input by the user is valid
     */
    private fun isLoginValid(): Boolean {
        email = et_email.text.toString().trim()
        password = et_password.text.toString().trim()

        if(!EmailValidator.validateEmail(email)) {
            wrapper_email.isErrorEnabled  = true
            wrapper_email.error = getString(R.string.error_invalid_email)
            return false
        }
        else {
            wrapper_email.error = null
            wrapper_email.isErrorEnabled = false
        }

        if(password.length <= Util.PSSW_MIN_CHAR) {
            wrapper_password.isErrorEnabled = true
            wrapper_password.error = getString(R.string.error_invalid_password)
            return false
        }
        else {
            wrapper_password.error = null
            wrapper_password.isErrorEnabled = false
        }

        return true
    }

    /**
     * Function to check if there is connection
     */
    private fun isOnline(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
}