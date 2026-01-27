import ui.MainWindow;

public class App {
    public static void main(String[] args) throws Exception {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainWindow();
        });
    }
}
