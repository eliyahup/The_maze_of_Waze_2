package gameClient;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.*;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

import dataStructure.DGraph;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Range;

import static gameClient.KML_Logger.KML_Save;

public class MyGameGUI extends JFrame implements MouseListener {

    graph gra = new DGraph();                   //GUI elements
    Scenario s;
    Graph_Algo ga = new Graph_Algo();
    JMenuBar menuFrame;
    JMenu fileMenu, robotsMenu;
    JMenuItem openItem, saveKmlItem, saveItem, automaticItem, manualItem, savePngItem;

    LinkedList<Point3D> points = new LinkedList<Point3D>();         //Compilations elements
    private int kRADIUS = 5;
    game_service game;
    List<String> fruits = new ArrayList<>();
    List<String> robots = new ArrayList<>();
    int players;
    HashMap<Point3D, Integer> path = new HashMap<>();
    Integer id = null;
    Thread thread;
    public static final double EPS = 0.0001;
    private Range rx = new Range(Integer.MAX_VALUE, Integer.MIN_VALUE);
    private Range ry = new Range(Integer.MAX_VALUE, Integer.MIN_VALUE);
    int scenario;
    RunGame rg;
    boolean Auto;
    boolean Manu;
    boolean EndAuto;
    boolean EndManu;
    node_data pick;


    /**
     * constructor for gui without any parameter
     */

    public MyGameGUI() {                                       //starting without a loaded graph
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());
        this.setBounds(500, 500, 600, 600);
        this.setTitle("The maze of Waze 2");
        initComponents();
        actionsGui();
        this.setVisible(true);
        myGame();


    }

    /**
     * constructor for gui with graph parameter
     */

    public MyGameGUI(graph g) {                              //starting with a loaded graph
        myGame();
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        this.setLayout(new FlowLayout());
        this.setBounds(500, 500, 600, 600);
        this.setTitle("The maze of Waze 2");
        this.gra = g;
        this.ga.init(g);
        Collection<node_data> nd = this.gra.getV();
        Iterator<node_data> it = nd.iterator();             //painting the loaded graph
        while (it.hasNext()) {
            points.add(it.next().getLocation());
        }
        actionsGui();


    }

    /**
     * initiate the GUI components:
     * File tab
     * Driving robots tab
     */

    public void initComponents() {                          //creating the tabs of the GUI


        menuFrame = new JMenuBar();               //set menu bar
        setJMenuBar(menuFrame);
        menuFrame.setVisible(true);

        //set file menu	with open, save
        fileMenu = new JMenu("File");

        //keyboard shortcuts
        openItem = new JMenuItem("Open");
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        fileMenu.add(openItem);

        saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        fileMenu.add(saveItem);

        savePngItem = new JMenuItem("Save as Png");
        savePngItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        fileMenu.add(savePngItem);

        saveKmlItem = new JMenuItem("Save as KML");
        saveKmlItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.CTRL_MASK));
        fileMenu.add(saveKmlItem);

        menuFrame.add(fileMenu);

        // set algorithms menu
        robotsMenu = new JMenu("Robots");
        manualItem = new JMenuItem("Manual driving");
        manualItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK));
        robotsMenu.add(manualItem);

        automaticItem = new JMenuItem("Automatic driving");
        automaticItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.CTRL_MASK));
        robotsMenu.add(automaticItem);

        menuFrame.add(robotsMenu);
    }

    /**
     * Activating the Functions in each tab
     * Open graph
     * save graph
     * save png of graph
     * save KMl to file
     */

    public void actionsGui() {                  //After user chose an option the GUI option will execute the option here

        openItem.addActionListener(new ActionListener() {        //File menu actions
            @Override
            public void actionPerformed(ActionEvent e) {
                readFileDialog();
            }
        });
        saveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                writeFileDialog();
                actionsGui();
            }
        });
        savePngItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                savePng();
                actionsGui();
            }
        });
        saveKmlItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SaveKML();
            }

        });


        /**
         * Activating the funcions of driving robots
         *  drive robot manual
         *  drive robot automatic
         *
         */

        automaticItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                automaticDrive();
                actionsGui();
            }
        });
        manualItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manualDrive();
                actionsGui();
            }
        });
    }

    /**
     * Save KML file using KML logger
     */
    private void SaveKML() {
        for (int i = 0; i <= 23; i++) {
            Scenario s = new Scenario(i); // you have [0,23] games
            KML_Save(s);
        }
    }

    /**
     * Activating 'drive robot manual'
     */
    private void manualDrive() {
        try {
            Manu = true;
            s.game.startGame();
            manuDrive();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Activating 'drive robot automatic'
     */
    private void automaticDrive() {
        Auto = true;
        rg = new RunGame(scenario);
        game = rg.scenario.game;
        ThreadPaintAuto(rg.scenario.game);

    }

    /**
     * Saving the graph/status as png file
     */
    private void savePng() {
        try {
            BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight() + 45, BufferedImage.TYPE_INT_RGB);
            Graphics g = img.getGraphics();
            paint(g);
            ImageIO.write(img, "png", new File("Graph.png"));
            JFrame j = new JFrame();
            JOptionPane.showMessageDialog(j, "Graph is saved,now the GUI will close");
            j.setDefaultCloseOperation(3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * load graph using serializable number from txt file
     */
    public void readFileDialog() {
        FileDialog fd = new FileDialog(this, "Open text file", FileDialog.LOAD);
        fd.setFile("*.txt");
        fd.setDirectory("C:\\");
        fd.setFilenameFilter((dir, name) -> name.endsWith(".txt"));
        fd.setVisible(true);
        String fileName = fd.getFile();
        try {
            FileInputStream file = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(file);

            this.gra = (graph) in.readObject();
            ga.init(gra);
            repaint();
            in.close();
            file.close();

            System.out.println("Object has been deserialized");

        } catch (IOException | ClassNotFoundException ex) {
            System.out.print("Error reading file " + ex);
            System.exit(2);
        } catch (Exception ex) {
            System.out.println("No file was loaded");
        }
    }

    /**
     * Save graph using serializable number to txt file
     */
    public void writeFileDialog() {                                                   //write the graph into a file using serializable
        FileDialog fd = new FileDialog(this, "Save the text file", FileDialog.SAVE);
        fd.setFile("*.txt");
        fd.setFilenameFilter((dir, name) -> name.endsWith(".txt"));
        fd.setVisible(true);
        String fileName = fd.getFile();
        graph g = this.gra;
        try {
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(g);

            file.close();
            out.close();

        } catch (IOException ex) {
            System.out.print("Error writing file  " + ex);
            System.exit(2);
        } catch (NullPointerException ex) {
            JFrame f = new JFrame();
            JOptionPane.showMessageDialog(f, "The graph was saved");
            f.setDefaultCloseOperation(EXIT_ON_CLOSE);
            System.exit(-1);
        }

    }

    /**
     * coordinate to be scaled to x range
     */

    public double rangeX(double d) {
        double outX = (d - rx.get_min()) / (rx.get_max() - rx.get_min());
        double x = 100 * (12 * outX + 1);
        return x;
    }

    /**
     * coordinate to be scaled to y range
     */
    public double rangeY(double d) {
        double outY = (d - ry.get_min()) / (ry.get_max() - ry.get_min());
        double y = 400 * (1 - outY) + 100;
        return y;
    }

    /**
     * Double buffer for the paint function
     */
    @Override
    public void paint(Graphics g) {
        Image img = createImage(1300, 700);
        Graphics gImg = img.getGraphics();
        paintComponents(gImg);
        g.drawImage(img, 0, 0, this);

    }

    /**
     * paint the graph using oval for nodes(vertices) and lines as edges,
     * fruits as targets(apple means uphill and banana means downhill)
     * the robots(androids) mission is to collect the fruits
     */
    @Override
    public void paintComponents(Graphics g) {                                 //swing implementation of paint method
        super.paint(g);
        Collection<node_data> ver = this.gra.getV();
        Iterator<node_data> it = ver.iterator();
        while (it.hasNext()) {
            node_data nd = it.next();
            Point3D p = nd.getLocation();

            g.setColor(Color.BLUE);
            g.fillOval(((int) (rangeX(p.x()) - kRADIUS)), ((int) (rangeY(p.y()) - kRADIUS)),
                    2 * kRADIUS, 2 * kRADIUS);
            g.setColor(Color.BLACK);
            g.drawString(Integer.toString(nd.getKey()), (int) (rangeX(p.x())), -10 + (int) (rangeY(p.y())));
            path.put(p, nd.getKey());

            Iterator<edge_data> it1 = this.gra.getE(nd.getKey()).iterator();
            while (it1.hasNext()) {
                edge_data ed = it1.next();
                Point3D psrc = this.gra.getNode(ed.getSrc()).getLocation();
                Point3D pdest = this.gra.getNode(ed.getDest()).getLocation();

                g.setColor(Color.YELLOW);
                g.fillOval((int) (Math.round(((rangeX(psrc.x()) * (0.1) + rangeX(pdest.x()) * (0.9)))) - kRADIUS),
                        (int) (Math.round((rangeY(psrc.y()) * (0.1) + rangeY(pdest.y()) * (0.9))) - kRADIUS), 2 * kRADIUS, 2 * kRADIUS);

                g.setColor(Color.RED);
                g.drawLine((int) rangeX(pdest.x()), (int) rangeY(pdest.y()), (int) rangeX(psrc.x()), (int) rangeY(psrc.y()));

                double dist = this.gra.getEdge(ed.getSrc(), ed.getDest()).getWeight();
                g.drawString(String.format("%.2f", dist),
                        (int) (Math.round(((rangeX(psrc.x()) * (0.3) + rangeX(pdest.x()) * (0.7)))) - kRADIUS),
                        (int) (Math.round((rangeY(psrc.y()) * (0.3) + rangeY(pdest.y()) * (0.7))) - kRADIUS));

            }


        }
/**
 *
 *   print robots
 */
        for (int i = 0; i < players; i++) {
            try {
                String robot_json = robots.get(i);
                JSONObject line = new JSONObject(robot_json);
                JSONObject ttt = line.getJSONObject("Robot");
                String pos = ttt.getString("pos");
                int rid = ttt.getInt("id");
                this.id = rid;

                Point3D pBefore = new Point3D(pos);
                Point3D pAfter = new Point3D(rangeX(pBefore.x()), rangeY(pBefore.y()));

                BufferedImage rob = ImageIO.read(new File("rob.png"));
                Image newRob = rob.getScaledInstance(20, 20, Image.SCALE_DEFAULT);
                g.drawImage(newRob, (int) pAfter.x() - kRADIUS, (int) pAfter.y() - kRADIUS, null);
                //  }

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        /**
         *
         *   print fruits
         */
        for (int i = 0; i < fruits.size(); i++) {
            try {
                String fruit_json = fruits.get(i);
                JSONObject line = new JSONObject(fruit_json);
                JSONObject f = line.getJSONObject("Fruit");
                String pos = f.getString("pos");
                int type = f.getInt("type");
                Point3D pBefore = new Point3D(pos);
                Point3D pAfter = new Point3D(rangeX(pBefore.x()), rangeY(pBefore.y()));

                BufferedImage apple = ImageIO.read(new File("apple2.png"));
                Image newApple = apple.getScaledInstance(20, 20, Image.SCALE_DEFAULT);
                BufferedImage banana = ImageIO.read(new File("banana.png"));
                Image newBanana = banana.getScaledInstance(20, 20, Image.SCALE_DEFAULT);
                if (type > 0) {           //apple = 1
                    g.drawImage(newApple, (int) pAfter.x() - kRADIUS, (int) pAfter.y() - kRADIUS, null);
                } else {               //banana = -1
                    g.drawImage(newBanana, (int) pAfter.x() - kRADIUS, (int) pAfter.y() - kRADIUS, null);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        /**
         *   print seconds left till game ends
         */
        try {
            if (Auto) {
                String time = "Time till game Over: " + rg.scenario.game.timeToEnd() / 1000 + "";
                g.drawString(time, 650, 75);
            }
            if (Manu) {
                String time = "Time till game Over: " + s.game.timeToEnd() / 1000 + "";
                g.drawString(time, 650, 75);
            }
            /**
             *   print game score
             */
            if (EndAuto) {
                String end = "game Over: " + rg.scenario.game.toString();
                g.drawString(end, 450, 50);
            }
            if (EndManu) {
                String end = "game Over: " + s.game.toString();
                g.drawString(end, 450, 50);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Thread to print the movement of the robot every few milliseconds 'Auto thread'
     * different threads because auto run with a special scenario background support.
     */
    public void ThreadPaintAuto(game_service game) {

        thread = new Thread(() -> {
            while (rg.scenario.game.isRunning()) {
                try {
                    //          rg.scenario.game.move();
                    robots = rg.scenario.game.getRobots();
                    fruits = rg.scenario.game.getFruits();

                    Thread.sleep(100);
                    repaint();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            EndAuto = true;
            repaint();
            thread.interrupt();
        });

        thread.start();
    }

    /**
     * Thread to print the movement of the robot every few milliseconds 'Manual thread'
     */
    public void ThreadPaintManu(game_service game) {

        thread = new Thread(() -> {
            while (s.game.isRunning()) {
                try {
                    s.game.move();
                    robots = s.game.getRobots();
                    fruits = s.game.getFruits();
                    Thread.sleep(1000);
                    repaint();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            EndManu = true;
            repaint();
            thread.interrupt();
        });

        thread.start();
    }

    /**
     * Initiate the scenario of the game
     */
    public void myGame() {

        int scenario_num = Integer.parseInt(JOptionPane.showInputDialog("Enter scenario number between 0-23"));
        this.scenario = scenario_num;
        s = new Scenario(scenario);
        robots = s.game.getRobots();
        fruits = s.game.getFruits();
        this.gra = s.gr;
        this.ga.init(this.gra);
        game = s.game;
        players = s.robot.size();

/**
 *   Update nodes to gui window
 */
        Collection<node_data> c = this.gra.getV();
        Iterator<node_data> itrV = c.iterator();
        while (itrV.hasNext()) {
            node_data n = itrV.next();
            Point3D p = n.getLocation();
            double x = p.x();
            double y = p.y();
            if (x < rx.get_min())
                rx.set_min(x);
            else if (x > rx.get_max())
                rx.set_max(x);
            if (y < ry.get_min())
                ry.set_min(y);
            else if (y > ry.get_max())
                ry.set_max(y);
        }

    }

    /**
     * Manual drive of the robots using the input window
     */
    public void manuDrive() {
        s.game.startGame();
        ThreadPaintManu(s.game);
        this.addMouseListener(this);
        while (s.game.isRunning()) {
            List<String> log = s.game.move();
            if (log != null) {
                for (int i = 0; i < log.size(); i++) {
                    String robot_json = log.get(i);
                    try {
                        JSONObject line = new JSONObject(robot_json);
                        JSONObject ttt = line.getJSONObject("Robot");
                        int rid = ttt.getInt("id");
                        String des = JOptionPane.showInputDialog(rootPane, "Enter destination");
                        if (des != null) {
                            int dest = Integer.parseInt(des);
                            //   if(pick!=null) {
                            game.chooseNextEdge(rid, dest);
                            //                    s.game.move();
                        }
                        //     }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (!s.game.isRunning()) Manu = false;
        if (!s.game.isRunning()) this.removeMouseListener(this);
    }


    public static void main(String[] args) {
        MyGameGUI mg = new MyGameGUI();

    }

    /**
     * Manual drive of the robots using the mouse clicks +++needs more work
     */
    @Override
    public void mouseClicked(MouseEvent e) {


        try {
            int x = e.getX();
            int y = e.getY();
            Point3D p3 = new Point3D(x, y, 0);
            double min_dist = (kRADIUS * 3);
            double best_dist = 100000;

            Collection<node_data> ver = gra.getV();
            Iterator<node_data> itr = ver.iterator();
            while (itr.hasNext()) {
                node_data n = itr.next();
                Point3D p = n.getLocation();
                double x1 = (rangeX(p.x()));
                double y1 = (rangeY(p.y()));
                Point3D pTemp = new Point3D(x1, y1, 0);
                double dist = pTemp.distance2D(p3);
                if (dist < min_dist && dist < best_dist) {
                    best_dist = dist;
                    pick = n;
                }
            }


        } catch (Exception ex) {
            System.out.println("There is no route between the two");
            ex.printStackTrace();

        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}



