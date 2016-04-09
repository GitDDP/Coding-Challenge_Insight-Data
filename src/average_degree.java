import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class average_degree {
	
	public static void main(String[] args)throws JSONException, ParseException, IOException{
		// The input file directory needs to be changed before compiling in a new machine.
		File file = new File("C:/Users/Jerry/Desktop/coding-challenge-master/coding-challenge-master/data-gen/tweets.txt");
	    Scanner scnr = new Scanner(file);
	    int lineNumber = 1;

	    // The output file directory needs to be changed before compiling in a new machine. 
	    BufferedWriter out = new BufferedWriter(new FileWriter("C:/Users/Jerry/Desktop/coding-challenge-master/coding-challenge-master/tweet_output/output.txt"));
	    
        double average = 0;
        // Use a time far behind the current time as initial. So it can get updated once fall into the while loop. 
        Date currentMax = dateParse("Mon Mar 28 23:23:18 +0000 1900");
        
        Graph graph = new Graph();					//create an empty graph. 
	    while(scnr.hasNextLine()){
	    	 String line = scnr.nextLine();           
            JSONObject obj = new JSONObject(line);
            // if the Json is correctly beginning with "created_at", deal with it.
            if(obj.has("created_at")){
            	 String timestamp = obj.getString("created_at");
            	  
            	 if(dateParse(timestamp).after(currentMax)){			// update currentMax date.
            		 currentMax = dateParse(timestamp);
            	 }
            	 
            	 JSONArray arr = obj.getJSONObject("entities").getJSONArray("hashtags");		// creating JSONArray to store "hashtags" contents.
            	 
            	 long dif = Math.abs((dateParse(timestamp).getTime()-currentMax.getTime())/1000);
            	 //recalculate average when there is at least one edge in new line and time difference is within 60s. 
            	 if(arr.length()>1&&dif<=60){
            		
            		 	HashSet<String> hset = new HashSet<String>();			 // Store the Strings in a HashSet.
            		 	for (int i = 0; i < arr.length(); i++){
            		 	hset.add(arr.getJSONObject(i).getString("text"));
            		 	}
            		 	
            		 	updateGraph(graph,hset,currentMax,dateParse(timestamp) );		//update the graph using the Strings, currentMax date and incoming date.
            		 	
            		 	average = graph.calculateAverage();				// Calculate the degree of the graph.
            		 	System.out.println("Line Number: "+lineNumber);
            		 	System.out.println("TIMEUPDate: "+currentMax);
            		 	System.out.println(average);
            	 }
            	 
            	String result = String.format("%.2f", average);			// format the degree to two decimal number.
            	
            	out.write(result); 		// output to file.
     	    	out.newLine();
            }
            // if the JSON begins with "limited", ignore. 
            else{
            	//System.out.println("Line is limited: "+lineNumber);
            }
           
	    	lineNumber++;
	    	
	    	
	    }
	   
	    out.close(); 		// close the BufferWriter.
           
                
            
	}
	
	/**
	 * This method takes a string and turns it to a Date object.
     * @param s the timestamp String returned by the JSON file. 
     * @return a Date object converted from the timestamp String.
     */
	public static Date dateParse(String s) throws ParseException{
		
		String delims = "[ ]+";
		String[] tokens = s.split(delims);
		StringBuilder timeString = new StringBuilder();
		timeString.append(tokens[1]);
		timeString.append(" ");
		timeString.append(tokens[2]);
		timeString.append(" ");
		timeString.append(tokens[5]);
		timeString.append(" ");
		timeString.append(tokens[3]);
		//System.out.println(timeString);
		DateFormat formatter = new SimpleDateFormat("MMM dd yyyy hh:mm:ss");
		Date date = formatter.parse(timeString.toString());
	    //System.out.println(s+"Time is: "+date);
	    return date;
	}
	/**
     * 
     * This method update the Graph by adding new vertices and deleting old edges 
     * @param g, the original Graph.
     * @param hset, the HashSet contains the new Strings needs to be add to the Graph.
     * @param currentMax, the most recent Date.
     * @param currentDate, the current incoming Date. 
     */
    public static void updateGraph(Graph g,HashSet<String> hset,Date currentMax, Date currentDate){
    	String[] labelset = new String[hset.size()];
    	hset.toArray(labelset);					//store the date to a string array.
    	
    	System.out.println("Labelset: "+Arrays.toString(labelset));
    //	Create vertex using the strings, create edges between each two vertices, add edges and vertices to the graph.
    	for(int i=0; i<labelset.length;i++){
    		for(int j=i+1;j<labelset.length;j++){
    			Vertex vone = new Vertex(labelset[i]);
    			Vertex vtwo = new Vertex(labelset[j]);
    			g.addEdge(vone, vtwo, currentDate);
    			g.addVertex(vone);
    			g.addVertex(vtwo);
    			//System.out.println("Vertex Neighbour: "+vone.getLabel()+" :"+vone.getNeighborCount());
    			//System.out.println("Vertex Neighbour: "+vtwo.getLabel()+" :"+vtwo.getNeighborCount());
    		}
    	}
    	System.out.println("EdgeNum: "+g.getEdgeNum());
    	System.out.println("VerticesNum: "+g.getVerticesNum());
    	
    //	remove the Edges out of date. 
    	Set<Edge> edgeset = g.getEdges();
    	ArrayList<Edge> oldEdges = new ArrayList<Edge>();
    	for(Edge e:edgeset){
    		if(dateDifference(e.getDate(),currentMax)>60){
    			oldEdges.add(e);
    		}
    	}
    	for(int i=0; i<oldEdges.size();i++){
    		g.removeEdge(oldEdges.get(i));
    	}
    	System.out.println("AfterEdgeRemoval: EdgeNum: "+g.getEdgeNum());
    	System.out.println("AfterEdgeRemoval: VerticesNum: "+g.getVerticesNum());
    	
    // remove the Vertices with no Edges.
    	Set<String> vertexset = g.vertexKeys();
    	ArrayList<String> singleVertices = new ArrayList<String>();
    	for(String s:vertexset){
    		if(g.getVertex(s).getNeighborCount()<1){
    			singleVertices.add(s);
    			System.out.println("Vertices to delete: "+g.getVertex(s).getLabel());
    		}
    	}
    	for(int i=0;i<singleVertices.size();i++){
    		g.removeVertex(singleVertices.get(i));
    	}
    	System.out.println("AfterVerticeRemoval: EdgeNum: "+g.getEdgeNum());
    	System.out.println("AfterVerticeRemoval: VerticesNum: "+g.getVerticesNum());
    	
    	
    }
    
    /**
     * This method Calculate the difference in seconds between two Date objects.
     * @param date1, date2, Two Date object 
     * @return the difference in seconds between the two Date objects. 
     */ 
    public static double dateDifference(Date date1, Date date2){
    	return Math.abs((date1.getTime()-date2.getTime())/1000);
    }
	
	
	
}
