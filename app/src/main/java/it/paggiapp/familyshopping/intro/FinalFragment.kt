package it.paggiapp.familyshopping.intro

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heinrichreimersoftware.materialintro.app.SlideFragment
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import it.paggiapp.familyshopping.R
import it.paggiapp.familyshopping.backend.Comunication.*
import it.paggiapp.familyshopping.data.Utente
import it.paggiapp.familyshopping.util.Util
import kotlinx.android.synthetic.main.fragment_intro_final.*
import org.json.JSONObject

/**
 * Third slide of IntroActivity
 * Created by nicola on 28/01/18.
 */
class FinalFragment : SlideFragment() {
    var savedFamilyCode: Boolean = false

    companion object {
        /**
         * Gives a new instance of the fragment
         */
        fun newInstance(): FinalFragment {
            return FinalFragment()
        }
    }

    /**
     * onCreate function
     */
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_intro_final, container, false)
    }

    /**
     * Function that tell me if the frag is visible
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && !Util.isNewUser(context)) {
            displayOldUser()
        }
    }

    /**
     * onActivityCreated fun
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // click lister for the SAVE button
        btn_save.setOnClickListener {

            if (isCodeValid()) {

                if (Util.isNewUser(context)) {
                    btn_save.text = getText(R.string.action_save_fetching)
                    btn_save.isClickable = false
                    val requestParams = RequestParams()
                    val client = AsyncHttpClient()

                    val insertCode = et_familycode.text.toString()
                    if (insertCode.length != 5) { //CREATE A NEW CODE
                        requestParams.put(Login.LOGIN_STEP, Login.LOGIN_SECOND_STEP)
                        requestParams.put(Login.NEWUSER_ID_FIELD, Util.getUser(context).id)
                        client.post(context, Login.LOGIN_URL, requestParams, object : JsonHttpResponseHandler() {

                            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: JSONObject?) {

                                if (responseString?.getInt(Login.SUCCESS_FIELD) == 0) {
                                    // CODE OK
                                    btn_save.text = getText(R.string.action_save_done)

                                    // prendo il codice e lo salvo nelle sharedPref
                                    val code: Int = responseString.getInt("code")
                                    val u = Util.getUser(context)
                                    u.codiceFamiglia = code
                                    Util.saveUser(context, u)
                                    savedFamilyCode = true

                                    // lo mostro all'utente
                                    et_familycode.append(code.toString())

                                    // user is logged
                                    Util.loginUser(context)
                                } else {
                                    // CODE ERROR
                                    showLastGeneralError()
                                }
                            }

                            override fun onFailure(statusCode: Int, headers: Array<out Header>?, errorResponse: String?, throwable: Throwable?) {
                                showLastGeneralError()
                            }

                            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                                showLastGeneralError()
                            }
                        })
                    } else { // UPDATE THE NEW USER OF NEW CODE
                        requestParams.put(Login.LOGIN_STEP, Login.LOGIN_SECOND_STEP2)
                        requestParams.put(Login.NEWUSER_ID_FIELD, Util.getUser(context).id)
                        requestParams.put(Login.CODE_LABEL, et_familycode.text.toString())

                        client.post(context, Login.LOGIN_URL, requestParams, object : JsonHttpResponseHandler() {

                            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: JSONObject?) {

                                if (responseString?.getInt(Login.SUCCESS_FIELD) == 0) {
                                    // CODE OK
                                    btn_save.text = getText(R.string.action_save_done)

                                    val u = Util.getUser(context)
                                    u.codiceFamiglia = Integer.parseInt(insertCode)
                                    Util.saveUser(context, u)
                                    savedFamilyCode = true

                                    // user is logged
                                    Util.loginUser(context)
                                } else {
                                    // CODE ERROR
                                    showLastGeneralError()
                                }
                            }

                            override fun onFailure(statusCode: Int, headers: Array<out Header>?, errorResponse: String?, throwable: Throwable?) {
                                showLastGeneralError()
                            }

                            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                                showLastGeneralError()
                            }
                        })
                    }

                } else {
                    Util.loginUser(context)
                    btn_save.text = getText(R.string.action_save_done)
                    savedFamilyCode = true
                }
            }
        }
    }

    /**
     * Display the label for OLD USER
     */
    fun displayOldUser() {
        // retrieve old user info
        val utente: Utente = Util.getUser(context)

        tv_loginfinal_title.text = getString(R.string.intro_title2_old)
        tv_loginfinal_subtitle.text = getString(R.string.intro_subtitle2_old)
        et_familycode.append(utente.codiceFamiglia.toString())
    }

    /**
     * Verify the validity of the familyCode
     */
    fun isCodeValid(): Boolean {
        val code = et_familycode.text.toString()

        if (code.length < 5 && code.length != 0) {
            wrapper_familycode.isErrorEnabled = true
            wrapper_familycode.error = getString(R.string.error_invalid_familycode)
            return false
        } else {
            wrapper_familycode.error = null
            wrapper_familycode.isErrorEnabled = false
        }

        return true
    }

    /**
     * If the user click the button then he can go forward
     */
    override fun canGoForward(): Boolean {
        return savedFamilyCode
    }

    /**
     * Function that show to user the general error
     */
    private fun showLastGeneralError() {
        btn_save.isClickable = true
        btn_save.text = getString(R.string.action_save)
        Snackbar.make(introActivity.contentView, R.string.error_login, Snackbar.LENGTH_LONG).show()
    }

}