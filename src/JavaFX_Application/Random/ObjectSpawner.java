package JavaFX_Application.Random;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Object Spawner is a GUI application which gives the user the ability to create basic lines, circles, and text objects
 * which can then be moved around to create pictures.
 * @Author Vincent B
 * @Requirments JavaFX
 */
public class ObjectSpawner extends Application {
    private double spawnX;
    private double spawnY;
    Pane mainPane = new Pane();

    /**
     * Method which runs when program starts. Creates GUI.
     * @param primaryStage
     */
    public void start(Stage primaryStage){
        //Object Creator
        Button messageButton = new Button("Message");
        Button circleButton = new Button("Circle");
        Button horizontalLineButton = new Button("H Line");
        Button verticalLineButton = new Button("V Line");

        TextField messageField = new TextField();
        TextField lineLengthField = new TextField();

        //Layout
        messageButton.setLayoutX(50);
        messageButton.setLayoutY(50);

        circleButton.setLayoutX(150);
        circleButton.setLayoutY(50);

        horizontalLineButton.setLayoutX(250);
        horizontalLineButton.setLayoutY(50);

        verticalLineButton.setLayoutX(350);
        verticalLineButton.setLayoutY(50);

        messageField.setLayoutX(50);
        messageField.setLayoutY(20);

        lineLengthField.setLayoutX(250);
        lineLengthField.setLayoutY(20);

        //Add Nodes
        mainPane.getChildren().addAll(messageButton, circleButton, horizontalLineButton, verticalLineButton,
                messageField, lineLengthField);

        //Button Event Handlers
        messageButton.setOnAction( e -> {
            if (!(messageField.getText().equals(""))) {
                spawn(new Text(), messageField.getText());
            }
        });

        circleButton.setOnAction( e -> {
            spawn(new Circle());
        });

        horizontalLineButton.setOnAction( e -> {
            if (!(lineLengthField.getText().equals(""))) {
                spawnH(new Line(), Double.parseDouble(lineLengthField.getText()));
            }
        });

        verticalLineButton.setOnAction( e -> {
            if (!(lineLengthField.getText().equals(""))) {
                spawnV(new Line(), Double.parseDouble(lineLengthField.getText()));
            }
        });

        //Set spawn x and y
        spawnX = 500;
        spawnY = 500;

        //Scene and Stage setup
        Scene mainScene = new Scene(mainPane);
        primaryStage.setScene(mainScene);
        primaryStage.setMinHeight(1000);
        primaryStage.setMinWidth(1000);
        primaryStage.setTitle("Draggable Items");
        primaryStage.show();
    }

    /**
     * Spawns a horizontal line at point (spawnX, spawnY)
     * @param line JavaFX line object
     * @param length length of line object
     */
    public void spawnH(Line line, double length){
        line = new Line(spawnX, spawnY, (spawnX + length), (spawnY));
        makeDraggable(line);
        makeDestroyable(line);
        line.setStrokeWidth(5);
        mainPane.getChildren().add(line);
    }

    /**
     * Spawns a vertical line at point (spawnX, spawnY)
     * @param line JavaFX line object
     * @param length length of line object
     */
    public void spawnV(Line line, double length){
        line = new Line(spawnX, spawnY, spawnX, (spawnY + length));
        makeDraggable(line);
        makeDestroyable(line);
        line.setStrokeWidth(5);
        mainPane.getChildren().add(line);
    }

    /**
     * Spawns a circle at point (spawnX, spawnY)
     * @param circle JavaFX circle object
     */
    public void spawn(Circle circle){
        circle = new Circle(spawnX, spawnY, 50);
        makeDraggable(circle);
        makeDestroyable(circle);
        circle.setFill(Color.BLACK);
        mainPane.getChildren().add(circle);
    }

    /**
     * Spawns a line of text at point (spawnX, spawnY)
     * @param text JavaFX text object
     * @param message string of text
     */
    public void spawn(Text text, String message){
        text = new Text(spawnX, spawnY, message);
        makeDraggable(text);
        makeDestroyable(text);
        mainPane.getChildren().add(text);
    }

    /**
     * Enables destroyObject to be destroyed by right-clicking it
     * @param destroyObject object that will become destroyable
     */
    void makeDestroyable(Object destroyObject){
        Node destroyObject_ = (Node)destroyObject;
        destroyObject_.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY){
                mainPane.getChildren().remove(destroyObject_);
            }
        });
    }

    /**
     * Enables dragObject (Text) to be dragged across the screen
     * @param dragObject object that will become draggable
     */
    void makeDraggable(Text dragObject) {
        //Move message with mouse
        dragObject.setOnMouseDragged(e -> {
            dragObject.setX(e.getX());
            dragObject.setY(e.getY());
        });
    }

    /**
     * Enables dragObject (Circle) to be dragged across the screen
     * @param dragObject object that will become draggable
     */
    void makeDraggable(Circle dragObject){
        dragObject.setOnMouseDragged(e -> {
            dragObject.setCenterX(e.getX());
            dragObject.setCenterY(e.getY());
        });
    }

    /**
     * Enables dragObject (Line) to be dragged across the screen
     * @param dragObject object that will become draggable
     */
    void makeDraggable(Line dragObject){
        double xDifference = dragObject.getStartX() - dragObject.getEndX();
        double yDifference = dragObject.getStartY()- dragObject.getEndY();
        dragObject.setOnMouseDragged(e -> {
            double x = e.getX();
            double y = e.getY();
            dragObject.setStartX(x);
            dragObject.setEndX(x - xDifference);
            dragObject.setStartY(y);
            dragObject.setEndY(y - yDifference);
        });
    }
}
