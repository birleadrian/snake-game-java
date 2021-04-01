import javax.swing.*;

public class GameFrame extends JFrame {
    GameFrame(){
        GPanel panel = new GPanel();
        this.add(panel);
        this.setTitle("Snake game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
