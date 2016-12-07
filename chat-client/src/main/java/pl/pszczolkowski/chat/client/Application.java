package pl.pszczolkowski.chat.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pl.pszczolkowski.chat.client.configuration.ApplicationConfiguration;
import pl.pszczolkowski.chat.client.service.ApiWrapper;
import pl.pszczolkowski.chat.client.service.SceneManager;
import pl.pszczolkowski.chat.client.ui.alert.AlertBox;
import pl.pszczolkowski.chat.client.ui.signin.SignInView;

public class Application extends javafx.application.Application {

    @Autowired
    private SceneManager sceneManager;
    @Autowired
    private ApiWrapper apiWrapper;
    @Autowired
    private SignInView signInView;

    private AnnotationConfigApplicationContext applicationContext;

    public static void main(String[] args) {
        Application.launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        initializeSpringContext();
        initializeUI(primaryStage);
        Thread.setDefaultUncaughtExceptionHandler(Application::handleUncaughtError);
    }
    
    private void initializeSpringContext() {
        applicationContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        applicationContext
            .getAutowireCapableBeanFactory()
            .autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
    }

    private void initializeUI(Stage primaryStage) {
        primaryStage.setTitle("Chat client");
        primaryStage.setWidth(300);
        primaryStage.setHeight(500);
        setIcon(primaryStage);
        
        sceneManager.setStage(primaryStage);
        sceneManager.showScene(signInView);
    }

    private void setIcon(Stage primaryStage) {
        String iconPath = getClass()
                .getClassLoader()
                .getResource("icon.png")
                .toString();
        Image icon = new Image(iconPath);
        primaryStage.getIcons().add(icon);
    }
    
    private static void handleUncaughtError(Thread thread, Throwable throwable) {
        throwable.printStackTrace();
        
        new AlertBox()
            .ofType(AlertType.ERROR)
            .withContent("Some error occurred")
            .showAndWait();
    }

    @Override
    public void stop() throws Exception {
        apiWrapper.getAccountApi().logout();
        
        applicationContext.destroy();
        Platform.exit();
        super.stop();
    }

}