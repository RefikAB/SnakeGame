import javax.swing.*;
import java.util.Random;
import java.awt.event.*;
import java.awt.*;


public class GamePanel extends JPanel implements ActionListener{
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    static final int UNIT_SIZE = 50;
    static final int SCREEN_WIDTH = 1400;//resolve issue with errors while using smaller screen sizes
    static final int SCREEN_HEIGHT = 800;//resolve issue with errors while using smaller screen sizes
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
    static final int DELAY = 175;
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {

        if(running) {

            g.setColor(Color.orange);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            int i = 0;
            while (i < bodyParts) {
                if (i == 0) {
                    g.setColor(Color.magenta.brighter());
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(127, 255, 212));

                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                i++;
            }

            g.setColor(Color.red);
            g.setFont( new Font("Ink Free",Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }

    }
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){
        int i = bodyParts;
        while (i > 0) {
            x[i] = x[i-1];
            y[i] = y[i-1];
            i--;
        }

        if (direction == 'U') {
            y[0] = y[0] - UNIT_SIZE;
        } else if (direction == 'D') {
            y[0] = y[0] + UNIT_SIZE;
        } else if (direction == 'L') {
            x[0] = x[0] - UNIT_SIZE;
        } else if (direction == 'R') {
            x[0] = x[0] + UNIT_SIZE;
        }


    }
    public void checkApple() {
        if((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions() {

        int i = bodyParts;
        while (i > 0) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
            i--;
        }

        if(x[0] < 0) {
            running = false;
        }

        if(x[0] > SCREEN_WIDTH) {
            running = false;
        }

        if(y[0] < 0) {
            running = false;
        }

        if(y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if(!running) {
            timer.stop();
        }
    }
    public void gameOver(Graphics g) {
        //Score
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
        //Game Over text
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("LOSER! HAHAHA", (SCREEN_WIDTH - metrics2.stringWidth("You suck"))/2, SCREEN_HEIGHT/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if(running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_LEFT) {
                if (direction != 'R') {
                    direction = 'L';
                }
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                if (direction != 'L') {
                    direction = 'R';
                }
            } else if (keyCode == KeyEvent.VK_UP) {
                if (direction != 'D') {
                    direction = 'U';
                }
            } else if (keyCode == KeyEvent.VK_DOWN) {
                if (direction != 'U') {
                    direction = 'D';
                }
            }
        }

    }
}
