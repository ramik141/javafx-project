
package com.company;

import com.company.util.FileHandler;
import com.company.util.JavaCompiler;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.*;
//import javafx.scene.control.MenuItemBuilder;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.BorderPane;

import javax.swing.*;
import java.io.File;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class App extends Application {

    private TextArea textArea;
    private FileHandler fileHandler;
    private MenuItem fileOpen;
    private MenuItem fileSave;
    private String path;
    public JavaCompiler compiler;

    public App(){
        System.out.println("Constructor");
    }

    public void saveAs(ActionEvent e){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        File f = fileChooser.showSaveDialog(new Stage());

        try {
            if (f != null) {
                String path = f.getAbsolutePath();
                fileHandler.setFilePath(path);
                fileHandler.saveFile(textArea.getText());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            errorMSG(ex);
        }
        System.out.println("SaveAs");
    }

    public void save(ActionEvent e){

        try{
            fileHandler.setFilePath(path);
            fileHandler.saveFile(textArea.getText());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            errorMSG(ex);
        }
        System.out.println("Save");
    }

    public void open(ActionEvent e){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");

        //File f = fileChooser.showOpenDialog(fileOpen.getParentPopup().getScene().getWindow());
        File f = fileChooser.showOpenDialog(null);

        try {
            if (f == null) {
                System.out.println("Cancel");
                return;
            } else {
                path = f.getAbsolutePath();
                fileHandler.setFilePath(path);
                String content = fileHandler.openFile();
                textArea.setText(content);
                fileSave.setDisable(false);
            }
        } catch (Exception except) {
            System.out.println(except.getMessage());
            errorMSG(except);
        }
        System.out.println("Open");
    }

    public void errorMSG (Exception e){
        Alert open = new Alert(Alert.AlertType.ERROR);
        open.setTitle("Error Dialog");
        open.setHeaderText("Something go wrong this time.");
        open.setContentText(e.getMessage());
        open.showAndWait();
    }

    public void search(String searchText) {

        int index = this.textArea.getText().indexOf(searchText);

        textArea.selectRange(index, index + searchText.length());

    }

    @Override
    public void start(Stage stage) {

        fileHandler = new FileHandler();
        compiler = new JavaCompiler();
        TextArea output = new TextArea();

        SplitPane splitpane = new SplitPane();
        splitpane.setOrientation(Orientation.VERTICAL);

        Locale locale = Locale.getDefault();
        //Locale locale = new Locale("fi", "FI");
        ResourceBundle labels = ResourceBundle.getBundle("ui", locale);
        String title = labels.getString("title");

        BorderPane root = new BorderPane();


        FileChooser fileChooser = new FileChooser();

        Menu mFile = new Menu(labels.getString("file"));

        MenuItem fileNew = new MenuItem(labels.getString("new"));
        fileNew.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        mFile.getItems().add(fileNew);

        fileOpen = new MenuItem(labels.getString("open"));
        fileOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        fileOpen.setOnAction(this::open);
        mFile.getItems().add(fileOpen);

        MenuItem fileSaveAs = new MenuItem(labels.getString("save_as"));
        fileSaveAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHIFT_DOWN, KeyCombination.CONTROL_DOWN));
        mFile.getItems().add(fileSaveAs);
        fileSaveAs.setOnAction(this::saveAs);

        fileSave = new MenuItem(labels.getString("save"));
        fileSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        mFile.getItems().add(fileSave);
        fileSave.setDisable(true);
        fileSave.setOnAction(this::save);

        SeparatorMenuItem separator = new SeparatorMenuItem();
        mFile.getItems().add(separator);

        MenuItem fileExit = new MenuItem(labels.getString("exit"));
        mFile.getItems().add(fileExit);

        fileExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //System.out.println("Exit");
                //System.exit(0);
                Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
                exitAlert.setTitle("Exit dialog");
                exitAlert.setHeaderText("Are you sure you want to exit?");

                Optional<ButtonType> result = exitAlert.showAndWait();
                ButtonType buttonType = result.get();

                if(buttonType == ButtonType.OK){
                    System.exit(0);
                } else {
                    return;
                }
            }
        });

        textArea = new TextArea();
        textArea.setStyle("-fx-font-size: 12");

        // EDIT MENU
        Menu mEdit = new Menu(labels.getString("edit"));
        MenuItem editCut = new MenuItem(labels.getString("cut"));
        editCut.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
        mEdit.getItems().add(editCut);

        MenuItem editCopy = new MenuItem(labels.getString("copy"));
        editCopy.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        mEdit.getItems().add(editCopy);

        MenuItem editPaste = new MenuItem(labels.getString("paste"));
        editPaste.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));
        mEdit.getItems().add(editPaste);

        /*editCut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                textArea.cut();
            }
        });*/

        editCut.setOnAction(e ->   textArea.cut());

        editCopy.setOnAction(e ->  textArea.copy());

        editPaste.setOnAction(e -> textArea.paste());


        // RUN MENU
        Menu mRun = new Menu(labels.getString("run"));
        MenuItem runItem1 = new MenuItem(labels.getString("compile_run"));
        mRun.getItems().add(runItem1);

        // ABOUT MENU
        Menu mAbout = new Menu(labels.getString("about"));
        MenuItem aboutItem1 = new MenuItem(labels.getString("about_app"));
        mAbout.getItems().add(aboutItem1);

        aboutItem1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Information Dialog");
                info.setHeaderText("About");
                info.setContentText("Mika");

                info.showAndWait();
            }
        });

        // Add components to menubar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(mFile, mEdit, mRun, mAbout);

        //VBox vBox = new VBox(menuBar);

        // Toolbar
        ToolBar toolBar = new ToolBar();

        Button compile = new Button("Compile & Run");
        compile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Compile");
                //compiler.compileAndRun(path);
                String compContent = compiler.compileAndRun(path);;
                output.setText(compContent);
            }
        });
        toolBar.getItems().add(compile);

        Button button = new Button("Clear");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Clear");
                textArea.clear();
            }
        });
        //toolBar.getItems().add(button);

        ComboBox comboFont = new ComboBox(FXCollections.observableList(Font.getFamilies()));
        //comboFont.setPrefWidth(150);
        comboFont.getSelectionModel().select(1);
        comboFont.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                textArea.setStyle("-fx-font-family: " + comboFont.getValue() + ";");
            }
        });
        toolBar.getItems().add(comboFont);

        TextField fontSize = new TextField("12");
        fontSize.setPrefColumnCount(2);

        fontSize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                textArea.setStyle("-fx-font-size: " + fontSize.getText() + ";");
            }
        });
        toolBar.getItems().add(fontSize);


        // Colorpicker
        ColorPicker colorPicker = new ColorPicker();
        //Color value = colorPicker.getValue();

        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                textArea.setStyle("-fx-text-fill: " + toRgbString(colorPicker.getValue()) + ";");
            }
        });

        toolBar.getItems().add(colorPicker);



        TextField searchText = new TextField();

        searchText.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ENTER) {
                    search(searchText.getText());
                }
            }
        });
        toolBar.getItems().add(searchText);
        Label label = new Label();



        output.setStyle("-fx-text-fill: lime; -fx-control-inner-background: black;");
        //output.setStyle("text-area-background: black;");

        //splitpane.getItems().addAll(textArea, new TextArea("down"));
        splitpane.getItems().addAll(textArea, output);

        textArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.TAB){
                    System.out.println("....");
                    int index = textArea.getCaretPosition();
                    textArea.replaceText(index-1, index,"    ");
                }
            }
        });

        VBox vBox = new VBox(menuBar, toolBar);
        root.setTop(vBox);
        root.setCenter(splitpane);

        Scene scene = new Scene(root, 720, 600);

        stage.setTitle(title);
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("Stop");
    }

    public String toRgbString(Color c){
        return "rgb("
                + to255Int(c.getRed())
                + "," + to255Int(c.getGreen())
                + "," + to255Int(c.getBlue())
                + ")";
    }

    public int to255Int(double d){
        return (int) (d * 255);
    }



    public static void main(String args[]) {
        launch(args);
    }
}