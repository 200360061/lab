/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Car;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Gagandeep Kaur
 */
public class ImageViewController implements Initializable {

    @FXML
    ImageView imageView;

    /**
     * This method initializes the controller class
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            File imageFile = new File("./src/images/sample.png");
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(image);
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }

    }
    /**
     * Method changes scene back to all cars
     */
    public void goBackButtonClicked(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("TableView.FXML"));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        stage.setTitle("All Cars");
        stage.setScene(scene);
        stage.show();
    }
}
