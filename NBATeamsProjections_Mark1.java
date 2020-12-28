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

public class NBATeamsProjections_Mark1 {   
	public static ArrayList<String> teamNames;
	public static ArrayList<Integer> teamCurrentRating;
	public static ArrayList<String> teamConference;
	public static ArrayList<String> teamProjRecords;
	public static ArrayList<String> teamPlayoffChance;
	public static ArrayList<String> teamFinalAppChance;
	public static ArrayList<String> teamFinalWinChance;
	public static String[][] finalTeamInfo;
    public static void main(String[] args) throws IOException, ParseException {
    	teamNames = new ArrayList();
    	teamCurrentRating = new ArrayList();
    	teamConference = new ArrayList();
    	teamProjRecords = new ArrayList();
    	teamPlayoffChance = new ArrayList();
    	teamFinalAppChance = new ArrayList();
    	teamFinalWinChance = new ArrayList();
    	
    	/* Get the web site to scrape, in this case, whereever we get the NBA Projections */
    	Document document = Jsoup.connect("https://projects.fivethirtyeight.com/2020-nba-predictions/").get();
    	
    	/* Get the teams' names */
    	Elements gotTeamName = document.select("tbody > tr > td > a").append("/split/");
    	String[] teamNamesExtract = gotTeamName.text().split("/split/");
    	for (String string : teamNamesExtract) {
    		teamNames.add(string.trim());
    	}
    	
    	/* Get the teams' rating */
    	String teamRatingRaw = document.select("tbody > tr > td.carmelo-current").text(); 
    	String[] teamRatingExtract = teamRatingRaw.split(" ");
    	for (String string : teamRatingExtract) {
    		teamCurrentRating.add(Integer.parseInt(string));
    	}
    	
    	/* Get the teams' conference */
    	String teamsConference = document.select("tbody > tr > td.border-right").text(); 
    	String[] teamConferenceExtract = teamsConference.split(" ");
    	for (String string : teamConferenceExtract) {
    		teamConference.add(string.trim());
    	}
    	
    	/* Get the teams' projected records */
    	String teamProjectedRecord = document.select("tbody > tr > td.proj-rec").text(); 
    	String[] teamProjectedRecordExtract = teamProjectedRecord.split(" ");
    	for (String string : teamProjectedRecordExtract) {
    		teamProjRecords.add(string.trim());
    	}
    	
    	/* Get the teams' play-offs making chance */
    	String teamProjPlayoffChance = document.select("tbody > tr > td.make-playoffs").text(); 
    	String[] teamPlayoffChanceExtract = teamProjPlayoffChance.split(" ");
    	for (String string : teamPlayoffChanceExtract) {
    		teamPlayoffChance.add(string.trim());
    	}
    	
    	/* Get the teams' finals appearance chances */
    	String teamProjFinalAppChance = document.select("tbody > tr > td.top-seed").text(); 
    	String[] teamProjFinalAppChanceExtract = teamProjFinalAppChance.split(" ");
    	for (String string : teamProjFinalAppChanceExtract) {
    		teamFinalAppChance.add(string.trim());
    	}
    	
    	/* Get the teams' finals winning / being champs chances */
    	String teamProjFinalWinChance = document.select("tbody > tr > td.pct").text(); 
    	String[] teamProjFinalWinChanceExtract = teamProjFinalWinChance.split(" ");
    	for (String string : teamProjFinalWinChanceExtract) {
    		teamFinalWinChance.add(string.trim());
    	}
    	
    	finalTeamInfo = new String[7][teamNames.size()];
    	for(int k = 0; k < teamNames.size(); k++) {
    		finalTeamInfo[0][k] = teamNames.get(k);
    		finalTeamInfo[1][k] = Integer.toString(teamCurrentRating.get(k));
    		finalTeamInfo[2][k] = teamConference.get(k);
    		finalTeamInfo[3][k] = teamProjRecords.get(k);
    		finalTeamInfo[4][k] = teamPlayoffChance.get(k);
    		finalTeamInfo[5][k] = teamFinalAppChance.get(k);
    		finalTeamInfo[6][k] = teamFinalWinChance.get(k);
    	}
    	
    }
}