// Created by Sukhman Lally

import java.io.File;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ParseXML {

    static class LocationData {
        String name;
        List<String> neighbors = new ArrayList<>();

        boolean isTrailer = false;
        boolean isCastingOffice = false;

        int shotsRemaining;

        List<RoleData> roles = new ArrayList<>();
        int[][] upgradeCosts;
    }

    static class CardData {
        String name;
        int budget;
        int sceneNumber;
        List<RoleData> roles = new ArrayList<>();
    }

    static class RoleData {
        String name;
        int rank;
        boolean onCard;
    }
    
    private Map<String, LocationData> locations = new HashMap<>();
    private Map<String, CardData> cards = new HashMap<>();

    public Collection<LocationData> getLocations() {
        return locations.values();
    }

    public Collection<CardData> getCards() {
        return cards.values();
    }


    public void readBoardData(String filename) {
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
              
                NodeList neighbors = set.getElementsByTagName("neighbor");
                for (int j = 0; j < neighbors.getLength(); j++) {
                    location.neighbors.add(((Element) neighbors.item(j)).getAttribute("name"));
                }

                location.shotsRemaining = set.getElementsByTagName("take").getLength();

                NodeList roles = set.getElementsByTagName("part");
                for (int j = 0; j < roles.getLength(); j++) {
                    Element rolesElement = (Element) roles.item(j);

                    RoleData role = new RoleData();
                    role.name = rolesElement.getAttribute("name");
                    role.rank = Integer.parseInt(rolesElement.getAttribute("level")); // parseInt converts the string to int
                    role.onCard = false; // these are off the board

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

    public void readCardData(String filename) {
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

                    card.roles.add(role);
                }

                cards.put(card.name, card);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
}


