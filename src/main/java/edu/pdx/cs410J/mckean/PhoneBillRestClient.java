package edu.pdx.cs410J.mckean;

import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.  You'll need to change it
 * to do something other than just send key/value pairs.
 */
public class PhoneBillRestClient extends HttpRequestHelper
{
    private static final String WEB_APP = "phonebill";
    private static final String SERVLET = "calls";

    private final String url;


    /**
     * Creates a client to the Phone Bil REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    public PhoneBillRestClient( String hostName, int port)
    {
        this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET);
    }

    public PhoneBillRestClient(String hostName, int port, String customer, String startTime, String endTime) {

        this.url = String.format("http://%s:%d/%s%s?customer=%s&startTime=%s&endTime=%s", hostName, port, WEB_APP, SERVLET, customer, startTime, endTime);
    }

    /**
     * Returns all keys and values from the server
     */
    public Response getAllKeysAndValues() throws IOException
    {
        return get(this.url );
    }

    /**
     * Returns all values for the given key
     */
    public Response getValues( String customer) throws IOException
    {
        return get(this.url, "customer", customer);
    }

    public Response getSearch(String customer, String startTime, String endTime) throws IOException {
        return get(this.url, "customer", customer, "startTime", startTime, "endTime", endTime);
    }
    public Response addCall(String customername, PhoneCall call) throws IOException
    {
        String customer = customername;
        String caller = call.getCaller();
        String callee = call.getCallee();
        String startTime = call.getStartTimeString();
        String endTime = call.getEndTimeString();


        return post( this.url, "customer", customer, "caller", caller, "callee", callee, "startTime", startTime, "endTime", endTime);
    }
}
