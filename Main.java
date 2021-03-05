
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.*;
import java.util.Arrays;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Main class to represent the CT Head visually
 *
 * @author Bartosz Kubica 976006
 * @version 1.0
 */
public class Main extends Application {

    private final int CT_X_AXIS = 256;
    private final int CT_Y_AXIS = 256;
    private final int CT_Z_AXIS = 113;
    private final int MAX_OPACITY = 100;
    private final int WINDOW_WIDTH = 1100;
    private final int WINDOW_HEIGHT = 520;
    private final String WIDNOW_TITLE = "CThead Viewer";

    private short[][][] ctHead; //store the 3D volume data set
    private short min; //min value in the 3D volume data set
    private short max; //max value in the 3D volume data set
    private double[] lightSource = new double[]{1, 1, 1};

    @Override
    public void start(Stage stage) throws FileNotFoundException, IOException {
        stage.setTitle(WIDNOW_TITLE);

        ReadData();

        //Good practice: Define your top view, front view and side view images (get the height and width correct)
        //Here's the top view - looking down on the top of the head (each slice we are looking at is CT_X_AXIS x CT_Y_AXIS)
        int topWidth = CT_X_AXIS;
        int topHeight = CT_Y_AXIS;

        //Here's the front view - looking at the front (nose) of the head (each slice we are looking at is CT_X_AXIS x CT_Z_AXIS)
        int frontWidth = CT_X_AXIS;
        int frontHeight = CT_Z_AXIS;

        int sideWidth = CT_Y_AXIS;
        int sideHeight = CT_Z_AXIS;

        //and you do the other (side view) - looking at the ear of the head
        //We need 3 things to see an image
        //1. We create an image we can write to
        WritableImage topImage = new WritableImage(topWidth, topHeight);
        WritableImage frontImage = new WritableImage(frontWidth, frontHeight);
        WritableImage sideImage = new WritableImage(sideWidth, sideHeight);
        WritableImage shadeImage = new WritableImage(frontWidth, frontHeight);

        //2. We create a view of that image
        ImageView topView = new ImageView(topImage);
        ImageView frontView = new ImageView(frontImage);
        ImageView sideView = new ImageView(sideImage);
        ImageView shadeView = new ImageView(shadeImage);

        Button slice76Button = new Button("Slice 76"); //an example button to get the slice 76
        //sliders to step through the slices (top and front directions) (remember 113 slices in top direction 0-112)
        Slider topSlider = new Slider(0, CT_Z_AXIS - 1, 0);
        topSlider.setShowTickMarks(true);
        topSlider.setShowTickLabels(true);

        Slider frontSlider = new Slider(0, CT_Y_AXIS - 1, 0);
        frontSlider.setShowTickMarks(true);
        frontSlider.setShowTickLabels(true);

        Slider sideSlider = new Slider(0, CT_X_AXIS - 1, 0);
        sideSlider.setShowTickMarks(true);
        sideSlider.setShowTickLabels(true);

        Slider topOpacitySlider = new Slider(0, MAX_OPACITY, 0);
        topOpacitySlider.setShowTickMarks(true);
        topOpacitySlider.setShowTickLabels(true);

        Slider frontOpacitySlider = new Slider(0, MAX_OPACITY, 0);
        frontOpacitySlider.setShowTickMarks(true);
        frontOpacitySlider.setShowTickLabels(true);

        Slider SideOpacitySlider = new Slider(0, MAX_OPACITY, 0);
        SideOpacitySlider.setShowTickMarks(true);
        SideOpacitySlider.setShowTickLabels(true);

        Slider lightXSlider = new Slider(1, CT_X_AXIS - 1, 0);
        lightXSlider.setShowTickMarks(true);
        lightXSlider.setShowTickLabels(true);

        Slider lightYSlider = new Slider(1, CT_Y_AXIS - 1, 0);
        lightYSlider.setShowTickMarks(true);
        lightYSlider.setShowTickLabels(true);

        Slider lightZSlider = new Slider(1, CT_Z_AXIS - 1, 0);
        lightZSlider.setShowTickMarks(true);
        lightZSlider.setShowTickLabels(true);

        Label topLabel = new Label("Top View");
        Label topSliceLabel = new Label("Slice: 0");
        Label topVolLabel = new Label("Volume Render: 0");

        Label frontLabel = new Label("Front View");
        Label frontSliceLabel = new Label("Slice: 0");
        Label frontVolLabel = new Label("Volume Render: 0");

        Label sideLabel = new Label("Side View");
        Label sideSliceLabel = new Label("Slice: 0");
        Label sideVolLabel = new Label("Volume Render: 0");

        Label shadeLabel = new Label("Shaded View");
        Label valueXLabel = new Label("X: 1");
        Label valueYLabel = new Label("Y: 1");
        Label valueZLabel = new Label("Z: 1");

        slice76Button.setOnAction(e -> {
            changeTopImage(topImage, 76);
            changeFrontImage(frontImage, 76);
            changeSideImage(sideImage, 76);
            topSlider.setValue(76);
            frontSlider.setValue(76);
            sideSlider.setValue(76);
        });

        Button volrendButton = new Button("Volume Render");
        volrendButton.setOnAction(e -> {
            compositingTop(topImage, 12);
            compositingFront(frontImage, 12);
            compositingSide(sideImage, 12);
            topOpacitySlider.setValue(12);
            frontOpacitySlider.setValue(12);
            SideOpacitySlider.setValue(12);
        });

        topSlider.valueProperty().addListener(
                new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //System.out.println(newValue.intValue());
                changeTopImage(topImage, newValue.intValue());
                topSliceLabel.setText("Slice: " + newValue.intValue());
            }
        });

        frontSlider.valueProperty().addListener(
                new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //System.out.println(newValue.intValue());
                changeFrontImage(frontImage, newValue.intValue());
                frontSliceLabel.setText("Slice: " + newValue.intValue());
            }
        });

        sideSlider.valueProperty().addListener(
                new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //System.out.println(newValue.intValue());
                changeSideImage(sideImage, newValue.intValue());
                sideSliceLabel.setText("Slice: " + newValue.intValue());
            }
        });

        topOpacitySlider.valueProperty().addListener(
                new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //System.out.println(newValue.intValue());
                compositingTop(topImage, newValue.intValue());
                topVolLabel.setText("Volume Render: " + newValue.intValue());
            }
        });

        frontOpacitySlider.valueProperty().addListener(
                new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //System.out.println(newValue.intValue());
                compositingFront(frontImage, newValue.intValue());
                frontVolLabel.setText("Volume Render: " + newValue.intValue());
            }
        });

        SideOpacitySlider.valueProperty().addListener(
                new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //System.out.println(newValue.intValue());
                compositingSide(sideImage, newValue.intValue());
                sideVolLabel.setText("Volume Render: " + newValue.intValue());
            }
        });

        lightXSlider.valueProperty().addListener(
                new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //System.out.println(newValue.intValue());
                lightSource[0] = newValue.intValue();
                valueXLabel.setText("X: " + newValue.intValue());
                shading(shadeImage);
            }
        });

        lightYSlider.valueProperty().addListener(
                new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //System.out.println(newValue.intValue());
                lightSource[1] = newValue.intValue();
                valueYLabel.setText("Y: " + newValue.intValue());
                shading(shadeImage);
            }
        });

        lightZSlider.valueProperty().addListener(
                new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //System.out.println(newValue.intValue());
                lightSource[2] = newValue.intValue();
                valueZLabel.setText("Z: " + newValue.intValue());
                shading(shadeImage);
            }
        });

        changeTopImage(topImage, 0);
        changeFrontImage(frontImage, 0);
        changeSideImage(sideImage, 0);
        shading(shadeImage);

        VBox root = new VBox(10);

        VBox topViewBox = new VBox(10);
        topViewBox.getChildren().addAll(topView, topLabel, topSliceLabel, topSlider, topVolLabel, topOpacitySlider);

        VBox frontViewBox = new VBox(10);
        frontViewBox.getChildren().addAll(frontView, frontLabel, frontSliceLabel, frontSlider, frontVolLabel, frontOpacitySlider);

        VBox sideViewBox = new VBox(10);
        sideViewBox.getChildren().addAll(sideView, sideLabel, sideSliceLabel, sideSlider, sideVolLabel, SideOpacitySlider);

        VBox shadeViewBox = new VBox(10);
        shadeViewBox.getChildren().addAll(shadeView, shadeLabel, valueXLabel, lightXSlider, valueYLabel, lightYSlider, valueZLabel, lightZSlider);

        HBox viewer = new HBox(10);
        viewer.getChildren().addAll(topViewBox, frontViewBox, sideViewBox, shadeViewBox);

        VBox volrendBox = new VBox(10);
        volrendBox.getChildren().addAll(volrendButton);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(slice76Button);

        //3. (referring to the 3 things we need to display an image)
        //we need to add it to the flow pane
        root.getChildren().addAll(viewer, buttonBox, volrendBox);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Function to create Gradient Shading on CT Head
     *
     * @param image WriteableImage to change
     */
    public void shading(WritableImage image) {
        PixelWriter image_writer = image.getPixelWriter();
        double[] lightVector = new double[3];
        double[] surface;
        double gX, gY, gZ;
        double length;
        double lightLength;
        double col;

        for (int z = 0; z < CT_Z_AXIS; z++) {
            for (int x = 0; x < CT_X_AXIS; x++) {
                for (int y = 0; y < CT_Y_AXIS; y++) {
                    if (ctHead[z][y][x] >= 400 && ctHead[z][y + 1][x] >= 400) {
                        if (x == 0) {
                            gX = ctHead[z][y][(x + 1)] - ctHead[z][y][x];
                        } else if (x >= CT_X_AXIS - 1) {
                            gX = ctHead[z][y][x] - ctHead[z][y][(x - 1)];
                        } else {
                            gX = ctHead[z][y][(x + 1)] - ctHead[z][y][(x - 1)];
                        }
                        if (y == 0) {
                            gY = ctHead[z][(y + 1)][x] - ctHead[z][(y)][x];
                        } else if (y >= CT_Y_AXIS - 1) {
                            gY = ctHead[z][(y)][x] - ctHead[z][(y - 1)][x];
                        } else {
                            gY = ctHead[z][(y + 1)][x] - ctHead[z][(y - 1)][x];
                        }
                        if (z == 0) {
                            gZ = ctHead[(z + 1)][y][x] - ctHead[(z)][y][x];
                        } else if (z >= CT_Z_AXIS - 1) {
                            gZ = ctHead[(z)][y][x] - ctHead[(z - 1)][y][x];
                        } else {
                            gZ = ctHead[(z + 1)][y][x] - ctHead[(z - 1)][y][x];
                        }

                        lightVector[0] = x - lightSource[0];
                        lightVector[1] = y - lightSource[1];
                        lightVector[2] = z - lightSource[2];

                        lightLength = Math.sqrt((lightVector[0] * lightVector[0])
                                + (lightVector[1] * lightVector[1])
                                + (lightVector[2] * lightVector[2]));

                        lightVector[0] = lightVector[0] / lightLength;
                        lightVector[1] = lightVector[1] / lightLength;
                        lightVector[2] = lightVector[2] / lightLength;

                        length = Math.sqrt((gX * gX) + (gY * gY) + (gZ * gZ));
                        surface = new double[]{(gX / length), (gY / length), (gZ / length)};
                        if ((lightVector[0] * surface[0]) <= 0) {
                            surface[0] = 0;
                        } else {
                            surface[0] = (lightVector[0] * surface[0]);
                        }
                        if ((lightVector[1] * surface[1]) <= 0) {
                            surface[1] = 0;
                        } else {
                            surface[1] = (lightVector[1] * surface[1]);
                        }
                        if ((lightVector[2] * surface[2]) <= 0) {
                            surface[2] = 0;
                        } else {
                            surface[2] = (lightVector[2] * surface[2]);
                        }
                        col = surface[0] + surface[1] + surface[2];
                        if (col > 1.0) {
                            col = 1.0;
                        }
                        image_writer.setColor(x, z, Color.color(col, col, col, 1.0));
                        break;
                    }
                }
            }
        }
    }

    /**
     * Compositing function for Front image.
     *
     * @param image WritableImage to change.
     * @param opacityInt Opacity from slider
     */
    public void compositingFront(WritableImage image, int opacityInt) {
        double opacity = (double) opacityInt / 100;
        double alphaAcc = opacity;
        double[] colourAcc = {0.0, 0.0, 0.0};
        PixelWriter image_writer = image.getPixelWriter();

        int value;
        double[] valueCol;

        for (int x = 0; x < CT_X_AXIS; x++) {
            for (int z = 0; z < CT_Z_AXIS; z++) {
                for (int y = 0; y < CT_Y_AXIS; y++) {
                    value = ctHead[z][y][x];
                    valueCol = transferFunction(value, opacity);
                    colourAcc[0] = colourAcc[0] + (alphaAcc * (valueCol[0] * valueCol[3]));
                    colourAcc[1] = colourAcc[1] + (alphaAcc * (valueCol[1] * valueCol[3]));
                    colourAcc[2] = colourAcc[2] + (alphaAcc * (valueCol[2] * valueCol[3]));
                    alphaAcc = alphaAcc * (1 - valueCol[3]);
                    if (colourAcc[0] > 1) {
                        colourAcc[0] = 1.0;
                    }
                    if (colourAcc[1] > 1) {
                        colourAcc[1] = 1.0;
                    }
                    if (colourAcc[2] > 1) {
                        colourAcc[2] = 1.0;
                    }
                    if (valueCol[3] >= 1.0 || alphaAcc < 0) {
                        break;
                    }
                }
                image_writer.setColor(x, z, Color.color(colourAcc[0], colourAcc[1], colourAcc[2], 1.0));
                colourAcc[0] = 0.0;
                colourAcc[1] = 0.0;
                colourAcc[2] = 0.0;
                alphaAcc = 1.0;
            }
        }

    }

    /**
     * Compositing function for Top image.
     *
     * @param image WritableImage to change.
     * @param opacityInt Opacity from slider
     */
    public void compositingTop(WritableImage image, int opacityInt) {
        double opacity = (double) opacityInt / 100;
        double alphaAcc = opacity;
        double[] colourAcc = {0.0, 0.0, 0.0};
        PixelWriter image_writer = image.getPixelWriter();

        int value;
        double[] valueCol;

        for (int x = 0; x < CT_X_AXIS; x++) {
            for (int y = 0; y < CT_Y_AXIS; y++) {
                for (int z = 0; z < CT_Z_AXIS; z++) {
                    value = ctHead[z][y][x];
                    valueCol = transferFunction(value, opacity);
                    colourAcc[0] = colourAcc[0] + (alphaAcc * (valueCol[0] * valueCol[3]));
                    colourAcc[1] = colourAcc[1] + (alphaAcc * (valueCol[1] * valueCol[3]));
                    colourAcc[2] = colourAcc[2] + (alphaAcc * (valueCol[2] * valueCol[3]));
                    alphaAcc = alphaAcc * (1 - valueCol[3]);
                    if (colourAcc[0] > 1) {
                        colourAcc[0] = 1.0;
                    }
                    if (colourAcc[1] > 1) {
                        colourAcc[1] = 1.0;
                    }
                    if (colourAcc[2] > 1) {
                        colourAcc[2] = 1.0;
                    }
                }
                image_writer.setColor(x, y, Color.color(colourAcc[0], colourAcc[1], colourAcc[2], 1.0));
                colourAcc[0] = 0.0;
                colourAcc[1] = 0.0;
                colourAcc[2] = 0.0;
                alphaAcc = 1.0;
            }
        }

    }

    /**
     * Compositing function for side image.
     *
     * @param image WritableImage to change.
     * @param opacityInt Opacity from slider
     */
    public void compositingSide(WritableImage image, int opacityInt) {
        double opacity = (double) opacityInt / 100;
        double alphaAcc = opacity;
        double[] colourAcc = {0.0, 0.0, 0.0};
        PixelWriter image_writer = image.getPixelWriter();

        int value;
        double[] valueCol;

        for (int y = 0; y < CT_Y_AXIS; y++) {
            for (int z = 0; z < CT_Z_AXIS; z++) {
                for (int x = 0; x < CT_X_AXIS; x++) {
                    value = ctHead[z][y][x];
                    valueCol = transferFunction(value, opacity);
                    colourAcc[0] = colourAcc[0] + (alphaAcc * (valueCol[0] * valueCol[3]));
                    colourAcc[1] = colourAcc[1] + (alphaAcc * (valueCol[1] * valueCol[3]));
                    colourAcc[2] = colourAcc[2] + (alphaAcc * (valueCol[2] * valueCol[3]));
                    alphaAcc = alphaAcc * (1 - valueCol[3]);
                    if (colourAcc[0] > 1) {
                        colourAcc[0] = 1.0;
                    }
                    if (colourAcc[1] > 1) {
                        colourAcc[1] = 1.0;
                    }
                    if (colourAcc[2] > 1) {
                        colourAcc[2] = 1.0;
                    }
                }
                image_writer.setColor(y, z, Color.color(colourAcc[0], colourAcc[1], colourAcc[2], 1.0));
                colourAcc[0] = 0.0;
                colourAcc[1] = 0.0;
                colourAcc[2] = 0.0;
                alphaAcc = 1.0;
            }
        }

    }

    /**
     * Transfer function to return RGBA values depending on reading from CT Head
     *
     * @param value Reading from CT Head
     * @param opacity Opacity from slider
     * @return RGBA in double array
     */
    public double[] transferFunction(int value, double opacity) {
        if (value < -300) {
            return new double[]{0, 0, 0, 0};
        } else if (value >= -300 && value <= 49) {
            return new double[]{1.0, 0.79, 0.6, opacity};
        } else if (value >= 50 && value <= 299) {
            return new double[]{0, 0, 0, 0};
        } else if (value >= 300 && value <= 4096) {
            return new double[]{1.0, 1.0, 1.0, 0.8};
        }
        return new double[]{0, 0, 0, 0};
    }

    /**
     * Change the top view image
     *
     * @param image image to change (topImage)
     * @param slice slice number to present
     */
    public void changeTopImage(WritableImage image, int slice) {
        int w = (int) image.getWidth(), h = (int) image.getHeight();
        PixelWriter image_writer = image.getPixelWriter();

        double col;
        short datum;

        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                datum = ctHead[slice][j][i];
                col = (((float) datum - (float) min) / ((float) (max - min)));
                image_writer.setColor(i, j, Color.color(col, col, col, 1.0));
            }
        }
    }

    /**
     * Change the front view image
     *
     * @param image image to change (frontImage)
     * @param slice slice number to present
     */
    public void changeFrontImage(WritableImage image, int slice) {
        int w = (int) image.getWidth(), h = (int) image.getHeight();
        PixelWriter image_writer = image.getPixelWriter();

        double col;
        short datum;

        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                datum = ctHead[j][slice][i];
                col = (((float) datum - (float) min) / ((float) (max - min)));
                image_writer.setColor(i, j, Color.color(col, col, col, 1.0));
            }
        }
    }

    /**
     * Change the side view image
     *
     * @param image image to change (sideImage)
     * @param slice slice number to present
     */
    public void changeSideImage(WritableImage image, int slice) {
        int w = (int) image.getWidth(), h = (int) image.getHeight();
        PixelWriter image_writer = image.getPixelWriter();

        double col;
        short datum;

        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                datum = ctHead[j][i][slice];
                col = (((float) datum - (float) min) / ((float) (max - min)));
                image_writer.setColor(i, j, Color.color(col, col, col, 1.0));
            }
        }
    }

    /**
     * Function to read CT Head file
     *
     * @throws IOException if file not found
     */
    public void ReadData() throws IOException {
        //File name is hardcoded here - much nicer to have a dialog to select it and capture the size from the user
        File file = new File("CThead");
        //Read the data quickly via a buffer (in C++ you can just do a single fread - I couldn't find if there is an equivalent in Java)
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));

        int i, j, k; //loop through the 3D data set

        min = Short.MAX_VALUE;
        max = Short.MIN_VALUE; //set to extreme values
        short read; //value read in
        int b1, b2; //data is wrong Endian (check wikipedia) for Java so we need to swap the bytes around

        ctHead = new short[CT_Z_AXIS][CT_Y_AXIS][CT_X_AXIS]; //allocate the memory - note this is fixed for this data set
        //loop through the data reading it in
        for (k = 0; k < CT_Z_AXIS; k++) {
            for (j = 0; j < CT_Y_AXIS; j++) {
                for (i = 0; i < CT_X_AXIS; i++) {
                    //because the Endianess is wrong, it needs to be read byte at a time and swapped
                    b1 = ((int) in.readByte()) & 0xff; //the 0xff is because Java does not have unsigned types
                    b2 = ((int) in.readByte()) & 0xff; //the 0xff is because Java does not have unsigned types
                    read = (short) ((b2 << 8) | b1); //and swizzle the bytes around
                    if (read < min) {
                        min = read; //update the minimum
                    }
                    if (read > max) {
                        max = read; //update the maximum
                    }
                    ctHead[k][j][i] = read; //put the short into memory (in C++ you can replace all this code with one fread)
                }
            }
        }
        System.out.println(min + " " + max); //diagnostic - for CThead this should be -1117, 2248
        //(i.e. there are 3366 levels of grey (we are trying to display on 256 levels of grey)
        //therefore histogram equalization would be a good thing
        //maybe put your histogram equalization code here to set up the mapping array
    }
    
    public static void main(String[] args) {
        launch();
    }

}
