package edu.pdx.cs410J.mckean;


import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.AbstractPhoneCall;
import edu.pdx.cs410J.ParserException;


import java.util.*;
import java.io.IOException;

/**
 * The main class for the CS410J Phonebill project 3.
 * Created by TylerMcKean on 7/18/15.
 */
public class Project3 {

    /**
     * The main method of the program. Reads in arguments from the command line and performs functionality based
     * on what options it has been given. Can print the new call, read in from a phone bill file, write to a phone bill
     * file, pretty print to a file or command line,
     * and a combination of all three. In addition it can print the README statement depending on the arguments.
     *
     * @param args Represents the data read in from the command line in an array of strings.
     */
    public static void main(String[] args) throws IOException {

        if(args[0].isEmpty()) {
            System.err.println("No command line arguments!");
            System.exit(1);
        }

        String [] arguments = new String[10];
        System.arraycopy(commandLineParse(args), 0, arguments, 0, 10);

        PhoneCall newPhoneCall = new PhoneCall(arguments[6], arguments[7], arguments[8], arguments[9]);
        PhoneBill newPhoneBill = new PhoneBill(arguments[5]);
        newPhoneBill.addPhoneCall(newPhoneCall);

        if(arguments[0].equals("y")) {
            System.out.println("The phone call from the command line to be added:" + newPhoneCall.toString());
        }

        if(arguments[1].equals("y")) {
            TextParser fileTextParser = new TextParser(arguments[2]);
            try {
                /**
                 * If statement that runs as long as the file named in the command line arguments already exists.
                 * Otherwise, the new phone bill is added to a new file.
                 */
                if (fileTextParser.parse() != null) {
                    AbstractPhoneBill inputPhoneBill = fileTextParser.parse();
                    /**
                     * If statement that runs if the customer name in the text file is the same as the one from the
                     * command line. Otherwise, it returns an error to the user that the names are not the same.
                     */
                    if (inputPhoneBill.getCustomer().equals(arguments[5])) {
                        TextDumper fileTextDumper = new TextDumper(arguments[2]);
                        List<PhoneCall> temp = (List<PhoneCall>) inputPhoneBill.getPhoneCalls();
                        for (int i = 0; i < temp.size(); ++i) {
                            newPhoneBill.addPhoneCall(temp.get(i));
                        }
                        List<PhoneCall> temp2 = (List<PhoneCall>) newPhoneBill.getPhoneCalls();
                        Collections.sort(temp2);
                        PhoneBill dumpBill = new PhoneBill(arguments[5], temp2);
                        fileTextDumper.dump(dumpBill);

                        if (arguments[3].equals("y")) {
                            PrettyPrinter prettyPrinter = new PrettyPrinter(arguments[4]);
                            prettyPrinter.dump(dumpBill);
                            System.exit(0);
                        }
                    }
                    else {
                            System.out.println("The phone bill in the text file is not for the same customer as entered in the command line");
                            System.exit(1);
                    }
                }
                else {
                        TextDumper fileTextDumper = new TextDumper(arguments[2]);
                        PhoneBill inputPhoneBill = new PhoneBill(arguments[5]);
                        inputPhoneBill.addPhoneCall(newPhoneCall);
                        fileTextDumper.dump(inputPhoneBill);
                    }
                }
             catch (ParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            if (arguments[3].equals("y")) {
            PrettyPrinter prettyPrinter = new PrettyPrinter(arguments[4]);
            prettyPrinter.dump(newPhoneBill);

        }

        System.exit(0);
    }

    /**
     * Function that parses the information from the command line and checks it for errors.
     * @param args Takes in the command line information as an array of strings to be checked.
     * @return Returns an array of strings that is formatted for ease of use in the rest of the program.
     *
     */
    public static String[] commandLineParse (String [] args){
        List<String> arguments = new ArrayList<String>(Arrays.asList(args));
        String [] parsed = new String[10];
        String print = "n";
        String toFile = "n";
        String fileName = "n";
        String toPretty = "n";
        String prettyFile = "n";

        for(int i = 0; i < arguments.size(); ++i) {
            if(arguments.get(i).equals("-README")) {
                readMe();
            }
        }

        for(int i = 0; i < arguments.size(); ++i) {
            if(arguments.get(i).equals("-print")) {
                print = "y";
                arguments.remove(i);
                break;
            }
        }
        for(int i = 0; i < arguments.size(); ++i) {
            if(arguments.get(i).equals("-textFile")) {
                toFile = "y";
                fileName = arguments.get(i+1);
                arguments.remove(i+1);
                arguments.remove(i);
                break;
            }
        }
        for(int i = 0; i < arguments.size(); ++i) {
            if(arguments.get(i).equals("-pretty")) {
                toPretty = "y";
                if(arguments.get(i+1).equals("-") || arguments.get(i+1).equals("file -")) {
                    prettyFile = "out";
                }
                else {
                    prettyFile = arguments.get(i+1);
                }
                arguments.remove(i+1);
                arguments.remove(i);
                break;
            }
        }
        if(arguments.size() < 9) {
            System.err.println("Missing command line arguments!");
            System.exit(1);
        }
        if(arguments.size() > 9) {
            System.err.println("Extra command line arguments!");
            System.exit(1);
        }

        parsed[0] = print;
        parsed[1] = toFile;
        parsed[2] = fileName;
        parsed[3] = toPretty;
        parsed[4] = prettyFile;
        parsed[5] = arguments.get(0);
        parsed[6] = arguments.get(1);
        parsed[7] = arguments.get(2);
        parsed[8] = arguments.get(3) + " " + arguments.get(4) + " " + arguments.get(5);
        parsed[9] = arguments.get(6) + " " + arguments.get(7) + " " + arguments.get(8);

        if(parsed[5].matches("^[\\s]+") || arguments.get(0).isEmpty()) {
            System.err.println("Customer name was entered incorrectly");
            System.exit(1);
        }
        if(!parsed[6].matches("\\d\\d\\d-\\d\\d\\d-\\d\\d\\d\\d")) {
            System.err.println("Caller phone number in the incorrect form");
            System.exit(1);
        }
        if(!parsed[7].matches("\\d\\d\\d-\\d\\d\\d-\\d\\d\\d\\d")) {
            System.err.println("Callee phone number is in the incorrect form");
            System.exit(1);
        }
        if(!parsed[8].matches("\\d{1,2}/\\d{1,2}/\\d{2,4} (1[012]|[1-9]):[0-5][0-9] (?i)(am|pm)")) {
            System.err.println("Start date and time is in the incorrect form");
            System.exit(1);
        }
        if(!parsed[9].matches("\\d{1,2}/\\d{1,2}/\\d{2,4} (1[012]|[1-9]):[0-5][0-9] (?i)(am|pm)")) {
            System.err.println("End date or time is in the incorrect form");
            System.exit(1);
        }


        return parsed;
    }

    /**
     * Function that is called if the README option is present in the command line.
     */
    public static void readMe() {
        System.out.println("Project 2 README by Tyler McKean for CS410J Summer 2015.");
        System.out.println("This program implements two interfaces called TextDumper and TextParser.");
        System.out.println("It will read in a file name and destination from the command line.");
        System.out.println("From the information entered on the command line it will then either read or write");
        System.out.println("a new call to a Phone Bill and either write it to a new text file or read in from one.");
        System.out.println("It can also add new calls to Phone Bills for customers of the same name");
        System.exit(0);
    }
}