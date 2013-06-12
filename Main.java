package ii.olma;

import java.io.*;

public class Main {

    public static void main(String[] args) {

        NewSequitur sq = new NewSequitur();
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
        String test1 = "abcdbcabcd"; //starts with a
        for(int i = 0; i<test1.length(); ++i)
            sq.addLetter(test1.charAt(i));

        sq.printStart();



        if (args.length != 20) {
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
        Sequitur asq = null;
        try {
            boolean firstRun = true;
            while ((r = inputReader.read()) != -1) {
                char c = (char) r;
                if (firstRun) {
                    asq = new Sequitur(c);
                    firstRun = false;
                } else
                    asq.addLetter(c);
            }
        } catch (IOException e) {
            System.err.println("Something went wrong while reading the input.");
            e.printStackTrace();
        }

        asq.printRules();
    }
}
