package view.menus;

import controller.GameMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.User;

import java.net.URL;
import java.util.Objects;

public class MainMenu extends Application {
    private static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        MainMenu.stage = stage;
        URL url = new URL(Objects.requireNonNull(this.getClass().getResource("/FXML/mainMenu.fxml")).toExternalForm());
        AnchorPane pane = FXMLLoader.load(url);
        Text name = new Text(0, 20, User.getCurrentUser().getUsername() + "'s room");
        name.setFill(Color.RED);
        name.setTextAlignment(TextAlignment.CENTER);
        name.setFont(Font.font(20));
        pane.getChildren().add(name);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    public void startGame() throws Exception {
        GameMenuController.setIs2Player(false);
        new GameMenu().start(stage);
    }

    public void twoPlayerGame() throws Exception {
        GameMenuController.setIs2Player(true);
        new GameMenu().start(stage);
    }

    public void goSettings() throws Exception {
        new SettingsMenu().start(MainMenu.stage);
    }

    public void exitGame() {
        stage.close();
    }

    public void goProfile() throws Exception {
        new ProfileMenu().start(MainMenu.stage);
    }

    public void goScoreBoard() throws Exception {
        new ScoreboardMenu().start(stage);
    }
}
