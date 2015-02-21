package View;

import Model.LineObject;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * The view class consists of FXML and display methods 
 */

public class View extends Application {

    // Fields
    private boolean launched = false;
    private GraphicsContext gc;
    private Group page;

    // Constructor
    public View() {}

    /**
     * Initializes the view 
     * @param primaryStage - top level JavaFX container 
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Load view
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("View.fxml"));
            Parent drawView = loader.load();
            primaryStage.setTitle("Line drawer");
            // Lock size
            primaryStage.setResizable(false);
            // Show view
            primaryStage.setScene(new Scene(drawView));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Something went wrong..."); // Show dialogue
        }
    }

    /**
     * Separates main from view 
     * @param arg - 
     */
    public void launchApplication(String[] arg) {
        if (!launched) {
            launch(arg);
            launched = true;
        } else System.out.println("The application is already running.");
    }

    /**
     * Sets the Graphics Context used for drawing in canvas 
     * @param graphics - used to issue draw calls to canvas 
     */
    public void setGraphics(GraphicsContext graphics) {
        gc = graphics;
    }

    /**
     * Clears canvas (e.g. when loading repeatedly) 
     */
    public void clearCanvas() {
        gc.clearRect(0, 0, 1022, 697); // Overwrite
        gc.setFill(Color.WHITE); // White background 
        gc.fillRect(0, 0, 1022, 697); // Recolor
    }

    /**
     * Draws representation of map using Line objects that holds coordinates  
     * @param lineList - holds Line objects with coordinates 
     */
    public void drawMap(ObservableList<LineObject> lineList) {
        clearCanvas(); // Reset before drawing
        for (LineObject line : lineList) {
            // Draw thick black line
            gc.setLineWidth(2);
            gc.setStroke(Color.BLACK);
            gc.setLineCap(StrokeLineCap.BUTT);
            gc.strokeLine(line.getX1(), line.getY1(), line.getX2(), line.getY2());
            // Draw thin overlapping white line to show roads on map 
            gc.setLineWidth(1);
            gc.setStroke(Color.WHITE);
            gc.setLineCap(StrokeLineCap.BUTT);
            gc.strokeLine(line.getX1(), line.getY1(), line.getX2(), line.getY2());
        }
    }

    public void loadMap() {}

    /**
     * Creates a file menu dialogue upon using the load button in GUI and save data input in file 
     * @return - file with data input 
     */
    public File chooseFile() {
        FileChooser fileChooser = new FileChooser(); // File menu dialogue 
        // Show text files only 
        FileChooser.ExtensionFilter textFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(textFilter);
        // Show dialogue 
        fileChooser.setTitle("Choose file");
        File file = fileChooser.showOpenDialog(new Stage()); // Opens new window and saves data input in File variable 

        return file;
    }

    /**
     * Zoom in on the Canvas using scroll
     * @param obsList - allows listener to track changes
     */
    public void zoomIn(ObservableList obsList) {
        double scaleFactor = 0.9;
        gc.scale(scaleFactor, scaleFactor);
        drawMap(obsList); // dynamic resize
    }

    /**
     * Zoom out on the canvas using scroll
     * @param obsList - allows listener to track changes
     */
    public void zoomOut(ObservableList obsList) {
        double scaleFactor = 1.1;
        gc.scale(scaleFactor, scaleFactor);
        drawMap(obsList); // dynamic resize
    }

    // (NOT WORKING YET) Should zoom relative to mouse position, found on Stacksocial
    private void setSceneEvents(Scene scene) {
        //handles mouse scrolling
        scene.setOnScroll(
                new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        double zoomFactor = 1.05;
                        double deltaY = event.getDeltaY();
                        if (deltaY < 0) {
                            zoomFactor = 2.0 - zoomFactor;
                        }
                        System.out.println(zoomFactor);
                        page.setScaleX(page.getScaleX() * zoomFactor);
                        page.setScaleY(page.getScaleY() * zoomFactor);
                        event.consume();
                    }
                });
    }

}

