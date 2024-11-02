package clinic;

/**
 * Represents a visit in the clinic, which contains an appointment and a reference to the next visit.
 * @author Dhawal Arora (Netid: da812)
 */
public class Visit {
    private Appointment appointment;
    private Visit next;

    /**
     * Default constructor initializes an empty Visit.
     */
    public Visit() {
        appointment = null;
        next = null;
    }

    /**
     * Constructor initializes a Visit with a specific appointment.
     *
     * @param appt The appointment for this visit.
     */
    public Visit(Appointment appt) {
        appointment = appt;
        next = null;
    }

    /**
     * Gets the appointment associated with this visit.
     *
     * @return The appointment for this visit.
     */
    public Appointment getAppointmentNode() {
        return appointment;
    }

    /**
     * Gets the next visit in the linked list.
     *
     * @return The next Visit object.
     */
    public Visit getNextVisitNode() {
        return next;
    }

    /**
     * Traverses the linked list to find the last visit node.
     *
     * @return The last Visit object in the linked list.
     */
    public Visit getLastVisitNode() {
        Visit visitNode = this;
        while (visitNode.next != null) {
            visitNode = visitNode.next;
        }
        return visitNode;
    }

    /**
     * Sets the next visit node in the linked list.
     *
     * @param visit The next Visit object to link to this visit.
     */
    public void setNextVisitNode(Visit visit) {
        next = visit;
    }
}
