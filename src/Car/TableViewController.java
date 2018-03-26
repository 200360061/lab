/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Car;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class TableViewController implements Initializable {

    @FXML
    private TableView<Car> carTableView;
    @FXML
    private TableColumn<Car, String> makeColumn;
    @FXML
    private TableColumn<Car, String> modelColumn;
    @FXML
    private TableColumn<Car, Float> mileageColumn;
    @FXML
    private TableColumn<Car, Integer> yearColumn;
    @FXML
    private Slider minYearSlider;
    @FXML
    private Slider maxYearSlider;
    @FXML
    private ComboBox<String> makeComboBox;
    @FXML
    private Label selectedMinimumYear;
    @FXML
    private Label selectedMaximumYear;

    Connection conn = null;
    Statement statement = null;
    ResultSet resultSet = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            makeColumn.setCellValueFactory(new PropertyValueFactory<Car, String>("make"));
            modelColumn.setCellValueFactory(new PropertyValueFactory<Car, String>("model"));
            mileageColumn.setCellValueFactory(new PropertyValueFactory<Car, Float>("mileage"));
            yearColumn.setCellValueFactory(new PropertyValueFactory<Car, Integer>("year"));
            loadCars();
            initialiseComboBox();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * Method Populates all cars from database to TableView and also initializes values for Slider
     */
    private void loadCars() throws SQLException {
        ObservableList<Car> allCars = FXCollections.observableArrayList();
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Gagandeep", "root", "");
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Cars");
            while (resultSet.next()) {
                Car car = new Car(resultSet.getString("make"),
                        resultSet.getString("model"),
                        resultSet.getFloat("mileage"),
                        resultSet.getInt("year"));
                allCars.add(car);
            }
            carTableView.setItems(allCars);
            resultSet = statement.executeQuery("SELECT MIN(year) FROM cars");
            resultSet.next();
            minYearSlider.valueProperty().addListener((obs, oldVal, newVal)
                    -> minYearSlider.setValue(newVal.intValue()));
            minYearSlider.setMin(Double.parseDouble(String.valueOf(resultSet.getInt("MIN(year)"))));
            maxYearSlider.setMin(Double.parseDouble(String.valueOf(resultSet.getInt("MIN(year)"))));
            minYearSlider.setValue(Double.parseDouble(String.valueOf(resultSet.getInt("MIN(year)"))));
            selectedMinimumYear.setText("Selected Minimum Year : " + String.valueOf(resultSet.getInt("MIN(year)")));
            resultSet = statement.executeQuery("Select MAX(year) FROM cars");
            resultSet.next();
            maxYearSlider.valueProperty().addListener((obs, oldVal, newVal)
                    -> maxYearSlider.setValue(newVal.intValue()));
            minYearSlider.setMax(Double.parseDouble(String.valueOf(resultSet.getInt("MAX(year)"))));
            maxYearSlider.setMax(Double.parseDouble(String.valueOf(resultSet.getInt("MAX(year)"))));
            maxYearSlider.setValue(Double.parseDouble(String.valueOf(resultSet.getInt("MAX(year)"))));
            selectedMaximumYear.setText("Selected Maximum Year : " + String.valueOf(resultSet.getInt("MAX(year)")));
        } catch (Exception ex) {

        } finally {
            if (conn != null) {
                conn.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    /**
     * This method works when slider is used or changes it's value then it executes query to manipulate table data
     */
    public void sliderValueChanged() {
        try {
            selectedMinimumYear.setText("Selected Minimum Year : " + minYearSlider.getValue());
            selectedMaximumYear.setText("Selected Maximum Year : " + maxYearSlider.getValue());
            maxYearSlider.setValue(maxYearSlider.getValue());
            minYearSlider.setValue(minYearSlider.getValue());
            maxYearSlider.setMin(minYearSlider.getValue());
            minYearSlider.setMax(maxYearSlider.getValue());
            ObservableList<Car> allCars = FXCollections.observableArrayList();
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Gagandeep", "root", "");
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Cars where year >= " + minYearSlider.getValue() + " && year <= " + maxYearSlider.getValue());
            while (resultSet.next()) {
                Car c = new Car(resultSet.getString("make"), resultSet.getString("model"), resultSet.getFloat("mileage"), resultSet.getInt("year"));
                allCars.add(c);
            }
            carTableView.setItems(allCars);
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * This method initializes ComboBox
     * @throws SQLException 
     */
    public void initialiseComboBox() throws SQLException {
        try {
            ObservableList list = FXCollections.observableArrayList();
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Gagandeep", "root", "");
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT DISTINCT make FROM Cars");
            while (resultSet.next()) {
                list.add(resultSet.getString("make"));
            }
            makeComboBox.setItems(list);

        } catch (Exception ex) {

        } finally {
            if (conn != null) {
                conn.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    /**
     * This method executes query when selection is made from ComboBox
     * @throws SQLException 
     */
    public void selectedFromComboBox() throws SQLException {
        ObservableList<Car> allCars = FXCollections.observableArrayList();
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Gagandeep", "root", "");
        statement = conn.createStatement();
        resultSet = statement.executeQuery("SELECT * FROM Cars where make = '" + makeComboBox.getValue().toString() + "' && year >= " + minYearSlider.getValue() + " && year <= " + maxYearSlider.getValue());
        while (resultSet.next()) {
            Car c = new Car(resultSet.getString("make"), resultSet.getString("model"), resultSet.getFloat("mileage"), resultSet.getInt("year"));
            allCars.add(c);
        }
        carTableView.setItems(allCars);
    }
    
    /**
     * This Method changes scene to imageView
     * @param event
     * @throws IOException 
     */
        public void doneButtonClicked(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("imageView.FXML"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setTitle("image Making Happy");
        stage.setScene(scene);
        stage.show();
    }
}
