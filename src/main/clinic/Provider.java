package clinic;

/**
 * Represents a Provider, which is an abstract class that extends Person.
 * A Provider has a profile and operates in a specific location.
 * 
 * @author Joshua Goykhman (Netid: jg1986)
 */
public abstract class Provider extends Person {

    private Location location; // Location where the provider operates

    /**
     * Constructs a Provider with a null profile and location.
     * Initializes the provider with no profile and no location information.
     */
    public Provider() {
        super();  // Calls the default constructor of Person (initializes profile as null)
        location = null;
    }

    /**
     * Constructs a Provider with the specified profile and location.
     *
     * @param profile the profile of the provider
     * @param location the location where the provider operates
     */
    public Provider(Profile profile, Location location) {
        super(profile);  // Calls the constructor of Person to set the profile
        this.location = location;
    }

    /**
     * Abstract method to get the rate charged by the provider.
     * This method must be implemented by subclasses to provide specific charge rates.
     *
     * @return the charge rate as an integer
     */
    public abstract int rate();

    /**
     * Gets the location of the provider.
     *
     * @return the location of the provider
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Returns a string representation of the provider, including their profile and location details.
     *
     * @return a string containing the provider's profile and location information
     */
    @Override
    public String toString() {
        return ("[" + super.toString() + ", " + location.name() + ", " + location.getCounty() + " " + location.getZip() + "]");
    }
}
