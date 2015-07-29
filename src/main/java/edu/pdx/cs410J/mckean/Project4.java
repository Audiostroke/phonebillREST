package edu.pdx.cs410J.mckean;

import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The main class that parses the command line and communicates with the
 * Phone Bill server using REST.
 */
public class Project4 {

    public static final String MISSING_ARGS = "Missing command line arguments";

    public static void main(String... args) throws IOException {
        String hostName = null;
        String portString = null;
        String key = null;
        String value = null;

        if(args.length == 0) {
            System.err.println("No command line arguments!");
            System.exit(1);
        }

        String [] arguments = new String[commandLineParse(args).length];
        System.arraycopy(commandLineParse(args), 0, arguments, 0, arguments.length);

        hostName = arguments[2];
        portString = arguments[4];

        int port;
        try {
            port = Integer.parseInt( portString );
            
        } catch (NumberFormatException ex) {
            usage("Port \"" + portString + "\" must be an integer");
            return;
        }

        HttpRequestHelper.Response response;
        String cust = arguments[5].replaceAll("\\s+", "");

        if(arguments[0].equals("search")) {
            String start = arguments[6];//.replaceAll("\\s+", "");
            String end = arguments[7];//.replaceAll("\\s+", "");
            PhoneBillRestClient client = new PhoneBillRestClient(hostName, port);
            response = client.getSearch(arguments[5], start, end);
            checkResponseCode(HttpURLConnection.HTTP_OK, response);
            System.out.println(response.getContent());
            System.exit(0);
        }
        else {
            PhoneCall newPhoneCall = new PhoneCall(arguments[6], arguments[7], arguments[8], arguments[9]);
            if(arguments[0].equals("y")) {
                System.out.println("Phone call to be added: " + newPhoneCall.toString());
            }
            PhoneBillRestClient client = new PhoneBillRestClient(hostName, port);
            response = client.addCall(arguments[5], newPhoneCall);
            checkResponseCode(HttpURLConnection.HTTP_OK, response);
            System.out.println(response.getContent());
            System.exit(0);
        }

        /*
        try {
            if (key == null) {
                // Print all key/value pairs
                response = client.getAllKeysAndValues();

            } else if (value == null) {
                // Print all values of key
                response = client.getValues(key);

            } else {
                // Post the key/value pair
                response = client.addKeyValuePair(key, value);
            }

            checkResponseCode( HttpURLConnection.HTTP_OK, response);

        } catch ( IOException ex ) {
            error("While contacting server: " + ex);
            return;
        }
        */
        System.exit(0);
    }

    /**
     * Makes sure that the give response has the expected HTTP status code
     * @param code The expected status code
     * @param response The response from the server
     */
    private static void checkResponseCode( int code, HttpRequestHelper.Response response )
    {
        if (response.getCode() != code) {
            error(String.format("Expected HTTP code %d, got code %d.\n\n%s", code,
                                response.getCode(), response.getContent()));
        }
    }

    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);

        System.exit(1);
    }

    /**
     * Prints usage information for this program and exits
     * @param message An error message to print
     */
    private static void usage( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java Project4 host port [key] [value]");
        err.println("  host    Host of web server");
        err.println("  port    Port of web server");
        err.println("  key     Key to query");
        err.println("  value   Value to add to server");
        err.println();
        err.println("This simple program posts key/value pairs to the server");
        err.println("If no value is specified, then all values are printed");
        err.println("If no key is specified, all key/value pairs are printed");
        err.println();

        System.exit(1);
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
        String toHost = "n";
        String hostName = "n";
        String toPort = "n";
        String port = "n";
        String search = "n";

        for(int i = 0; i < arguments.size(); ++i) {
            if(arguments.get(i).equals("-README")) {
                readMe();
            }
        }

        if(arguments.contains("-host") && !arguments.contains("-port")) {
            System.err.println("Hostname supplied but port was not!");
            System.exit(1);
        }

        if(arguments.contains("-port") && !arguments.contains("-host")) {
            System.err.println("Port supplied but Hostname was not!");
            System.exit(1);
        }

        for(int i = 0; i < arguments.size(); ++i) {
            if(arguments.get(i).equals("-print")) {
                if(arguments.contains("-search")) {
                    System.err.println("Cannot add a new call and search for calls at the same time!");
                    System.exit(1);
                }
                print = "y";
                arguments.remove(i);
                break;
            }
        }

        for(int i = 0; i < arguments.size(); ++i) {
            if(arguments.get(i).equals("-host")) {
                toHost = "y";
                hostName = arguments.get(i+1);
                arguments.remove(i+1);
                arguments.remove(i);
                break;
            }
        }

        for(int i = 0; i < arguments.size(); ++i) {
            if(arguments.get(i).equals("-port")) {
                toPort = "y";
                    port = arguments.get(i+1);
                arguments.remove(i+1);
                arguments.remove(i);
                break;
            }
        }

        for(int i = 0; i < arguments.size(); ++i) {
            if(arguments.get(i).equals("-search")) {
                search = "search";
                arguments.remove(i);

                if(arguments.size() < 7) {
                    System.err.println("Missing command line arguments!");
                    System.exit(1);
                }

                if(arguments.size() > 7) {
                    System.err.println("Extra command line arguments!");
                    System.exit(1);
                }

                String [] parsedsearch = new String[8];
                parsedsearch[0] = search;
                parsedsearch[1] = toHost;
                parsedsearch[2] = hostName;
                parsedsearch[3] = toPort;
                parsedsearch[4] = port;
                parsedsearch[5] = arguments.get(0);
                parsedsearch[6] = arguments.get(1) + " " + arguments.get(2) + " " + arguments.get(3);
                parsedsearch[7] = arguments.get(4) + " " + arguments.get(5) + " " + arguments.get(6);

                if(parsedsearch[5].matches("^[\\s]+") || arguments.get(0).isEmpty()) {
                    System.err.println("Customer name was entered incorrectly");
                    System.exit(1);
                }
                if(!parsedsearch[6].matches("\\d{1,2}/\\d{1,2}/\\d{2,4} (1[012]|[1-9]):[0-5][0-9] (?i)(am|pm)")) {
                    System.err.println("Start date and time is in the incorrect form");
                    System.exit(1);
                }
                if(!parsedsearch[7].matches("\\d{1,2}/\\d{1,2}/\\d{2,4} (1[012]|[1-9]):[0-5][0-9] (?i)(am|pm)")) {
                    System.err.println("End date or time is in the incorrect form");
                    System.exit(1);
                }

                return parsedsearch;

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
        parsed[1] = toHost;
        parsed[2] = hostName;
        parsed[3] = toPort;
        parsed[4] = port;
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