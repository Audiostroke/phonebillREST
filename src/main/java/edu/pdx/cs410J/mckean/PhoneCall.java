package edu.pdx.cs410J.mckean;

import edu.pdx.cs410J.AbstractPhoneCall;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by TylerMcKean on 7/8/15.
 */
public class PhoneCall extends AbstractPhoneCall implements Comparable<PhoneCall> {


    /**
     * Data members for a phone call object including the phone number of the caller
     * the phone number of who is being called, the data and time the call started,
     * and the data and time the call was terminated.
     */
    private String Caller;
    private String Callee;
    private Date StartTime;
    private Date EndTime;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
    private long callDuration;

    /**
     * Constructor that sets the data members of an individual phone call
     * based on what is read in from the command line.
     * @param caller String that represents the person making the call
     * @param callee String that represents the person receiving the call
     * @param endTime String that represents the time the call ended
     * @param startTime String that represents the time the call started
     */
    public PhoneCall(String caller, String callee, String startTime, String endTime) {
        this.Caller = caller;
        this.Callee = callee;
        try {
            this.StartTime = dateFormat.parse(startTime);
            this.EndTime = dateFormat.parse(endTime);
        } catch (ParseException e) {
            System.out.println("Parse exception when formatting date");
        }
        this.callDuration = TimeUnit.MILLISECONDS.toMinutes(EndTime.getTime() - StartTime.getTime());
    }

    /**
     * @return Returns the phone number of the person who originated this phone call
     */
    @Override
    public String getCaller() {
        return Caller;
    }

    /**
     * @return Returns the phone number of the person who received this phone call.
     */
    @Override
    public String getCallee() {
        return Callee;
    }

    /**
     * @return Returns a textual representation of the time that this phone call was originated.
     */
    @Override
    public String getStartTimeString() {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(StartTime);
    }

    /**
     * @return Returns a textual representation of the time that this phone call was completed.
     */
    @Override
    public String getEndTimeString() {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(EndTime);
    }

    /**
     * Returns the time that this phone call was originated as a
     * {@link Date}.
     */
    @Override
    public Date getStartTime() {
        return StartTime;
    }

    /**
     * Returns the time that this phone call was completed as a
     * {@link Date}.
     */
    @Override
    public Date getEndTime() {
        return EndTime;
    }


    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(PhoneCall o) {
        if(getStartTime().before(o.getStartTime())) {
            return -1;
        }
        if(getStartTime().after(o.getStartTime())) {
            return 1;
        }
        else {
            return this.getCaller().compareTo(o.getCaller());
        }
    }

    public long getCallDuration() {
        return callDuration;
    }
}


