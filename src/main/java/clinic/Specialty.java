package clinic;

/**
 * Enum representing various medical specialties and their associated charges.
 * @author Dhawal Arora (Netid: da812)
 */
public enum Specialty {
    FAMILY(250),
    PEDIATRICIAN(300),
    ALLERGIST(350);

    private final int charge;

    /**
     * Constructor for the Specialty enum.
     *
     * @param charge The charge associated with the specialty.
     */
    Specialty(int charge) {
        this.charge = charge;
    }

    /**
     * Gets the charge associated with the specialty.
     *
     * @return The charge for the specialty.
     */
    public int getCharge() {
        return charge;
    }
}
