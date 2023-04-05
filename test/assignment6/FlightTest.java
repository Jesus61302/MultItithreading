
package assignment6;

import org.junit.Test;

import static org.junit.Assert.*;

public class FlightTest {

    @Test
    public void setPrintDelay() {
        System.out.println(1/4);
    }

    @Test
    public void getPrintDelay() {
    }

    @Test
    public void getNextAvailableSeat() {
        Flight flight = new Flight("ABC", 2,3,7);
//        for (int i = 0; i < 10; i++){
//            flight.testGetNextAvailableSeat();
//        }
    }

    @Test
    public void printTicket() throws InterruptedException {
        Flight flight = new Flight("ABC", 2,3,7);
        Flight.Seat seat = new Flight.Seat(Flight.SeatClass.FIRST, 2, Flight.SeatLetter.A);
        flight.printTicket("ABC", seat, 3);
    }

    @Test
    public void getSeatLog() {
    }

    @Test
    public void getTransactionLog() {
    }
}