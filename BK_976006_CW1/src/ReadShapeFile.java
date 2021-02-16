
/**
 * This class reads a shape file.  For the format of this shape file, see the assignment description.
 * Also, please see the shape files ExampleShapes.txt, ExampleShapesStill.txt, and TwoRedCircles.txt
 *
 * @author Bartosz Kubica
 *
 */
import javafx.scene.paint.Color;
import java.io.*;
import java.util.Scanner;

public class ReadShapeFile {

    // TODO: You will likely need to write four methods here. One method to
    // construct each shape
    // given the Scanner passed as a parameter. I would suggest static
    // methods in this case.
    private static Color color(int r, int g, int b) {
        Color c = Color.rgb(r, g, b);
        return c;
    }

    /**
     * method to create a circle
     * @param in scanner
     * @return circle object as Circle class
     */
    private static Circle circle(Scanner in) {
        int px, py, vx, vy, diameter, insTime, r, g, b;
        boolean isFilled;

        px = in.nextInt();
        py = in.nextInt();
        vx = in.nextInt();
        vy = in.nextInt();
        isFilled = in.nextBoolean();
        diameter = in.nextInt();
        r = in.nextInt();
        g = in.nextInt();
        b = in.nextInt();
        insTime = in.nextInt();

        Circle c = new Circle(insTime, px, py, vx, vy, diameter, color(r, g, b), isFilled);
        //System.out.println(c.toString());
        return c;
    }

    /**
     * method to create an oval
     * @param in scanner
     * @return oval object as Oval class
     */
    private static Oval oval(Scanner in) {
        int px, py, vx, vy, width, hieght, insTime, r, g, b;
        boolean isFilled;
        
        px = in.nextInt();
        py = in.nextInt();
        vx = in.nextInt();
        vy = in.nextInt();
        isFilled = in.nextBoolean();
        width = in.nextInt();
        hieght = in.nextInt();
        r = in.nextInt();
        g = in.nextInt();
        b = in.nextInt();
        insTime = in.nextInt();

        Oval o = new Oval(insTime, px, py, vx, vy, width, hieght, color(r, g, b), isFilled);
        //System.out.println(o.toString());
        return o;
    }

    /**
     * method to create a square
     * @param in scanner
     * @return square object as Square class
     */
    private static Square square(Scanner in) {
        int px, py, vx, vy, side, insTime, r, g, b;
        boolean isFilled;
        
        px = in.nextInt();
        py = in.nextInt();
        vx = in.nextInt();
        vy = in.nextInt();
        isFilled = in.nextBoolean();
        side = in.nextInt();
        r = in.nextInt();
        g = in.nextInt();
        b = in.nextInt();
        insTime = in.nextInt();

        Square sqr = new Square(insTime, px, py, vx, vy, side, color(r, g, b), isFilled);
        //System.out.println(sqr.toString());
        return sqr;
    }
    
    /**
     * method to create a rectangle
     * @param in scanner
     * @return rectangle object as Rect class
     */
    private static Rect rect(Scanner in) {
        int px, py, vx, vy, width, hieght, insTime, r, g, b;
        boolean isFilled;
        
        px = in.nextInt();
        py = in.nextInt();
        vx = in.nextInt();
        vy = in.nextInt();
        isFilled = in.nextBoolean();
        width = in.nextInt();
        hieght = in.nextInt();
        r = in.nextInt();
        g = in.nextInt();
        b = in.nextInt();
        insTime = in.nextInt();

        Rect rec = new Rect(insTime, px, py, vx, vy, width, hieght, color(r, g, b), isFilled);
        //System.out.println(rec.toString());
        return rec;
    }
    
    /**
     * method to create a line shape
     * @param in scanner
     * @return line object
     */
    private static Line line(Scanner in){
        int px, py, vx, vy, insTime, r, g, b,length;
        
        px = in.nextInt();
        py = in.nextInt();
        vx = in.nextInt();
        vy = in.nextInt();
        length = in.nextInt();
        r = in.nextInt();
        g = in.nextInt();
        b = in.nextInt();
        insTime = in.nextInt();
        
        Line l = new Line(insTime, px, py, vx, vy, color(r,g,b),length);
        //System.out.println(l.toString());
        return l;
    }
    
    /**
     * Reads the data file used by the program and returns the constructed queue
     *
     * @param in the scanner of the file
     * @return the queue represented by the data file
     */
    private static Queue<ClosedShape> readDataFile(Scanner in) {
        Queue<ClosedShape> shapeQueue = new Queue<ClosedShape>();
        String type;

        while (in.hasNext()) {
            type = in.next();
            switch (type) {
                case "circle":
                    Circle c = circle(in);
                    shapeQueue.enqueue(c);
                    break;
                case "oval":
                    Oval o = oval(in);
                    shapeQueue.enqueue(o);
                    break;
                case "square":
                    Square s = square(in);
                    shapeQueue.enqueue(s);
                    break;
                case "rect":
                    Rect r = rect(in);
                    shapeQueue.enqueue(r);
                    break;
                case "line":
                    Line l = line(in);
                    shapeQueue.enqueue(l);
                    break;
                default:
                    break;
            }

        }
        
        
        //shapeQueue.print();

        in.close();

        return shapeQueue;
    }

    /**
     * Method to read the file and return a queue of shapes from this file. The program should handle the file not found exception here and shut down the program gracefully.
     *
     * @param filename the name of the file
     * @return the queue of shapes from the file
     */
    public static Queue<ClosedShape> readDataFile(String filename) {
        // HINT: You might want to open a file here.
        Scanner in = new Scanner(System.in); //null is not sensible. Please change
        File file = new File(filename);

        try {
            in = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found in directory");
            System.exit(0);
        }

        return ReadShapeFile.readDataFile(in);

    }
}
