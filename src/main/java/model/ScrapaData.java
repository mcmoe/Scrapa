package model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Represents a ScrapaData object
 * composed of a url and the scraper data scraped from it
 * Created by mcmoe on 4/28/2014.
 */

@Data
public class ScrapaData {

    private String url;
    private String data;
    private Timestamp addedOnUTC;

    public ScrapaData(String url, String data, Timestamp addedOnUTC) {
        this.url = url;
        this.data = data;
        this.addedOnUTC = addedOnUTC;
    }
}
