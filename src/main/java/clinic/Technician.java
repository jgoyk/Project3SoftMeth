package clinic;

/**
 * Represents a Technician, a type of Provider who performs medical procedures.
 * A Technician has a profile, location, and a rate charged per visit.
 * 
 * @author Joshua Goykhman (Netid: jg1986)
 */
public class Technician extends Provider {
    private final int RATE_NOT_SET = -1; // Constant representing an unset rate
    private int ratePerVisit; // Rate charged per visit

    /**
     * Constructs a Technician with a null profile, no location, and an unset rate.
     * Initializes the technician with default values.
     */
    public Technician() {
        super(); // Calls the default constructor of Provider (initializes profile and location as null)
        ratePerVisit = RATE_NOT_SET; // Sets the rate to the constant indicating not set
    }

    /**
     * Constructs a Technician with the specified profile, location, and rate per visit.
     *
     * @param profile the profile of the technician
     * @param location the location where the technician operates
     * @param ratePerVisit the rate charged per visit
     */
    public Technician(Profile profile, Location location, int ratePerVisit) {
        super(profile, location); // Calls the constructor of Provider to set the profile and location
        this.ratePerVisit = ratePerVisit; // Sets the rate per visit
    }

    /**
     * Gets the rate charged by the technician per visit.
     *
     * @return the rate charged per visit as an integer
     */
    @Override
    public int rate() {
        return ratePerVisit; // Returns the rate charged per visit
    }

    /**
     * Returns a string representation of the technician, including their profile, location, and rate.
     *
     * @return a string containing the technician's details and rate
     */
    @Override
    public String toString() {
        return (super.toString() + "[rate: $" + ratePerVisit + ".00]"); // Combines superclass toString with rate information
    }
}
