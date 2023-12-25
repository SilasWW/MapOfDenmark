package Map_of_Denmark.view;

import java.io.IOException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.stage.Stage;
import Map_of_Denmark.model.Highway;
import Map_of_Denmark.model.Model;
import Map_of_Denmark.model.Way;
import Map_of_Denmark.model.Route;
import Map_of_Denmark.model.*;

/**
 * This class is the View of the MVC pattern. It is responsible for drawing the
 * canvas and handling the view logic.
 */
public class View {

    public Canvas canvas = new Canvas(640, 480);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    double x1 = 100;
    double y1 = 100;
    double x2 = 200;
    double y2 = 800;
    double diagonalDistance;
    public static boolean colorBlind = false;
    public static boolean colorHighway = false;

    Affine trans = new Affine();
    Model model;

    private Stage primaryStage;
    private double[] borders;
    
    private double minZoom;
    private double maxZoom;
    private double zoomFactor;
    
    private UserInterface buttons;
    
    StackPane stackPane;

    /**
     * This method is a constructor for the View class.
     * @param model The model of the MVC pattern.
     * @param primaryStage The stage of the application.
     */
    public View(Model model, Stage primaryStage) {
        this.model = model;
        this.primaryStage = primaryStage;

        primaryStage.setTitle("MapScope");
        BorderPane borderPane = new BorderPane(canvas);
//      borderPane.setTop(createMenuBar());
        stackPane = new StackPane();
        stackPane.getChildren().add(borderPane);
        Scene scene = new Scene(stackPane);
        primaryStage.setScene(scene);
        primaryStage.show();
        borderPane.getChildren().add(new DebugView(primaryStage).getGroup());
        
        DebugView.getInstance().toggle();

        // Handling and refreshing the borders.
        borders = new double[4];
        refreshBorders();

        //Adds search button, with some setup
        buttons = new UserInterface(this);
        //VBox buttonsHolder = buttons.createButtonsHolder(stackPane);

        VBox uiElements = buttons.createUI();
        stackPane.getChildren().add(uiElements);
        stackPane.setAlignment(uiElements, Pos.TOP_LEFT);
        stackPane.setMargin(uiElements, new Insets(10));

        redraw();
        pan(-0.56*model.getMinlon(), model.getMaxlat());
        zoom(0, 0, canvas.getHeight() / (model.getMaxlat() - model.getMinlat()));

        // Listener for the on key when released
        scene.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.F1) DebugView.getInstance().toggle();
        });

        // Listener for the resizing of the window
        primaryStage.widthProperty().addListener((observable, startWidth, endWidth) -> {
            canvas.setWidth((double) endWidth);
            redraw();
            DebugView.getInstance().resize();
        });

        // Listener for the resizing of the window
        primaryStage.heightProperty().addListener((observable, startHeight, endHeight) -> {
            canvas.setHeight((double) endHeight);
            redraw();
            DebugView.getInstance().resize();
        });

        // Setting up the zoombar on the StackPane
        setupZoomBar(stackPane);

        // Max and minimum for the zoom function
        maxZoom = 900000;
        minZoom = trans.getMxx() * 0.1;
    }

    /**
     * This method redraws the canvas.
     */
    void redraw() {

        // Benchmark
        long startTime = System.nanoTime();

        // Setup for the canvas
        gc.setTransform(new Affine());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
        gc.setLineWidth(1/Math.sqrt(trans.determinant()));

        // Initial zoomLevel and layers defined
        int zoomLevel = 0;

        if(diagonalDistance <= 0.04){
            zoomLevel = 1;
            colorHighway = false;
        } else if(diagonalDistance <= 0.07 && diagonalDistance >= 0.04){
            zoomLevel = 2;
            colorHighway = false;
        } else {
            zoomLevel = 3;
        }
        if(zoomLevel == 3){
            colorHighway = true;
        }

        switch (zoomLevel) {
            case 1 : draw1(); break; /* draws everything */
            case 2 : draw2(); break; /* draws coastlines for now should add primary highways, motorways and different shades of nature areas*/
            case 3 : draw3(); break; /* should draw motorways and coastlines, cityareas as one color and natureareas as one color */
        }

        // Handling the points of interest
        for(InterestPoint loopInterestPoint : model.getInterestPointList()) {
        	loopInterestPoint.draw(gc);
        }
        
        DebugView.getInstance().updateRefreshRate(System.nanoTime() - startTime);
        DebugView.getInstance().updateMemory();
        
        refreshBorders();
    }

    /**
     * The first draw method. This method draws everything.
     */
    private void draw1() {

        for(var water : model.getWaterTreeRoot().findLeaf(borders[0], borders[1], borders[2], borders[3])) {
            water.draw(gc);
        }
        for(var grass : model.getGrassTreeRoot().findLeaf(borders[0], borders[1], borders[2], borders[3])) {
            grass.draw(gc);
        }
        for(var parking : model.getParkingTreeRoot().findLeaf(borders[0], borders[1], borders[2], borders[3])) {
            parking.draw(gc);
        }
        for(var garden : model.getGardenTreeRoot().findLeaf(borders[0], borders[1], borders[2], borders[3])){
            garden.draw(gc);
        }
//        for (var line : model.lines) {
//            line.draw(gc);
//        }
        for (var way : model.wayTreeRoot.findLeaf(borders[0], borders[1], borders[2], borders[3])) {
            way.draw(gc);
        }
        for(var building : model.getBuildingTreeRoot().findLeaf(borders[0], borders[1], borders[2], borders[3])){
            building.draw(gc);
        }
//        for(var tree : model.treeList){
//            tree.draw(gc);
//        }
        for(var park : model.parkTreeRoot.findLeaf(borders[0], borders[1], borders[2], borders[3])){
            park.draw(gc);
        }
        for(var forest : model.getForestTreeRoot().findLeaf(borders[0], borders[1], borders[2], borders[3])){
            forest.draw(gc);
        }
        for (Highway highway : model.getHighwayCat1List()) {
            highway.draw(gc);
        }
        for (Highway highway : model.getHighwayCat2List()) {
            highway.draw(gc);
        }
        for (Highway highway : model.getHighwayCat3List()) {
            highway.draw(gc);
        }

        // Handling the layers of the highways in 4 different methods.
        highWay1();
        highWay2();
        highWay3();
        highWay4();

        // Draws the route on the map
        for (Route route : model.getRouteList()) {
            route.draw(gc);
            System.out.println("dummy");
            System.out.println(route);
        }
    }

    /**
     * The second draw method. This method draws the coastlines and the ways.
     */
    private void draw2() {

        for (Way coastline : model.coastlineTreeRoot.findLeaf(borders[0], borders[1], borders[2], borders[3])) {
            coastline.draw(gc);
        }

        for(Way landuse : model.developedAreasTreeRoot.findLeaf(borders[0], borders[1], borders[2], borders[3])){
            landuse.draw(gc);
        }
        for(Way landuse : model.natureAreasTreeRoot.findLeaf(borders[0], borders[1], borders[2], borders[3])){
            landuse.draw(gc);
        }
        for (var way : model.getWayTreeRoot().findLeaf(borders[0], borders[1], borders[2], borders[3])) {
            way.draw(gc);
        }
        for (var way : model.wayTreeRoot.findLeaf(borders[0], borders[1], borders[2], borders[3])) {
            way.draw(gc);
        }

        // Draws the highways without level 4
        highWay1();
        highWay2();
        highWay3();

        // Draw the route
        for (Route route : model.getRouteList()) {
            route.draw(gc);
        }
    }

    /**
     * The third draw method. This method draws the motorways, coastlines and landuse.
     */
    private void draw3(){
        for(Way landuse : model.developedAreasTreeRoot.findLeaf(borders[0], borders[1], borders[2], borders[3])){
            landuse.draw(gc);
        }
        for(Way landuse : model.natureAreasTreeRoot.findLeaf(borders[0], borders[1], borders[2], borders[3])){
            landuse.draw(gc);
        }
        for (MapRelation mapRelation : model.getBoundingRelationList()) {
            mapRelation.draw(gc);
        }
        for (MapRelation mapRelation : model.getGeoStructureRelationList()) {
            mapRelation.draw(gc);
        }

        // Draws the highways without level 2
        highWay1();
        highWay2();

        // Draw the route
        for (Route route : model.getRouteList()) {
            route.draw(gc);
        }
    }

    /**
     * Highways with level 1
     */
    public void highWay1(){
        for (var way : model.getHighwayCat1TreeRoot().findLeaf(borders[0], borders[1], borders[2], borders[3])) {
            way.draw(gc);
        }
    }

    /**
     * Highways with level 2
     */
    public void highWay2(){
        for (var way : model.getHighwayCat2TreeRoot().findLeaf(borders[0], borders[1], borders[2], borders[3])) {
            way.draw(gc);
        }
    }

    /**
     * Highways with level 3
     */
    public void highWay3(){
        for (var way : model.getHighwayCat3TreeRoot().findLeaf(borders[0], borders[1], borders[2], borders[3])) {
            way.draw(gc);
        }
    }

    /**
     * Highways with level 4
     */
    public void highWay4(){
        for (var way : model.getHighwayCat4TreeRoot().findLeaf(borders[0], borders[1], borders[2], borders[3])) {
            way.draw(gc);
        }
    }

    /**
     * This method is used to handle the panning of the map
     * @param dx The x coordinate as a double
     * @param dy The y coordinate as a double
     */
    public void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        redraw();
    }

    //Crates zoomBar
    Slider zoomBar;

    /**
     * This method is used to create the zoom functionality
     * @param dx The x coordinate as a double
     * @param dy The y coordinate as a double
     * @param factor The factor as a double
     */
    public void zoom(double dx, double dy, double factor) {

    	if(trans.getMxx() * factor < minZoom) {
    		factor = minZoom / trans.getMxx() + 0.0001;
    	}
    	if(trans.getMxx() * factor > maxZoom) {
    		factor = maxZoom / trans.getMxx() - 0.0001;
    	}

        // Setting up the coordinates for the factor calculation
        pan(-dx, -dy);
        trans.prependScale(factor, factor);
        pan(dx, dy);

        Point2D topLeftCorner = mousetoModel(0, 0);
        Point2D bottomRightCorner = mousetoModel(canvas.getWidth(), canvas.getHeight());
        diagonalDistance = topLeftCorner.distance(bottomRightCorner);
        
        redraw();
        
        zoomFactor = (trans.getMxx())  / (minZoom / 0.1);
        DebugView.getInstance().updateZoom(zoomFactor, diagonalDistance);
        updateZoomSlider();
    }

    /**
     * This method is used to refresh the borders of the map
     */
    private void refreshBorders() {
    	borders[0] = mousetoModel(0, 0).getX();
        borders[1] = mousetoModel(0, 0).getY();
        borders[2] = mousetoModel(primaryStage.widthProperty().doubleValue(), 0).getX();
        borders[3] = mousetoModel(0, primaryStage.heightProperty().doubleValue()).getY();
    }

    /**
     * This method is used to update the zoom slider
     * @param stackPane The stackPane as a StackPane to add the slider to
     */
    private void setupZoomBar(StackPane stackPane) {

        zoomBar = new Slider(0.1, maxZoom / (minZoom / 0.1), 0.1);
        stackPane.getChildren().add(zoomBar);
        stackPane.setAlignment(zoomBar, Pos.BOTTOM_RIGHT);
        stackPane.setMargin(zoomBar, new Insets(0, 10, 10, 0));
        zoomBar.setOrientation(Orientation.VERTICAL);
        zoomBar.setMaxSize(0, 150);
        zoomBar.setShowTickLabels(false);
        zoomBar.setShowTickMarks(false);
        zoomBar.setMajorTickUnit(1);
        zoomBar.setBlockIncrement(0.01);

        // Listener for the mouse dragged.
        zoomBar.setOnMouseDragged(e -> {
        	zoom(primaryStage.getWidth() / 2, primaryStage.getHeight() / 2, zoomBar.getValue() / zoomFactor);
//        	trans.setMxx((minZoom / 0.1) * zoomBar.getValue());
//        	trans.setMyy((minZoom / 0.1) * zoomBar.getValue());
//        	redraw();
        });
    }

    /**
     * This method is used to update the zoom slider
     */
    public void updateZoomSlider() {
        if(zoomBar != null){
            zoomBar.setValue(zoomFactor);
        }
    }

    /**
     * This method is used to convert the mouse coordinates to model coordinates
     * @param lastX The x coordinate as a double
     * @param lastY The y coordinate as a double
     * @return The coordinates as a Point2D
     */
    public Point2D mousetoModel(double lastX, double lastY) {
        try {
            return trans.inverseTransform(lastX, lastY);
        } catch (NonInvertibleTransformException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }

    /**
     * Method for calculating the distance between two points
     * @param lon The longitude as a double
     * @param lat The latitude as a double
     */
    public void gotoCoords(double lon, double lat) {
    	double currentLon = mousetoModel(primaryStage.getWidth() / 2, 0).getX();
    	double currentLat = mousetoModel(0, primaryStage.getHeight() / 2).getY();
    	
    	double dx = currentLon - lon;
    	
    	double dy = currentLat - lat;
    	
    	pan(dx * trans.getMxx(), dy * trans.getMyy());
    }

    /**
     * This method is used to load a file
     * @param fileName The file name as a String
     */
    public void loadFile(String fileName) {
		try {
	    	long startTime = System.nanoTime();
			this.model = Model.load(fileName);
			primaryStage.setTitle("MapScope (" + fileName.split("/")[fileName.split("/").length - 1] + ")");
			DebugView.getInstance().updateFileName(primaryStage.getTitle());
			trans = new Affine();
	        pan(-0.56*model.getMinlon(), model.getMaxlat());
	        zoom(0, 0, canvas.getHeight() / (model.getMaxlat() - model.getMinlat()));

	        minZoom = trans.getMxx() * 0.1;
        	zoomBar.setMax(maxZoom / (minZoom / 0.1));
	        
	        DebugView.getInstance().updateLoadTime(System.nanoTime() - startTime);
	        DebugView.getInstance().updateMemory();
		} catch (ClassNotFoundException | IOException | XMLStreamException | FactoryConfigurationError e) {
			// TODO Error pop up bør laves
			e.printStackTrace();
		}
    }

    /**
     * This method adds a addPOI to the map
     * @param lat The latitude as a double
     * @param lon The longitude as a double
     */
    public void addPOI(double lat, double lon) {

    	InterestPoint pointToRemove = null;

        // Handling the case where the user clicks on an existing POI
    	for(InterestPoint loopInterestPoint : model.getInterestPointList()) {
    		System.out.println(loopInterestPoint.getName() + ": " + loopInterestPoint.getLat() + " , " + loopInterestPoint.getLon());
    		if(lat >= loopInterestPoint.getLat() && lat <= loopInterestPoint.getLat() + 0.00012 &&
    			lon >= loopInterestPoint.getLon() && lon <= loopInterestPoint.getLon() + 0.00012) {
    			pointToRemove = loopInterestPoint;
    			break;
    		}
    	}

        // Handling the case where the user clicks on an existing POI to remove it
    	if(pointToRemove != null) {
    		model.getInterestPointList().remove(pointToRemove);
    		redraw();
    		return;
    	}

        // Adds the POI to the model and redraws the map
    	model.addPOI(lat, lon);
    	redraw();
    }
}