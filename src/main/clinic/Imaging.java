package clinic;
import util.Date;
/**
 * Represents an imaging appointment that extends the Appointment class.
 * Includes additional details specific to imaging services, such as the type of imaging room.
 * This class is used for appointments involving X-rays, ultrasounds, or CAT scans.
 * 
 * Imaging services are assigned to a specific room based on the type of imaging service.
 * 
 * @author Dhawal Arora (Netid: da812)
 */
public class Imaging extends Appointment {
    private Radiology room; // Room type for the imaging service (X-ray, ultrasound, or CAT scan)

    /**
     * Default constructor that initializes the imaging appointment with null values.
     */
    public Imaging() {
        super();
        this.room = null;
    }

    /**
     * Constructs an imaging appointment with the specified date, timeslot, patient, provider, and room type.
     *
     * @param date The date of the appointment.
     * @param timeslot The time slot of the appointment.
     * @param patient The patient for the appointment.
     * @param provider The technician/provider for the appointment.
     * @param room The radiology room for the imaging service (X-ray, ultrasound, CAT scan).
     */
    public Imaging(Date date, Timeslot timeslot, Person patient, Person provider, Radiology room) {
        super(date, timeslot, patient, provider);
        this.room = room;
    }

    /**
     * Gets the type of room assigned for this imaging appointment.
     *
     * @return The type of radiology room (X-ray, ultrasound, or CAT scan).
     */
    public Radiology getRoom() {
        return room;
    }

    /**
     * Sets the type of room for this imaging appointment.
     *
     * @param room The type of radiology room to assign (X-ray, ultrasound, CAT scan).
     */
    public void setRoom(Radiology room) {
        this.room = room;
    }

    /**
     * Returns a string representation of the imaging appointment, combining the date, time slot,
     * patient, provider, and room information.
     *
     * @return A string representing the imaging appointment.
     */
    @Override
    public String toString() {
        return super.toString() + "[" + room.name() +"]";
    }

    /**
     * Checks if this imaging appointment is equal to another based on the date, timeslot, patient,
     * provider, and room type.
     *
     * @param other The object to compare against.
     * @return true if the appointments are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Imaging)) {
            return false;
        }
        Imaging otherImaging = (Imaging) other;
        return super.equals(otherImaging) && room == otherImaging.room;
    }


}
