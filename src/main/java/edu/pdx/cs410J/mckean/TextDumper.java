package edu.pdx.cs410J.mckean;


import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.AbstractPhoneCall;
import edu.pdx.cs410J.PhoneBillDumper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by TylerMcKean on 7/15/15.
 */

/**
 * Implements the PhoneBillDumper interface in order to write phone bill data to a text file.
 */
public class TextDumper implements PhoneBillDumper {

    /**
     * String that represents the name of the file to be written to.
     */
    protected String fileName;

    /**
     * Constructor for the TextDumper class that reads in a string for a file name and sets
     * its filename data member to it.
     * @param filename A string that contains the filename of the file to be written to.
     */
    public TextDumper(String filename) {
        this.fileName = filename;
    }

    /**
     * Function that creates a file if one doesn't exist and writes phone bill data to it.
     * @param bill Represents a phone bill. This is what data will be written to the text file.
     * @throws IOException
     */
    public void dump(AbstractPhoneBill bill) throws IOException {
        try {
            FileWriter writer = new FileWriter(fileName);
            BufferedWriter buffer = new BufferedWriter(writer);

            buffer.write(bill.getCustomer() + ",");
            List<PhoneCall> temp = (List<PhoneCall>) bill.getPhoneCalls();
            for(AbstractPhoneCall call : temp) {
                buffer.write(call.getCaller() + ";" + call.getCallee() + ";" + call.getStartTimeString() + ";" + call.getEndTimeString() + ",");
            }

            buffer.close();
        } catch (IOException e) {
            System.err.println("Cannot write to file" + fileName);
            System.exit(1);
        }


    }
}
