/*
 * @author : Birle Adrian
 * @version : 1.0
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GPanel extends JPanel implements ActionListener {

        //Game's screen Dimension
        static final int SCREEN_WIDTH = 1300;
        static final int SCREEN_HEIGHT = 700;
        //Dimension of a cell
        static final int UNIT_SIZE = 50;
        //Number of cells
        static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
        //Delay of the game
        static final int DELAY = 75;
        //Coordinates of the body parts of the snake -> (x[0],y[0]) is the Head of the snake
        final int x[] = new int[GAME_UNITS];
        final int y[] = new int[GAME_UNITS];
        //Snake's length
        private int bodyParts = 3;
        //Score
        private int applesEaten;
        //Coordinates of an apple
        private int appleX;
        private int appleY;
        //The direction of movement of the snake
        String direction = "Right";
        //Check if the game is not over
        boolean running = false;
        Timer timer;
        Random rand;
        //Constructor of the Game Panel
        GPanel(){
                rand = new Random();
                this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
                this.setBackground(new Color(204,204,204));
                this.setFocusable(true);
                this.addKeyListener(new KAdapter());
                startGame();
        }
        //Method which start the game
        public void startGame(){
                newApple();
                running = true;
                timer = new Timer(DELAY,this);
                timer.start();
        }
        public void paintComponent(Graphics g){
                super.paintComponent(g);
                draw(g);
        }
        public void draw(Graphics g){
                if(running) {
                        //Create the Grid
                        for (int i = 0; i <= SCREEN_WIDTH / UNIT_SIZE; i++) {
                                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                        }
                        for(int i = 0 ; i <= SCREEN_HEIGHT / UNIT_SIZE ; i++){
                                g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH,i*UNIT_SIZE);
                        }
                        //Insert a red apple on the map
                        g.setColor(Color.red);
                        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

                        for (int i = 0; i < bodyParts; i++) {
                                if (i == 0) { //Verify if the bodyPart is the head of the snake and set a color for the head
                                        g.setColor(new Color(255, 204, 0));
                                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                                } else {
                                        //Set a random color for the body of the snake
                                        g.setColor(new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255)));
                                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                                }
                        }
                        //Display the score
                        g.setColor(new Color(0,204,153));
                        g.setFont(new Font("Monospaced",Font.BOLD,30));
                        FontMetrics metrics = getFontMetrics(g.getFont());
                        g.drawString("Score: " + applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2,metrics.getHeight());
                }
                else{
                        gameOver(g);
                }
        }
        //Method that create a new Apple at a random position
        public void newApple(){
                appleX = rand.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
                appleY = rand.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
        }
        //Method that move the snake
        public void move(){
                for(int i = bodyParts ; i>0 ; i--){
                        x[i] = x[i-1];
                        y[i] = y[i-1];
                }
                switch (direction){
                        case "Right":
                                x[0] = x[0] + UNIT_SIZE;
                                break;
                        case "Left":
                                x[0] = x[0] - UNIT_SIZE;
                                break;
                        case "Up":
                                y[0] = y[0] - UNIT_SIZE;
                                break;
                        case "Down":
                                y[0] = y[0] + UNIT_SIZE;
                                break;
                }
        }
        //Method that checks if we met an apple
        public void checkApple(){
                if(x[0]==appleX && y[0]== appleY){
                        applesEaten++;
                        bodyParts++;
                        newApple();
                }
        }
        //Method that checks if we had a collision
        public void checkCollision(){
                for(int i=bodyParts ; i>0 ;i--){
                        if((x[0]== x[i]) && ( y[0] == y[i])){
                                running = false;
                        }
                }
                if(x[0]<0){
                        x[0] = SCREEN_WIDTH;
                }
                if(x[0]>SCREEN_WIDTH){
                        x[0] = 0;
                }
                if(y[0]<0){
                        y[0] = SCREEN_HEIGHT;
                }
                if(y[0]>SCREEN_HEIGHT){
                        y[0] = 0;
                }
                if(running == false){
                        timer.stop();
                }
        }
        //Method that is called when the game is over and display the final score
        public void gameOver(Graphics g){
                g.setColor(Color.orange);
                g.setFont(new Font("Monospaced",Font.BOLD,80));
                FontMetrics metrics = getFontMetrics(g.getFont());
                this.setBackground(new Color(0,153,204));
                g.drawString("Game Over!",(SCREEN_WIDTH - metrics.stringWidth("Game Over!"))/2,SCREEN_HEIGHT/2-metrics.getHeight());
                g.drawString("Your score: " + applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Your score: " + applesEaten))/2,SCREEN_HEIGHT/2);
                JButton restart = new JButton("Restart");
                restart.setFont(new Font("Monospaced",Font.BOLD,30));
                restart.setBounds((SCREEN_WIDTH-175)/2,(SCREEN_HEIGHT-75)/2 + metrics.getHeight(),175,75);
                restart.setBackground(new Color(204,204,255));
                restart.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                SnakeGame.newFrame(1);
                        }
                });
                this.add(restart);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
                if(running){
                        move();
                        checkApple();
                        checkCollision();
                }
                repaint();
        }
        //Inner class that determines which key we pressed to change the direction
        public class KAdapter extends KeyAdapter{
                @Override
                public void keyPressed(KeyEvent e){
                        switch (e.getKeyCode()){
                                case KeyEvent.VK_LEFT:
                                        if(!direction.equals("Right")){
                                                direction = "Left";
                                        }
                                        break;
                                case KeyEvent.VK_RIGHT:
                                        if(!direction.equals("Left")){
                                                direction = "Right";
                                        }
                                        break;
                                case KeyEvent.VK_UP:
                                        if(!direction.equals("Down")){
                                                direction = "Up";
                                        }
                                        break;
                                case KeyEvent.VK_DOWN:
                                        if(!direction.equals("Up")){
                                                direction = "Down";
                                        }
                                        break;
                        }
                }

        }
}
