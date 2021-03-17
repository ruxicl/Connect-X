import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class SavedGame {

    // Filepath of the external file in which the old game will be saved
    private static final String filepath = "./output.txt";

    public SavedGame () {
    }

    // Write object to the file at the given filepath
    public void writeObjectToFile(Object obj) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filepath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

            objectOut.writeObject(obj);
            objectOut.close();

        }
        catch (Exception ex) {
        }
    }

    // Read object from the file at the given filepath
    public Object readObjectFromFile(String filepath) {

        try {
            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            Object obj = objectIn.readObject();
            objectIn.close();

            return obj;

        }
        catch (Exception ex) {
            return null;
        }
    }

}