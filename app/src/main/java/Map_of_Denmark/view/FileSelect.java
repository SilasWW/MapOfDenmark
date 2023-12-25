package Map_of_Denmark.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * This class is used to select a file from the data directory or browse for a file.
 */
public class FileSelect extends Stage {
	
	private Scene scene;
	private StackPane layout;
	private View view;

	/**
	 * Constructor for the FileSelect class.
	 * @param view as View object
	 */
	public FileSelect(View view) {
		this.view = view;

		// Setup
		setWidth(360);
		setHeight(260);
		setTitle("Select map");
		setResizable(false);
		initModality(Modality.APPLICATION_MODAL);
		
		FileChooser fileDialog = new FileChooser();

		fileDialog.getExtensionFilters().add(new ExtensionFilter("Map files", ".osm", ".zip", ".txt"));
		fileDialog.getExtensionFilters().add(new ExtensionFilter("All files", "*"));

		// Setting up the Scene and StackPane
		layout = new StackPane();
		scene = new Scene(layout, getWidth(), getHeight());
		this.setScene(scene);

		// Text for the files
		Text txtFiles = new Text("Select a map from the drop-down menu");
		txtFiles.setTranslateY(-80);
		layout.getChildren().add(txtFiles);
		
		ComboBox<String> cmbFiles = new ComboBox<String>();
		File dataDirectory = new File("data");
		for(File loopFile : dataDirectory.listFiles()) {
			if(!loopFile.getName().endsWith(".osm") && !loopFile.getName().endsWith(".zip") && !loopFile.getName().endsWith(".txt") && !loopFile.getName().endsWith(".obj")) continue;
			if(loopFile.getName().equalsIgnoreCase("empty.osm")) continue;
			cmbFiles.getItems().add(loopFile.getName());
		}
		cmbFiles.setOnAction(event -> {
			this.view.loadFile("data/" + cmbFiles.getValue());
			this.close();
		});
		cmbFiles.setTranslateY(-50);
		layout.getChildren().add(cmbFiles);
		
		Text txtBrowse = new Text("OR");
		txtBrowse.setFont(Font.font(txtBrowse.getFont().getFamily(), FontWeight.NORMAL, FontPosture.REGULAR, 30));
		layout.getChildren().add(txtBrowse);
		
		Button btnBrowse = new Button("Browse files ...");
		btnBrowse.setTranslateY(50);
		btnBrowse.setOnAction(event -> {
			File selectedFile = fileDialog.showOpenDialog(this);
			try {
				this.view.loadFile(selectedFile.getCanonicalPath());
				this.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		});
		layout.getChildren().add(btnBrowse);
	}
}
