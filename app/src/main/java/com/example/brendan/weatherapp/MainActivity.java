/*!
 * @author Brendan Beerman
 *
 * @brief MainActivity
 */

package com.example.brendan.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private static SharedPreferences sharedPreferences;
    private static final String languageAndLocationPreference = "userPreferences";
    private static final String languageKey = "languageKey";
    private static final String locationKey = "locationKey";
    private static final String titlesKey = "titlesKey";
    private static final String summariesKey = "summariesKey";

    private static final int languageActivityRequestCode = 1;
    private static final int locationActivityRequestCode = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(languageAndLocationPreference, this.MODE_PRIVATE);

        setUpDriver();
    }

    /*!
     * @brief setUpDriver       Driver to set up language, weather text, and location text
     */
    private void setUpDriver()
    {
        setTextLanguage();
        setTextLocation();

        String [] location = sharedPreferences.getString(locationKey, " , , ").split(",");

        // no location selected
        if (!location[0].equals(" "))
        {
            new retrieveAndParseXML().execute(location[0]);
            setWeatherText();
        }
    }

    /*!
     * @brief setTextLanguage           Sets the language to English or French
     */
    private void setTextLanguage()
    {
        String language = sharedPreferences.getString(languageKey, "english");
        Button currentButton;

        if (language.equals("english"))
        {
            currentButton = (Button) findViewById(R.id.setLocationButton);
            currentButton.setText("Set Location\n");

            currentButton = (Button) findViewById(R.id.setLanguageButton);
            currentButton.setText("Set Language\n");

            currentButton = (Button) findViewById(R.id.specialStatementButton);
            currentButton.setText("Special Weather Statement: \n");

            currentButton = (Button) findViewById(R.id.currentConditionsButton);
            currentButton.setText("Current Conditions: \n");

            currentButton = (Button) findViewById(R.id.todayButton);
            currentButton.setText("Today's Forecast: \n");

            currentButton = (Button) findViewById(R.id.tonightButton);
            currentButton.setText("Tonight's Forecast: \n");

            currentButton = (Button) findViewById(R.id.sixDaysButton);
            currentButton.setText("Six Day Forecast: \n");
        }
        else
        {
            currentButton = (Button) findViewById(R.id.setLocationButton);
            currentButton.setText("Définir l'emplacement\n");

            currentButton = (Button) findViewById(R.id.setLanguageButton);
            currentButton.setText("Définir la langue\n");

            currentButton = (Button) findViewById(R.id.specialStatementButton);
            currentButton.setText("Déclaration météorologique spéciale: \n");

            currentButton = (Button) findViewById(R.id.currentConditionsButton);
            currentButton.setText("Conditions actuelles: \n");

            currentButton = (Button) findViewById(R.id.todayButton);
            currentButton.setText("Prévisions du jour: \n");

            currentButton = (Button) findViewById(R.id.tonightButton);
            currentButton.setText("Prévisions du soir: \n");

            currentButton = (Button) findViewById(R.id.sixDaysButton);
            currentButton.setText("Prévisions à six jours\n");
        }
    }

    /*!
     * @brief setTextLocation       Tells the user their selected location if they have one selected
     */
    private void setTextLocation()
    {
        String [] location = sharedPreferences.getString(locationKey, " , , ").split(",");

        TextView locationTextView = (TextView) findViewById(R.id.locationSetTextView);

        locationTextView.setText(((location[0].equals(" ")) ? ("") : (location[1] + ", " + location[2])));
    }

    /*!
     * @brief setWeatherInformation
     */
    public static void setWeatherInformation(ArrayList<WeatherDetails> weatherDetails)
    {
        String [] titles = sortOutDetails(weatherDetails, true);
        String [] summaries = sortOutDetails(weatherDetails, false);

        String combinedTitles = combineStringArrayBy(titles, "::");

        String combinedSummaries = combineStringArrayBy(summaries, "::");

        addWeatherToSharedPreferences(combinedTitles, combinedSummaries);
    }

    // sets the weather titles onto the buttons of the activity
    private void setWeatherText()
    {
        String [] weatherTitles = sharedPreferences.getString(titlesKey, " ").split("::");

        StringBuilder newButtonTextBuilder = new StringBuilder();

        Button currentButton;

        boolean isEnglish = sharedPreferences.getString(languageKey, "english").equals("english");

        if (!weatherTitles[0].equals(" "))
        {
            currentButton = (Button) findViewById(R.id.specialStatementButton);
            newButtonTextBuilder.append(currentButton.getText().toString());

            if (isEnglish)
            {
                String [] tempSplit = weatherTitles[0].split(",");

                if (tempSplit[0].equals("No watches or warnings in effect"))
                    newButtonTextBuilder.append("None");
                else
                    newButtonTextBuilder.append("Yes");
            }
            else
            {
                String [] tempSplit = weatherTitles[0].split(",");

                if (tempSplit[0].equals("No watches or warnings in effect"))
                    newButtonTextBuilder.append("Aucun");
                else
                    newButtonTextBuilder.append("Oui");
            }

            currentButton.setText(newButtonTextBuilder.toString());
        }
        if (!weatherTitles[1].equals(" "))
        {
            newButtonTextBuilder = new StringBuilder();

            currentButton = (Button) findViewById(R.id.currentConditionsButton);
            newButtonTextBuilder.append(currentButton.getText().toString());

            String [] tempSplit = weatherTitles[1].split(", ");

            newButtonTextBuilder.append(tempSplit[1]);

            currentButton.setText(newButtonTextBuilder.toString());
        }
        if (!weatherTitles[2].equals(" "))
        {
            newButtonTextBuilder = new StringBuilder();

            currentButton = (Button) findViewById(R.id.todayButton);
            newButtonTextBuilder.append(currentButton.getText().toString());

            String [] tempSplit = weatherTitles[2].split(": ");

            newButtonTextBuilder.append(tempSplit[1]);

            currentButton.setText(newButtonTextBuilder.toString());
        }
        if (!weatherTitles[3].equals(" "))
        {
            newButtonTextBuilder = new StringBuilder();

            currentButton = (Button) findViewById(R.id.tonightButton);
            newButtonTextBuilder.append(currentButton.getText().toString());

            String [] tempSplit = weatherTitles[3].split(": ");

            newButtonTextBuilder.append(tempSplit[1]);

            currentButton.setText(newButtonTextBuilder.toString());
        }
    }

    // Adds the weather titles and summaries to the shared preferences
    private static void addWeatherToSharedPreferences(String combinedTitles, String combinedSummaries)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(titlesKey, combinedTitles);
        editor.putString(summariesKey, combinedSummaries);

        editor.commit();
    }

    // combines a given string array by another string
    private static String combineStringArrayBy(String [] toCombine, String combiner)
    {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < toCombine.length; i++)
        {
            if (i + 1 == toCombine.length)
                stringBuilder.append(toCombine[i]);
            else
                stringBuilder.append(toCombine[i] + combiner);
        }

        return stringBuilder.toString();
    }

    // Stores the title or summary values into the titles SharedPreferences
    private static String [] sortOutDetails(ArrayList<WeatherDetails> weatherDetails, boolean isTitle)
    {
        String [] details = new String[weatherDetails.size()];

        for (int i = 0; i < weatherDetails.size(); i++)
            details[i] = ((isTitle) ? (weatherDetails.get(i).getTitle()) : (weatherDetails.get(i).getSummary()));

        return details;
    }

    // Prepares the Intent for the Location Activity
    public void setLocationClicked(View view) throws XmlPullParserException, IOException
    {
        Intent intent = new Intent(MainActivity.this, LocationActivity.class);
        intent.putExtra("Language", sharedPreferences.getString(languageKey, "english"));
        intent.putExtra("Location", sharedPreferences.getString(locationKey, " , , "));
        startActivityForResult(intent, locationActivityRequestCode);

        setUpDriver();
    }

    // Prepares the Intent for the language activity
    public void setLanguageClicked(View view) throws XmlPullParserException, IOException
    {
        Intent intent = new Intent(MainActivity.this, LanguageActivity.class);
        intent.putExtra("Language", sharedPreferences.getString(languageKey, "english"));
        startActivityForResult(intent, languageActivityRequestCode);

        setUpDriver();
    }

    // prepares the Intent for the Details Activity
    public void openDetailsClick(View view)
    {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);

        switch (view.getId())
        {
            case R.id.specialStatementButton:
                intent.putExtra("ButtonChoice", "0");
                break;

            case R.id.currentConditionsButton:
                intent.putExtra("ButtonChoice", "1");
                break;

            case R.id.todayButton:
                intent.putExtra("ButtonChoice", "2");
                break;

            case R.id.tonightButton:
                intent.putExtra("ButtonChoice", "3");
                break;

            case R.id.sixDaysButton:
                intent.putExtra("ButtonChoice", "4");
                break;
        }

        intent.putExtra("Summaries", sharedPreferences.getString(summariesKey, " "));
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (requestCode)
        {
            case languageActivityRequestCode:
                String newLanguage = data.getStringExtra("Language");
                editor.putString(languageKey, newLanguage);
                editor.commit();

                setUpDriver();

                break;

            case locationActivityRequestCode:
                String newLocation = data.getStringExtra("Location");

                // makes sure a location is set
                if (!newLocation.equals(" , , "))
                {
                    editor.putString(locationKey, newLocation);
                    editor.commit();
                }

                setUpDriver();

                break;
        }
    }
}
