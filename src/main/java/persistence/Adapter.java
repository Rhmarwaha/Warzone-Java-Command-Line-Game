package persistence;

import model.ResponseWrapper;

public class Adapter extends MapFileAlteration{

    public static String typeOfMap="DominationMap";

    Adaptee d_adaptee = new Adaptee();

    public Adapter(Adaptee adaptee) {
        this.d_adaptee = adaptee;
    }

    public void readMapFile(){
        d_adaptee.readMapFile();
    }

    public ResponseWrapper saveMapFile(String mapFileName) {
        d_adaptee.saveMap(mapFileName);
        return new ResponseWrapper(200, "Save Map successfully ");
    }

}
