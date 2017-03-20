/*!
 * @author Brendan Beerman
 *
 * @brief LocationActivity          Gets the users location and verifies its existence
 */

package com.example.brendan.weatherapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends AppCompatActivity
{
    private boolean isEnglish;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            String languageValue = extras.getString("Language");
            String locationValue = extras.getString("Location");

            isEnglish = languageValue.equals("english");

            setTextLanguage();
            setTextLocation(locationValue);
        }
    }

    /*!
     * @brief setTextLanguage       If the user's language is set to french the button text's are changed
     */
    private void setTextLanguage()
    {
        TextView currentTextView;
        Button currentButton;

        if (!isEnglish)
        {
            currentTextView = (TextView) findViewById(R.id.cityTextView);
            currentTextView.setText("Ville: ");

            currentTextView = (TextView) findViewById(R.id.provinceTextView);
            currentTextView.setText("Province: ");

            currentButton = (Button) findViewById(R.id.submitLocationButton);
            currentButton.setText("Soumettre");
        }
    }

    /*!
     * @brief setTextLocation       If a user has already set a location the EditTexts are filled with the location
     *
     * @param location              A string of the province and city names
     */
    private void setTextLocation(String location)
    {
        EditText currentEditText;
        String [] splitLocation = location.split(",");

        if (!splitLocation[0].equals(" "))
        {
            currentEditText = (EditText) findViewById(R.id.cityEditText);
            currentEditText.setText(splitLocation[1]);

            currentEditText = (EditText) findViewById(R.id.provinceEditText);
            currentEditText.setText(splitLocation[2]);
        }
    }

    /*!
     * @brief displayToast          Displays a toast on the success or failure of a location being found or not being found
     *
     * @param isError               A boolean of whether the location was valid, true => location was not found
     *                                                                           false => location was found
     * @param city                  A string of the city that the user was searching for
     */
    private void displayToast(boolean isError, String city)
    {
        Toast toast;

        if (isError)
        {
            toast = Toast.makeText(getApplicationContext(), ((isEnglish) ? (" This is an invalid location ") : (" Il s'agit d'un emplacement non valide ")), Toast.LENGTH_SHORT);
            toast.getView().setBackgroundColor(Color.RED);
            toast.setGravity(Gravity.CENTER | Gravity.TOP, 25, 50);
        }
        else
            toast = Toast.makeText(getApplicationContext(), ((isEnglish) ? (city + " is a location ") : (city + " est un endroit ")), Toast.LENGTH_SHORT);

        toast.setGravity(Gravity.CENTER | Gravity.TOP, 25, 50);
        toast.show();
    }

    /*!
     * @brief submitLocationClicked         The action that occurs if the submit button is clicked
     */
    public void submitLocationClicked(View view)
    {
        EditText cityText = (EditText) findViewById(R.id.cityEditText);
        EditText provinceText = (EditText) findViewById(R.id.provinceEditText);

        String [] location = csvHandler.checkLocationExistence(getApplication().getApplicationContext(), cityText.getText().toString(), provinceText.getText().toString(), isEnglish);

        // the location was not found, otherwise return to the main activity
        if (location[0].equals(" "))
            displayToast(true, "");
        else
        {
            displayToast(false, location[1]);

            Intent intent = new Intent();
            intent.putExtra("Location", location[0] + "," + location[1] + "," + location[2]);
            setResult(RESULT_OK, intent);

            finish();
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();

        intent.putExtra("Location", " , , ");

        setResult(RESULT_OK, intent);

        finish();
    }
}
