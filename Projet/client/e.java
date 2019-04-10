import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class e extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    Scene scene = new Scene(new Group());
    stage.setTitle("Sample");
    stage.setWidth(300);
    stage.setHeight(200);

    VBox vbox = new VBox();
    vbox.setPrefHeight(200);
    vbox.setPrefWidth(200);
    vbox.setLayoutX(100);
    vbox.setLayoutY(100);
    
    Polygon p = new Polygon(new double[]{200,200,200+20,200+10,200+20,200-10});
    p.getTransforms().add(new Rotate(45,0,0));
    
    vbox.getChildren().add(p);
    vbox.setSpacing(10);
    ((Group) scene.getRoot()).getChildren().add(vbox);

    stage.setScene(scene);
    stage.show();
  }
}

