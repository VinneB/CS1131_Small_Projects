package Week_8_Program;

// IMPORTS
// These are some classes that may be useful for completing the project.
// You may have to add others.
import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * The main class for Week8Program. Week8Program constructs the JavaFX window and
 * handles interactions with the dynamic components contained therein.
 *
 * @author Vincent Barfield
 */
public class Week8Program extends Application {
    // INSTANCE VARIABLES
    // These variables are included to get you started.
    private BorderPane borderPane = null;
    private WebView view = null;
    private WebEngine webEngine = null;
    private TextField statusBar = null;

    private boolean isHelpMessageOpen = false;

    //This is the helpMessage which is displayed if you hit the help button or run the program without arguments
    //I wrote it html because: 1. Chrome does something similar 2. It's easier to do this then to make a new pane
    private String helpMessageHTML = "<html><head><title>Generic Browser</title></head><body><h1>Generic Web Browser</h1>" +
            "<br /><p>This is a simple web browser where" +
            " you can visit webpages, view this help message, and navigate through history.</p><hr /><p>Use the back and" +
            " forward buttons to navigate through history</p><br /><p>Use the text field in the navigation bar to visit " +
            "webpages</p><br /><p>Use the text field in the navigation bar to visit webpages</p><hr /> <p>Creator: " +
            "Vincent Barfield-----Class: CS1131-----Lab Section: L02 </p</body></html>";

    // HELPER METHODS

    /**
     * Retrieves the value of a command line argument specified by the index.
     *
     * @param index - position of the argument in the args list.
     * @return The value of the command line argument.
     */
    private String getParameter( int index ) {
        Parameters params = getParameters();
        List<String> parameters = params.getRaw();
        return !parameters.isEmpty() ? parameters.get(index) : "";
    }

    /**
     * Creates the scene for Main stage using borderPane and sets up stage.title to be updated with loaded webpage title
     * @param stage - Main Stage for Application
     */
    private void configureStage( Stage stage){
        stage.setScene(new Scene(borderPane));

        //Bind Title to loaded webpage title
        stage.titleProperty().bind(webEngine.titleProperty());
    }

    //MAKER METHODS

    /**
     * Creates a WebView which handles mouse and some keyboard events, and
     * manages scrolling automatically, so there's no need to put it into a ScrollPane.
     * The associated WebEngine is created automatically at construction time.
     *
     * @return browser - a WebView container for the WebEngine.
     */
    private WebView makeHtmlView( ) {
        view = new WebView();
        webEngine = view.getEngine();
        return view;
    }

    /**
     * Creates the pane for which webView will display webPages
     * @param webView WebView node which is used in conjunction with WebEngine
     * @return webViewPane
     */
    private Pane makeWebViewPane(WebView webView){
        return new Pane(webView);
    }

    /**
     * Generates the status bar layout and text field.
     *
     * @return statusbarPane - the HBox layout that contains the statusbar.
     */
    private HBox makeStatusBarPane( ) {
        HBox statusbarPane = new HBox();
        statusbarPane.setPadding(new Insets(5, 4, 5, 4));
        statusbarPane.setSpacing(10);
        statusbarPane.setStyle("-fx-background-color: #2c17a0;");
        statusBar = new TextField();
        HBox.setHgrow(statusBar, Priority.ALWAYS);
        statusbarPane.getChildren().addAll(statusBar);
        return statusbarPane;
    }

    /**
     * Creates Navigation Pane which is where most user to application interaction will (obviously excepting loaded pages).
     * @param backHistoryButton Button object which navigates back in webHistory (ActionEvent handled in start method)
     * @param nextHistoryButton Button object which navigates back in webHistory (ActionEvent handled in start method)
     * @param helpButton Button object which displays information about this application
     * @param navigationField Text field where users enter URLs
     * @return navigationPane
     */
    private HBox makeNavigationPane( Button backHistoryButton, Button nextHistoryButton, Button helpButton, TextField
            navigationField ){
        HBox navigationPane = new HBox();
        navigationPane.setPadding(new Insets(10, 10, 10, 10));
        navigationPane.setSpacing(20);
        navigationPane.setStyle("-fx-background-color: #2c17a0; -fx-opacity: 0.8");

        //Navigation Field
        navigationField.setMinWidth(550);

        navigationPane.getChildren().addAll(backHistoryButton, nextHistoryButton, navigationField, helpButton);

        return navigationPane;
    }

    /**
     * Creates the main pane which will be displayed in stage
     * @param topPane Object to be displayed at top of page (Navigation Pane)
     * @param bottomPane Object to be displayed at bottom of page (Status Bar)
     * @param centerPane Object to displayed at center of page (Web View)
     * @return borderPane
     */
    private BorderPane makeBorderPane(Pane topPane, Pane bottomPane, Pane centerPane){
        borderPane = new BorderPane();
        borderPane.setTop(topPane);
        borderPane.setBottom(bottomPane);
        borderPane.setCenter(centerPane);

        return borderPane;
    }

    //HANDLER METHODS

    /**
     * Navigates the user back in webHistory. Intended to be used in the handling of backHistoryButton. If isHelpMessageOpen
     * is true, loads the most recent url in webHistory. When this happens, sets isHelpMessageOpen to false.
     */
    private void handleBackHistory(){
        WebHistory webHistory = webEngine.getHistory();
        if (isHelpMessageOpen){
            //This line is a bit convoluted but it all it does is load the last last webpage in webHistory
            webEngine.load(webHistory.getEntries().get(webHistory.getCurrentIndex()).getUrl());
            isHelpMessageOpen = false;
        } else if (webHistory.getCurrentIndex() != 0) {
            isHelpMessageOpen = false;
            webHistory.go(-1);
        } else{
            System.out.println("Error: User has no history further back");
        }
    }

    /**
     * Navigates the user forward in webHistory. Intended to be used in the handling of backHistoryButton. Sets isHelpMessageOpen
     * to false.
     */
    private void handleNextHistory(){
        WebHistory webHistory = webEngine.getHistory();
        if (webHistory.getCurrentIndex() != (webHistory.getEntries().size() - 1) && webHistory.getEntries().size() != 0) {
            isHelpMessageOpen = false;
            webHistory.go(1);
        } else {
            System.out.println("Error: User has no history further forward");
        }
    }

    /**
     * Navigates user to URL specified in arguments. Since WebEngine.load() only loads webpages if 'https://' or 'http://'
     * is the beginning of the URL, this method will only handle these types of strings. Otherwise, returns error. Sets
     * isHelpMessageOpen to false
     * @param URL URL to be loaded
     * @return true if URL meets guidelines. Otherwise, false.
     */
    private boolean handleNavigateTo( String URL ) {
        boolean isRealURL;
        if (URL.startsWith("https://") || URL.startsWith("http://")) {
            isRealURL = true;
            isHelpMessageOpen = false;
            webEngine.load(URL);
        } else{
            isRealURL = false;
            System.out.println("Invalid URL");
        }
        return isRealURL;
    }

    /**
     * Loads webPage with displaying helpMessageHTML. Sets isHelpMessageOpen to true.
     * NOTE: I was having a bug where, if loadHelpPage was open, then navigating backwards in history would move 1 index
     * to far backwards. This is because webEngine.loadContent doesn't add to webHistory. To fix this, I used a work around
     * of loading newest url in webHistory if the Help Message Page was open.
     */
    private void loadHelpPage(){
        isHelpMessageOpen = true;
        webEngine.loadContent(helpMessageHTML);
    }

    // REQUIRED METHODS
    /**
     * The main entry point for all JavaFX applications. The start method is
     * called after the init method has returned, and after the system is ready
     * for the application to begin running.
     *
     * NOTE: This method is called on the JavaFX Application Thread.
     *
     * @param stage - the primary stage for this application, onto which
     * the application scene can be set.
     */
    @Override
    public void start(Stage stage) {
        //Setup of browser pane, nodes, and webview
        makeHtmlView();

        Button backHistoryButton = new Button("Back");
        Button nextHistoryButton = new Button("Next");
        Button helpButton = new Button("Help");
        TextField navigationField = new TextField();

        HBox navigationPane = makeNavigationPane(backHistoryButton, nextHistoryButton, helpButton, navigationField);
        HBox statusBarPane = makeStatusBarPane();
        Pane webViewPane = makeWebViewPane(view);

        borderPane = makeBorderPane(navigationPane, statusBarPane, webViewPane);

        //Set Button Handlers
        backHistoryButton.setOnAction(e ->handleBackHistory() );
        nextHistoryButton.setOnAction(e -> handleNextHistory() );
        helpButton.setOnAction(e -> loadHelpPage() );

        //Set Navigation Field Handler
        navigationField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event){
                if (event.getCode() == KeyCode.ENTER){
                    handleNavigateTo(navigationField.getText());
                }
            }
        });

        //Essentially sets statusbar.text to the page's status (defined in the html/javascript) whenever there is a status change
        webEngine.setOnStatusChanged(e -> statusBar.setText(e.getData()) );

        //Setup Stage
        configureStage(stage);
        stage.show();

        //Displays the Help message as startup page or loads url parameter
        if (!getParameter(0).isEmpty()){
            boolean isRealURL = handleNavigateTo(getParameter(0));
            if (!isRealURL){
                loadHelpPage();
            }
        } else {
            loadHelpPage();
        }
    }

    /**
     * The main( ) method is ignored in JavaFX applications.
     * main( ) serves only as fallback in case the application is launched
     * as a regular Java application, e.g., in IDEs with limited FX
     * support.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
