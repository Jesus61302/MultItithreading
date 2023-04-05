/* MULTITHREADING <Flight.java>
 * EE422C Project 6 submission by
 * Jesus Hernandez
 * jh69848
 * 17155
 * Slip days used: 1
 * April 2023
 */

/*
* Remember that method parameters and return types may not be altered except as specified in the description PDF. If in doubt, ask.
*/
package assignment6;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.lang.Thread;

public class BookingClient {


    private Map<String, Flight.SeatClass[]> offices;
    private Flight flight;
    private int customerID;
    private Boolean flightFull;
    private Boolean soldOutMessagePrinted;

    /**
     * @param offices maps ticket office id to seat class preferences of customers in line
     * @param flight the flight for which tickets are sold for
     */
    public BookingClient(Map<String, Flight.SeatClass[]> offices, Flight flight) {
        this.offices = offices;
        this.flight = flight;
        this.customerID = 0;
        this.flightFull = false;
        this.soldOutMessagePrinted = false;
    }

    /**
     * Starts the ticket office simulation by creating (and starting) threads
     * for each ticket office to sell tickets for the given flight
     *
     * @return list of threads used in the simulation,
     * should have as many threads as there are ticket offices
     */
    public List<Thread> simulate() {
        Thread[] threads = new Thread[offices.size()];
        int i = 0;
        for (Map.Entry<String,Flight.SeatClass[]> entry:
             offices.entrySet()) {
            threads[i] = new Thread(new Operate(entry.getKey(), entry.getValue()));
            threads[i].start();
            i++;
        }
        return Arrays.asList(threads);
    }
    public static void main(String[] args) throws InterruptedException {
        final Flight.SeatClass[] seatPreferences1 = new Flight.SeatClass[]{Flight.SeatClass.BUSINESS,
                Flight.SeatClass.BUSINESS, Flight.SeatClass.BUSINESS};
        final Flight.SeatClass[] seatPreferences2 = new Flight.SeatClass[]{Flight.SeatClass.FIRST, 
                Flight.SeatClass.BUSINESS, Flight.SeatClass.ECONOMY, Flight.SeatClass.ECONOMY};
        final Flight.SeatClass[] seatPreferences3 = new Flight.SeatClass[]{Flight.SeatClass.BUSINESS,
                Flight.SeatClass.ECONOMY, Flight.SeatClass.ECONOMY};
        final Flight.SeatClass[] seatPreferences4 = new Flight.SeatClass[]{Flight.SeatClass.ECONOMY,
                Flight.SeatClass.ECONOMY, Flight.SeatClass.ECONOMY};
        final Flight.SeatClass[] seatPreferences5 = new Flight.SeatClass[]{Flight.SeatClass.BUSINESS,
                Flight.SeatClass.BUSINESS, Flight.SeatClass.BUSINESS};

        Map<String, Flight.SeatClass[]> offices = new HashMap<String, Flight.SeatClass[]>() {{
            put("TO1", seatPreferences1);
            put("TO2", seatPreferences2);
            put("TO3", seatPreferences3);
            put("TO4", seatPreferences4);
            put("TO5", seatPreferences5);
            
        }};

        Flight flight = new Flight("TRI123", 1, 1, 1);
        BookingClient bookingClient = new BookingClient(offices, flight);

        bookingClient.joinAllThreads(bookingClient.simulate());

    }
    private static void joinAllThreads(List<Thread> threads)
            throws InterruptedException {
        for (Thread t : threads) {
            t.join();
        }
    }
    class Operate implements Runnable{


        private Flight.SeatClass[] preferences;
        private String office;

        public Operate(String office, Flight.SeatClass[] preferences){
            this.preferences = preferences;
            this.office = office;

        }


        @Override
        public void run() {
            Flight.Ticket ticket;
            Flight.Seat seat;
            Boolean customersInline = true;
            while(customersInline && !flightFull){
                for(Flight.SeatClass seatType: preferences){
                    seat = flight.getNextAvailableSeat(seatType);
                    if(seat == null){
                        flightFull = true;
                        break;
                    }
                    customerID++;
                    try {
                        flight.printTicket(office, seat, customerID);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                customersInline = false;
            }
            if(flightFull){
                soldOut();
            }
        }

        private synchronized void soldOut(){
            if (!soldOutMessagePrinted){
                System.out.println("Sorry, we are sold out");
                soldOutMessagePrinted = true;
            }
        }

    }
}
