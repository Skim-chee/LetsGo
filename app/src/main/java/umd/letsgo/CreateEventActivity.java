package umd.letsgo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class CreateEventActivity extends Activity {

    private static final String TAG = "Create Event Interface";
    // 7 days in milliseconds - 7 * 24 * 60 * 60 * 1000
    private static final int SEVEN_DAYS = 604800000;
    private static final int RESULT_UPLOAD_IMAGE = 1;

    private static String timeString;
    private static String dateString;
    private static TextView dateView;
    private static TextView timeView;
    private static TextView uploadView;

    private Date mDate;
    private EditText mEventName;
    private EditText mEventAddress;
    private EditText mEventDescription;
    private String encodedImage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        //need listener for create submit
        //need listener for reset
        //need listener for cancel
        mEventName = (EditText) findViewById(R.id.title);
        mEventAddress = (EditText) findViewById(R.id.enteredLocation);
        mEventDescription = (EditText) findViewById(R.id.description);
        dateView = (TextView) findViewById(R.id.date);
        timeView = (TextView) findViewById(R.id.time);
        uploadView = (TextView) findViewById(R.id.eventImageUpload);

        // Set the default date and time
        setDefaultDateTime();

        // OnClickListener for the Date button, calls showDatePickerDialog() to
        // show the Date dialog

        final Button datePickerButton = (Button) findViewById(R.id.date_picker_button);
        datePickerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // OnClickListener for the Time button, calls showTimePickerDialog() to
        // show the Time Dialog

        final Button timePickerButton = (Button) findViewById(R.id.time_picker_button);
        timePickerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        // OnClickListener for the Cancel Button,
        final Button uploadButton = (Button) findViewById(R.id.uploadEventImage);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO - Indicate result and finish
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_UPLOAD_IMAGE);
            }
        });



        final Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO - Indicate result and finish
                Intent intento = new Intent();
                setResult(RESULT_CANCELED,intento);
                finish();

            }
        });

        // TODO - Set up OnClickListener for the Reset Button
        final Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO - Reset data to default values
                mEventName.getText().clear();
                mEventAddress.getText().clear();
                mEventDescription.getText().clear();
                // reset date and time
                setDefaultDateTime();

            }
        });

        // Set up OnClickListener for the Submit Button

        final Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // gather Event data
                String name = mEventName.getText().toString();
                String address = mEventAddress.getText().toString();
                String description = mEventDescription.getText().toString();
                // Construct the Date string
                String fullDate = dateString + " " + timeString;

                if (encodedImage != null){
                    new GoogleLocationAsynchTask().execute(name, address, description, fullDate, encodedImage);
                } else {
                    Toast.makeText(v.getContext(), "Please Upload Event Image", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_UPLOAD_IMAGE
                && resultCode == RESULT_OK
                && data != null){
            Uri selectedImage = data.getData();
            //String path = getPathPic(selectedImage);
            try {
                InputStream is = getContentResolver().openInputStream(selectedImage);
                Bitmap image = BitmapFactory.decodeStream(is);
                is.close();
                encodedImage = selectedImage.toString();
                //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                //image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                //encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Image not Found", Toast.LENGTH_LONG).show();
            }


            //toast uploaded image
            Toast.makeText(this, "Image successfully uploaded", Toast.LENGTH_LONG).show();
        } else {
            //toast fail to upload image
            Toast.makeText(this, "Fail to Upload Image", Toast.LENGTH_LONG).show();
        }
    }

    private String getPathPic(Uri uri) {
        String[]  data = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getBaseContext(), uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private void setDefaultDateTime() {

        // Default is current time + 7 days
        mDate = new Date();
        mDate = new Date(mDate.getTime() + SEVEN_DAYS);

        Calendar c = Calendar.getInstance();
        c.setTime(mDate);

        setDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));

        dateView.setText(dateString);

        setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                c.get(Calendar.MILLISECOND));

        timeView.setText(timeString);
    }

    private static void setDateString(int year, int monthOfYear, int dayOfMonth) {

        // Increment monthOfYear for Calendar/Date -> Time Format setting
        monthOfYear++;
        String mon = "" + monthOfYear;
        String day = "" + dayOfMonth;

        if (monthOfYear < 10)
            mon = "0" + monthOfYear;
        if (dayOfMonth < 10)
            day = "0" + dayOfMonth;

        dateString = year + "-" + mon + "-" + day;
    }

    private static void setTimeString(int hourOfDay, int minute, int mili) {
        String hour = "" + hourOfDay;
        String min = "" + minute;

        if (hourOfDay < 10)
            hour = "0" + hourOfDay;
        if (minute < 10)
            min = "0" + minute;

        timeString = hour + ":" + min + ":00";
    }

//    private String getToDoTitle() {
//        return mTitleText.getText().toString();
//    }


    // DialogFragment used to pick a ToDoItem deadline date

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            setDateString(year, monthOfYear, dayOfMonth);

            dateView.setText(dateString);
        }

    }

    // DialogFragment used to pick a ToDoItem deadline time

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return
            return new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            setTimeString(hourOfDay, minute, 0);

            timeView.setText(timeString);
        }
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    private class GoogleLocationAsynchTask extends AsyncTask<String, Void, String[]> {

    @Override
        protected String[] doInBackground(String... params) {

            String key="AIzaSyCv73k2d8V-Muf9Xp-6vlqsWoLvPADb8hY";
            String local= params[1].replaceAll(" ", "+").toLowerCase();
            String URL= "https://maps.google.com/maps/api/geocode/json?address="+ local + "&key=" + key;
            String response;


            try {

                Log.d(TAG , " this is the url: " + URL);

                response = getLatLongByURL(URL);

                Log.d(TAG , " this is the response: " + response);

                return new String[]{response, params[0],params[1],params[2],params[3],params[4]};
            } catch (Exception e) {
                return new String[]{"error"};
            }
        }

        @Override
        protected void onPostExecute(String... result) {

            double longitude = 0.0;
            double latitude = 0.0;


            try {
                String name = result[1];
                String address = result[2];
                String description = result[3];
                String fullDate = result[4];
                String image = result[5];

                Log.d("Event ", "RESULT 0" + result[0]);

                Log.d("Event ", "name :"+ name +". address:"+ address +
                        ". description:"+ description +". fullDate:"+ fullDate + ". image:" + image);

                JSONObject jsonObject = new JSONObject(result[0]);

                longitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");


                Log.d("longitude obtained", "longitude obtained :" + longitude);
                Log.d("latitude obtained", "latitude obtained: " + latitude);


                // Package ToDoItem data into an Intent
                Intent data = new Intent();
                //ADDD INTENT TO GET THE LATITUD AND LONGITUD
                //USE ASYNTASK

                Event.packageIntent(data, name, address, description, fullDate,
                        Double.toString(longitude),  Double.toString(latitude), image);

                // TODO - return data Intent and finish
                setResult(RESULT_OK, data);
                finish();


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

                Log.d("Event ", "RESPONSE CODE::  " + responseCode);

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