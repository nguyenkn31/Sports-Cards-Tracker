package sportsCardsTracker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class NBAPlayersInfo_Mark2 {
	
	public static void main(String args[]){
		
		//vars
		String url = "";
		String[] firstTable = new String[4];
		String[] secondTable = new String[14];
		Document document;
		Player_Mark1 player;
		String firstName = "";
		String lastName = "";
		String thirdName = "";
		BufferedReader reader;
		int count1 =0;
		//ArrayList<Player> playerList = new ArrayList<Player>();
		
		/* Directory grabber */
    	String directory = System.getProperty("user.dir");
		
		//READ FILE
		try {
			reader = new BufferedReader(new FileReader(directory + "/src/sportsCardsTracker/playerNames.txt"));
			
			String line = reader.readLine();
			while(line != null) {
				//count1++;
				//System.out.println(count1);
				String[] splitLine = line.split(" ");
			
				if(splitLine.length == 2) {
					player = new Player_Mark1(splitLine[0],splitLine[1]);
					firstName = splitLine[0].toLowerCase().replace(".","");
					lastName = splitLine[1].toLowerCase().replace(".", "");
					firstName = firstName.replace("'", "");
					lastName = lastName.replace("'", "");
					url = "https://projects.fivethirtyeight.com/2020-nba-player-projections/"
							   + firstName + "-" + lastName + "/";
				}
				else {
					player = new Player_Mark1(splitLine[0],splitLine[1],splitLine[2]);
					firstName = splitLine[0].toLowerCase().replace(".","");
					lastName = splitLine[1].toLowerCase().replace(".", "");
					thirdName = splitLine[2].toLowerCase().replace(".", "");
					firstName = firstName.replace("'", "");
					lastName = lastName.replace("'", "");
					thirdName = thirdName.replace("'", "");
					url = "https://projects.fivethirtyeight.com/2020-nba-player-projections/"
						   + firstName + "-" + lastName + "-" + thirdName + "/";
				}
				
				//player = new Player("LeBron","James");
				//url = "https://projects.fivethirtyeight.com/2020-nba-player-projections/giannis-antetokounmpo/";
				
				try {
					document = Jsoup.connect(url).get();
					
					String str = "";
					int count = 0;
					for (Element row : document.select(
							"ul li")) {
						str = row.select(".age").text();
						if(str.contentEquals(""))
							continue;
						if(count == 4)
							break;
						firstTable[count] = str;
						count++;
						//System.out.println(str);
					}
					
					count = 0;
					for (Element row : document.select(
							"table tr")) {
						str = row.select("td.last").text();
						if(str.contentEquals(""))
							continue;
						secondTable[count] = str;
						count++;
						//System.out.println(str);
					}
					
					//set player attributes
					player.setTeam(firstTable[1]);
					player.setPos(firstTable[2]);
					player.setAge(firstTable[3]);
					player.setHeight(secondTable[0]);
					player.setWeight(secondTable[1]);
					player.setDraftPos(secondTable[2]);
					player.setShooting(secondTable[3]);
					player.setFreeThrow(secondTable[4]);
					player.setUsage(secondTable[5]);
					player.setThreePt(secondTable[6]);
					player.setFTFreq(secondTable[7]);
					player.setAssist(secondTable[8]);
					player.setTurnover(secondTable[9]);
					player.setRebound(secondTable[10]);
					player.setBlock(secondTable[11]);
					player.setSteal(secondTable[12]);
					player.setDefense(secondTable[13]);
					
					saveRecord(player);
					
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
				
				
				
				line = reader.readLine();
			}
			reader.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("DONE");
	}
	
	public static void saveRecord(Player_Mark1 player) {
		
		try {

	    	String directory = System.getProperty("user.dir");
			FileWriter fw = new FileWriter(directory + "/src/sportsCardsTracker/playerList.csv",true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
	        
			System.out.println(player.getfirstName());
			pw.println(player.getfirstName()+","+player.getlastName()+","+player.getTeam()+","+player.getPos()+","+player.getAge()+","+player.getHeight()
					   +","+player.getWeight()+","+player.getDraftPos()+","+player.getShooting()+","+player.getFreeThrow()+","+player.getUsage()+","+player.getThreePt()
					   +","+player.getFTFreq()+","+player.getAssist()+","+player.getTurnover()+","+player.getRebound()+","+player.getBlock()+","+player.getSteal()+","+player.getDefense());
			pw.flush();
			pw.close();
			
			fw.write(String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s\n", 
					player.getfirstName() + player.getlastName() + player.getTeam() + player.getPos() + player.getAge() + player.getHeight()
			   + player.getWeight() + player.getDraftPos() + player.getShooting() + player.getFreeThrow() + player.getUsage() + player.getThreePt()
			   + player.getFTFreq() + player.getAssist() + player.getTurnover() + player.getRebound() +player.getBlock() + player.getSteal() + player.getDefense()));
			fw.close();
			   
			   //JOptionPane.showMessageDialog(null, "Record Saved");
			
			
		}
		catch(Exception E)
		{
			
		}
	}

}
