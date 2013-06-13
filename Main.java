package ii.olma;

import java.io.*;

public class Main {

    public static void main(String[] args) {

        final String SEPARATOR = "^&";


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
        final NewSequitur sq = new NewSequitur();
//        String test1 = "Ala ma kota kot nie ma ali a ali też nie ma"; //starts with a
//        for(int i = 0; i<test1.length(); ++i){
//            sq.addLetter(test1.charAt(i));
//
//            System.err.println(Rule.getAllRules());
//            SequenceElement.printDigrams();
//
//        }
//
//       System.err.println(Rule.getAllRules());

        //System.err.println(sq.decompress());

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
        try {
            while ((r = inputReader.read()) != -1) {
                char c = (char) r;
                sq.addLetter(c);
            }
        } catch (IOException e) {
            System.err.println("Something went wrong while reading the input.");
            e.printStackTrace();
        }

        //System.err.println(Rule.getAllRules());

        //System.err.print(sq.decompress());

        try {
            for(Rule rule: Rule.rules){
                outputWriter.write(rule.toString());
                outputWriter.write(SEPARATOR);
            }
            outputWriter.write(Rule.getAllRules());
        } catch (IOException e) {
            System.err.println("Error while saving the grammar.");
            e.printStackTrace();
        }
    }
}
