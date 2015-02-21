package Model;

import javafx.collections.ObservableList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Model handles data from text files and stores them in LineObjects
 * Created by TVANO on 07/02/15.
 */
public class Model {

    /**
     * Reads a file and stores LineObjects holding coordinates in obsList used in Controller class
     * Credits for iteration method: Troels Sørensen
     * @param filename - file holding coordinates 
     * @return
     */
    public ObservableList<Line2D> readFile(File filename, ObservableList obsList) {
        
        
        // Reads tags from XML
        class OSMHandler extends DefaultHandler {

            Map<Long, Point2D> map = new HashMap<>(); // Used to store coords for later use 
            Point2D last = null; // Most recent coordinate
            
            public void startElement(String uri, String localName, String qName, Attributes atts) {
                
                if(qName.equals("node")) {
                    double lat = Double.parseDouble(atts.getValue("lat")); // Read lat attribute and parse 
                    double lon = Double.parseDouble(atts.getValue("lon"));
                    long id = Long.parseLong(atts.getValue("id")); // Store unique id for each coord 
                    Point2D coord = new Point2D.Double(lat, lon); // Create coordinate 
                    map.put(id, coord);
                }
                // Node part of road 
                else if (qName.equals("nd")) { 
                    long id = Long.parseLong(atts.getValue("ref")); // Reference to coord parsed previously
                    Point2D coord = map.get(id);
                    if (last != null) { // Checks whether other node has already been examined
                        obsList.add(new Line2D.Double(coord,last)); // If previous exists, draw a line
                    }
                    last = coord; // New last coord 
                }
                // Happens when new road is encountered 
                else if (qName.equals("way")) {
                    last = null;
                }
            }

            // Create XMLReader and OSMHandler
            public void parseOSM(String filename) {
                try {
                    XMLReader reader = XMLReaderFactory.createXMLReader();
                    reader.setContentHandler(new OSMHandler());
                    reader.parse(filename);
                    
                } 
                catch (SAXException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return obsList;
    }
    
}
