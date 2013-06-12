package ii.olma;

import java.io.*;

public class Main {

    public static void main(String[] args) {

        //Sequitur sq = new Sequitur('a');
//        sq.addLetter('b');
//        sq.addLetter('c');
//        sq.addLetter('b');
//        sq.addLetter('c');
//        sq.addLetter('b');
//        sq.addLetter('c');
//        sq.addLetter('b');
//        sq.addLetter('c');


        //rule utility test
//        String test1 = "bcdbcabcd"; //starts with a
//        for(int i = 0; i<test1.length(); ++i)
//            sq.addLetter(test1.charAt(i));


        if (args.length != 2) {
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
        Sequitur sq = null;
        try {
            boolean firstRun = true;
            while ((r = inputReader.read()) != -1) {
                char c = (char) r;
                if (firstRun) {
                    sq = new Sequitur(c);
                    firstRun = false;
                } else
                    sq.addLetter(c);
            }
        } catch (IOException e) {
            System.err.println("Something went wrong while reading the input.");
            e.printStackTrace();
        }

        sq.printRules();
    }
}
