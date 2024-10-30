package clinic;

/**
 * Represents a Patient, which is a type of Person with an associated visit history.
 * 
 * @author Dhawal Arora (Netid: da812)
 */
public class Patient extends Person{
    private Visit visit;

    /**
     * Constructs a Patient with a null profile and visits.
     */
    public Patient() {
        super();  // Calls the default constructor of Person (initializes profile as null)
        visit = null;
    }

    /**
     * Constructs a Patient using the given appointment.
     * The patient's profile is set from the appointment's patient, and the visit is initialized.
     *
     * @param appointment the appointment associated with the new patient
     */
    public Patient(Appointment appointment) {
        super(appointment.getPatient().getProfile());  // Sets the profile using the appointment's patient profile
        visit = new Visit(appointment);
    }

    /**
     * Constructs a Patient with the given profile.
     *
     * @param profile the profile of the patient
     */
    public Patient(Profile profile) {
        super(profile);  // Calls the constructor of Person to set the profile
        visit = null;
    }

    /**
     * Gets the head node of the visit linked list for this patient.
     *
     * @return the head visit node
     */
    public Visit getVisitHeadNode() {
        return visit;
    }

    /**
     * Calculates the total charge for all visits associated with this patient.
     *
     * @return the total charge as an integer
     */
    public int charge() {
        int totalCharge = 0;
        return totalCharge;
    }

    /**
     * Adds a visit node for a given appointment to this patient's visits.
     *
     * @param appt the appointment to be added as a visit
     */
    public void addVisitNode(Appointment appt) {
        Visit newVisit = new Visit(appt);
        newVisit.setNextVisitNode(visit);
        visit = newVisit;
    }

    /**
     * Returns a string representation of the patient, including the profile and total amount due.
     *
     * @return a string representation of the patient
     */
    @Override
    public String toString() {
        return super.toString() + " [amount due: $" + String.format("%,.2f", (float) charge()) + "]";
    }
}
