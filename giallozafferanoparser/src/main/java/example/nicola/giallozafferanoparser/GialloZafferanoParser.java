package example.nicola.giallozafferanoparser;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class GialloZafferanoParser {
    private static final String STR_TO_REPLACE = "%searchkey%";
    private static final String BASE_SEARCH_URL = "https://www.giallozafferano.it/ricerca-ricette/" + STR_TO_REPLACE + "/";
    private static String linkToNextPage;

    /**
     * Metodo che lancia un async task che chiede tutte le ricette
     * @param query ricetta da cercare
     * @param callback callback da eseguire quando ha finito il caricamento
     */
    public static void getRicette(String query, RicetteResponse callback) {
        new GetRicette(callback, QueryFormatter.getQueryParsed(query)).execute();
    }

    /**
     * Classe dell'AsyncTask per le ricette
     */
    static class GetRicette extends AsyncTask<Void, Void, ArrayList<Ricetta>> {
        private static final String DIFFICOLTA_LABEL = "Difficoltà: ";
        private static final String PREPARAZIONE_LABEL = "Preparazione: ";
        private static final String COTTURA_LABEL = "Cottura: ";
        private RicetteResponse mCallback;
        private String query;

        /**
         * Costruttore
         * @param mCallback callback da eseguire quando finito
         * @param query stringa da cercare
         */
        GetRicette(RicetteResponse mCallback, String query) {
            this.mCallback = mCallback;
            this.query = query;
        }

        @Override
        protected ArrayList<Ricetta> doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(BASE_SEARCH_URL.replace(STR_TO_REPLACE, query)).get();

                ArrayList<Ricetta> res = new ArrayList<>();
                Elements article = doc.getElementsByClass("flex");
                for (Element e : article) {
                    // non voglio prendere il primo risultato
                    if(e.hasClass("consiglia") || !e.hasAttr("data-recipe")) {
                        continue;
                    }

                    Ricetta t = new Ricetta();
                    t.setTitolo(e.getElementsByClass("title-recipe").text());
                    t.setDescrizione(e.getElementsByTag("p").eq(1).text());
                    t.setLink(e.getElementsByClass("close").attr("href"));
                    t.setDifficolta(e.getElementsByClass("difficolta")
                            .text()
                            .replace(DIFFICOLTA_LABEL, ""));
                    t.setTempoPreparazione(e.getElementsByClass("preptime")
                            .text()
                            .replace(PREPARAZIONE_LABEL, ""));
                    t.setTempoCottura(e.getElementsByClass("cooktime")
                            .text()
                            .replace(COTTURA_LABEL, ""));
                    t.setImmagine(e.getElementsByTag("img").attr("src"));
                    res.add(t);
                }

                return res;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Ricetta> ricettas) {
            super.onPostExecute(ricettas);
            if(mCallback != null) {
                mCallback.onResult(ricettas);
            }
        }
    }

    public interface RicetteResponse {
        void onResult(ArrayList<Ricetta> res);
    }
}
