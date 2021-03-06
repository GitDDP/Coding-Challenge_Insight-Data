import java.util.*;

public class Graph {
    
    private HashMap<String, Vertex> vertices;
    private HashMap<Integer, Edge> edges;
	private int myNumVertices;
	private int myNumEdges;
    public Graph(){
        this.vertices = new HashMap<String, Vertex>();
        this.edges = new HashMap<Integer, Edge>();
        this.myNumVertices = 0;
        this.myNumEdges = 0;
    }

    
    /**
     * This method Accepts two vertices and a Date, and adds the edge
     * ({one, two, indate}) iff no Edge relating one and two 
     * exists in the Graph.
     * 
     * @param one The first Vertex of the Edge
     * @param two The second Vertex of the Edge
     * @param indate The incoming date of the Edge
     * @return true iff no Edge already exists in the Graph
     */
    public boolean addEdge(Vertex one, Vertex two, Date indate){
        if(one.equals(two)){
            return false;   
        }
       
        //ensures the Edge is not in the Graph
        //if the Edge exists, compare the incoming date, update to the nearest one. 
        Edge e = new Edge(one, two,indate);
        if(edges.containsKey(e.hashCode())){
        	if(edges.get(e.hashCode()).getDate().before(indate))
            edges.get(e.hashCode()).setDate(indate);
        	//System.out.println("EDGESDaTES: "+edges.get(e.hashCode()).getDate());
        	return false;
        }
       
        //and that the Edge isn't already incident to one of the vertices
        else if(one.containsNeighbor(e) || two.containsNeighbor(e)){
            return false;
        }
        
        edges.put(e.hashCode(), e);
        one.addNeighbor(e);
        two.addNeighbor(e); 
        myNumEdges++;
        
        return true;
    }
    
    /**
     * 
     * @param e The Edge to look up
     * @return true iff this Graph contains the Edge e
     */
    public boolean containsEdge(Edge e){
        if(e.getOne() == null || e.getTwo() == null){
            return false;
        }
        
        return this.edges.containsKey(e.hashCode());
    }
    
    
    /**
     * This method removes the specified Edge from the Graph,
     * including as each vertex's incidence neighborhood.
     * 
     * @param e The Edge to remove from the Graph
     * @return Edge The Edge removed from the Graph
     */
    public Edge removeEdge(Edge e){
       e.getOne().removeNeighbor(e);
       e.getTwo().removeNeighbor(e);
       myNumEdges--;
       return this.edges.remove(e.hashCode());
    }
    
    /**
     * 
     * @param vertex The Vertex to look up
     * @return true iff this Graph contains vertex
     */
    public boolean containsVertex(Vertex vertex){
        return this.vertices.get(vertex.getLabel()) != null;
    }
    
    /**
     * 
     * @param label The specified Vertex label
     * @return Vertex The Vertex with the specified label
     */
    public Vertex getVertex(String label){
        return vertices.get(label);
    }
    
    /**
     * This method adds a Vertex to the graph. If the Vertex exists already, 
     * combine the neibouring Edges together.
     * @param vertex
     * @return true iff vertex was added to the Graph
     */
    public boolean addVertex(Vertex vertex){
        Vertex current = this.vertices.get(vertex.getLabel());
        if(current != null){
        	
        	if(vertex.getNeighbors().size()>0){   // combine the Neighbouring Edges of the Vertex.
        		for(int i=0; i<vertex.getNeighbors().size();i++){
        			current.addNeighbor(vertex.getNeighbors().get(i));
        		}
        		
        	}
                return false;
         }
        vertices.put(vertex.getLabel(), vertex);
        myNumVertices++;
        return true;
    }
    
    /**
     * 
     * @param label The label of the Vertex to remove
     * @return Vertex The removed Vertex object
     */
    public Vertex removeVertex(String label){
        Vertex v = vertices.remove(label);
        myNumVertices--;
        return v;
    }
    
    /**
     * 
     * @return Set<String> The unique labels of the Graph's Vertex objects
     */
    public Set<String> vertexKeys(){
        return this.vertices.keySet();
    }
    
    /**
     * 
     * @return Set<Edge> The Edges of this graph
     */
    public Set<Edge> getEdges(){
        return new HashSet<Edge>(this.edges.values());
    }
  
    /**
     * 
     * @return double The degree of the graph
     */
    public double calculateAverage(){
    	if(this.myNumVertices!=0){
    		return (double)(this.myNumEdges*2)/this.myNumVertices;
    	}
    	else{
    		return -1;
    	}
    	
    }
    /**
     * 
     * @return the vertices number of the graph
     */
    public int getVerticesNum(){
    	return this.myNumVertices;
    	
    }
    
    /**
     * 
     * @return the edge number of the graph
     */
    public int getEdgeNum (){
    	return this.myNumEdges;
    }
    
    
}