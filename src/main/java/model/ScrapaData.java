package model;

import lombok.Data;

/**
 * Represents a ScrapaData object
 * composed of a url and the scraper data scraped from it
 * Created by mcmoe on 4/28/2014.
 */

@Data
public class ScrapaData {

    private String url;
    private String data;

    public ScrapaData(String url, String data) {
        this.url = url;
        this.data = data;
    }
}
