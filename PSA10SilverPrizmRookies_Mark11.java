package sportsCardsTracker;

import java.io.*;
import java.lang.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.*;

public class PSA10SilverPrizmRookies_Mark11 {
    public static ArrayList<String> listingNameList;
    public static ArrayList<BigDecimal> listingPriceList;
    public static ArrayList<String> finalNameList;
    public static ArrayList<BigDecimal> finalPriceList;
    public static String[][] tempList;
    public static int j;
    
    public static void main(String[] args) throws IOException, ParseException {
    	/* Initialization Section */
    	/* Initialize anything here */
    	listingNameList = new ArrayList();
    	listingPriceList = new ArrayList();
    	String[] listingName_extract = null;
    	String[] listingPrice_extract = null;
    	StringBuilder builder = new StringBuilder();   	
    	int page = 1;
    	
    	/* FileWriter */
    	String directory = System.getProperty("user.dir");
        File file = new File(directory + "/src/sportsCardsTracker/CardPrices.csv");
        FileWriter fw = new FileWriter(file, false); //true to not over ride 
    	
        /* Get the web site to scrape, in our case - eBay */
    	Document document = Jsoup.connect("https://www.ebay.com/sch/i.html?_from=R40&_nkw=PSA+10+silver+prizm+rookies&_sacat=0&LH_TitleDesc=0&rt=nc&LH_Sold=1&LH_Complete=1&_pgn=1").get();
    	
    	/* To get the number of results and to get the page number from the number of search results */
    	Elements listingCount = document.select("h1.srp-controls__count-heading > span:first-of-type"); // This is the number of search results
        String[] listingCount_split = listingCount.text().split(",");
        for(String character : listingCount_split) {
            builder.append(character);
            }
        int resultsNum = Integer.parseInt(builder.toString());
        int numPages = (resultsNum / 50) + 1;
    	
    	/* To get the listings' name */
    	Elements listingName = document.select("a.s-item__link > h3").append("/split/"); // I append % in order to split them later
        String listingName_temp = listingName.text().replace(",", " ");
        
        /* To get the listings' prices */
        Elements listingsPrice = document.select("span.s-item__price").append("/split/"); // I append % in order to split them later
        String listingPrice_temp = listingsPrice.text().replace(",", " ");
        
        
        /* To get all listings from all pages - because with the codes above, we can only grab 50 per page and from page 1 */
        while(page < numPages) {
        	if (page > 1) {
        	document = Jsoup.connect("https://www.ebay.com/sch/i.html?_from=R40&_nkw=PSA+10+silver+prizm+rookies&_sacat=0&LH_TitleDesc=0&rt=nc&LH_Sold=1&LH_Complete=1&_pgn=" + page).get();
        	
        	/* To get the listings' name...again*/
        	listingName = document.select("a.s-item__link > h3").append("/split/"); // I append % in order to split them later
            listingName_temp = listingName.text().replace(",", " ");
            
            /* To get the listings' prices...and again*/
            listingsPrice = document.select("span.s-item__price").append("/split/"); // I append % in order to split them later
            listingPrice_temp = listingsPrice.text().replace(",", " ");
            System.out.println(page);
        	}
            page++;
            
            int counter = 1;
            listingName_extract = listingName_temp.split("/split/");
            listingPrice_extract = listingPrice_temp.split("/split/");
            
            while(counter < listingName_extract.length - 1) {
                listingName_extract = listingName_temp.split("/split/");
                listingPrice_extract = listingPrice_temp.split("/split/");
                counter++;
            }
                for(String element : listingName_extract) {
                	listingNameList.add(element);
                    }

                for(String element : listingPrice_extract) {
                	listingPriceList.add(parse(element, Locale.US)); // parse is a method to tract double from US-format currency, check it out below
                    } 
            } // End of "while(page < numPages)"
        
        
        /* Add names and prices to the list */
        tempList = new String[2][listingNameList.size()];
        for (int i = 0; i < listingNameList.size(); i++) {
        	tempList[0][i] = listingNameList.get(i);
        	tempList[1][i] = String.valueOf(listingPriceList.get(i));
            }
        List<String> NBARosters = NBARostersScraper_Mark3.getNBARoster();
        List<String> temp = new ArrayList<>();
        List<String> finalNameList = new ArrayList<>();
        List<String> finalPriceList = new ArrayList<>();
        List<String> toBeRemoved = new ArrayList<>();
        toBeRemoved.add("?");
        List<String> removeThis = new ArrayList<>();
        
    	for (j = 0; j < tempList[0].length; j++) {
    		temp = NBARosters.parallelStream().filter(s -> tempList[0][j].toLowerCase().contains(s.toLowerCase())).map(s -> s).collect(Collectors.toList()); 
    		removeThis = toBeRemoved.parallelStream().filter(s -> tempList[0][j].toLowerCase().contains(s.toLowerCase())).map(s -> s).collect(Collectors.toList()); 
    		if(temp.size() > 0 && removeThis.isEmpty()){
    			finalNameList.add(temp.get(0));
    			finalPriceList.add(tempList[1][j]);
    			System.out.println(removeThis);
    			} 
    		}
    	for(int k = 0; k < finalNameList.size(); k++) {
    		fw.write(String.format("%s, %s\n", finalNameList.get(k), finalPriceList.get(k)));
    	}
        fw.close();
        } // End of main method   
    
    
    /* This method here is to extract the price from $ to normal decimals */
    public static BigDecimal parse(final String amount, final Locale locale) throws ParseException {
        final NumberFormat format = NumberFormat.getNumberInstance(locale);
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }
        return (BigDecimal) format.parse(amount.replaceAll("[^\\d.,]",""));
    }
}