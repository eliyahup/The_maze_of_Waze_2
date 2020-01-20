package gameClient;


import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import algorithms.Graph_Algo;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

/**
 * This class save the game in kml file
 */
public class KML_Logger {
    static StringBuilder Kfile = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<kml xmlns=\"http://earth.google.com/kml/2.2\">\n<Document>\n<name>The maze of Waze 2</name>\n" +
            //"       <Style>\n" +
            "       <Style id=\"apple\">\n" +
            "   <IconStyle>\n" +
            "        <Icon>\n" +
            "          <href>https://user-images.githubusercontent.com/57434608/72728392-ed8f9f80-3b95-11ea-81e4-566409cc8961.png</href>\n" +
            "        </Icon>\n" +
            "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\n" +
            "      </IconStyle>\n" +
            "    </Style>\n" +
            "    <Style id=\"banana\">\n" +
            "      <IconStyle>\n" +
            "        <Icon>\n" +
            "          <href>https://user-images.githubusercontent.com/57434608/72728436-04ce8d00-3b96-11ea-9acb-fc4993d74103.png</href>\n" +
            "        </Icon>\n" +
            "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\n" +
            "      </IconStyle>\n" +
            "    </Style>\n" +
            "    <Style id=\"robot\">\n" +
            "      <IconStyle>\n" +
            "        <Icon>\n" +
            "          <href>https://user-images.githubusercontent.com/57434608/72728463-16179980-3b96-11ea-80a8-1525c4977bb3.png</href>\n" +
            "        </Icon>\n" +
            "        <hotSpot x=\"0\" y=\".5\" xunits=\"fraction\" yunits=\"fraction\"/>\n" +
            "      </IconStyle>\n" +
            "    </Style>\n" +
            "    <Style id=\"check-hide-children\">\n" +
            "      <ListStyle>\n" +
            "        <listItemType>checkHideChildren</listItemType>\n" +
            "      </ListStyle>\n" +
            "    </Style>\n");
    private static int scenarioInt;
    static RunGame rg = new RunGame(scenarioInt);


    /**
     * This is aMethod that can be clled fron anywhere and can save the data into a KML file
     */
    public static int KML_Save(Scenario s) {
        try {

            /**
             * Restarting the data needed to the KML file
             */
            scenarioInt = s.num;
            List<String> robots = s.game.getRobots();
            List<String> fruits = s.game.getFruits();
            graph gra = s.gr;
            Graph_Algo ga = new Graph_Algo();
            ga.init(gra);
            Date date = new Date();
            DateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat hms = new SimpleDateFormat("HH:mm:ss");
            String year = ymd.format(date);
            String hour = hms.format(date);
            String fDate = year + "T" + hour + "Z";
            /**
             * Building the graph to the KML Language
             */
            for (node_data nd : gra.getV()) {
                Kfile.append("<Placemark>\n<description>\nLocation num:" + nd.getKey() + "\n</description>\n<TimeStamp>\n<when>" + fDate + "</when>\n</TimeStamp>\n<Point>\n<coordinates> " + nd.getLocation().x() + "," + nd.getLocation().y() + "\n</coordinates>\n</Point>\n</Placemark>\n");
                for (edge_data ed : gra.getE(nd.getKey())) {
                    Point3D psrc = gra.getNode(ed.getSrc()).getLocation();
                    Point3D pdest = gra.getNode(ed.getDest()).getLocation();
                    Kfile.append("\n" +
                            "<Placemark> \n" +
                            " <LineString>\n" +
                            "  <coordinates>" +
                            psrc.x() + "," + psrc.y() + ",0\t"
                            + pdest.x() + "," + pdest.y() + ",0" +
                            "  </coordinates>\n" +
                            " </LineString>\n" + "<Style>\n<LineStyle>\n <color>#ff0000ff</color> \n<width>5</width> \n</LineStyle> \n</Style>\n</Placemark>\n");
                }

            }
/**
 * Saving the robots and fruits to the KML
 */
            while (rg.scenario.game.isRunning()) {

                robots = rg.scenario.game.getRobots();
                fruits = rg.scenario.game.getFruits();


                Iterator it = robots.iterator();
                while (it.hasNext()) {
                    Robot r = new Robot((String) it.next());
                    Kfile.append("<Placemark>\n<description> Robot id:" + r.getId() + "\n</description>\n<TimeStamp>\n<when>" + fDate + "</when>\n</TimeStamp>\n<styleUrl>#robot</styleUrl>\n<Point>\n<coordinates>" + r.getPos().x() + "," + r.getPos().y() + "</coordinates>\n</Point>\n\"</Placemark>\\n\"");
                     date = new Date();
                     ymd = new SimpleDateFormat("yyyy-MM-dd");
                     hms = new SimpleDateFormat("HH:mm:ss");
                     year = ymd.format(date);
                     hour = hms.format(date);
                     fDate = year + "T" + hour + "Z";
                }
                Iterator it2 = fruits.iterator();
                while (it2.hasNext()) {
                    Fruit f = new Fruit((String) it2.next());
                    if (f.getType() > 0) {             //apple
                        Kfile.append("<Placemark>\n<description> Apple value:" + f.getValue() + "\n</description> \n<TimeStamp>\n<when> " + fDate + "</when>\n</TimeStamp>\n<styleUrl>#apple</styleUrl>\n<Point>\n<coordinates>" + f.getPos().x() + "," + f.getPos().y() + "</coordinates>\n</Point>\n");
                        Kfile.append("</Placemark>\n");
                    } else {                           //banana
                        Kfile.append("<Placemark>\n<description> Banana value:" + f.getValue() + "\n</description>\n<TimeStamp>\n<when> " + fDate + "</when>\n</TimeStamp>\n<styleUrl>#banana</styleUrl>\n<Point>\n<coordinates>" + f.getPos().x() + "," + f.getPos().y() + "</coordinates>\n</Point>\n");
                        Kfile.append("</Placemark>\n");

                    }

                }
                try {
                    Thread.sleep(500);                 //1000 milliseconds is one second.
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            //    rg.scenario.game.move();
            }
            Kfile.append("</Document>\n</kml>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saveFile();
    }

    /**
     * The actual method that writes the data into a file
     */
    private static int saveFile() {

        int flag;
        try {

            FileWriter writer = new FileWriter(scenarioInt + ".kml", true);
            writer.write(String.valueOf(Kfile));
            writer.close();
            flag = 1;
        } catch (Exception e) {
            e.printStackTrace();
            flag = -1;
        }

        return flag;
    }
}
