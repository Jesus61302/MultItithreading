/* MULTITHREADING <BookingClient.java>
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * <Student Name>
 * <Student EID>
 * <Student 5-digit Unique No.>
 * Slip days used: <0>
 * Fall 2021
 */

/*
* Remember that method parameters and return types may not be altered except as specified in the description PDF. If in doubt, ask.
*/
package assignment6;

import java.util.Map;
import java.util.List;
import java.lang.Thread;

public class BookingClient {

	/**
     * @param offices maps ticket office id to seat class preferences of customers in line
     * @param flight the flight for which tickets are sold for
     */
    public BookingClient(Map<String, SeatClass[]> offices, Flight flight) {
        // TODO: Implement this constructor
    }

    /**
     * Starts the ticket office simulation by creating (and starting) threads
     * for each ticket office to sell tickets for the given flight
     *
     * @return list of threads used in the simulation,
     * should have as many threads as there are ticket offices
     */
    public List<Thread> simulate() {
        // TODO: Implement this method
        return null;
    }

    public static void main(String[] args) {
        // TODO: Initialize test data to description
    }
}
