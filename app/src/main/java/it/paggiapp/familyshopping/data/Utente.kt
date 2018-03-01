package it.paggiapp.familyshopping.data

import java.io.Serializable

/**
 * Created by nicola on 29/01/18.
 */
data class Utente(
        val id : Int,
        var nome : String?,
        val email : String,
        var codiceFamiglia : Int,
        var nomeFamiglia: String?
) : Serializable {
    companion object {
        val NEW_USER = "new_user"
        val ID_LABEL = "id"
        val NOME_LABEL = "nome"
        val EMAIL_LABEL = "email"
        val NOME_FAM_LABEL = "nomeFamiglia"
        val CODFAM_LABEL = "codiceFamiglia"
    }
}