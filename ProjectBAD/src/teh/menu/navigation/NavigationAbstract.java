package teh.menu.navigation;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import teh.AppContext;
import teh.data.User;
import teh.menu.Login;
import teh.menu.MenuAbstract;

public abstract class NavigationAbstract extends MenuAbstract {

    protected final BorderPane borderPane = new BorderPane();
    protected final Scene scene = new Scene(borderPane, 800, 700);

    protected final MenuBar menuBar = new MenuBar();

    public NavigationAbstract(Stage stage) {
        super(stage);

        //menu bar
        Menu home = new Menu("Home");
        MenuItem homePage = new MenuItem("Home Page");
        home.getItems().add(homePage);

        Menu account = new Menu("Account");
        MenuItem transactionHistory = new MenuItem("Purchase History");
        MenuItem logout = new MenuItem("Log out");


        homePage.setOnAction(event -> new Home(stage).start());

        transactionHistory.setOnAction(event -> new History(stage).start());

        logout.setOnAction(event -> {
            Login login = new Login(stage);
            login.start();
        });


        if (!context.getCurrentUser().isAdmin()) {
            account.getItems().addAll(transactionHistory, logout);
        } else {
            account.getItems().addAll(logout);
        }

        Menu cart = new Menu("Cart");
        MenuItem myCart = new MenuItem("My Cart");
        cart.getItems().add(myCart);

        myCart.setOnAction(event -> new CartNavigation(stage).start());

        Menu manageProduct = new Menu("Manage Product");
        MenuItem manageProducts = new MenuItem("Manage Products");
        manageProduct.getItems().add(manageProducts);

        manageProducts.setOnAction(event -> {
            ManageProduct manageproduct = new ManageProduct(stage);
            manageproduct.start();
        });


        if (context.getCurrentUser().isAdmin()) {
            menuBar.getMenus().addAll(home, manageProduct, account);
        } else {
            menuBar.getMenus().addAll(home, cart, account);
        }

        borderPane.setTop(menuBar);
        borderPane.setCenter(gridPane);



    }

    @Override
    public void start() {
        show();
        stage.setScene(scene);
        stage.show();
    }

    protected User getCurrentUser() {
        return context.getCurrentUser();
    }

}
