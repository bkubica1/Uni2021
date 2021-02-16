
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author Bartosz Kubica
 */
public class Line extends ClosedShape {

    private double length;

    public Line(int insertionTime, int x, int y, int vx, int vy, Color colour, int length) {
        super(insertionTime, x, y, vx, vy, colour, true);
        this.length = length;
    }

    public String toString() {
        String result = "This is a rectangle\n";
        result += super.toString();
        return result;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public int getWidth() {
        return (int) this.length;
    }

    public int getHeight() {
        return (int) this.length;
    }

    public void draw(GraphicsContext g) {
        g.setFill(colour);
        g.setStroke(colour);

        g.strokeLine(x, y, (x + length), (y + length));
    }

}
