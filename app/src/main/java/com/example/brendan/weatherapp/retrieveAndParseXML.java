/*!
 * @author Brendan Beerman
 *
 * @brief retrieveAndParseXML       Retrieves and parses a given XML file from a URL
 */

package com.example.brendan.weatherapp;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class retrieveAndParseXML extends AsyncTask<String, Void, ArrayList<WeatherDetails>>
{
    /*!
     * @brief doInBackground        Used to get the XML from a given URL
     *
     * @param urls                  A string an array of URLs to collect XML from
     *
     * @return                      An array list of the collected weather details from the XML
     */
    protected ArrayList<WeatherDetails> doInBackground(String... urls)
    {
        ArrayList<WeatherDetails> weatherDetails;

        URL url;
        InputStream inputStream;

        XmlPullParserFactory xmlPullParserFactory;
        XmlPullParser xmlPullParser;

        try
        {
            // only one URL should be passed
            url = new URL(urls[0]);

            // sets up the connection to the URL
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();

            xmlPullParserFactory = XmlPullParserFactory.newInstance();

            xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);
        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
            xmlPullParser = null;
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            xmlPullParser = null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            xmlPullParser = null;
        }

        try
        {
            weatherDetails = collectWeatherInformation(xmlPullParser);
        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
            weatherDetails = new ArrayList<WeatherDetails>();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            weatherDetails = new ArrayList<WeatherDetails>();
        }

        return weatherDetails;
    }

    /*!
     * @brief collectWeatherInformation     Driver for parsing XML information
     *
     * @param XmlPullParser                 An XML parser to parser the XML
     *
     * @return                              An array list of the collected weather details
     */
    private ArrayList<WeatherDetails> collectWeatherInformation(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException
    {
        ArrayList<WeatherDetails> weatherDetails;

        weatherDetails = parseXmlFile(xmlPullParser);

        return weatherDetails;
    }

    /*!
     * @brief parseXmlFile          Used to parse the XML file
     *
     * @param XmlPullParser         An XML parser to parser the XML
     *
     * @return                      An array list of the collected weather details
     */
    private ArrayList<WeatherDetails> parseXmlFile(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException
    {
        ArrayList<WeatherDetails> weatherDetailsArrayList = new ArrayList<WeatherDetails>();
        WeatherDetails currentWeatherDetails = new WeatherDetails();

        int eventType = xmlPullParser.getEventType();

        // the first title tag in Environment Canada XML is skippable
        boolean isFirstTitleTagFound = true;

        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            String name;

            if (eventType == XmlPullParser.START_TAG)
            {
                name = xmlPullParser.getName();

                // skipping the first tittle tag
                if (isFirstTitleTagFound && name.equals("title"))
                    isFirstTitleTagFound = !isFirstTitleTagFound;
                else
                {
                    // saving the title and summary values
                    if (name.equals("title"))
                    {
                        currentWeatherDetails = new WeatherDetails();

                        currentWeatherDetails.setTitle(xmlPullParser.nextText());
                    }
                    else if (name.equals("summary"))
                    {
                        currentWeatherDetails.setSummary(xmlPullParser.nextText());

                        weatherDetailsArrayList.add(currentWeatherDetails);
                    }
                }
            }

            eventType = xmlPullParser.next();
        }

        return weatherDetailsArrayList;
    }

    /*!
     * @brief onPostExecute         The method called to return to the main activity
     */
    protected void onPostExecute(ArrayList<WeatherDetails> weatherDetails)
    {
        MainActivity.setWeatherInformation(weatherDetails);
    }

}
