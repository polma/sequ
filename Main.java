package ii.olma;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        if(args.length != 2){
            System.err.print("Arguments: <input_file> <output_file>");
            return;
        }
        BufferedReader inputReader;
        BufferedWriter outputWriter;
        try {
            inputReader = new BufferedReader(new FileReader(args[0]));
            outputWriter = new BufferedWriter(new FileWriter(args[1]));
        } catch (IOException e) {
            System.err.println("Something went wrong with file creation.");
            e.printStackTrace();
            return;
        }

        int r;
        try {
            while((r=inputReader.read()) != -1){
                char c = (char)r;
            }
        } catch (IOException e) {
            System.err.println("Something went wrong while reading the input.");
            e.printStackTrace();
        }
    }
}
