package Week_8_Program;

// IMPORTS
// These are some classes that may be useful for completing the project.
// You may have to add others.
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * The main class for Week8Program. Week8Program constructs the JavaFX window and
 * handles interactions with the dynamic components contained therein.
 */
public class Week8Program extends Application {
    // INSTANCE VARIABLES
    // These variables are included to get you started.
    private Stage stage = null;
    private BorderPane borderPane = null;
    private WebView view = null;
    private WebEngine webEngine = null;
    private TextField statusbar = null;

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
     * Generates the status bar layout and text field.
     *
     * @return statusbarPane - the HBox layout that contains the statusbar.
     */
    private HBox makeStatusBarPane( ) {
        HBox statusbarPane = new HBox();
        statusbarPane.setPadding(new Insets(5, 4, 5, 4));
        statusbarPane.setSpacing(10);
        statusbarPane.setStyle("-fx-background-color: #336699;");
        statusbar = new TextField();
        HBox.setHgrow(statusbar, Priority.ALWAYS);
        statusbarPane.getChildren().addAll(statusbar);
        return statusbarPane;
    }

    private HBox makeNavigationPane( ){
        HBox navigationBar = new HBox();
        return navigationBar;
    }

    private BorderPane makeBorderPane(Pane topPane, Pane bottomPane, Pane centerPane){
        borderPane = new BorderPane();
        borderPane.setTop(topPane);
        borderPane.setBottom(bottomPane);
        borderPane.setCenter(centerPane);

        return borderPane;
    }

    private Pane makeWebViewPane(WebView webView){
        Pane webViewPane = new Pane(webView);
        return webViewPane;
    }

    private void configureStage( Stage stage ){
        stage.setScene(new Scene(borderPane));
        stage.setTitle("Generic Web Browser");

        stage.setMinHeight(300);
        stage.setMinWidth(500);

        stage.setMaximized(true);
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
        makeHtmlView();


        HBox statusBarPane = makeStatusBarPane();
        HBox navigationPane = makeNavigationPane();
        Pane webViewPane = makeWebViewPane(view);

        makeBorderPane(navigationPane, statusBarPane, webViewPane);

        configureStage(stage);
        stage.show();
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
