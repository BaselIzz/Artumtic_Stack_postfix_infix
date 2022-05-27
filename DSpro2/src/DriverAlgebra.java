	
import javafx.scene.control.TextArea;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;

public class DriverAlgebra extends Application {
	Algebric alg = new Algebric();
	public static void main(String[] args) {
		launch(args);
	}

	// GUI
	@Override
	public void start(Stage primaryStage) throws Exception {

		AnchorPane pane = new AnchorPane();
		ArrayList<String> equationLines = new ArrayList<>();
		Button btnChooseFile = new Button("Choose File");
		Button btnShowValid = new Button("Show Valid");
		btnShowValid.setDisable(true);
		Button btnShowNotValid = new Button("Show Not Valid");
		btnShowNotValid.setDisable(true);
		Button btnExit = new Button("Exit");
		btnExit.setDisable(false);
		Button btnSaveOnFile = new Button("Save to File");
		btnSaveOnFile.setDisable(true);
		VBox vbx = new VBox();
		vbx.setLayoutX(14.0);
		vbx.setLayoutY(79.0);

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Equaltions File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));

		vbx.getChildren().addAll(btnChooseFile, btnShowValid, btnShowNotValid, btnSaveOnFile, btnExit);
		vbx.setSpacing(40);

		TextArea txt = new TextArea();
		txt.setLayoutX(198);
		txt.setLayoutY(41.0);
		txt.setPrefSize(900.0, 520.0);
		txt.setEditable(false);
 		pane.getChildren().addAll(vbx, txt);
		Scene s = new Scene(pane, 1100, 600);
		s.getStylesheets().add("project2.css");

		btnChooseFile.setOnAction(e -> {
			String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
			fileChooser.setInitialDirectory(new File(currentPath));
			File selectedFile = fileChooser.showOpenDialog(primaryStage);
			if (selectedFile != null) {
				if (loadFile(equationLines, selectedFile)) {
					btnShowNotValid.setDisable(false);
					btnChooseFile.setDisable(true);

					btnSaveOnFile.setDisable(false);
					btnShowValid.setDisable(false);
				} else
					ShowMessage(Alert.AlertType.WARNING, "Warning", "THe file is empty ");

			}

		});
		btnSaveOnFile.setOnAction(e -> {
			String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
			fileChooser.setInitialDirectory(new File(currentPath));
			File selectedFile = fileChooser.showSaveDialog(primaryStage);
			if (selectedFile != null) {
			String txtString = preparePrint(equationLines);
			txt.setText(txtString);
		
			try {
				FileWriter fw = new FileWriter(selectedFile);
				fw.write(txtString);
				fw.close();
				ShowMessage(Alert.AlertType.INFORMATION,"File Saved", "All records are saved successfully to file  "+selectedFile);
			} catch (IOException e2) {

				e2.printStackTrace();
			}
			}

		});
		btnExit.setOnAction(e -> {
			System.exit(0);
		});
		btnShowValid.setOnAction(e -> {
			String txString = showValid(equationLines);
			txt.setText(txString);
		});
		btnShowNotValid.setOnAction(e -> {
			String txString = ShowNotValid(equationLines);
			txt.setText(txString);
		});

		primaryStage.setScene(s);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Data Structures Project No: 2 by Basel izz. Supervisor Dr. Mamoun Nawahdah  ");
		primaryStage.show();

	}

	public static void ShowMessage(AlertType alert, String Header, String ContentText) {
		Alert alt = new Alert(null);
		alt.setAlertType(alert);
		alt.setHeaderText(Header);
		alt.setContentText(ContentText);
		alt.showAndWait();
	}

// Method which return the valid equations and  evaluate them 	
	private String showValid(ArrayList<String> equationLines) {
		String equationLine, eq1, eq2;
		String result = "";
		for (int i = 0; i < equationLines.size(); i++) {

			equationLine = equationLines.get(i);

			int separate;
			separate = equationLine.lastIndexOf("=");
			eq1 = equationLine.substring(0, separate).trim();
			eq2 = equationLine.substring(separate + 1).trim();
			if (alg.isBalance(eq1) && alg.isBalance(eq2))
				result += (showResults(eq1, eq2) + "\n");

		}
		return result;
	}

// Method to show not valid equations	
	private String ShowNotValid(ArrayList<String> equationLines) {
		String equationLine, eq1, eq2, result = "";
		for (int i = 0; i < equationLines.size(); i++) {

			equationLine = equationLines.get(i);
			int separate;
			separate = equationLine.lastIndexOf("=");
			eq1 = equationLine.substring(0, separate).trim();
			eq2 = equationLine.substring(separate + 1).trim();
			if (!alg.isBalance(eq1) && alg.isBalance(eq2)) {
				result += eq1 + "-> " + "Invalid\n";
				result += (showResults(eq2) + "\n");
			} else if (alg.isBalance(eq1) && !alg.isBalance(eq2)) {
				result += (showResults(eq1) + "\n");
				result += (eq2 + "->" + "Invalid\n");
			} else if (!alg.isBalance(eq1) && !alg.isBalance(eq2)) {
				result += (eq1 + "->" + "Invalid\n");
				result += (eq2 + "->" + "Invalid\n"); 
			}

		}
		return result;

	}

// method to Find both valid and not valid equations and prepare string to output file. 	
	private  String preparePrint(ArrayList<String> equationLines) {
		String equationLine, eq1, eq2, result = "";

		for (int i = 0; i < equationLines.size(); i++) {
			equationLine = equationLines.get(i).trim();
			int separate;
			separate = equationLine.lastIndexOf("=");
			eq1 = equationLine.substring(0, separate).trim();
			eq2 = equationLine.substring(separate + 1).trim();
			if (alg.isBalance(eq1) && alg.isBalance(eq2)) {
				result += printVaild(eq1)+"\n";
				result+=printVaild(eq2)+"\n";
			} else if (!alg.isBalance(eq1) && alg.isBalance(eq2)) {
				result += (eq1 + "-> " + "Invalid\n");
				result+=printVaild(eq2)+"\n";
			} else if (alg.isBalance(eq1) && !alg.isBalance(eq2)) {
				result += printVaild(eq1)+"\n";
				result += (eq2 + "-> " + "Invalid\n");
			} else {
				result += (eq1 + "-> " + "Invalid\n");
				result += (eq2 + "-> " + "Invalid\n");
			}

		}
		return result;
	}
	
	// Method to read lines from file.
		public static boolean loadFile(ArrayList<String> equationLines, File f) {

			try {

				@SuppressWarnings("resource")
				Scanner scr = new Scanner(f);

				if (scr.hasNext()) {

					while (scr.hasNext()) {
						String s = scr.nextLine();
						equationLines.add(s);
					}
					scr.close();
					return true;

				}

			} catch (Exception e) {

				e.printStackTrace();
			}
			return false;
		}

		// case ---  two equations are Valid on line
		public  String showResults(String eq1, String eq2) {
			
			return "[" + alg.convertTOPostfix(eq1) + " = " + alg.evaluatePostfix(alg.convertTOPostfix(eq1))
					+ "]" + " =? " + "[" + alg.convertTOPostfix(eq2) + " = "
					+ alg.evaluatePostfix(alg.convertTOPostfix(eq2)) + "]" + "-> "
					+ (alg.evaluatePostfix(alg.convertTOPostfix(eq1)) == alg
							.evaluatePostfix(alg.convertTOPostfix(eq2)));
		}

		// case --- one equation is valid on line
		public  String showResults(String eq1) {
			
			return eq1 + "-> Valid" + " -> " + alg.evaluatePostfix(alg.convertTOPostfix(eq1));
		}

		public  String printVaild(String eq1) {
			
			return eq1 + "-> Valid" + " ->  " + alg.convertTOPostfix(eq1)+"  = "+alg.evaluatePostfix(alg.convertTOPostfix(eq1));
		}

		
		
	
}
