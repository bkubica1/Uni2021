
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * 
 * @author Bartosz Kubica
 */

public class Square extends ClosedShape{
    
    private int side;
    
    public Square(int insertionTime, int x, int y, int vx, int vy, int side, Color colour, boolean isFilled){
        super(insertionTime, x, y, vx, vy, colour, isFilled);
        this.side = side;
        
    }
    
    public String toString () {
    	String result = "This is a square\n";
    	result += super.toString ();
	result += "Its side is " + this.side + "\n";
    	return result;
    }
    
    public void setSide(int side){
        this.side = side;
    }
    
    public int getSide(){
        return side;
    }
    
    public int getWidth(){
        return side;
    }
    
    public int getHeight(){
        return side;
    }

    
    
    public void draw (GraphicsContext g) {
    	g.setFill( colour );
    	g.setStroke( colour );
    	if (isFilled) {
    		g.fillRect( x, y, side, side);
    	} 
    	else {
    		g.strokeRect( x, y, side, side);
	    }
    }

    
}