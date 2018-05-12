package example.nicola.giallozafferanoparser;

import org.apache.commons.lang3.StringUtils;

public class QueryFormatter {

    /**
     * Metodo che formatta bene la stringa per la ricerca in giallo zafferano
     * @param q query da formattare
     * @return la stringa messa bene
     */
    public static String getQueryParsed(String q) {
        String res = q.replace(" ", "+");
        res = StringUtils.stripAccents(res);
        return res;
    }
}
