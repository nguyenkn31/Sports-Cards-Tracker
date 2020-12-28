package sportsCardsTracker;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.stream.Collectors;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class NBARostersScraper_Mark3 {
  
    public static ArrayList<String> getNBARoster () throws IOException {
    	ArrayList<String> playersNamesList = new ArrayList();
        String[] teamName = {"hawks", "celtics", "nets", "hornets", "bulls", "cavaliers", "mavericks", "nuggets", "pistons", "warriors", "rockets", "pacers", "clippers", "lakers", 
                "grizzlies","heat", "bucks", "timberwolves","pelicans", "knicks", "thunder", "magic", "sixers", "suns", "blazers", "kings", "spurs", "raptors", "jazz", "wizards"};

        for(int i = 0; i < teamName.length; i++) {
        	String[] playerName_extract = null;
            Document document = Jsoup.connect("https://www.nba.com/teams/" + teamName[i]).get();
            Elements playerName = document.select("p.nba-player-index__name").append("%"); // I append % in order to split them later
            String playerName_temp = playerName.text();

            int counter = 1;
            playerName_extract = playerName_temp.split("%");

            while(counter < playerName_extract.length) {
                playerName_extract = playerName_temp.split("%");
                counter++;
            }
            
            // This code is to strip away all of the whitespace of the strings in the array
            for (int j = 0; j < playerName_extract.length; j++) {
            	playerName_extract[j] = playerName_extract[j].trim();
            }

            for(String names : playerName_extract) {
                playersNamesList.add(names);
                }  
            playersNamesList.stream().map(String::toLowerCase).collect(Collectors.toList());
        }
		return playersNamesList;
        }
    }