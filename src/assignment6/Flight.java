/* MULTITHREADING <Flight.java>
 * EE422C Project 6 submission by
 * Jesus Hernandez
 * jh69848
 * 17155
 * Slip days used: 1
 * April 2023
 */

/*
Remember that method parameters and return types should not be altered, in general. Please see the Project Description for what may be altered and/or ask on Piazza.
*/
package assignment6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class Flight {
    /**
     * the delay time you will use when print tickets
     */
    private int printDelay; // 50 ms. Use it to fix the delay time between prints.
    private SalesLogs log;
	private String flightNo;

	private int firstTotal;
	private int businessTotal;
	private int economyTotal;
	private int firstSold;
	private int businessSold;
	private int economySold;
	private ArrayBlockingQueue<Ticket> ticketsToBePrinted;


    public Flight(String flightNo, int firstNumRows, int businessNumRows, int economyNumRows) {
		this.printDelay = 50;// 50 ms. Use it to fix the delay time between
		this.log = new SalesLogs();
		this.firstTotal = firstNumRows * 4;
		this.businessTotal = businessNumRows * 6;
		this.economyTotal = economyNumRows * 6;
		this.flightNo = flightNo;
		this.firstSold = 0;
		this.businessSold = 0;
		this.economySold = 0;
		this.ticketsToBePrinted = new ArrayBlockingQueue<>(firstTotal + businessTotal + economyTotal);


    }
    
    public void setPrintDelay(int printDelay) {
        this.printDelay = printDelay;
    }

    public int getPrintDelay() {
        return printDelay;
    }

    /**
     * Returns the next available seat not yet reserved for a given class
     *
     * @param seatClass a seat class(FIRST, BUSINESS, ECONOMY)
     * @return the next available seat or null if flight is full
     */
	public Seat getNextAvailableSeat(SeatClass seatClass) {
		Seat seat;

		switch (seatClass){
			case FIRST:
				seat = checkFirst();
				if (seat != null){
					return seat;
				}
				seatClass = seatClass.BUSINESS;

			case BUSINESS:
				seat = checkBuisness();
				if (seat != null){
					return seat;
				}
				seatClass = seatClass.ECONOMY;
			case ECONOMY:
				seat = checkEconomy();
				if (seat != null){
					return seat;
				}
				return null;

		}
		return null;
	}
	//checks if there is an available seat in First class and returns next available seat
	//getNextAvailableSeat Helper
	private Seat checkFirst(){
		if(firstSold >= firstTotal){
			return null;
		}
		Seat temp = getSeat(SeatClass.FIRST, firstSold);
		firstSold++;

		return temp;
	}
	//checks if there is an available seat in Business class and returns next available seat
	//getNextAvailableSeat Helper
	private Seat checkBuisness(){
		if(businessSold >= businessTotal){
			return null;
		}
		Seat temp = getSeat(SeatClass.BUSINESS, businessSold);
		businessSold++;

		return temp;
	}
	//checks if there is an available seat in Economy class and returns next available seat
	//getNextAvailableSeat Helper
	private Seat checkEconomy(){
		if(economySold >= economyTotal){
			return null;
		}
		Seat temp = getSeat(SeatClass.ECONOMY, economySold);
		economySold++;

		return temp;
	}
	//returns correct seat based on position and Class
	//getNextAvailableSeat Helper
	private synchronized Seat getSeat(SeatClass seatClass, int position) {
		int row = 0;
		int column = 0;
		SeatLetter letter = null;

		switch (seatClass){
			case FIRST:
				row = (position /4) + 1;
				column = (position % 4);
				switch (column){
					case 0:
						letter = SeatLetter.A;
						break;
					case 1:
						letter = SeatLetter.B;
						break;
					case 2:
						letter = SeatLetter.E;
						break;
					case 3:
						letter = SeatLetter.F;
						break;
				}
				break;
			case BUSINESS:
				row = (position /6) + 1 + firstTotal/4;
				column = (position % 6);
				switch (column){
					case 0:
						letter = SeatLetter.A;
						break;
					case 1:
						letter = SeatLetter.B;
						break;
					case 2:
						letter = SeatLetter.C;
						break;
					case 3:
						letter = SeatLetter.D;
						break;
					case 4:
						letter = SeatLetter.E;
						break;
					case 5:
						letter = SeatLetter.F;
						break;

				}
				break;
			case ECONOMY:
				row = (position /6) + firstTotal/4 + businessTotal/6  + 1;
				column = (position % 6);
				switch (column){
					case 0:
						letter = SeatLetter.A;
						break;
					case 1:
						letter = SeatLetter.B;
						break;
					case 2:
						letter = SeatLetter.C;
						break;
					case 3:
						letter = SeatLetter.D;
						break;
					case 4:
						letter = SeatLetter.E;
						break;
					case 5:
						letter = SeatLetter.F;
						break;

				}
				break;


		}
		Seat seat = new Seat(seatClass, row, letter);
		log.addSeat(seat);
		return seat;
	}
//	public void testGetNextAvailableSeat(){
//		System.out.println(getNextAvailableSeat(SeatClass.FIRST));
//		System.out.println(getNextAvailableSeat(SeatClass.BUSINESS));
//		System.out.println(getNextAvailableSeat(SeatClass.ECONOMY));
//
//
//
//	}



	/**
     * Prints a ticket to the console for the customer after they reserve a seat.
     *
     * @param seat a particular seat in the airplane
     * @return a flight ticket or null if a ticket office failed to reserve the seat
     */
	public Ticket printTicket(String officeId, Seat seat, int customer) throws InterruptedException {
		if (seat == null){
			return null;
		}
        Ticket ticket = new Ticket(flightNo,officeId, seat,customer);
		ticketsToBePrinted.add(ticket);
		printTicketHelper(ticket);
		return ticket;
    }

	private synchronized void printTicketHelper(Ticket ticket) throws InterruptedException {
		Thread.sleep(printDelay);
		log.addTicket(ticket);
		System.out.println(ticket);
	}


	/**
     * Lists all seats sold for this flight in the order of allocation
     *
     * @return list of seats sold
     */
    public List<Seat> getSeatLog() {
        return log.getSeatLog();
    }

    /**
     * Lists all tickets sold for this flight in order of printing.
     *
     * @return list of tickets sold
     */
    public List<Ticket> getTransactionLog() {
        return log.getTicketLog();
    }
    
    static enum SeatClass {
		FIRST(0), BUSINESS(1), ECONOMY(2);

		private Integer intValue;

		private SeatClass(final Integer intValue) {
			this.intValue = intValue;
		}

		public Integer getIntValue() {
			return intValue;
		}
	}

	static enum SeatLetter {
		A(0), B(1), C(2), D(3), E(4), F(5);

		private Integer intValue;

		private SeatLetter(final Integer intValue) {
			this.intValue = intValue;
		}

		public Integer getIntValue() {
			return intValue;
		}
	}

	/**
     * Represents a seat in the airplane
     * FIRST Class: 1A, 1B, 1E, 1F ... 
     * BUSINESS Class: 2A, 2B, 2C, 2D, 2E, 2F  ...
     * ECONOMY Class: 3A, 3B, 3C, 3D, 3E, 3F  ...
     * (Row numbers for each class are subject to change)
     */
	static class Seat {
		private SeatClass seatClass;
		private int row;
		private SeatLetter letter;

		public Seat(SeatClass seatClass, int row, SeatLetter letter) {
			this.seatClass = seatClass;
			this.row = row;
			this.letter = letter;
		}

		public SeatClass getSeatClass() {
			return seatClass;
		}

		public void setSeatClass(SeatClass seatClass) {
			this.seatClass = seatClass;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public SeatLetter getLetter() {
			return letter;
		}

		public void setLetter(SeatLetter letter) {
			this.letter = letter;
		}

		@Override
		public String toString() {
			return Integer.toString(row) + letter + " (" + seatClass.toString() + ")";
		}
	}

	/**
	 * Represents a flight ticket purchased by a customer
	 */
	static class Ticket {
		private String flightNo;
		private String officeId;
		private Seat seat;
		private int customer;
		public static final int TICKET_STRING_ROW_LENGTH = 31;

		public Ticket(String flightNo, String officeId, Seat seat, int customer) {
			this.flightNo = flightNo;
			this.officeId = officeId;
			this.seat = seat;
			this.customer = customer;
		}

		public int getCustomer() {
			return customer;
		}

		public void setCustomer(int customer) {
			this.customer = customer;
		}

		public String getOfficeId() {
			return officeId;
		}

		public void setOfficeId(String officeId) {
			this.officeId = officeId;
		}

		@Override
		public String toString() {
			String result, dashLine, flightLine, officeLine, seatLine, customerLine, eol;

			eol = System.getProperty("line.separator");

			dashLine = new String(new char[TICKET_STRING_ROW_LENGTH]).replace('\0', '-');

			flightLine = "| Flight Number: " + flightNo;
			for (int i = flightLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				flightLine += " ";
			}
			flightLine += "|";

			officeLine = "| Ticket Office ID: " + officeId;
			for (int i = officeLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				officeLine += " ";
			}
			officeLine += "|";

			seatLine = "| Seat: " + seat.toString();
			for (int i = seatLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				seatLine += " ";
			}
			seatLine += "|";

			customerLine = "| Customer: " + customer;
			for (int i = customerLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				customerLine += " ";
			}
			customerLine += "|";

			result = dashLine + eol + flightLine + eol + officeLine + eol + seatLine + eol + customerLine + eol
					+ dashLine;

			return result;
		}
	}

	/**
	 * SalesLogs are security wrappers around an ArrayList of Seats and one of Tickets
	 * that cannot be altered, except for adding to them.
	 * getSeatLog returns a copy of the internal ArrayList of Seats.
	 * getTicketLog returns a copy of the internal ArrayList of Tickets.
	 */
	static class SalesLogs {
		private ArrayList<Seat> seatLog;
		private ArrayList<Ticket> ticketLog;

		private SalesLogs() {
			seatLog = new ArrayList<Seat>();
			ticketLog = new ArrayList<Ticket>();
		}

		@SuppressWarnings("unchecked")
		public ArrayList<Seat> getSeatLog() {
			return (ArrayList<Seat>) seatLog.clone();
		}

		@SuppressWarnings("unchecked")
		public ArrayList<Ticket> getTicketLog() {
			return (ArrayList<Ticket>) ticketLog.clone();
		}

		public void addSeat(Seat s) {
			seatLog.add(s);
		}

		public void addTicket(Ticket t) {
			ticketLog.add(t);
		}
	}
}
