package umd.letsgo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        //need listener for create submit

        //need listener for reset

        //need listener for cancel

    }


    private class GoogleLocationAsynchTask extends AsyncTask<String, Void, String[]> {

    @Override
        protected String[] doInBackground(String... params) {

            String URL= "http://maps.google.com/maps/api/geocode/json?address= "+ params[0] + "&sensor=false";

            String response;
            try {
                response = getLatLongByURL(URL);

                Log.d(" Lets see the response ", "" + response);

                return new String[]{response};
            } catch (Exception e) {
                return new String[]{"error"};
            }
        }

        @Override
        protected void onPostExecute(String... result) {

            double longitude = 0.0;
            double latitude = 0.0;


            try {
                JSONObject jsonObject = new JSONObject(result[0]);

                longitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");


                Log.d("longitude obtained", "" + longitude);
                Log.d("latitude obtained", "" + latitude);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        public String getLatLongByURL(String requestURL) {

            URL url;
            String answer = "";

            try {
                url = new URL(requestURL);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setDoOutput(true);
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        answer += line;
                    }
                } else {
                    answer = "";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return answer;
        }
    }
}