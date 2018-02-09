package it.paggiapp.familyshopping.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java email validation program
 * @author pankaj
 *
 */
public class EmailValidator {
    // Email Regex java
    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

    // static Pattern object, since pattern is fixed
    private static Pattern pattern;

    /**
     * This method validates the input email address with EMAIL_REGEX pattern
     * @param email
     * @return boolean
     */
    public static boolean validateEmail(String email) {
        pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}