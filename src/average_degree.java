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
		File file = new File("C:/Users/Jerry/Desktop/coding-challenge-master/coding-challenge-master/data-gen/test.txt");
	    Scanner scnr = new Scanner(file);
	    int lineNumber = 1;
	    
//	    Date date1 = dateParse("Mon Mar 28 23:23:18 +0000 2016");
//	    Date date2 = dateParse("Mon Mar 28 23:23:20 +0000 2016");
//	    if(date1.before(date2)){
//	    	System.out.println("date2 is larger");
//	    }
//	    else{
//	    	System.out.println("date1 is bigger");
//	    }
//	    long seconds = (date1.getTime()-date2.getTime())/1000;
//	    
//	    System.out.println("Time difference: "+Math.abs(seconds));
	    BufferedWriter out = new BufferedWriter(new FileWriter("C:/Users/Jerry/Desktop/coding-challenge-master/coding-challenge-master/tweet_output/output.txt"));
	    //out.write("Testing output");
        double average = 0;
        Date currentMax = dateParse("Mon Mar 28 23:23:18 +0000 2016");
        Graph graph = new Graph();
	    while(scnr.hasNextLine()){
	    	 String line = scnr.nextLine();
            //System.out.println("line " + lineNumber + " :" + line);
            
            JSONObject obj = new JSONObject(line);
            if(obj.has("created_at")){
            	 String timestamp = obj.getString("created_at");
            	 if(dateParse(timestamp).after(currentMax)){
            		 currentMax = dateParse(timestamp);
            	 }
            	 
            	 long dif = Math.abs((dateParse(timestamp).getTime()-currentMax.getTime())/1000);
            	 //System.out.println("Time: "+timestamp);
            	 JSONArray arr = obj.getJSONObject("entities").getJSONArray("hashtags");
            	 //System.out.println("Length: "+arr.length());
            	 //recalculate average when there is at least one edge in new line. 
            	 if(arr.length()>1&&dif<=60){
            		 HashSet<String> hset = new HashSet<String>();
            		 	for (int i = 0; i < arr.length(); i++){
            		 	hset.add(arr.getJSONObject(i).getString("text"));
            		 	}
            		 	
            		 	updateGraph(graph,hset,currentMax,dateParse(timestamp) );
            		 	average = graph.calculateAverage();
            		 	System.out.println("Line Number: "+lineNumber);
            		 	System.out.println(timestamp);
            		 	System.out.println(average);
            	 }
            	 
            	String result = String.format("%.2f", average);
            	out.write(result);
     	    	out.newLine();
            }
            else{
            	//System.out.println("Line is limited: "+lineNumber);
            }
           
	    	lineNumber++;
	    	
	    	
	    }
	    out.close();
           
                
            
	}
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
     * update the Graph by adding new vertices and deleting old edges 
     */
    public static void updateGraph(Graph g,HashSet<String> hset,Date currentMax, Date currentDate){
    	String[] labelset = new String[hset.size()];
    	hset.toArray(labelset);
    	//System.out.println("HSET: "+hset);
    	//System.out.println("Labelset: "+Arrays.toString(labelset));
    	for(int i=0; i<labelset.length;i++){
    		for(int j=i+1;j<labelset.length;j++){
    			Vertex vone = new Vertex(labelset[i]);
    			Vertex vtwo = new Vertex(labelset[j]);
    			g.addEdge(vone, vtwo, currentDate);
    			g.addVertex(vone, false);
    			g.addVertex(vtwo, false);
    		}
    	}
    	System.out.println("EdgeNum: "+g.getEdgeNum());
    	System.out.println("VerticesNum: "+g.getVerticesNum());
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
    	Set<String> vertexset = g.vertexKeys();
    	ArrayList<String> singleVertices = new ArrayList<String>();
    	for(String s:vertexset){
    		if(g.getVertex(s).getNeighborCount()<1){
    			singleVertices.add(s);
    		}
    	}
    	for(int i=0;i<singleVertices.size();i++){
    		g.removeVertex(singleVertices.get(i));
    	}
    	System.out.println("After: EdgeNum: "+g.getEdgeNum());
    	System.out.println("After: VerticesNum: "+g.getVerticesNum());
    	
    	
    }
    public static double dateDifference(Date date1, Date date2){
    	return Math.abs((date1.getTime()-date2.getTime())/1000);
    }
	
	
	
}
