import cn.com.pism.batslog.util.SqlFormatUtils;
import com.alibaba.druid.util.MySqlUtils;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import org.junit.Test;

import javax.swing.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/11/11 下午 07:43
 * @since 0.0.1
 */
public class BatslogTest {

    @Test
    public void format() {
    }

    public static void main(String[] args) {
        keyWordTest();
    }

    public static void keyWordTest(){
        String key = "SELECT";
        boolean keyword = MySqlUtils.isKeyword(key);
        System.out.println(keyword);
    }


    public static void web(){
        JFrame myFrame = new JFrame();
        JFXPanel myFxPanel = new JFXPanel();
        myFrame.add(myFxPanel);



        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                BorderPane borderPane = new BorderPane();
                WebView webComponent = new WebView();

                webComponent.getEngine().load("http://google.com/");

                borderPane.setCenter(webComponent);
                Scene scene = new Scene(borderPane,450,450);
//                myFXPanel.setScene(scene);

            }
        });
    }
}
