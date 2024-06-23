
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;

public class Board extends JPanel implements Runnable {
    private final int TILE_SIZE = 26; // min resolution = 9, max = 28 (preferred = 26)
    private final int speed = 5; // max speed = 10, min = 1 (preferred speed = 5)
    private final boolean showInfo = false;
    private int X_TILES, Y_TILES, SCREEN_HEIGHT, SCREEN_WIDTH;
    private Image down, ghost, up, left, right, win;

    // for getting data
    private int[] map;
    // for changing data
    private int[][] mapOrganizer; //map[] to-> mapOrganizer[][]
    private int pacmanX, pacmanY, pacmanDX, pacmanDY;
    private int[] ghostX, ghostY, ghostDX, ghostDY;

    private int score, num_Points;
    private boolean inGame;

    private int xP, yP, x1P = xP, y1P = yP;
    private boolean rP = true, lP = true, dP = true, uP = true;
    private int qP = 0;

    private int[] xG, yG, x1G, y1G;
    private boolean[] rG , lG , dG , uG;
    private int[] qG ;

    //            up  down  right  left
    private final boolean[] P = {true, true, true, true};

    public int getSCREEN_HEIGHT() {
        return SCREEN_HEIGHT;
    }

    public int getSCREEN_WIDTH() {
        return SCREEN_WIDTH;
    }
    public boolean isInGame() {
        return inGame;
    }

    public Board() {
        initBoard();
    }

    private void loadImages() {

        down = new ImageIcon("images\\down.gif").getImage();
        up = new ImageIcon("images\\up.gif").getImage();
        left = new ImageIcon("images\\left.gif").getImage();
        right = new ImageIcon("images\\right.gif").getImage();
        ghost = new ImageIcon("images\\ghost.gif").getImage();
        win = new ImageIcon("images" +
                "\\illustration-of-a-group-of-kids-celebrating-their-victory-HXYP6N.jpg").getImage();
    }

    private void initBoard() {

        if (TILE_SIZE > 28 || TILE_SIZE < 9) {
            System.out.println("the tile size is not valid");
            System.exit(1);
        }
        if (speed > 10 || speed < 1) {
            System.out.println("the speed is not valid");
            System.exit(1);
        }
        loadImages();
        level2();
        rG = new boolean[xG.length];
        lG = new boolean[xG.length];
        uG = new boolean[xG.length];
        dG = new boolean[xG.length];
        qG = new int[xG.length];
        for (int i = 0; i < xG.length; i++) {
            rG[i] = true;
            lG[i] = true;
            uG[i] = true;
            dG[i] = true;
            P[i] = true;
            qG[i] = 0;
        }
        setBounds(10, 2, SCREEN_WIDTH + 2, SCREEN_HEIGHT + 2);
        this.setBackground(Color.black);
        setFocusable(true);
        requestFocus();
        addKeyListener(new TAdapter());

        sum_Num();
        score = 0;
        inGame = true;

        Thread animator = new Thread(this);
        animator.start();
    }

    private void level1() {
        // wall = 1
        // right border = 2
        // left border = 4
        // upper border = 8
        // bottom border = 16
        // points/dots = 32
        // example: 32+8+2 = 42
        int X_TILES = 12;
        int Y_TILES = 12;
        xP = 6; // tile index
        yP = 9;
        xG = new int[]{2};
        yG = new int[]{3};
        // to calc (it's useless but necessary):
        x1G = new int[]{2};
        y1G = new int[]{3};

        int[] array = new int[xG.length];
        int[] array1 = new int[yG.length];
        for (int i = 0; i < xG.length; i++) {
            array[i] = xG[i] * TILE_SIZE;
            array1[i] = yG[i] * TILE_SIZE;
        }
        createLevel(X_TILES, Y_TILES, xP * TILE_SIZE, yP * TILE_SIZE,
                array, array1

                , new int[]{
                        //0 1  2  3  4  5  6  7  8  9  10 11
                        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                        1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                        1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1,
                        1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                        1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                        1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1,
                        1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                        1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1,
                        1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
                        1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                        1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                }
        );

    }

    private void level2() {
        int X_TILES = 21;
        int Y_TILES = 23;
        xP = 10;
        yP = 16;
        xG = new int[]{9, 10, 11, 10};
        yG = new int[]{11, 11, 11, 10};
        // to calc (it's useless but necessary):
        x1G = new int[]{9, 10, 11, 10};
        y1G = new int[]{11, 11, 11, 10};

        int[] array = new int[xG.length];
        int[] array1 = new int[yG.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = xG[i] * TILE_SIZE;
            array1[i] = yG[i] * TILE_SIZE;
        }
        createLevel(X_TILES, Y_TILES, xP * TILE_SIZE, yP * TILE_SIZE,
                array, array1
                , new int[]{
                        //0 1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19 20
                        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                        1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                        1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1,
                        1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1,
                        1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                        1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1,
                        1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1,
                        1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1,
                        1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 3, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1,
                        1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 3, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1,
                        1, 0, 0, 0, 0, 0, 0, 0, 1, 3, 4, 3, 1, 0, 0, 0, 0, 0, 0, 0, 1,
                        1, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 1, 0, 0, 0, 0, 0, 0, 0, 1,
                        1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1,
                        1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1,
                        1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1,
                        1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                        1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1,
                        1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1,
                        1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1,
                        1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1,
                        1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1,
                        1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                }
        );

    }

    private void createLevel(int X_TILES, int Y_TILES, int pacmanX, int pacmanY, int[] ghostX, int[] ghostY, int[] map) {
        this.X_TILES = X_TILES;
        this.Y_TILES = Y_TILES;
        SCREEN_HEIGHT = Y_TILES * TILE_SIZE;
        SCREEN_WIDTH = X_TILES * TILE_SIZE;
        //location of Pacman:
        this.pacmanX = pacmanX;
        this.pacmanY = pacmanY;
        //location of ghosts:
        this.ghostX = ghostX;
        this.ghostY = ghostY;
        this.ghostDX = new int[ghostX.length];
        this.ghostDY = new int[ghostY.length];

        this.map = new int[(X_TILES) * (Y_TILES)]; //map.length = rows*columns
        if (this.map.length == map.length)
            this.map = map;

        mapOrganizer = new int[Y_TILES][X_TILES];
        int index = 0;
        for (int i = 0; i < mapOrganizer.length; i++) {
            for (int j = 0; j < mapOrganizer[i].length; j++) {
                mapOrganizer[i][j] = this.map[index];
                index++;
            }
        }
        for (int i = 0; i < mapOrganizer.length; i++) {
            for (int j = 0; j < mapOrganizer[i].length; j++) {
                if (mapOrganizer[i][j] != 1) {  // i = y, j = x
                    int g = 0;
                    if (mapOrganizer[i][j] == 4)
                        mapOrganizer[i][j] = -32 + 16 + 4 + 2;
                    if (mapOrganizer[i][j] == 3)
                        mapOrganizer[i][j] = -32 + 16;
                    if (mapOrganizer[i][j] == 2) {
                        mapOrganizer[i][j] -= 34;
                    }
                    if (mapOrganizer[i][j + 1] == 1)
                        g += 2;
                    if (mapOrganizer[i][j - 1] == 1)
                        g += 4;
                    if (mapOrganizer[i - 1][j] == 1)
                        g += 8;
                    if (mapOrganizer[i + 1][j] == 1)
                        g += 16;
                    mapOrganizer[i][j] += g + 32;
                }
            }
        }
    }

    public String toString() {
        StringBuilder str1 = new StringBuilder();
        str1.append("the array is:\n");
        str1.append(Arrays.toString(map)).append(", ");
        str1.append("\nthe matrix is:\n");
        str1.append("   |");
        for (int i = 0; i < X_TILES; i++) {
            str1.append(i);
            if (i < 10)
                str1.append(" ");
            str1.append("|");
        }
        str1.append("\n");
        for (int i = 0; i < Y_TILES; i++) {
            str1.append(i).append(":");
            if (i < 10)
                str1.append(" ");
            str1.append("[");
            for (int j = 0; j < X_TILES; j++) {
                str1.append(mapOrganizer[i][j]);
                if (j < X_TILES - 1) {
                    if (mapOrganizer[i][j] / 10 == 0)
                        str1.append(" ");
                    str1.append(",");
                }
            }
            str1.append("]\n");
        }
        return str1.toString();
    }

    private void changePlace(int x, int y, String str, int index) {
        if (str.equalsIgnoreCase("pac")) {
            pacmanDX = x * TILE_SIZE;
            pacmanDY = y * TILE_SIZE;
        } else if (str.equalsIgnoreCase("ghost")) {
            ghostDX[index] = x * TILE_SIZE;
            ghostDY[index] = y * TILE_SIZE;
        }
    }

    private int[] binarySearch(int number) {
        // the array list must be binary
        int[] b = {0, 1, 2, 4, 8, 16, 32};
        int[] a = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            number = number - b[b.length - 1 - i];
            if (number < 0)
                number = number + b[b.length - 1 - i];
            else {
                a[b.length - 1 - i] = 1;
                if (number == 0)
                    break;
            }
        }
        return a;
    }

    // wall = 1
    // right border = 2
    // left border = 4
    // upper border = 8
    // bottom border = 16
    // points/dots = 32
    // example: 32+8+2 = 42
    private void sum_Num() {
        for (int[] ints : mapOrganizer) {
            for (int anInt : ints) {
                if (binarySearch(anInt)[6] == 1) {
                    num_Points += 10;
                }
            }
        }
    }

    private void moveRight(String str) { // change image

        for (int i = 0; i < ghostX.length; i++) {
            if (rP && str.equalsIgnoreCase("pac"))
                changePlace(1, 0, str, i);
            if (rG[i] && str.equalsIgnoreCase("ghost"))
                changePlace(1, 0, str, i);
        }
    }

    private void moveLeft(String str) {
        for (int i = 0; i < ghostX.length; i++) {
            if (lP && str.equalsIgnoreCase("pac"))
                changePlace(-1, 0, str, i);
            if (lG[i] && str.equalsIgnoreCase("ghost"))
                changePlace(-1, 0, str, i);
        }
    }

    private void moveUp(String str) {
        for (int i = 0; i < ghostX.length; i++) {
            if (uP && str.equalsIgnoreCase("pac"))
                changePlace(0, -1, str, i);
            if (uG[i] && str.equalsIgnoreCase("ghost"))
                changePlace(0, -1, str, i);
        }
    }

    private void moveDown(String str) {
        for (int i = 0; i < ghostX.length; i++) {
            if (dP && str.equalsIgnoreCase("pac"))
                changePlace(0, 1, str, i);
            if (dG[i] && str.equalsIgnoreCase("ghost"))
                changePlace(0, 1, str, i);
        }
    }

    private void movePacman() {
        if (qP == 0) {
            moveDown("pac");
            qP++;
        }
        pacmanX += pacmanDX;
        pacmanY += pacmanDY;

        xP += (pacmanDX) / TILE_SIZE;
        yP += (pacmanDY) / TILE_SIZE;
        if (xP != x1P || yP != y1P) {
            if (binarySearch(mapOrganizer[yP][xP])[2] == 1) { //can it move right?
                pacmanDX = 0;
                rP = false;

            } else if (binarySearch(mapOrganizer[yP][xP])[2] != 1) {
                rP = true;
            }
            if (binarySearch(mapOrganizer[yP][xP])[3] == 1) {  //can it move
                pacmanDX = 0;
                lP = false;
            } else if (binarySearch(mapOrganizer[yP][xP])[3] != 1) {
                lP = true;
            }
            if (binarySearch(mapOrganizer[yP][xP])[4] == 1) {
                pacmanDY = 0;
                uP = false;
            } else if (binarySearch(mapOrganizer[yP][xP])[4] != 1) {
                uP = true;
            }
            if (binarySearch(mapOrganizer[yP][xP])[5] == 1) {
                pacmanDY = 0;
                dP = false;
            } else if (binarySearch(mapOrganizer[yP][xP])[5] != 1) {
                dP = true;
            }
            if (binarySearch(mapOrganizer[yP][xP])[6] == 1) {
                mapOrganizer[yP][xP] -= 32;
                if (showInfo)
                    System.out.println(this);
                score += 10;
            }
            if (showInfo)
               System.out.println("pacman place (index): "+xP+", "+yP);
            x1P = xP;
            y1P = yP;
        }


    }

    private void moveGhost() {
        Random[] random = new Random[ghostX.length];
        for (int i = 0; i < ghostX.length; i++) {
            String g = "ghost";
            random[i] = new Random();
            if (random[i].nextInt(10) > 7) {
                int r = random[i].nextInt(4);
                switch (r) {
                    case 0:
                        moveRight(g);
                        break;
                    case 1:
                        moveLeft(g);
                        break;
                    case 2:
                        moveDown(g);
                        break;
                    case 3:
                        moveUp(g);
                        break;
                }
                if (qG[i] == 0) {
                    moveUp(g);
                    qG[i]++;
                }
                ghostX[i] += ghostDX[i];
                ghostY[i] += ghostDY[i];

                xG[i] += (ghostDX[i]) / TILE_SIZE;
                yG[i] += (ghostDY[i]) / TILE_SIZE;
                if (xG[i] != x1G[i] || yG[i] != y1G[i]) {
                    if (binarySearch(mapOrganizer[yG[i]][xG[i]])[2] == 1) {
                        ghostDX[i] = 0;
                        rG[i] = false;
                    } else if (binarySearch(mapOrganizer[yG[i]][xG[i]])[2] != 1) {
                        rG[i] = true;
                    }
                    if (binarySearch(mapOrganizer[yG[i]][xG[i]])[3] == 1) {
                        ghostDX[i] = 0;
                        lG[i] = false;
                    } else if (binarySearch(mapOrganizer[yG[i]][xG[i]])[3] != 1) {
                        lG[i] = true;
                    }
                    if (binarySearch(mapOrganizer[yG[i]][xG[i]])[4] == 1) {
                        ghostDY[i] = 0;
                        uG[i] = false;
                    } else if (binarySearch(mapOrganizer[yG[i]][xG[i]])[4] != 1) {
                        uG[i] = true;
                    }
                    if (binarySearch(mapOrganizer[yG[i]][xG[i]])[5] == 1) {
                        ghostDY[i] = 0;
                        dG[i] = false;
                    } else if (binarySearch(mapOrganizer[yG[i]][xG[i]])[5] != 1) {
                        dG[i] = true;
                    }
                    x1G[i] = xG[i];
                    y1G[i] = yG[i];
                }
            }
        }
    }

    private void checkCollision() {
        for (int i = 0; i < ghostX.length; i++) {
            if (pacmanX == ghostX[i] && pacmanY == ghostY[i]) {
                inGame = false;
                break;
            }
        }
    }
    private void drawWon(Graphics g){
        g.drawImage(win,  1,  1, SCREEN_WIDTH , SCREEN_HEIGHT , this);
    }
    private void drawPacman(Graphics g) {
        if (P[2])
            g.drawImage(right, pacmanX + 2, pacmanY + 2, TILE_SIZE - 1, TILE_SIZE - 1, this);
        if (P[0])
            g.drawImage(up, pacmanX + 2, pacmanY + 2, TILE_SIZE - 1, TILE_SIZE - 1, this);
        if (P[1]) {
            g.drawImage(down, pacmanX + 2, pacmanY + 2, TILE_SIZE - 1, TILE_SIZE - 1, this);
        }
        if (P[3])
            g.drawImage(left, pacmanX + 2, pacmanY + 2, TILE_SIZE - 1, TILE_SIZE - 1, this);
    }

    private void drawGhost(Graphics g) {
        for (int i = 0; i < ghostX.length; i++) {
            g.drawImage(ghost, ghostX[i] + 2, ghostY[i] + 2, TILE_SIZE - 1, TILE_SIZE - 1, this);
        }
    }

    private void drawScore(Graphics g) {
        StringBuilder s = new StringBuilder();
        s.append("Score: ").append(score);
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(String.valueOf(s), (SCREEN_HEIGHT - metr.stringWidth(String.valueOf(s))) / 2, SCREEN_WIDTH / 2);
    }
    private void drawWalls(Graphics g) {
        g.setColor(Color.blue);
        for (int i = 0; i < mapOrganizer.length; i++) {
            for (int j = 0; j < mapOrganizer[i].length; j++) {
                if (binarySearch(mapOrganizer[i][j])[1] == 1){
                    g.drawRoundRect(j * TILE_SIZE + 1, i * TILE_SIZE + 1, TILE_SIZE - 1, TILE_SIZE - 1, 6, 6);
                }
            }
        }
    }

    private void drawPoints(Graphics g) {


        g.setColor(Color.white);
        for (int i = 0; i < mapOrganizer.length; i++) {
            for (int j = 0; j < mapOrganizer[i].length; j++) {
                if (binarySearch(mapOrganizer[i][j])[6] == 1) {
                    g.fillOval((int) ((j + 0.3) * (TILE_SIZE) + 2), (int) ((i + 0.3) * (TILE_SIZE) + 2), TILE_SIZE / 3 - 1, TILE_SIZE / 3 - 1);
                }
            }
        }
    }

    private void drawBoard(Graphics g) {
        g.setColor(Color.green);
        for (int i = 0; i < SCREEN_HEIGHT; i += TILE_SIZE) {
            for (int j = 0; j < SCREEN_WIDTH; j += TILE_SIZE) {
                if (showInfo)
                    g.drawRoundRect(j+1,i+1,TILE_SIZE-1,TILE_SIZE-1,6,6);
            }
        }


        drawPoints(g);
        if (inGame)
            drawPacman(g);
        drawGhost(g);
        drawWalls(g);
        if (!inGame) {
            drawScore(g);
        }
        if (won) {
            drawWon(g);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
    }

    boolean won = false;
    @Override
    public void run() {
        while (true) {
            if (score == num_Points) {
                inGame = false;
                won = true;
            }
            if (inGame) {
                checkCollision();
                movePacman();
                moveGhost();
            }
            repaint();
            try {
                Thread.sleep(1000 / speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            String p = "pac";
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT) {
                moveLeft(p);
                if (lP) {
                    P[3] = true;
                    P[2] = false;
                    P[1] = false;
                    P[0] = false;
                }
            } else if (key == KeyEvent.VK_RIGHT) {
                moveRight(p);
                if (rP) {
                    P[2] = true;
                    P[3] = false;
                    P[1] = false;
                    P[0] = false;
                }
            } else if (key == KeyEvent.VK_UP) {
                moveUp(p);
                if (uP) {
                    P[0] = true;
                    P[3] = false;
                    P[1] = false;
                    P[2] = false;
                }
            } else if (key == KeyEvent.VK_DOWN) {
                moveDown(p);
                if (dP) {
                    P[1] = true;
                    P[3] = false;
                    P[0] = false;
                    P[2] = false;
                }
            }
        }
    }
}

