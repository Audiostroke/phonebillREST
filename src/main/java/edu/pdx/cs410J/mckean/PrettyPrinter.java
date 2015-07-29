package edu.pdx.cs410J.mckean;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.AbstractPhoneCall;
import edu.pdx.cs410J.PhoneBillDumper;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Implements the PhoneBillDumper interface in order to write phone bill data to a text file.
 */
public class PrettyPrinter implements PhoneBillDumper {

    /**
     * String that represents the name of the file to be written to.
     */
    protected String fileName;
    protected HttpServletResponse response;

    /**
     * Constructor for the PrettyPrinter class that reads in a string for a file name and sets
     * its filename data member to it.
     * @param filename A string that contains the filename of the file to be written to.
     */
    public PrettyPrinter(String filename) {
        this.fileName = filename;
    }

    public PrettyPrinter(HttpServletResponse response) {
        this.response = response;
    };

    public void dumptoserver(AbstractPhoneBill bill) throws IOException {
        try {
            PrintWriter pw = response.getWriter();
            BufferedWriter buffer = new BufferedWriter(pw);
            buffer.write("Customer: " + bill.getCustomer() + ",");
            List<PhoneCall> temp = (List<PhoneCall>) bill.getPhoneCalls();
            for (PhoneCall call : temp) {
                buffer.write("Caller number: " + call.getCaller() + " ");
                buffer.write("Callee number: " + call.getCallee() + " ");
                buffer.write("Start Date and Time: " + call.getStartTimeString() + " ");
                buffer.write("End Date and Time: " + call.getEndTimeString() + " ");
                buffer.write("Call Duration: " + call.getCallDuration() + " minutes.");
                buffer.newLine();
            }

            buffer.close();
        } catch (IOException e) {
            System.err.println("Cannot write to file" + fileName);
            System.exit(1);
        }
    }

    /**
     * Function that creates a file if one doesn't exist and writes phone bill data to it.
     * @param bill Represents a phone bill. This is what data will be written to the text file.
     * @throws IOException
     */
    public void dump(AbstractPhoneBill bill) throws IOException {
        if(fileName.equals("out")) {
            List<PhoneCall> temp = (List<PhoneCall>) bill.getPhoneCalls();
            for(PhoneCall call : temp) {
                System.out.println("Caller number: " + call.getCaller() + " ");
                System.out.println("Callee number: " + call.getCallee() + " ");
                System.out.println("Start Date and Time: " + call.getStartTimeString() + " ");
                System.out.println("End Date and Time: " + call.getEndTimeString() + " ");
                System.out.println("Call Duration: " + call.getCallDuration() + " minutes.");
                System.out.println("%n");
            }
        }
        else {
            try {
                FileWriter writer = new FileWriter(fileName);
                BufferedWriter buffer = new BufferedWriter(writer);

                buffer.write("Customer: " + bill.getCustomer() + ",");
                List<PhoneCall> temp = (List<PhoneCall>) bill.getPhoneCalls();
                for (PhoneCall call : temp) {
                    buffer.write("Caller number: " + call.getCaller() + " ");
                    buffer.write("Callee number: " + call.getCallee() + " ");
                    buffer.write("Start Date and Time: " + call.getStartTimeString() + " ");
                    buffer.write("End Date and Time: " + call.getEndTimeString() + " ");
                    buffer.write("Call Duration: " + call.getCallDuration() + " minutes.");
                    buffer.newLine();
                }

                buffer.close();
            } catch (IOException e) {
                System.err.println("Cannot write to file" + fileName);
                System.exit(1);
            }

        }
    }
}
