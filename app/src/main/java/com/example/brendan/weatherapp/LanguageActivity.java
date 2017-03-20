/*!
 * @author Brendan Beerman
 *
 * @brief LanguageActivity      Sets the language for the user
 */

package com.example.brendan.weatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class LanguageActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            String languageValue = extras.getString("Language");

            setTextLanguage(languageValue);
        }
    }

    /*!
     * @brief setTextLanguage       Sets the language of the activity and which language radio button is selected
     */
    private void setTextLanguage(String language)
    {
        RadioButton englishRadioButton = (RadioButton) findViewById(R.id.englishRadioButton);

        if (language.equals("english"))
            englishRadioButton.setChecked(true);
        else
        {
            RadioButton frenchRadioButton = (RadioButton) findViewById(R.id.frenchRadioButton);

            englishRadioButton.setText("Anglais");
            frenchRadioButton.setText("Français");

            frenchRadioButton.setChecked(true);

            Button submitButton = (Button) findViewById(R.id.submitLanguageButton);

            submitButton.setText("Définir la langue");
        }
    }

    /*!
     * @brief setSelectedRadioButton        When a radio button is clicked the other is unselected
     *
     * @param view                          The selected radio button
     */
    public void setSelectedRadioButton(View view)
    {
        RadioButton selectedRadioButton;

        switch (view.getId())
        {
            case R.id.englishRadioButton:
                selectedRadioButton = (RadioButton) findViewById(R.id.frenchRadioButton);
                selectedRadioButton.setChecked(false);
                break;
            case R.id.frenchRadioButton:
                selectedRadioButton = (RadioButton) findViewById(R.id.englishRadioButton);
                selectedRadioButton.setChecked(false);
                break;
        }
    }

    /*!
     * @brief submitLanguageClicked         Gets the selected language submits the language
     */
    public void submitLanguageClicked(View view)
    {
        Intent intent = new Intent();

        RadioButton englishRadioButton = (RadioButton) findViewById(R.id.englishRadioButton);

        if (englishRadioButton.isChecked())
            intent.putExtra("Language", "english");
        else
            intent.putExtra("Language", "french");

        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();

        setResult(RESULT_OK, intent);

        finish();
    }
}
