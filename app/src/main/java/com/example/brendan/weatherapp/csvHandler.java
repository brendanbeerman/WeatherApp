/*!
 * @author Brendan Beerman
 *
 * @brief csvHandler        Searches the feed csv files for the given location
 */

package com.example.brendan.weatherapp;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class csvHandler
{
    private static String englishLocationFile = "feeds.csv";
    private static String frenchLocationFile = "feeds-FR.csv";

    /*!
     * @brief csvHandler        A blank constructor
     */
    private csvHandler()
    {

    }

    /*!
     * @brief checkLocationExistence        Searches the feed csv file for a given city and province to find if the exist
     *
     * @param context                       The context of the application to get the feed files
     * @param city                          A string of the city to search for
     * @param province                      A string of the province to search for
     * @param isEnglish                     A boolean of which language the user is using, true => English
     *                                                                                     false => French
     *
     * @return                              A string array of the users selected xml file, city, and province
     *                                                                  Otherwise the array is returned blank
     */
    public static String [] checkLocationExistence(Context context, String city, String province, boolean isEnglish)
    {
        String fileName = ((isEnglish) ? (englishLocationFile) : (frenchLocationFile));

        String currentLine;
        String [] splitCurrentLine;

        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;

        try
        {
            inputStream  = context.getAssets().open(fileName);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            while ((currentLine = bufferedReader.readLine()) != null)
            {
                splitCurrentLine = currentLine.split(", ");

                // skips if the provinces are not the same
                if (!splitCurrentLine[2].toLowerCase().equals(province.toLowerCase()))
                    continue;
                else if (splitCurrentLine[1].toLowerCase().equals(city.toLowerCase()) && splitCurrentLine[2].toLowerCase().equals(province.toLowerCase()))
                    return splitCurrentLine;
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        splitCurrentLine = new String[]{" ", " ", " "};

        return splitCurrentLine;
    }
}
