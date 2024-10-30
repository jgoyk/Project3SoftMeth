package clinic;
import util.Date;

/**
 * Represents an appointment in a clinic system, storing information such as 
 * the date, timeslot, patient profile, and provider information.
 * 
 * @author Dhawal Arora (Netid: da812)
 */
public class Appointment implements Comparable<Appointment> {
    protected Date date; 
    protected Timeslot timeslot;
    protected Person patient;
    protected Person provider;

    /**
     * Default constructor that initializes the appointment with null values.
     */
    public Appointment() {
        this.date = null;
        this.timeslot = null;
        this.patient = null;
        this.provider = null;
    }

    /**
     * Constructs an appointment with the specified date, timeslot, patient, and provider.
     *
     * @param date The date of the appointment.
     * @param timeslot The timeslot of the appointment.
     * @param patient The patient for the appointment.
     * @param provider The provider (doctor/technician) for the appointment.
     */
    public Appointment(Date date, Timeslot timeslot, Person patient, Person provider) {
        this.date = new Date(date);
        this.timeslot = timeslot;
        this.patient = patient;
        this.provider = provider;
    }

    /**
     * Copy constructor to create a deep copy of another appointment.
     *
     * @param targetAppointment The appointment to copy from.
     */
    public Appointment(Appointment targetAppointment) {
        this.date = new Date(targetAppointment.getDate());
        this.timeslot = targetAppointment.getTimeslot();
        this.patient = targetAppointment.getPatient();
        this.provider = targetAppointment.getProvider();
    }

    /**
     * Gets the date of the appointment.
     *
     * @return The date of the appointment.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Gets the timeslot of the appointment.
     *
     * @return The timeslot of the appointment.
     */
    public Timeslot getTimeslot() {
        return timeslot;
    }

    /**
     * Gets the patient involved in the appointment.
     *
     * @return The patient for the appointment.
     */
    public Person getPatient() {
        return patient;
    }

    /**
     * Gets the provider handling the appointment.
     *
     * @return The provider for the appointment.
     */
    public Person getProvider() {
        return provider;
    }

    /**
     * Returns a string representation of the appointment, combining the date, timeslot, patient, and provider information.
     *
     * @return A string representing the appointment.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(date.toString()).append(" ");
        result.append(timeslot.toString()).append(" ");
        result.append(patient.toString()).append(" ");
        result.append(provider.toString());
        return result.toString();
    }

    /**
     * Compares this appointment to another based on their date and then time
     *
     * @param targetAppointment The appointment to compare against.
     * @return A negative integer, zero, or a positive integer if this appointment
     *         is less than, equal to, or greater than the specified appointment.
     */
    @Override
    public int compareTo(Appointment targetAppointment) {
        int dateComparison = this.date.compareTo(targetAppointment.date);
        if (dateComparison != 0) return dateComparison;

        return this.timeslot.compareTo(targetAppointment.timeslot);
    }

    /**
     * Checks if this appointment is equal to another based on their properties.
     *
     * @param other The object to compare against.
     * @return true if the appointments are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Appointment that = (Appointment) other;
        return date.equals(that.getDate()) &&
               timeslot.equals(that.getTimeslot()) &&
               patient.equals(that.getPatient()) &&
               provider.equals(that.getProvider());
    }


}
