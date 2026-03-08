// Implented by Sukhman
import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.w3c.dom.Element;
import java.io.File;

public class ParseXML {

    static class LocationData { // store location data
        String name;
        List<String> neighbors = new ArrayList<>();

        boolean isTrailer = false;
        boolean isCastingOffice = false;

        int shotsRemaining;
        CoordinatesData coords;
        List<ShotsData> shots = new ArrayList<>();

        List<RoleData> roles = new ArrayList<>();
        int[][] upgradeCosts;
    }

    static class CardData { // stores scene card data
        String name;
        int budget;
        int sceneNumber;
        String image;
        List<RoleData> roles = new ArrayList<>();
    }

    static class RoleData { // stores role data
        String name;
        int rank;
        boolean onCard;
        CoordinatesData coords;
    }

    static class CoordinatesData { // stores coordinate data
        int x;
        int y;
        int h;
        int w;
    }

    static class ShotsData{
        int shotNumber;
        CoordinatesData coords;
    }
    
    private Map<String, LocationData> locations = new HashMap<>();
    private Map<String, CardData> cards = new HashMap<>();

    public Collection<LocationData> getLocations() {
        return locations.values();
    }

    public Collection<CardData> getCards() {
        return cards.values();
    }


    public void readBoardData(String filename) { // reads and stores info from Board xml file
        locations.clear(); // in case this method ends up getting called multiple times, although it shouldn't

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(filename));

            // pull data from sets (not Trailer or CastingOffice)
            NodeList sets = doc.getElementsByTagName("set");
            for (int i = 0; i < sets.getLength(); i++) {
                Element set = (Element) sets.item(i); //typecast to Element to gain access to getAttribute method to pull tag data

                LocationData location = new LocationData();
                location.name = set.getAttribute("name");
              
                NodeList neighbors = set.getElementsByTagName("neighbor"); // grab neighbors
                for (int j = 0; j < neighbors.getLength(); j++) {
                    location.neighbors.add(((Element) neighbors.item(j)).getAttribute("name"));
                }

                NodeList locationCoords = set.getElementsByTagName("area");
                if(locationCoords.getLength() > 0) {
                    Element coordsElement = (Element) locationCoords.item(0);

                    CoordinatesData coordsdata = new CoordinatesData();
                    coordsdata.x = Integer.parseInt(coordsElement.getAttribute("x"));
                    coordsdata.y = Integer.parseInt(coordsElement.getAttribute("y"));
                    coordsdata.h = Integer.parseInt(coordsElement.getAttribute("h"));
                    coordsdata.w = Integer.parseInt(coordsElement.getAttribute("w"));

                    location.coords = coordsdata;
                }

                NodeList shots = set.getElementsByTagName("take");
                for (int j = 0; j < shots.getLength(); j++) {
                    Element takeElem = (Element) shots.item(j);

                    ShotsData take = new ShotsData();
                    take.shotNumber = Integer.parseInt(takeElem.getAttribute("number"));

                    Element areaElem = (Element) takeElem.getElementsByTagName("area").item(0);

                    CoordinatesData coordsData = new CoordinatesData();
                    coordsData.x = Integer.parseInt(areaElem.getAttribute("x"));
                    coordsData.y = Integer.parseInt(areaElem.getAttribute("y"));
                    coordsData.h = Integer.parseInt(areaElem.getAttribute("h"));
                    coordsData.w = Integer.parseInt(areaElem.getAttribute("w"));

                    take.coords = coordsData;

                    location.shots.add(take);
                }

                location.shotsRemaining = location.shots.size();

                NodeList roles = set.getElementsByTagName("part");
                for (int j = 0; j < roles.getLength(); j++) {
                    Element rolesElement = (Element) roles.item(j);

                    RoleData role = new RoleData();
                    role.name = rolesElement.getAttribute("name");
                    role.rank = Integer.parseInt(rolesElement.getAttribute("level")); // parseInt converts the string to int
                    role.onCard = false; // these are off the board

                    NodeList roleCoords = rolesElement.getElementsByTagName("area");
                    if (roleCoords.getLength() > 0) {
                        Element coordsElem = (Element) roleCoords.item(0);

                        CoordinatesData coords = new CoordinatesData();
                        coords.x = Integer.parseInt(coordsElem.getAttribute("x"));
                        coords.y = Integer.parseInt(coordsElem.getAttribute("y"));
                        coords.h = Integer.parseInt(coordsElem.getAttribute("h"));
                        coords.w = Integer.parseInt(coordsElem.getAttribute("w"));

                        role.coords = coords; // assign coords to role
                    }


                    location.roles.add(role);
                }

                locations.put(location.name, location);
            }

            // Trailer information (doesn't have role data)
            Element trailer = (Element) doc.getElementsByTagName("trailer").item(0); //only 1 item with tag "trailer" so grab the first and only one
            LocationData trailerLoc = new LocationData();
            trailerLoc.name = "Trailer";
            trailerLoc.isTrailer = true;

            NodeList trailerNeighbors = trailer.getElementsByTagName("neighbor");
            for (int i = 0; i < trailerNeighbors.getLength(); i++) {
                trailerLoc.neighbors.add(((Element) trailerNeighbors.item(i)).getAttribute("name"));
            }

            NodeList trailerCoords = trailer.getElementsByTagName("area");
            trailerLoc.coords = new CoordinatesData();
            if(trailerCoords.getLength() > 0) {
                Element coords = (Element) trailerCoords.item(0);
                trailerLoc.coords.x = Integer.parseInt(coords.getAttribute("x"));
                trailerLoc.coords.y = Integer.parseInt(coords.getAttribute("y"));
                trailerLoc.coords.h = Integer.parseInt(coords.getAttribute("h"));
                trailerLoc.coords.w = Integer.parseInt(coords.getAttribute("w"));
            }
            locations.put(trailerLoc.name, trailerLoc);

            // castingOffice information (doesn't have role data but has upgradeData)
            Element office = (Element) doc.getElementsByTagName("office").item(0);
            LocationData officeLoc = new LocationData();
            officeLoc.name = "Casting Office";
            officeLoc.isCastingOffice = true;
            officeLoc.upgradeCosts = new int[7][2]; // [rank][0 for dollar, 1 for credit] 7 "ranks" so it'll align when calling for specific ranks data

            NodeList officeNeighbors = office.getElementsByTagName("neighbor");
            for (int i = 0; i < officeNeighbors.getLength(); i++) {
                officeLoc.neighbors.add(
                    ((Element) officeNeighbors.item(i)).getAttribute("name")
                );
            }

            NodeList officeCoords = office.getElementsByTagName("area");
            officeLoc.coords = new CoordinatesData();
            if(officeCoords.getLength() > 0) {
                Element coords = (Element) officeCoords.item(0);
                officeLoc.coords.x = Integer.parseInt(coords.getAttribute("x"));
                officeLoc.coords.y = Integer.parseInt(coords.getAttribute("y"));
                officeLoc.coords.h = Integer.parseInt(coords.getAttribute("h"));
                officeLoc.coords.w = Integer.parseInt(coords.getAttribute("w"));
            }

            NodeList upgrades = office.getElementsByTagName("upgrade");
            for (int i = 0; i < upgrades.getLength(); i++) {
                Element upgrade = (Element) upgrades.item(i);
                int level = Integer.parseInt(upgrade.getAttribute("level"));
                int amount = Integer.parseInt(upgrade.getAttribute("amt"));
                String currency = upgrade.getAttribute("currency");

                if (currency.equals("dollar")) {
                    officeLoc.upgradeCosts[level][0] = amount;
                } else { 
                    officeLoc.upgradeCosts[level][1] = amount; //credits
                }
            }

            locations.put(officeLoc.name, officeLoc);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readCardData(String filename) { // read information from card xml file
        cards.clear();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(filename));

            NodeList cardNodes = doc.getElementsByTagName("card");

            for (int i = 0; i < cardNodes.getLength(); i++) {
                Element cardElement = (Element) cardNodes.item(i);

                CardData card = new CardData();
                card.name = cardElement.getAttribute("name");
                card.image = cardElement.getAttribute("img");
                card.budget = Integer.parseInt(cardElement.getAttribute("budget"));

                NodeList sceneNodes = cardElement.getElementsByTagName("scene");
                Element sceneElem = (Element) sceneNodes.item(0);
                card.sceneNumber = Integer.parseInt(sceneElem.getAttribute("number"));
               
                NodeList roles = cardElement.getElementsByTagName("part");
                for (int j = 0; j < roles.getLength(); j++) {
                    Element rolesElement = (Element) roles.item(j);

                    RoleData role = new RoleData();
                    role.name = rolesElement.getAttribute("name");
                    role.rank = Integer.parseInt(rolesElement.getAttribute("level"));
                    role.onCard = true; // on card

                    Element coordsElement = (Element) rolesElement.getElementsByTagName("area").item(0);
                    if(coordsElement != null) {
                        CoordinatesData coordsData = new CoordinatesData();
                        coordsData.x = Integer.parseInt(coordsElement.getAttribute("x"));
                        coordsData.y = Integer.parseInt(coordsElement.getAttribute("y"));
                        coordsData.h = Integer.parseInt(coordsElement.getAttribute("h"));
                        coordsData.w = Integer.parseInt(coordsElement.getAttribute("w"));    
                        role.coords = coordsData;
                    }

                    card.roles.add(role);
                }

                cards.put(card.name, card);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


