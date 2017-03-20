package com.example.brendan.weatherapp;

/*!
 * @author Brendan Beerman
 *
 * @brief WeatherDetails    An object for the collecting of weather titles and summaries
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            String clickedButtonValue = extras.getString("ButtonChoice");
            String summariesValue = extras.getString("Summaries");

            setWeatherText(clickedButtonValue, summariesValue);
        }
    }

    /*!
     * @brief setWeatherText        Depending on the chosen button the chosen summary of weather will be different
     *
     * @param clickedButton         The button from the MainActivity that was clicked
     * @param summaries             All the weather summaries that were taken from the Environment Canada XML file
     */
    private void setWeatherText(String clickedButton, String summaries)
    {
        String [] splitSummaries = summaries.split("::");
        TextView weatherTextView = (TextView) findViewById(R.id.weatherTextView);

        if (clickedButton.equals("0"))
            weatherTextView.setText(splitSummaries[0]);
        else if (clickedButton.equals("1"))
        {
            String cleanedSummary = removeMarkUp(splitSummaries[1], "<b>");
            cleanedSummary = removeMarkUp(cleanedSummary, "</b>");
            cleanedSummary = removeMarkUp(cleanedSummary, "<br/>");
            cleanedSummary = removeMarkUp(cleanedSummary, "&deg;");

            weatherTextView.setText(cleanedSummary);
        }
        else if (clickedButton.equals("2"))
            weatherTextView.setText(splitSummaries[2]);
        else if (clickedButton.equals("3"))
            weatherTextView.setText(splitSummaries[3]);
        else
        {
            String weeksSummary = splitSummaries[4] + "\n" + splitSummaries[5] + "\n" +
                                  splitSummaries[6] + "\n" + splitSummaries[7] + "\n" +
                                  splitSummaries[8] + "\n" + splitSummaries[9] + "\n" +
                                  splitSummaries[10] + "\n" + splitSummaries[11] + "\n" +
                                  splitSummaries[12] + "\n" + splitSummaries[13];

            weatherTextView.setText(weeksSummary);
        }

    }

    /*!
     * @brief removeMarkUp      Removes HTML markup from a given string
     *
     * @param toClean           A string of the text to clear HTML markup out of
     * @param toSplitBy         The value of HTML to remove
     */
    private String removeMarkUp(String toClean, String toSplitBy)
    {
        // removes markup by splitting the text up by the markup tag
        String [] splitToClean = toClean.split(toSplitBy);
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < splitToClean.length; i++)
            stringBuilder.append(splitToClean[i] + " ");

        return stringBuilder.toString();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();

        setResult(RESULT_OK, intent);

        finish();
    }
}
