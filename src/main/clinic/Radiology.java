package clinic;

/**
 * Enum representing the types of radiology services offered in the clinic.
 * This includes CAT scan, ultrasound, and X-ray services.
 * 
 * The Radiology enum is used in imaging appointments to specify which
 * type of imaging service the appointment is for.
 * 
 * @author Dhawal Arora (Netid: da812)
 */
public enum Radiology {
    CATSCAN("CAT Scan"),
    ULTRASOUND("Ultrasound"),
    XRAY("X-Ray");

    private final String serviceName;

    /**
     * Constructor for the Radiology enum.
     *
     * @param serviceName The display name for the radiology service.
     */
    Radiology(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * Gets the display name of the radiology service.
     *
     * @return The display name of the radiology service.
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Returns a string representation of the radiology service.
     *
     * @return The string representation of the radiology service.
     */
    @Override
    public String toString() {
        return serviceName;
    }
}
