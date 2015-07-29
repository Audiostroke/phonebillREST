package edu.pdx.cs410J.mckean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>PhoneBill</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple key/value pairs.
 */
public class PhoneBillServlet extends HttpServlet
{
    private final Map<String, PhoneBill> data = new HashMap<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm a");

    /**
     * Handles an HTTP GET request from a client by writing the value of the key
     * specified in the "key" HTTP parameter to the HTTP response.  If the "key"
     * parameter is not specified, all of the key/value pairs are written to the
     * HTTP response.
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );

        String customer = getParameter( "customer", request );
        String startTime = getParameter("startTime", request);
        String endTime = getParameter("endTime", request);
        if (customer != null) {
            if(startTime != null && endTime != null) {
                writeSearchBill(customer, response, startTime, endTime);
            }
            else {
                writeValue(customer, response);
            }

        }
        else {
            writeAllMappings(response);
        }
    }

    /**
     * Handles an HTTP POST request by storing the key/value pair specified by the
     * "key" and "value" request parameters.  It writes the key/value pair to the
     * HTTP response.
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType("text/plain");

        String customer = getParameter( "customer", request );
        if (customer == null) {
            missingRequiredParameter(response, "customer" );
            return;
        }
        String caller = getParameter("caller", request);
        if(caller == null ) {
            missingRequiredParameter(response, "caller" );
            return;
        }
        String callee = getParameter("callee", request);
        if(callee == null ) {
            missingRequiredParameter(response, "callee" );
            return;
        }
        String startTime = getParameter("startTime", request);
        if(startTime == null ) {
            missingRequiredParameter(response, "startTime" );
            return;
        }
        String endTime = getParameter("endTime", request);
        if(endTime == null ) {
            missingRequiredParameter(response, "endTime" );
            return;
        }

        PhoneCall newCall = new PhoneCall(caller, callee, startTime, endTime);
        PhoneBill newBill = data.get(customer);
        if(newBill != null) {
            newBill.addPhoneCall(newCall);
        }
        else {
            data.put(customer, new PhoneBill(customer));
            data.get(customer).addPhoneCall(newCall);
        }

        PrintWriter pw = response.getWriter();
        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK);
    }

    /**
     * Writes an error message about a missing parameter to the HTTP response.
     *
     * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
     */
    private void missingRequiredParameter( HttpServletResponse response, String parameterName )
        throws IOException
    {
        PrintWriter pw = response.getWriter();
        pw.println( Messages.missingRequiredParameter(parameterName));
        pw.flush();
        
        response.setStatus( HttpServletResponse.SC_PRECONDITION_FAILED );
    }

    /**
     * Writes the value of the given key to the HTTP response.
     *
     * The text of the message is formatted with {@link Messages#getMappingCount(int)}
     * and
     */
    private void writeValue(String customer, HttpServletResponse response ) throws IOException
    {
        PhoneBill newBill = this.data.get(customer);

        PrettyPrinter printer = new PrettyPrinter(response);
        printer.dumptoserver(newBill);

        response.setStatus( HttpServletResponse.SC_OK );
    }

    private void writeSearchBill(String customer, HttpServletResponse response, String startTime, String endTime) throws IOException
    {
        System.out.println(customer);
        PhoneBill newBill = this.data.get(customer);
        List<PhoneCall> temp = (List<PhoneCall>) newBill.getPhoneCalls();
        PhoneBill newerBill = new PhoneBill(customer);
        try {
            Date Start = dateFormat.parse(startTime);
            Date End = dateFormat.parse(endTime);
            for(PhoneCall call : temp) {
                if(call.getStartTime().after(Start) && call.getEndTime().before(End)) {
                    newerBill.addPhoneCall(call);
                }
            }
            PrettyPrinter printer = new PrettyPrinter(response);
            printer.dumptoserver(newerBill);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (ParseException e) {
            System.out.println("Parse exception when formatting date");
        }
    }

    /**
     * Writes all of the key/value pairs to the HTTP response.
     *
     * The text of the message is formatted with
     * {@link Messages#formatKeyValuePair(String, PhoneBill)}
     */
    private void writeAllMappings( HttpServletResponse response ) throws IOException
    {
        PrintWriter pw = response.getWriter();
        pw.println(Messages.getMappingCount(data.size() ));

        for (Map.Entry<String, PhoneBill> entry : this.data.entrySet()) {
            pw.println(Messages.formatKeyValuePair(entry.getKey(), entry.getValue()));
        }

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK );
    }

    /**
     * Returns the value of the HTTP request parameter with the given name.
     *
     * @return <code>null</code> if the value of the parameter is
     *         <code>null</code> or is the empty string
     */
    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || "".equals(value)) {
        return null;

      } else {
        return value;
      }
    }

}
