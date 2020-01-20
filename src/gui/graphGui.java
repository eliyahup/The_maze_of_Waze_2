package gui;

import algorithms.Graph_Algo;
import dataStructure.*;
import utils.Point3D;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

public class graphGui extends JFrame implements Serializable, MouseListener, MouseMotionListener {
    graph gra = new DGraph();                   //GUI elements
    Graph_Algo ga = new Graph_Algo();
    JMenuBar menuFrame;
    JMenu fileMenu, AlgorithmsMenu;
    JMenuItem openItem, createItem, saveItem, ShortestPathLengthItem, ShortestPathRouteItem, TravelSalesmanProblemItem, IsConnectedItem, savePngItem, generateItem;

    LinkedList<Point3D> points = new LinkedList<Point3D>();         //Compilations elements
    Point3D mPivot_point = null;
    boolean mDraw_pivot = false;
    boolean mMoving_point = false;
    boolean mCreate = false;
    private int kRADIUS = 5;
    int lastKey = 0;
    boolean generate = false;


    public graphGui() {                                       //starting without a loaded graph
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());
        this.setBounds(500, 500, 600, 600);
        this.setTitle("The maze of Waze");
        initComponents();
        actionsGui();
        this.setVisible(true);
    }

    public graphGui(graph g) {                              //starting with a loaded graph
        this.setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());
        this.setBounds(500, 500, 600, 600);
        this.setTitle("The maze of Waze");
        this.gra = g;
        this.ga.init(g);
        Collection<node_data> nd = this.gra.getV();
        Iterator<node_data> it = nd.iterator();             //painting the loaded graph
        while (it.hasNext()) {
            points.add(it.next().getLocation());
        }

        repaint();
        initComponents();
        actionsGui();


    }



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

        createItem = new JMenuItem("Create");
        createItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        fileMenu.add(createItem);

        generateItem = new JMenuItem("Generate");
        generateItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        fileMenu.add(generateItem);

        menuFrame.add(fileMenu);

        // set algorithms menu
        AlgorithmsMenu = new JMenu("Algorithms");
        ShortestPathLengthItem = new JMenuItem("Shortest Path Length");
        ShortestPathLengthItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK));
        AlgorithmsMenu.add(ShortestPathLengthItem);

        ShortestPathRouteItem = new JMenuItem("Shortest Path Route");
        ShortestPathRouteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.CTRL_MASK));
        AlgorithmsMenu.add(ShortestPathRouteItem);

        TravelSalesmanProblemItem = new JMenuItem("Travel Salesman Problem");
        TravelSalesmanProblemItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.CTRL_MASK));
        AlgorithmsMenu.add(TravelSalesmanProblemItem);

        IsConnectedItem = new JMenuItem("Is connected?");
        IsConnectedItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.CTRL_MASK));
        AlgorithmsMenu.add(IsConnectedItem);

        menuFrame.add(AlgorithmsMenu);
        if (mCreate) {                          //if the user chose create option then the mouse listener will come to live
            this.ga.init(this.gra);

            this.addMouseListener(this);
            this.addMouseMotionListener(this);
        }
    }

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
        createItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mCreate = true;
                initComponents();
                actionsGui();
            }

        });
        generateItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buildGraph();
                generate = true;
                repaint();
                generate = false;
                actionsGui();
            }
        });
        // Algorithms menu actions
        ShortestPathLengthItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shortPathLeng();
                actionsGui();
            }
        });
        ShortestPathRouteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shortPathList();
                actionsGui();
            }
        });
        TravelSalesmanProblemItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TSPgui();
                actionsGui();
            }
        });
        IsConnectedItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isconnected();
                actionsGui();
            }
        });
    }

    private void savePng() {                            //save the graph as png
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

    private void isconnected() {                       //check if graph is connected
        ga.init(this.gra);
        boolean connect = false;
        connect = this.ga.isConnected();
        JFrame j = new JFrame();
        if (connect) {
            JOptionPane.showMessageDialog(j, "Graph is connected");
            j.setDefaultCloseOperation(3);
        } else {
            JOptionPane.showMessageDialog(j, "Graph is not connected");
            j.setDefaultCloseOperation(3);
        }
        actionsGui();
    }

    //all the mouse options are for the create option
    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (mDraw_pivot) {
            mPivot_point.setX(x);
            mPivot_point.setY(y);

            repaint();
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (mDraw_pivot) {
            mPivot_point.setX(x);
            mPivot_point.setY(y);

            repaint();
        }
        actionsGui();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        Point3D tmp = new Point3D(x, y);
        this.mPivot_point = tmp;
        int key;
        if (this.gra != null) {
            key = this.gra.nodeSize() + 1;
        } else {
            key = 0;
        }
        nodeData nd = new nodeData(tmp, key, 0.0, "", 0);
        this.gra.addNode(nd);
        int min_dist = (int) (kRADIUS * 1.5);
        double best_dist = 1000000;
        for (Point3D p : points) {
            double dist = tmp.distance3D(p);
            if (dist < min_dist && dist < best_dist) {
                mPivot_point = p;
                best_dist = dist;
                mMoving_point = true;
            }
        }

        if (mPivot_point == null) {
            mPivot_point = new Point3D(x, y);
        } else {
            if (isExist(tmp)) {
                mPivot_point = tmp;
                //      points.add(tmp);
                int sr = this.gra.nodeSize() - 1;
                int de = this.lastKey;
                double dist = this.gra.getNode(sr).getLocation().distance3D(tmp);
                gra.connect(sr, de, dist);
            }
        }

        mDraw_pivot = true;
        repaint();


    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!mMoving_point && !isExist(mPivot_point)) {
            points.add(new Point3D(mPivot_point));
            repaint();
        }
        mMoving_point = false;
        mPivot_point = null;
        mDraw_pivot = false;
    }

    private void shortPathLeng() {                           //shortest path length execution
        this.ga.init(this.gra);
        JFrame f = new JFrame("Shortest Path Dist");
        String src = JOptionPane.showInputDialog(f, "Enter src: ");
        String dest = JOptionPane.showInputDialog(f, "Enter dest: ");
        if (src != null && dest != null) {
            double answer = this.ga.shortestPathDist(Integer.parseInt(src), Integer.parseInt(dest));
            if(answer<0){JOptionPane.showMessageDialog(f, "Error there is no path, or no kind of vertices in graph");}
            else{JOptionPane.showMessageDialog(f, "The shortest dist between " + src + " and " + dest + " is: " + answer);}
            f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
        initComponents();
    }

    private void TSPgui() {                                 //TSP between two stops
        try {
            this.ga.init(this.gra);
            JFrame f = new JFrame("Shortest Path through the selected ");
            String sto = JOptionPane.showInputDialog(f, "Enter stop: ");
            int stop = Integer.parseInt(sto);
            List<Integer> lnd = new ArrayList<>();
            lnd.add(stop);
            boolean flag = true;
            while (flag) {
                sto = JOptionPane.showInputDialog(f, "Enter stops: \nExit type 'end'");
                if (sto.equals("end")){
                    sto.toLowerCase();
                    break;
                }
                stop = Integer.parseInt(sto);
                lnd.add(stop);

            }
            if (lnd.get(0) != null && lnd.get(lnd.size() - 1) != null) {
                List<node_data> answer = this.ga.TSP(lnd);
                Iterator<node_data> it = answer.iterator();
                StringBuilder path = new StringBuilder();
                while (it.hasNext()) {
                    path.append("->");
                    path.append(it.next().getKey());
                }
                JOptionPane.showMessageDialog(f, "The TSP  soultion between " + answer.get(0).getKey() + " and " + answer.get(answer.size()-1).getKey() + " is: " + path);
                f.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("There is no route between the two");
        }


        initComponents();
    }

    private void shortPathList() {                                          //shortest path list in between two stops
        this.ga.init(this.gra);
        JFrame f = new JFrame("Shortest Path through the selected ");
        String src = JOptionPane.showInputDialog(f, "Enter src: ");
        String dest = JOptionPane.showInputDialog(f, "Enter dest: ");
        try {
            if (src != null && dest != null) {
                List<node_data> answer = this.ga.shortestPath(Integer.parseInt(src), Integer.parseInt(dest));
                Iterator<node_data> it = answer.iterator();
                StringBuilder path = new StringBuilder();
                while (it.hasNext()) {
                    path.append("->");
                    path.append(it.next().getKey());
                }
                JOptionPane.showMessageDialog(f, "The shortest path between " + src + " and " + dest + " is: " + path);
                f.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        } catch (Exception ex) {
            System.out.println("There is no route between the two");
            ex.printStackTrace();

        }
        initComponents();
    }

    public void readFileDialog() {                                      //load graph from file
        FileDialog fd = new FileDialog(this, "Open text file", FileDialog.LOAD);
        fd.setFile("*.txt");
        fd.setDirectory("C:\\");
        fd.setFilenameFilter((dir, name) -> name.endsWith(".txt"));
        fd.setVisible(true);
        String fileName = fd.getFile();
        //      DGraph dg;
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
        }catch(Exception ex){
            System.out.println("No file was loaded");
        }
    }

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

    public void paint(Graphics g) {                                 //swing implementation of paint method

        super.paint(g);
        Collection<node_data> ver = this.gra.getV();
        Iterator<node_data> it = ver.iterator();
        Point3D prev = null;
        int i = 0;
        while (it.hasNext()) {
            node_data nd = it.next();
            Point3D p = nd.getLocation();


            g.setColor(Color.BLUE);
            g.fillOval((int) p.x() - kRADIUS, (int) p.y() - kRADIUS,
                    2 * kRADIUS, 2 * kRADIUS);
            g.setColor(Color.BLACK);
            g.drawString(Integer.toString(nd.getKey()), (int) p.x(), (int) p.y());

            Iterator<edge_data> it1 = this.gra.getE(nd.getKey()).iterator();
            while (it1.hasNext()) {
                edge_data ed = it1.next();
                Point3D psrc = this.gra.getNode(ed.getSrc()).getLocation();
                Point3D pdest = this.gra.getNode(ed.getDest()).getLocation();

                g.setColor(Color.YELLOW);
                g.fillOval((int) (Math.round(((psrc.x() * (0.1) + pdest.x() * (0.9)))) - kRADIUS),
                        (int) (Math.round((psrc.y() * (0.1) + pdest.y() * (0.9))) - kRADIUS), 2 * kRADIUS, 2 * kRADIUS);

                g.setColor(Color.RED);
                g.drawLine((int) pdest.x(), (int) pdest.y(), (int) psrc.x(), (int) psrc.y());

                double dist = this.gra.getEdge(ed.getSrc(),ed.getDest()).getWeight();
                g.drawString(String.format("%.2f", dist),
                        (int) ((pdest.x() + psrc.x()) / 2),
                        (int) ((pdest.y() + psrc.y()) / 2));
                i++;

            }
            if (mCreate) {
                if (prev != null) {
                    g.setColor(Color.YELLOW);
                    g.fillOval((int) (Math.round(((prev.x() * (0.1) + p.x() * (0.9)))) - kRADIUS),
                            (int) (Math.round((prev.y() * (0.1) + p.y() * (0.9))) - kRADIUS), 2 * kRADIUS, 2 * kRADIUS);

                    g.setColor(Color.RED);
                    g.drawLine((int) p.x(), (int) p.y(), (int) prev.x(), (int) prev.y());

                    double dist = prev.distance3D(p);
                    g.drawString(String.format("%.2f", dist),
                            (int) ((p.x() + prev.x()) / 2),
                            (int) ((p.y() + prev.y()) / 2));
                }
            }

            if (mCreate) prev = p;

        }

        if (mDraw_pivot && !mMoving_point && mCreate) {                                            //create option when moving a point3d
            g.setColor(Color.BLUE);
            g.fillOval((int) mPivot_point.x() - kRADIUS, (int) mPivot_point.y() - kRADIUS,
                    2 * kRADIUS, 2 * kRADIUS);
            if (prev != null) {
                g.setColor(Color.RED);

                g.drawLine((int) mPivot_point.x(), (int) mPivot_point.y(),
                        (int) prev.x(), (int) prev.y());
                g.setColor(Color.GRAY);
                double dist = prev.distance3D(mPivot_point);
                g.drawString(String.format("%.2f", dist), (int) ((mPivot_point.x() + prev.x()) / 2), (int) ((mPivot_point.y() + prev.y()) / 2));
            }

        }
        if (!generate && mCreate) {                                    //if creating a graph or loading one doesn't need to save the graph type
            int i1 = 1;
            double wei = 0;
            Point3D pre = null;
            for (Point3D p : points) {
                if (pre != null && i1 != points.size()) {
                    wei = pre.distance3D(p);
                    int b = i1 - 1;
                    gra.connect(b, i1, wei);
                }

                pre = p;
                ++i1;
            }
        }
    }

    private boolean isExist(Point3D p) {                    //check if the point is already in graph
        int count = 1;
        for (Point3D p1 : points) {
            if (p1.close2equals(p)) {
                this.lastKey = count;
                return true;
            }
            if (p1.equalsXY(p)) return true;
            count++;
        }
        return false;
    }
    public void buildGraph() {                      //generate graph

        generate = true;
        for (int i = 0; i < 100; i++) {
            Point3D  pi = new Point3D(Math.random() * 700 + 10, Math.random() * 500 + 100, 0);
            nodeData ti = new nodeData(pi,i,0.0,null,0);
            this.gra.addNode(ti);
        }
        for (int i = 0; i < 250; i++) {
            this.gra.connect((int)(Math.random() * 100), (int)(Math.random() * 100),(int)(Math.random() * 20));
        }

    }
    public void buildGraph(graph G) {                             //generate option to create graph

        generate = true;
        Iterator<node_data> it = G.getV().iterator();
        while(it.hasNext()) {
            this.points.add(it.next().getLocation());
        }
        this.gra=G;

}


    public static void main(String[] args) {

        graphGui Menu = new graphGui();

    }

}


