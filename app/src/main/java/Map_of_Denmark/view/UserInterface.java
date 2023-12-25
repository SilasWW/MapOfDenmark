package Map_of_Denmark.view;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import Map_of_Denmark.model.InterestPoint;
import Map_of_Denmark.utilities.FindRoute;

import java.util.ArrayList;

/**
 * This class is the UserInterface class. It is responsible
 * for userinterface elements, that allow the user to interact
 * with the map.
 */
public class UserInterface {
    boolean menuVisible = false;
    boolean directionsVisible = false;
    VBox menuPanel;
    HBox searchBar2;
    long imagesize = 20;
    TextField searchField;
    TextField searchField2;
    VBox searchPanel;
    FindRoute findroute = new FindRoute();
    Boolean focusOnFirstSearchbar = true;

    /**
     * Section that defines all images, to be used for buttons
     */
    Image burgerPNG = new Image("file:data/images/Burger.png"); ImageView burgerImage = new ImageView(burgerPNG); // (Burger)Menu
    Image searchPNG = new Image("file:data/images/Search.png"); ImageView searchImage = new ImageView(searchPNG); // Magnifying glass
    Image directionPNG = new Image("file:data/images/Directions(grey).png"); ImageView directionsImage = new ImageView(directionPNG); // Directions sign?
    Image walkingPNG = new Image("file:data/images/Walking.png"); ImageView walkingImage = new ImageView(walkingPNG); // Walking man
    Image drivingPNG = new Image("file:data/images/Driving(edit).png"); ImageView drivingImage = new ImageView(drivingPNG); // Sedan, i think
    Image car2 = new Image("file:data/images/Car2.png"); ImageView Car2 = new ImageView(car2); // Sedan, i think
    Image bike = new Image("file:data/images/AlmostBiking.png"); ImageView biking = new ImageView(bike); // Walking man
    Image clipBoardImage = new Image("file:data/images/Clipboard.png"); ImageView clipboardIcon = new ImageView(clipBoardImage);

    View view;

    /**
     * This method is a constructor for the UserInterface class
     * It creates an object for the View-class
     * @param view object of view
     */
    public UserInterface(View view) {
        this.view = view;
    }

    /**
     * Holds the full UI. Returns a VBox, that holds and
     * positions the searchbar, that holds the further
     * UI elements
     * @return VBox returns object of VBox which is a container for the searchbar
     */
    public VBox createUI() {
        VBox root = new VBox();
        root.setMaxSize(0,0);

        VBox container = new VBox();
        HBox searchBar = createSearchBar(container);
        container.getChildren().add(searchBar);
        root.getChildren().add(container);

        root.setMargin(container, new Insets(10));
        return root;
    }

    /**
     * Creates and styles the full searchbar, including
     * buttons/icons for menu, directions and search
     * @param container VBox
     * @return HBox
     */
    public HBox createSearchBar(VBox container) {
        //Creates the menu Button
        Button menuButton = new Button();
        menuButton.setGraphic(burgerImage);
        burgerImage.setFitHeight(imagesize);
        burgerImage.setFitWidth(imagesize);
        menuButton.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 6 12 6 12; -fx-border-width: 0; -fx-border-color: transparent; -fx-border-radius: 8 0 0 0; -fx-font-size: 14; -fx-text-fill: black;");

        // Creates the search text field
        searchField = new TextField();
        searchField.setPromptText("Search...");
        HBox.setHgrow(searchField, Priority.ALWAYS); // Make the text field grow horizontally
        searchField.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 6 12 6 12; -fx-border-width: 0; -fx-border-color: transparent; -fx-border-radius: 8 0 0 8; -fx-font-size: 14; -fx-text-fill: black;");

        // Creates the search button
        Button searchButton = new Button();
        searchButton.setGraphic(searchImage);
        searchImage.setFitHeight(imagesize);
        searchImage.setFitWidth(imagesize);
        searchButton.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 6 12 6 12; -fx-border-width: 0; -fx-border-color: transparent; -fx-border-radius: 0; -fx-font-size: 14; -fx-text-fill: black;");

        /**
         * Calls the search function, that executes our
         * shortest-path algorithm. It does not allow for
         * empty searches, or special characters
         */
        searchButton.setOnAction(event -> {
            try {
                String one = searchField.getText();
                String two = searchField2.getText();

                // Check if either text field is empty
                if (one.isEmpty() || two.isEmpty()) {
                    throw new IllegalArgumentException("Fields cannot be empty");
                }

                // Check if either text field contains special characters
                if (!one.matches("[a-zA-Z0-9 ]*") || !two.matches("[a-zA-Z0-9 ]*")) {
                    throw new IllegalArgumentException("Fields cannot contain special characters");
                }

                view.model.search(one, two);
                view.redraw();

            } catch (IllegalArgumentException e) {
                // Create a new alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());

                alert.showAndWait();
            }
        });

        // Create the directions button
        Button directionsButton = new Button();
        directionsButton.setGraphic(directionsImage);
        directionsImage.setFitWidth(imagesize);
        directionsImage.setFitHeight(imagesize);
        directionsButton.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 6 12 6 12; -fx-border-width: 0; -fx-border-color: transparent; -fx-border-radius: 0 8 8 0; -fx-font-size: 14; -fx-text-fill: black;");

        // Create the search bar container (HBox)
        HBox searchBar = new HBox();
        searchBar.setSpacing(0);
        searchBar.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-border-radius: 8; -fx-background-radius: 8");
        searchBar.setMaxWidth(350); // Set maximum width for searchBar
        searchBar.setMinWidth(350); // Set minimum width for searchBar

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(3.0);
        dropShadow.setOffsetX(0.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.color(0, 0, 0, 0.16));
        searchBar.setEffect(dropShadow);

        searchBar.getChildren().addAll(menuButton, searchField, directionsButton, searchButton);

        /**
         * Displays and removes the menu-panel, upon click of
         * the menu button. It does so by adding and removing
         * the menu-panel object, from the searchbar container
         */
        menuButton.setOnAction(event -> {
            menuVisible = !menuVisible;
            directionsVisible = false;

            if(menuVisible){
                searchBar.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-border-radius: 8; -fx-background-radius: 8 8 0 0");
                searchBar.setEffect(null);
                if(container.getChildren().size() == 2){
                    container.getChildren().remove(container.getChildren().size() - 1);
                    menuPanel = createMenuPanel();
                    container.getChildren().add(menuPanel);
                } else {
                    menuPanel = createMenuPanel();
                    container.getChildren().add(menuPanel);
                }
            } else {
                container.getChildren().remove(container.getChildren().size() - 1);
                searchBar.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-border-radius: 8; -fx-background-radius: 8");
                searchBar.setEffect(dropShadow);
            }

            System.out.println(container.getChildren());
            System.out.println("menuVisible: " + menuVisible);
            System.out.println("directionVisible: " + directionsVisible);

        });

        /**
         * Displays and removes a search panel, holding a second
         * Textfield, to allow for route-searches. It also holds
         * buttons/icons for search-suggestions, mode of
         * transportation and copy to clipboard
         */
        directionsButton.setOnAction(event -> {
            directionsVisible = !directionsVisible;
            menuVisible = false;

            if(directionsVisible){
                searchField.setPromptText("From...");
                searchBar.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-background-radius: 8 8 0 0; -fx-border-width: 0 0 0.5 0; -fx-border-color: LIGHTGREY");
                searchBar.setEffect(null);
                if(container.getChildren().size() == 2){
                    container.getChildren().remove(container.getChildren().size() - 1);
                    searchPanel = createSearchPanel();
                    container.getChildren().add(searchPanel);
                } else {
                    searchPanel = createSearchPanel();
                    container.getChildren().add(searchPanel);
                }
            } else {
                searchField.setPromptText("Search...");
                container.getChildren().remove(container.getChildren().size() - 1);
                searchBar.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-border-radius: 8; -fx-background-radius: 8");
                searchBar.setEffect(dropShadow);
            }
        });
        return searchBar;
    }

    /**
     * Creates the menu-panel, that is held by the searchbar
     * and activated when the menu-button is clicked. The panel
     * holds buttons for entering colorblind-mode, and it holds
     * an overview of the points of interest.
     * @return VBox
     */
    public VBox createMenuPanel() {
        VBox menuPanel = new VBox();
        menuPanel.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-border-radius: 0 0 0 0; -fx-background-radius: 0 0 8 8");

        menuPanel.setMaxWidth(350); // Set maximum width for menuPanel
        menuPanel.setMinWidth(350); // Set minimum width for menuPanel

        Button greenButton = new Button();
        greenButton.setStyle("-fx-background-color: GREEN; -fx-border-width: 0; -fx-border-color: transparent; -fx-background-radius: 8");
        HBox.setHgrow(greenButton, Priority.ALWAYS); // Make the yellow button grow horizontally
        greenButton.setMinSize(70, 50);

        Button yellowButton = new Button();
        yellowButton.setStyle("-fx-background-color: YELLOW; -fx-border-width: 0; -fx-border-color: transparent; -fx-background-radius: 8");
        HBox.setHgrow(yellowButton, Priority.ALWAYS); // Make the yellow button grow horizontally
        yellowButton.setMinSize(70,50);

        HBox buttonContainer = new HBox();
        buttonContainer.getChildren().addAll(greenButton, yellowButton);
        buttonContainer.setSpacing(70);
        menuPanel.setMargin(buttonContainer, new Insets(0, 70, 0, 60));

        menuPanel.getChildren().addAll(buttonContainer);
        
        Separator separator = new Separator();
        separator.setMaxWidth(menuPanel.getMaxWidth() - 10);
        separator.setHalignment(HPos.CENTER);
        separator.setStyle("-fx-padding: 20 0 20 0");
        menuPanel.getChildren().add(separator);
        
        Text pointTitle = new Text("Points of interest");
        pointTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        pointTitle.setTranslateY(-10);
        menuPanel.getChildren().add(pointTitle);

        for(int i = 0; i < view.model.getInterestPointList().size() / 6 + 1; i++) {

            HBox pointContainer = new HBox();
            for(int j = 0 + 6 * i; j < 6 + 6 * i; j++) {
                if(j >= view.model.getInterestPointList().size()) break;
                InterestPoint loopPoint = view.model.getInterestPointList().get(j);
                Group poiGroup = new Group();
                Button poiButton = new Button();
                Text poiTitle = new Text();

                poiButton.setStyle("-fx-background-color: #" + loopPoint.getColor().toString().substring(2) + "; -fx-border-width: 3; -fx-border-color: black; -fx-border-radius: 20; -fx-background-radius: 20;");
                poiButton.setMinSize(30,30);
                poiGroup.getChildren().add(poiButton);

                poiTitle.setText("[ " + loopPoint.getName() + " ]");
                poiTitle.setFont(Font.font("Arial", 12));
                poiTitle.setTranslateX(-poiTitle.getLayoutBounds().getWidth() / 4);
                poiTitle.setTranslateY(45);
                poiGroup.getChildren().add(poiTitle);

                pointContainer.getChildren().add(poiGroup);

                poiButton.setOnAction(event -> {
                    view.gotoCoords(loopPoint.getLat(), loopPoint.getLon());
                });
            }
            menuPanel.getChildren().add(pointContainer);
        }

        /**
         * Exits colorblind-mode, if in colorblind-mode
         */
        greenButton.setOnAction(event -> {
            view.colorBlind = false;
            view.redraw();
        });

        /**
         * Enters colorblind-mode, if not in colorblind-mode
         */
        yellowButton.setOnAction(event -> {
            view.colorBlind = true;
            view.redraw();
        });
        return menuPanel;
    }

    /**
     * Creates a second searchbar. It is made up of a new
     * Textfield, but not new buttons. The second searchbar
     * is held by the searchPanel
     * @return HBox
     */
    public HBox createSearchBar2(){
        HBox searchbar2 = new HBox();

        // Create the search text field
        searchField2 = new TextField();
        searchField2.setPromptText("To...");
        searchbar2.setMargin(searchField2, new Insets(0, 0, 0, 43));
        HBox.setHgrow(searchField2, Priority.ALWAYS); // Make the text field grow horizontally
        searchField2.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 6 12 6 12; -fx-border-width: 0; -fx-border-color: transparent; -fx-border-radius: 8 0 0 8; -fx-font-size: 14; -fx-text-fill: black;");

        searchbar2.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-border-width: 0 0 0.5 0; -fx-border-color: LIGHTGREY");

        searchbar2.setMaxWidth(350); // Set maximum width for searchBar
        searchbar2.setMinWidth(350); // Set minimum width for searchBar

        searchbar2.getChildren().add(searchField2);

        return searchbar2;
    }

    /**
     * Creates the searchPanel, that is shown at the click of
     * the directions-button.
     * @return VBox
     */
    public VBox createSearchPanel(){
        VBox searchPanel = new VBox();

        searchPanel.setStyle("-fx-padding: 0; -fx-border-radius: 8; -fx-background-radius: 0 0 8 8");
        searchPanel.setSpacing(0);

        searchBar2 = createSearchBar2();
        searchPanel.getChildren().add(searchBar2);

        HBox modeOfTransportation = new HBox();
        modeOfTransportation.setStyle("-fx-background-color: #FFFFFF; -fx-border-radius: 0 0 8 8; -fx-background-radius: 0 0 8 8; -fx-border-width: 0.5 0 0 0; -fx-border-color: LIGHTGREY");

        Button walking = new Button();
        walking.setStyle("-fx-border-color: transparent; -fx-background-color: transparent");
        ImageView notCar = biking;
        walking.setGraphic(notCar);
        notCar.setFitHeight(33);
        notCar.setFitWidth(33);

        Button driving = new Button();
        driving.setStyle("-fx-border-color: transparent; -fx-background-color: transparent");
        ImageView car = drivingImage;
        driving.setGraphic(car);
        car.setFitHeight(33);
        car.setFitWidth(33);

        /**
         * Makes the route-search avoid highways of type 1, 2, 3
         */
        walking.setOnAction(event -> {
            view.model.setCarRoute(false);
            driving.setStyle("-fx-border-color: transparent; -fx-background-color: transparent");
            walking.setStyle("-fx-border-color: transparent; -fx-background-color: LIGHTGREY");
        });

        /**
         * Allows the route-search to use highways of type 2, 3, 4
         */
        driving.setOnAction(event -> {
            view.model.setCarRoute(true);
            driving.setStyle("-fx-border-color: transparent; -fx-background-color: LIGHTGREY");
            walking.setStyle("-fx-border-color: transparent; -fx-background-color: transparent");
        });

        Button clipBoard = new Button();
        clipBoard.setStyle("-fx-background-color: transparent; -fx-border-color: transparent");
        clipBoard.setGraphic(clipboardIcon);
        clipboardIcon.setFitHeight(33);
        clipboardIcon.setFitWidth(33);
        modeOfTransportation.setMargin(clipBoard, new Insets(0, 0, 0, (180)));
        modeOfTransportation.getChildren().addAll(walking, driving, clipBoard);

        /**
         * Copies the route-directions to the clipboard, and
         * alerts the user
         */
        clipBoard.setOnAction(event -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();content.putString(findroute.getDirectionsText(view.model.RouteList.get(0).getList()));
            clipboard.setContent(content);

            //Notifies user that clipboard has been changed
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Text has been copied to the clipboard!");

            alert.showAndWait();
        });

        //Panel for holding 4 buttons, each being a search
        //suggestion
        VBox searchSuggestions = new VBox();
        searchSuggestions.setMinHeight(100);
        searchSuggestions.setStyle("-fx-background-color: White");
        searchPanel.getChildren().add(searchSuggestions);
        searchPanel.getChildren().add(modeOfTransportation);

        //Initializes buttons
        Button suggest1 = new Button("Option1");
        Button suggest2 = new Button("Option2");
        Button suggest3 = new Button("Option3");
        Button suggest4 = new Button("Option4");

        searchSuggestions.setSpacing(0);
        searchSuggestions.setMargin(suggest1, new Insets(0));
        searchSuggestions.setMargin(suggest2, new Insets(0));
        searchSuggestions.setMargin(suggest3, new Insets(0));
        searchSuggestions.setMargin(suggest4, new Insets(0));

        double suggestPanelHeight = searchSuggestions.getHeight();
        double suggestPanelWidth = 350;

        String suggestStyle = "-fx-background-color: transparent; -fx-border-color: LIGHTGREY; -fx-border-width: 0 0 0.5 0";
        String suggestStyleHover = "-fx-background-color: #7979ee; -fx-border-color: LIGHTGREY; -fx-border-width: 0 0 0.5 0";

        suggest1.setStyle(suggestStyle);
        suggest1.setMinHeight(suggestPanelHeight / 4);
        suggest1.setMinWidth(suggestPanelWidth);
        suggest1.setAlignment(Pos.BASELINE_LEFT);

        suggest2.setStyle(suggestStyle);
        suggest2.setMinHeight(suggestPanelHeight / 4);
        suggest2.setMinWidth(suggestPanelWidth);
        suggest2.setAlignment(Pos.BASELINE_LEFT);

        suggest3.setStyle(suggestStyle);
        suggest3.setMinHeight(suggestPanelHeight / 4);
        suggest3.setMinWidth(suggestPanelWidth);
        suggest3.setAlignment(Pos.BASELINE_LEFT);

        suggest4.setStyle(suggestStyle);
        suggest4.setMinHeight(suggestPanelHeight / 4);
        suggest4.setMinWidth(suggestPanelWidth);
        suggest4.setAlignment(Pos.BASELINE_LEFT);

        /**
         * Creates hover-effects for the search-suggestions
         */
        suggest1.setOnMouseEntered(event -> {
            suggest1.setStyle(suggestStyleHover);
        });

        suggest1.setOnMouseExited(event -> {
            suggest1.setStyle(suggestStyle);
        });

        suggest2.setOnMouseEntered(event -> {
            suggest2.setStyle(suggestStyleHover);
        });

        suggest2.setOnMouseExited(event -> {
            suggest2.setStyle(suggestStyle);
        });

        suggest3.setOnMouseEntered(event -> {
            suggest3.setStyle(suggestStyleHover);
        });

        suggest3.setOnMouseExited(event -> {
            suggest3.setStyle(suggestStyle);
        });

        suggest4.setOnMouseEntered(event -> {
            suggest4.setStyle(suggestStyleHover);
        });

        suggest4.setOnMouseExited(event -> {
            suggest4.setStyle(suggestStyle);
        });

        //focusOnFirst is used to keep track of which searchbar
        //to send the text from the search-suggestion to
        searchField.setOnMouseClicked(event -> {
            focusOnFirstSearchbar = true;
        });

        searchField2.setOnMouseClicked(event -> {
            focusOnFirstSearchbar = false;
        });

        //Sends text from the search-suggestions to the
        //searchbar upon a click on the search-suggestion
        suggest1.setOnAction(event -> {
            sendToSearchbar(suggest1);
        });

        suggest2.setOnAction(event -> {
            sendToSearchbar(suggest2);
        });

        suggest3.setOnAction(event -> {
            sendToSearchbar(suggest3);
        });

        suggest4.setOnAction(event -> {
            sendToSearchbar(suggest4);
        });

        //Creates a list, that holds the search-suggestions
        ArrayList<Button> suggestionsList = new ArrayList<>();
        suggestionsList.add(suggest1);
        suggestionsList.add(suggest2);
        suggestionsList.add(suggest3);
        suggestionsList.add(suggest4);

        //When a key is typed into the first searchbar, the
        //search suggestions is filled with the first 4 items
        //of the address list, when the text in the searchbar,
        //is sent to a Ternary Search Tree
        searchField.setOnKeyTyped(event -> {
            Iterable<String> match = view.model.preSearch(searchField.getText());
            int counter = 0;
            for(String element : match){
                suggestionsList.get(counter).setText(element);
                counter = counter + 1;
                if(counter == 4){
                    break;
                }
            }
        });

        //When a key is typed into the second searchbar, the
        //search suggestions is filled with the first 4 items
        //of the address list, when the text in the searchbar,
        //is sent to a Ternary Search Tree
        searchField2.setOnKeyTyped(event -> {
            Iterable<String> match = view.model.preSearch(searchField2.getText());
            int counter = 0;
            for(String element : match){
                suggestionsList.get(counter).setText(element);
                counter = counter + 1;
                if(counter == 4){
                    break;
                }
            }
        });

        searchSuggestions.getChildren().addAll(suggest1, suggest2, suggest3, suggest4);
        return searchPanel;
    }

    /**
     * Makes sure the search-suggestion is sent to the correct
     * searchbar when clicked
     * @param exampleButton
     */
    public void sendToSearchbar(Button exampleButton){
        if(focusOnFirstSearchbar){
            searchField.setText(exampleButton.getText());
        }
        else{
            searchField2.setText(exampleButton.getText());
        }
    }
}