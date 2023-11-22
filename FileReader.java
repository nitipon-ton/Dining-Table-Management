import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/*
 * Reads data from a file
 */
public class FileReader {

  private Scanner fileReader;   // The Scanner object to read the file
  private File myFile;          // The File object containing the data

  /*
   * Constructor to create a FileReader with
   * the specified filename to read
   */
  public FileReader(String filename) {
    setFile(filename);
  }

  /*
   * Sets the file to the specified filename
   */
  public void setFile(String filename) {
    myFile = new File(filename);
    fileReader = createScanner(myFile);
  }

  /*
   * Returns a Scanner object to read the file
   * or notifies the user if the file is not found
   */
  public Scanner createScanner(File theFile) {
    Scanner tempScanner = null;

    try {
      tempScanner = new Scanner(theFile);
    } catch(FileNotFoundException error) {
      System.out.println("File not found.");
    }

    return tempScanner;
  }

  /*
   * Returns an int array containing the values in the file
   */
  public int[] getIntData(int numValues) {
    int[] values = new int[numValues];
    
    for (int index = 0; index < values.length; index++) {
      if (fileReader.hasNextInt()) {
        values[index] = fileReader.nextInt();
      }
    }

    fileReader.close();
    return values;
  }

  /*
   * Returns a double array containing the values in the file
   */
  public double[] getDoubleData(int numValues) {
    double[] values = new double[numValues];

    for (int index = 0; index < values.length; index++) {
      if (fileReader.hasNextDouble()) {
        values[index] = fileReader.nextDouble();
      }
    }

    fileReader.close();
    return values;
  }

  /*
   * Returns a String array containing the values in the file
   */
  public String[] getStringData(int numValues) {
    String[] values = new String[numValues];

    for (int index = 0; index < values.length; index++) {
      if (fileReader.hasNextLine()) {
        values[index] = fileReader.nextLine();
      }
    }

    fileReader.close();
    return values;
  }
  
}