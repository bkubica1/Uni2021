
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * 
 * @author Bartosz Kubica
 */

public class Rect extends ClosedShape{
    
    private int width;
    private int height;
    
     public Rect(int insertionTime, int x, int y, int vx, int vy, int width, int height, Color colour, boolean isFilled){
        super(insertionTime, x, y, vx, vy, colour, isFilled);
        this.width = width;
        this.height = height;
        
    }
    
    public String toString () {
    	String result = "This is a rectangle\n";
    	result += super.toString ();
	result += "Its width is " + this.width + "\n";
        result += "Its height is " + this.height + "\n";
    	return result;
    }
    
    public void setWidth(int width){
        this.width = width;
    }
    
    public void setHeight(int height){
        this.height = height;
    }
    
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }

    
    
    public void draw (GraphicsContext g) {
    	g.setFill( colour );
    	g.setStroke( colour );
    	if (isFilled) {
    		g.fillRect( x, y, width, height);
    	} 
    	else {
    		g.strokeRect( x, y, width, height);
	    }
    }
}