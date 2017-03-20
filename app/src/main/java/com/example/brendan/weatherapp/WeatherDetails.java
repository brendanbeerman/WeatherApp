package com.example.brendan.weatherapp;

/**
 * @author Brendan Beerman
 *
 * @brief WeatherDetails    An object for the collecting of weather titles and summaries
 */

public class WeatherDetails
{
    private String title;
    private String summary;

    // ----- Constructors -----

    /*!
     * @brief WeatherDetails    An empty contrustor for the object
     */
    public WeatherDetails()
    {

    }

    /*!
     * @brief WeatherDetails    An empty contrustor for the object
     */
    public WeatherDetails(String title, String summary)
    {
        this.title = title;
        this.summary = summary;
    }

    // ----- Setters -----

    /*!
     * @brief setTitle          Sets the title
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /*!
     * @brief setSummary         Sets the summary
     */
    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    // ----- Getters -----

    /*!
     * @brief getTitle          Gets the title
     */
    public String getTitle()
    {
        return title;
    }

    /*!
     * @brief getSummary          Gets the Summary
     */
    public String getSummary()
    {
        return summary;
    }
}
