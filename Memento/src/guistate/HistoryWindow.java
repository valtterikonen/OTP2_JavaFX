package guistate;

import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class HistoryWindow {
    private Controller controller;

    public HistoryWindow(Controller controller) {
        this.controller = controller;
    }

    public void show() {
        Stage stage = new Stage();
        ListView<String> listView = new ListView<>();

        // Populate the ListView with the history states
        for (IMemento memento : controller.getHistory()) {
            listView.getItems().add(memento.toString());
        }

        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                int index = listView.getSelectionModel().getSelectedIndex();
                if (index >= 0) {
                    controller.restoreFromHistory(index);
                    stage.close();
                }
            }
        });

        Scene scene = new Scene(listView, 300, 400);
        stage.setScene(scene);
        stage.setTitle("History");
        stage.show();
    }
}