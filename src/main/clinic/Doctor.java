package clinic;

/**
 * Represents a Doctor, which is a type of Provider with an associated specialty 
 * and a National Provider Identification (NPI) number.
 * 
 * @author Joshua Goykhman (Netid: jg1986)
 */
public class Doctor extends Provider {

    private Specialty specialty; // encapsulates the rate per visit based on specialty
    private String npi; // National Provider Identification unique to the doctor

    /**
     * Default constructor for the Doctor class.
     * Initializes the doctor with no specialty and no NPI number.
     */
    public Doctor() {
        super();
        specialty = null;
        npi = null;
    }

    /**
     * Constructor for the Doctor class with specified profile, location, NPI, and specialty.
     *
     * @param profile the profile of the doctor (includes name, contact details, etc.)
     * @param location the location where the doctor practices
     * @param npi the National Provider Identification number of the doctor
     * @param specialty the specialty of the doctor, which determines their rate
     */
    public Doctor(Profile profile, Location location, String npi, Specialty specialty) {
        super(profile, location);
        this.specialty = specialty;
        this.npi = npi;
    }

    /**
     * Gets the National Provider Identification (NPI) number of the doctor.
     *
     * @return the NPI number of the doctor
     */
    public String getNpi() {
        return npi;
    }

    /**
     * Returns the rate charged by the doctor based on their specialty.
     *
     * @return the charge rate as determined by the doctor's specialty
     */
    @Override
    public int rate() {
        return specialty.getCharge();
    }

    /**
     * Returns a string representation of the Doctor, which includes
     * the details from the Provider class along with the doctor's specialty and NPI number.
     *
     * @return a string containing the provider's profile, location, specialty, and NPI number
     */
    @Override
    public String toString() {
        return (super.toString() + "[" + specialty.name() + ", #" + npi + "]");
    }
}
