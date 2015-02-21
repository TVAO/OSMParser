package Controller;

import Model.LineObject;
import Model.Model;
import View.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.ScrollEvent;
import java.io.File;

/**
 * Controller defines the GUI's behavior for interacting with the user with use of View methods
 */
public class Controller {

    // Fields 
    Model model;
    View view;

    @FXML
    private Canvas canvas;

    @FXML
    private static ObservableList<LineObject> obsList = FXCollections.observableArrayList();

    // Constructor
    public Controller() {
        model = new Model();
        view = new View();
    }

    @FXML
    private void initialize() {
        view.setGraphics(canvas.getGraphicsContext2D());
        view.clearCanvas();
    }

    @FXML
    private void draw() {
        view.drawMap(obsList);
    }

    @FXML
    private void clear() {
        view.clearCanvas();
    }

    @FXML
    private void loadFile() {
        obsList.clear(); // Avoid drawing previous lines 
        File file = view.chooseFile();

        // File is null if user presses cancel
        if (file != null) {
            model.readFile(file, obsList);
        }
        draw();
    }

    @FXML
    private void zoom(ScrollEvent event) {

        if (event.getDeltaY()>0) view.zoomIn(obsList);
        else view.zoomOut(obsList);
    }

}