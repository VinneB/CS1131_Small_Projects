package Week_9_Program;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.Scanner;
import java.util.MissingFormatArgumentException;

/**
 * This class loads PPM images, performs operations on the image, and can save the modified image afterwards.
 * MTU CS1131
 * @Author Vincent Barfield
 */
public class ImageManipulator extends Application implements ImageManipulatorInterface {
    private ImageView currentImageView = new ImageView();
    private WritableImage currentImage = null;

    //FRONT END METHODS

    /**
     * The primary method for all JavaFX applications. This method is called when the program is run.
     * This particular start method builds GUI and sets up button bindings.
     * @param primaryStage
     */
    public void start(Stage primaryStage) {
        BorderPane mainPane = new BorderPane();

        //Button Box
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setPadding(new Insets(10, 10, 10, 10));
        buttonBox.setAlignment(Pos.CENTER);

        mainPane.setBottom(buttonBox);

        //Buttons
        Button loadButton = new Button("Load");
        Button saveButton = new Button("Save");
        Button flipButton = new Button("Flip");
        Button grayscaleButton = new Button("Grayscale");
        Button pixelateButton = new Button("Pixelate");
        Button invertButton = new Button("Invert");

        buttonBox.getChildren().addAll(loadButton, saveButton, flipButton, grayscaleButton, pixelateButton, invertButton);

        //Setup ImageView
        Label imageLabel = new Label();
        imageLabel.setMinHeight(450);
        imageLabel.setMinWidth(500);

        imageLabel.setGraphic(currentImageView);

        mainPane.setCenter(imageLabel);

        //Setup Button Bindings
        loadButton.setOnAction(e -> handleLoadButton());
        saveButton.setOnAction(e -> handleSaveButton());
        flipButton.setOnAction(e -> handleFlipButton());
        grayscaleButton.setOnAction(e -> handleGrayscaleButton());
        pixelateButton.setOnAction(e -> handlePixelateButton());
        invertButton.setOnAction(e -> handleInvertButton());



        //Stage Setup
        primaryStage.setScene(new Scene(mainPane));

        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(700);

        primaryStage.show();
    }

    /**
     * A front-end helper method which sets a given WritableImage as the current working image. Also sets the image to be displayed
     * through the 'currentImageView' variable
     * @param image A working image which will be displayed in GUI
     */
    public void setImage(WritableImage image) {
        currentImage = image;
        currentImageView.setImage(currentImage);
    }

    //HANDLER METHODS

    /**
     * loadButton Handler. Performs intended load functionality. These functions include: requesting filename from user and
     * setting that file, if PPM file type, as 'currentImage'
     *
     * Calls: 'loadImage', 'setImage'
     */
    private void handleLoadButton() {
        FileChooser fileChooser = new FileChooser();
        Stage fileChooserStage = new Stage();

        File file = fileChooser.showOpenDialog(fileChooserStage);
        try {
            WritableImage image = loadImage(file.getAbsolutePath());
            setImage(image);
            System.out.println("Image File " + file.getAbsolutePath() + " has been loaded.");
        } catch (FileNotFoundException e){
            System.out.println("File doesn't exist");
        }

    }

    /**
     * saveButton Handler. Provides necessary save functionality. These functions include: Requesting filename, and
     * creating a file in the current directory with given filename and storing 'currentImage' in this file.
     *
     * Calls: 'saveImage'
     */
    private void handleSaveButton() {
        FileChooser fileChooser = new FileChooser();
        Stage fileChooserStage = new Stage();

        File file = fileChooser.showSaveDialog(fileChooserStage);

        try {
            saveImage(file.getAbsolutePath(), currentImage);
            System.out.println("File saved to " + file.getAbsolutePath());
        } catch (FileNotFoundException e){
            System.out.println("File does not exist");
        }

    }

    /**
     * flipButton handler. Flips 'currentImage'
     *
     * Calls: 'flipImage', 'setImage'
     */
    private void handleFlipButton(){
        setImage(flipImage(currentImage));
    }

    /**
     * grayscaleButton handler. Converts 'currentImage' to grayscale
     *
     * Calls: 'grayifyImage', 'setImage'
     */
    private void handleGrayscaleButton(){
        setImage(grayifyImage(currentImage));
    }

    /**
     * pixelateButton handler. Pixelates 'currentImage'
     *
     * Calls: 'pixelateImage', 'setImage'
     */
    private void handlePixelateButton(){
        setImage(pixelateImage(currentImage));
    }

    /**
     * invertButton handler. Inverts the color of 'currentImage'
     *
     * Calls: 'invertImage', 'setImage'
     */
    private void handleInvertButton(){
        setImage(invertImage(currentImage));
    }




    //BACK END METHODS
    /**
     * Load the specified PPM image file.
     * The image file must be in the PPM P3 format
     * see https://www.netpbm.sourceforge.net/doc/ppm.html
     *
     * Don't forget to add a load button to the application!
     *
     * @param filename
     * @return WritableImage
     * @throws FileNotFoundException
     */
    @Override
    public WritableImage loadImage(String filename) throws FileNotFoundException {
        int height = 0;
        int width = 0;
        int maxColorValue;
        String next;
        Scanner scan = null;
        WritableImage image = null;

        try {
            File imageFile = new File(filename);
            scan = new Scanner(imageFile);
            //Checks if imageFile is the proper file type
            if (!isPPM(scan)) {
                throw new InvalidParameterException("Parameter 'filename' points to invalid file type");
            }

            //Gets width
            int possibleWidth = getNextIntFromFile(scan, "Width argument does not exist");
            if (possibleWidth > 0) {
                width = possibleWidth;
            } else {
                throw new MissingFormatArgumentException("Image file's height argument does not exist");
            }

            //Gets height
            int possibleHeight = getNextIntFromFile(scan, "height argument does not exist");
            if (possibleHeight > 0) {
                height = possibleHeight;
            } else {
                throw new MissingFormatArgumentException("Image file's height argument is invalid");
            }

            //Gets maxColorValue
            int possibleMaxColorValue = getNextIntFromFile(scan, "Max Color Value argument does not exist");
            if (possibleMaxColorValue == 255) {
                maxColorValue = possibleMaxColorValue;
            } else {
                throw new InvalidParameterException("Parameter 'filename' points to PPM file with invalid 'max color value'");
            }

            //Creates WritableImage and gets the it's pixel writer
            image = new WritableImage(width, height);
            PixelWriter imagePixelWriter = image.getPixelWriter();

            //Creates image based on pixel specification in imageFile
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int red = scan.nextInt();
                    int green = scan.nextInt();
                    int blue = scan.nextInt();
                    Color color = Color.rgb(red, green, blue);
                    imagePixelWriter.setColor(x, y, color);
                }
            }

        } catch (MissingFormatArgumentException | InvalidParameterException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            scan.close();
        }

        return image;
    }

    /**
     * Save the specified image to a PPM file.
     * The image file must be in the PPM P3 format
     *  http://netpbm.sourceforge.net/doc/ppm.html
     *
     * Don't forget to add a save button to the application!
     *
     * @param filename
     * @param image
     * @throws FileNotFoundException
     */
    @Override
    public void saveImage(String filename, WritableImage image) throws FileNotFoundException {
        PrintWriter saveFileWriter = new PrintWriter(new File(filename)); //Could throw FileNotFoundException

        String magicNumber = "P3";
        int maxColorValue = 255;
        int imageHeight = (int) image.getHeight();
        int imageWidth = (int) image.getWidth();
        PixelReader pixelReader = image.getPixelReader();

        //Necessary PPM File format values
        saveFileWriter.println(magicNumber);
        saveFileWriter.println(imageWidth + " " + imageHeight);
        saveFileWriter.println(maxColorValue);

        //Saves Pixel colors
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                Color pixelColor = pixelReader.getColor(x, y);
                int red = (int) (pixelColor.getRed() * maxColorValue);
                int green = (int) (pixelColor.getGreen() * maxColorValue);
                int blue = (int) (pixelColor.getBlue() * maxColorValue);
                saveFileWriter.format("%03d %03d %03d", red, green, blue);
                saveFileWriter.print("  ");
            }
            saveFileWriter.print("\n");
        }

        System.out.format("Saved image %s", image.hashCode() + "\n");
        saveFileWriter.close();
    }

    /**
     * Invert an image by subtracting each RGB component from its max value
     *
     * For example:
     * rbg( 255, 255, 255 ) -- invert --> rbg( 0, 0, 0 )
     * rbg( 0, 0, 0 ) -- invert --> rbg( 255, 255, 255 )
     * rbg( 255, 110, 63 ) -- invert --> rbg( 0, 145, 192 )
     * rbg( 0, 145, 192 ) -- invert --> rbg( 255, 110, 63 )
     *
     * @param image - the image to be inverted, do not modify!
     * @return a new inverted image
     */
    @Override
    public WritableImage invertImage(WritableImage image) {
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = image.getPixelWriter();
        int maxColorValue = 255;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                //Inverts Colors
                int invertedRed = (int) Math.ceil(maxColorValue - (pixelReader.getColor(x, y).getRed() * maxColorValue));
                int invertedGreen = (int) Math.ceil(maxColorValue - (pixelReader.getColor(x, y).getGreen() * maxColorValue));
                int invertedBlue = (int) Math.ceil(maxColorValue - (pixelReader.getColor(x, y).getBlue() * maxColorValue));

                pixelWriter.setColor(x, y, Color.rgb(invertedRed, invertedGreen, invertedBlue));
            }
        }
        return image;
    }

    /**
     * Convert an image to grayscale using the following formula:
     * intensity = 0.2989*red + 0.5870*green + 0.1140*blue
     * new rgb( intensity, intensity, intensity );
     *
     * For example:
     * rbg( 0, 255, 255 ) -- grayify --> rbg( 178, 178, 178 )
     * rbg( 255, 0, 255 ) -- grayify --> rbg( 105, 105, 105 )
     * rbg( 255, 255, 0 ) -- grayify --> rbg( 225, 225, 225 )
     * rbg( 21, 11, 11 ) -- grayify --> rbg( 13, 13, 13 )
     *
     * @param image - the image to be converted to grayscale, do not modify!
     * @return a new image that displays in shades of gray
     */
    @Override
    public WritableImage grayifyImage(WritableImage image) {
        PixelWriter pixelWriter = image.getPixelWriter();
        PixelReader pixelReader = image.getPixelReader();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                //Calculates pixel color value based on 'luminescence' algorithm
                int red = (int) (pixelReader.getColor(x, y).getRed() * 255);
                int green = (int) (pixelReader.getColor(x, y).getGreen() * 255);
                int blue = (int) (pixelReader.getColor(x, y).getBlue() * 255);
                int luminescence = (int) ((0.2989*red) + (0.5870*green) + (0.1140*blue));

                pixelWriter.setColor(x, y, Color.rgb(luminescence, luminescence, luminescence));
            }
        }
        return image;
    }

    /**
     * Pixelates the image by dividing it into 5x5 regions, then assigning
     * all pixels in the region the same color as the central pixel.
     *
     * For example:
     * [0,0,0] [0,0,0] [0,0,0] [0,0,0] [0,0,0]
     * [0,0,0] [5,5,5] [5,5,5] [5,5,5] [0,0,0]
     * [0,0,0] [5,5,5] [1,2,3] [5,5,5] [0,0,0]
     * [0,0,0] [5,5,5] [5,5,5] [5,5,5] [0,0,0]
     * [0,0,0] [0,0,0] [0,0,0] [0,0,0] [0,0,0]
     *
     * is pixelated to
     *
     * [1,2,3] [1,2,3] [1,2,3] [1,2,3] [1,2,3]
     * [1,2,3] [1,2,3] [1,2,3] [1,2,3] [1,2,3]
     * [1,2,3] [1,2,3] [1,2,3] [1,2,3] [1,2,3]
     * [1,2,3] [1,2,3] [1,2,3] [1,2,3] [1,2,3]
     * [1,2,3] [1,2,3] [1,2,3] [1,2,3] [1,2,3]
     *
     * @param image - the image to be converted to grayscale, do not modify!
     * @return a new image that displays in shades of gray
     */
    @Override
    public WritableImage pixelateImage(WritableImage image) {
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = image.getPixelWriter();

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        int blockSize = 5;
        int distanceToCenter = (int) Math.floor(blockSize / 2.0);

        //Iterates through images pixels in 'blockSize' area chunks
        for (int y = 0; y < height - (distanceToCenter - 1); y += blockSize) {
            for (int x = 0; x < width - (distanceToCenter - 1); x += blockSize) {
                Color medianColor;

                //Gets median pixel color
                if ((x + (blockSize - 1)) < width && (y + (blockSize - 1)) < height) { //Base Case: There is a 5 x 5 area
                    medianColor = pixelReader.getColor((x + distanceToCenter), (y + distanceToCenter));
                } else {
                    if ((x + (blockSize - 1)) >= width && (y + (blockSize - 1)) < height) { //Edge Case: There is a n x 5
                        medianColor = pixelReader.getColor((width - 1), (y + distanceToCenter));
                    } else if ((x + (blockSize - 1)) < width && (y + (blockSize - 1)) >= height) { //Edge Case: There is a 5 x n area
                        medianColor = pixelReader.getColor((x + distanceToCenter), (height - 1));
                    } else { //Edge Case: There is a n x n area
                        medianColor = pixelReader.getColor((width - 1), (height - 1));
                    }
                }

                //Sets a square area of dimensions 'blockSize' to the median color there is a blockSize area
                //Otherwise it just fills the available space
                for (int i = 0; (i < blockSize && (y + i) < height); i++) {
                    for (int j = 0; (j < blockSize && (x + j) < width); j++) {
                        pixelWriter.setColor((x + j), (y + i), medianColor);
                    }
                }
            }
        }
        return image;
    }

    /**
     * Flips the image vertically.
     *
     * For example:
     * [0,0,0] [0,0,0] [0,0,0] [0,0,0] [0,0,0]
     * [0,0,0] [5,5,5] [1,4,7] [5,5,5] [0,0,0]
     * [0,0,0] [1,2,3] [2,5,8] [1,2,3] [0,0,0]
     * [0,0,0] [4,4,4] [3,6,9] [4,4,4] [0,0,0]
     * [0,0,0] [0,0,0] [0,0,0] [0,0,0] [0,0,0]
     *
     * is flipped to
     *
     * [0,0,0] [0,0,0] [0,0,0] [0,0,0] [0,0,0]
     * [0,0,0] [4,4,4] [3,6,9] [4,4,4] [0,0,0]
     * [0,0,0] [1,2,3] [2,5,8] [1,2,3] [0,0,0]
     * [0,0,0] [5,5,5] [1,4,7] [5,5,5] [0,0,0]
     * [0,0,0] [0,0,0] [0,0,0] [0,0,0] [0,0,0]
     *
     * @param image - the image to be flipped, do not modify!
     * @return a new image that displays upside-down (but not rotated!)
     */
    @Override
    public WritableImage flipImage(WritableImage image) {
        PixelReader pixelReader = image.getPixelReader();

        int height = (int) image.getHeight();
        int width = (int) image.getWidth();

        WritableImage newImage = new WritableImage(width, height);
        PixelWriter newPixelWriter = newImage.getPixelWriter();

        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                newPixelWriter.setColor(x, (height - (1 + y)), pixelReader.getColor(x, y)); //Flips the y position of the image
            }
        }
        return newImage;
    }

    /**
     * A back-end helper method used in 'loadImage'. Checks if given file is a PPM file. Accounts for comments.
     * @param scan A scanner whose input is a text file. Scanner must have not read any lines up until this point.
     * @return returns true is file is PPM according to PPM file 'magic number' specifications
     */
    public boolean isPPM(Scanner scan) {
        //Checks if file type is PPM
        while (true) {
            String next = scan.next();
            switch (next) {
                case "#":
                    System.out.println(scan.nextLine());
                    continue;
                case "P3":
                    return true;
                default:
                    return false;
            }
        }
    }

    /**
     * Returns next integer in file if next section is integer. Accounts for comments.
     * @param scan - Scanner with PPM file as input
     * @param exceptionMessage - Error Message to be displayed in case of MissingFormatArgumentException
     * @return - Next integer in file
     * @throws MissingFormatArgumentException
     */
    private int getNextIntFromFile(Scanner scan, String exceptionMessage) {
        while (true) {
            if (scan.hasNextInt()){
                return scan.nextInt();
            } else {
                String next = scan.next();
                if (next.equals("#")){
                    System.out.println(scan.nextLine());
                    continue;
                } else {
                    throw new MissingFormatArgumentException(exceptionMessage);
                }
            }
        }
    }

}
