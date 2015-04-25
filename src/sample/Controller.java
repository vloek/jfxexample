package sample;

import com.sun.deploy.util.StringUtils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Stream;

public class Controller {

    public WebView   webView;
    public TextField searchField;

    public void loadPage(ActionEvent actionEvent) throws IOException {
        WebEngine we = webView.getEngine();
        String url = "http://eda.ru/recipesearch?q=" + searchField.getText();
        Document doc = Jsoup.connect(url).get();
        Elements receipts = doc.select("div.b-recipe-widget");


        we.locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, final String oldLoc, final String loc) {
                if (!loc.contains("recipesearch") && !Objects.equals(loc, "")) Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            we.loadContent(String.valueOf(new Receipt(loc)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        String receiptsAccepts = receipts.stream()
                .map(Controller::fixReceipt)
                .map(Object::toString)
                .reduce("", (a, b) -> a + b);
        we.loadContent(receiptsAccepts);
    }

    private static Object fixReceipt(Element element) {
        return new Receipt(element);
    }

}
