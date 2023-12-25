package Map_of_Denmark.view;

import java.text.DecimalFormat;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This class is responsible for the debug view.
 */
public class DebugView {
	
	private static DebugView DebugView;
	
	private Group debugGroup;
	private Stage primaryStage;
	private Font debugFont;
	private Rectangle rect;
	
	private Text info;
	private Text fileName;
	private Text refresh;
	private Text loadTime;
	private Text memoryUsage;
	private Text coordX;
	private Text coordY;
	private Text zoom;
	private Text diagonalDistance;
	
	private boolean visible;

	/**
	 * Constructor for the debug view.
	 * @param primaryStage The primary stage of the application.
	 */
	public DebugView(Stage primaryStage) {

		DebugView = this;
		this.primaryStage = primaryStage;
		
		debugGroup = new Group();
		
		debugFont = Font.font("Monospaced", FontWeight.BOLD, FontPosture.REGULAR, 15);

		rect = new Rectangle(240, 180);
        rect.setFill(Color.rgb(0, 0, 0, 0.9));
        debugGroup.setTranslateY(primaryStage.heightProperty().doubleValue() - 180);
        
        info = new Text("DEBUG MODE");
        info.setFill(Color.rgb(255, 0, 0));
        info.setTranslateY(14);
        info.setFont(debugFont);
        info.setUnderline(true);
        
        fileName = new Text("File: <file>");
        fileName.setFill(Color.rgb(255, 175, 0));
        fileName.setTranslateY(32);
        fileName.setFont(debugFont);
        
        loadTime = new Text("File load time: ?? s");
        loadTime.setFill(Color.rgb(255, 175, 0));
        loadTime.setTranslateY(46);
        loadTime.setFont(debugFont);
        
        refresh = new Text("Redraw time: ?? ms");
        refresh.setFill(Color.rgb(255, 255, 0));
        refresh.setTranslateY(60);
        refresh.setFont(debugFont);
        
        memoryUsage = new Text("Memory usage: ?? mb");
        memoryUsage.setFill(Color.rgb(255, 255, 0));
        memoryUsage.setTranslateY(74);
        memoryUsage.setFont(debugFont);
        
        coordX = new Text("X: ?? (MAP ??)");
        coordX.setFill(Color.rgb(0, 180, 255));
        coordX.setTranslateY(88);
        coordX.setFont(debugFont);
        
        coordY = new Text("Y: ?? (MAP ??)");
        coordY.setFill(Color.rgb(0, 255, 0));
        coordY.setTranslateY(102);
        coordY.setFont(debugFont);
        
        zoom = new Text("Zoom: ??%");
        zoom.setFill(Color.rgb(255, 0, 255));
        zoom.setTranslateY(116);
        zoom.setFont(debugFont);
        
        diagonalDistance = new Text("DiagDist: ??");
        diagonalDistance.setFill(Color.rgb(255, 0, 255));
        diagonalDistance.setTranslateY(130);
        diagonalDistance.setFont(debugFont);

        debugGroup.getChildren().add(rect);
        debugGroup.getChildren().add(info);
        debugGroup.getChildren().add(fileName);
        debugGroup.getChildren().add(refresh);
        debugGroup.getChildren().add(loadTime);
        debugGroup.getChildren().add(memoryUsage);
        debugGroup.getChildren().add(coordX);
        debugGroup.getChildren().add(coordY);
        debugGroup.getChildren().add(zoom);
        debugGroup.getChildren().add(diagonalDistance);
	}

	/**
	 * Returns the instance of the debug view.
	 * @return The instance of the debug view as DebugView.
	 */
	public static DebugView getInstance() {
		return DebugView;
	}

	/**
	 * Returns the group of the debug view.
	 * @return The group of the debug view as Group.
	 */
	public Group getGroup() {
		return debugGroup;
	}

	/**
	 * Toggles the visibility of the debug view.
	 */
	public void toggle() {
		if(!visible) debugGroup.setOpacity(0);
		else debugGroup.setOpacity(1);
		
		visible = !visible;
	}

	/**
	 * Returns whether the debug view is visible or not.
	 * @return True if the debug view is visible, false if not.
	 */
	public boolean isVisible() {
		return !visible;
	}

	/**
	 * Updates the coordinates of the debug view.
	 * @param mouseX as double for the mouse X coordinate
	 * @param mouseY as double for the mouse Y coordinate
	 * @param mapX as double for the map X coordinate
	 * @param mapY as double for the map Y coordinate
	 */
	public void updateCoords(double mouseX, double mouseY, double mapX, double mapY) {
		DecimalFormat formatMouse = new DecimalFormat("#");
		DecimalFormat formatMap = new DecimalFormat("#.####");
		
        coordX.setText("X: " + formatMouse.format(mouseX) + " (MAP " + formatMap.format(mapX) + ")");
        coordY.setText("Y: " + formatMouse.format(mouseY) + " (MAP " + formatMap.format(mapY) + ")");
	}

	/**
	 * Updates the file name of the map data that is loaded
	 * @param fileName as String for the file name
	 */
	public void updateFileName(String fileName) {
		this.fileName.setText("File: " + fileName);
	}

	/**
	 * Updates the load time of the map data that is loaded
	 * @param timeInNanos as long for the load time in nanoseconds
	 */
	public void updateLoadTime(long timeInNanos) {
		DecimalFormat df = new DecimalFormat("#.####");
		double timeInMillis = (double) timeInNanos / 1000000000;
		loadTime.setText("Load time: " + (df.format(timeInMillis)) + " s");
	}

	/**
	 * Updates the memory usage of the application
	 */
	public void updateMemory() {
		Runtime runtime = Runtime.getRuntime();
		
		memoryUsage.setText("Memory usage: " + ((runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024) + " mb");
	}

	/**
	 * Updates the redraw time of the application
	 * @param timeInNanos as long for the redraw time in nanoseconds
	 */
	public void updateRefreshRate(long timeInNanos) {
		DecimalFormat df = new DecimalFormat("#.####");
		double timeInMillis = (double) timeInNanos / 1000000;
		refresh.setText("Redraw time: " + (df.format(timeInMillis)) + " ms");
	}

	/**
	 * Updates the zoom factor and the diagonal distance of the map
	 * @param zoomFactor as double for the zoom factor
	 * @param diagonalDistance as double for the diagonal distance
	 */
	public void updateZoom(double zoomFactor, double diagonalDistance) {
		int zoomPercentage = (int) (zoomFactor * 100);
		zoom.setText("Zoom factor: " + zoomPercentage + "%");

		DecimalFormat formatDistance = new DecimalFormat("#.####");
		this.diagonalDistance.setText("DiagDist: " + formatDistance.format(diagonalDistance));
	}

	/**
	 * Resizes the debug view
	 */
	public void resize() {
        debugGroup.setTranslateY(primaryStage.heightProperty().doubleValue() - 160);
	}
}
