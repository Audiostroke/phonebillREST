package edu.pdx.cs410J.mckean;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.PhoneBillParser;

import java.io.*;
import java.util.*;

/**
 * Created by TylerMcKean on 7/15/15.
 */

/**
 * Implements the PhoneBillParser interface to read some source and create
 * a phone bill.
 */
public class TextParser implements PhoneBillParser {

    /**
     * String that represents the name of the file that the data will be parsed from.
     */
    protected String fileName;
    /**
     * Scanner object to assist with reading in data from the external text file.
     */
    private static Scanner fileInput;
    /**
     * Parses some source and returns a phone bill
     *
     * @throws ParserException
     *         If the source cannot be parsed
     */
    /**
     * Constructor for the TextParser class
     * @param filename String that represents the fileName to be read from as determined from the command line.
     */
    public TextParser(String filename) {
        this.fileName = filename;
    }

    /**
     * Function that parses information from a text file and turns it into a valid Phone bill.
     * @return Returns a phone bill with a customer name and a list of phone calls.
     * @throws ParserException
     */
    public AbstractPhoneBill parse() throws ParserException {

        File inputPhoneBillFile = new File(fileName);
        try {
            fileInput = new Scanner(inputPhoneBillFile);
        }
        catch (FileNotFoundException e) {
            System.out.println("No file to read from, creating a new one called " + fileName);
            return null;
        }
        if(!fileInput.hasNextLine()) {
            System.err.println("The file to be read exists but is empty");
            System.exit(1);
        }
        while(fileInput.hasNextLine()) {
            String inputLine = fileInput.nextLine();
            String delims = "[,]";
            String calldelims = "[;]";
            String [] readBill = inputLine.split(delims);
            if(readBill[0].matches("^[\\s]+") || readBill[0].isEmpty()) {
                System.err.println("Customer name was entered incorrectly in the text file");
                System.exit(1);
            }
            PhoneBill inputPhoneBill = new PhoneBill(readBill[0]);
            String [] calls = new String[readBill.length-1];
            for(int i = 0; i < calls.length; ++i) {
                calls[i] = readBill[i+1];
            }
            for(int i = 0; i < calls.length; ++i) {
                String [] newCall = calls[i].split(calldelims);
                if(newCall.length < 4) {
                    System.err.println("Missing arguments in the phone call from the text file");
                }
                if(newCall.length > 4) {
                    System.err.println("Extra arguments in the phone call from the text file");
                }
                if(!newCall[0].matches("\\d\\d\\d-\\d\\d\\d-\\d\\d\\d\\d")) {
                    System.err.println("Caller phone number in the incorrect form in the text file");
                    System.exit(1);
                }
                if(!newCall[1].matches("\\d\\d\\d-\\d\\d\\d-\\d\\d\\d\\d")) {
                    System.err.println("Callee phone number is in the incorrect form in the text file");
                    System.exit(1);
                }
                if(!newCall[2].matches("\\d{1,2}/\\d{1,2}/\\d{2,4} (1[012]|[1-9]):[0-5][0-9] (?i)(am|pm)")) {
                    System.err.println("Start date and time is in the incorrect form in the text file");
                    System.exit(1);
                }
                if(!newCall[3].matches("\\d{1,2}/\\d{1,2}/\\d{2,4} (1[012]|[1-9]):[0-5][0-9] (?i)(am|pm)")) {
                    System.err.println("End date or time is in the incorrect form in the text file");
                    System.exit(1);
                }
                PhoneCall inputPhoneCall = new PhoneCall(newCall[0], newCall[1], newCall[2], newCall[3]);
                inputPhoneBill.addPhoneCall(inputPhoneCall);
            }
            fileInput.close();
            return inputPhoneBill;
        }
        return null;
    }
}
