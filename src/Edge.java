import java.util.Date;

public class Edge implements Comparable<Edge> {

    private Vertex one, two;
    private Date indate;
    

    /**
     * 
     * @param one The first vertex in the Edge
     * @param two The second vertex of the Edge
     * @param indate The incoming date of this Edge
     */
    public Edge(Vertex one, Vertex two, Date indate){
        this.one = (one.getLabel().compareTo(two.getLabel()) <= 0) ? one : two;
        this.two = (this.one == one) ? two : one;
        this.indate = indate;
    }
    

    
    /**
     * 
     * @return Vertex this.one
     */
    public Vertex getOne(){
        return this.one;
    }
    
    /**
     * @return Vertex this.two
     */
    public Vertex getTwo(){
        return this.two;
    }
    
    
    /**
     * 
     * @return Date The incoming date of the Edge
     */
    public Date getDate(){
        return this.indate;
    }
    
    
    /**
     * 
     * @param Date The new Date of this Edge
     */
    public void setDate(Date indate){
        this.indate = indate;
    }
    
    
    /**
     * Note that the compareTo() method deviates from 
     * the specifications in the Comparable interface. A 
     * return value of 0 does not indicate that this.equals(other).
     * The equals() method checks the Vertex endpoints, while the 
     * compareTo() is used to compare Edge weights
     * 
     * @param other The Edge to compare against this
     * @return int this.indate comparing with other.indate
     */
    public int compareTo(Edge other){
        if(this.indate.before(other.indate)){
        	return -1;
        }
        else if(this.indate.equals(other.indate)){
        	return 0;
        }
        else{
        	return 1;
        }
    }
    
    /**
     * 
     * @return String A String representation of this Edge
     */
    public String toString(){
        return "({" + one + ", " + two + "}, " + indate + ")";
    }
    
    /**
     * 
     * @return int The hash code for this Edge 
     */
    public int hashCode(){
        return (one.getLabel() + two.getLabel()).hashCode(); 
    }
    
    /**
     * 
     * @param other The Object to compare against this
     * @return ture iff other is an Edge with the same Vertices as this
     */
    public boolean equals(Object other){
        if(!(other instanceof Edge)){
            return false;
        }
        
        Edge e = (Edge)other;
        
        return e.one.equals(this.one) && e.two.equals(this.two);
    }   
}