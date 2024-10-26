package clinic;
import util.Date;

/**
 * Represents a profile for a patient, including personal details such as name and date of birth.
 * This class implements the Comparable interface to enable comparison between Profile objects.
 *
 * @author Dhawal Arora (Netid: da812)
 */
public class Profile implements Comparable<Profile> {
    public static final int EQUAL_COMP_VALUE = 0;
    public static final int GREATER_COMP_VALUE = 1;
    public static final int LESSER_COMP_VALUE = -1;


    private String firstName;
    private String lastName;
    private Date dateOfBirth;

    /**
     * Constructs a Profile with default values (empty name and null date of birth).
     */
    public Profile() {
        firstName = "";
        lastName = "";
        dateOfBirth = null;
    }

    /**
     * Constructs a Profile with the specified first name, last name, and date of birth.
     *
     * @param firstName the first name of the profile
     * @param lastName the last name of the profile
     * @param dateOfBirth the date of birth of the profile
     */
    public Profile(String firstName, String lastName, Date dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = new Date(dateOfBirth.getMonth(), dateOfBirth.getDay(), dateOfBirth.getYear());
    }

    /**
     * Constructs a Profile with the specified first name, last name, and date of birth
     * provided as separate month, day, and year values.
     *
     * @param firstName the first name of the profile
     * @param lastName the last name of the profile
     * @param month the month of birth
     * @param day the day of birth
     * @param year the year of birth
     */
    public Profile(String firstName, String lastName, int month, int day, int year) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = new Date(month, day, year);
    }

    /**
     * Constructs a Profile using another Profile object.
     *
     * @param profile the Profile object to copy from
     */
    public Profile(Profile profile) {
        firstName = profile.getFirstName();
        lastName = profile.getLastName();
        dateOfBirth = new Date(profile.getDateOfBirth());
    }

    /**
     * Gets the first name of this profile.
     *
     * @return the first name of the profile
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of this profile.
     *
     * @return the last name of the profile
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the date of birth of this profile.
     *
     * @return the date of birth of the profile
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Compares this profile with another profile based on last name, then first name,
     * then date of birth.
     *
     * @param targetProfile the profile to compare with
     * @return -1, 0, or 1 as this profile
     *         is less than, equal to, or greater than the specified profile
     */
    @Override
    public int compareTo(Profile targetProfile) {
        int comparison = lastName.compareTo(targetProfile.getLastName());
        if (comparison != 0) {
            return Integer.compare(comparison, 0);
        }

        comparison = firstName.compareTo(targetProfile.getFirstName());
        if (comparison != 0) {
            return Integer.compare(comparison, 0);
        }

        comparison = dateOfBirth.compareTo(targetProfile.getDateOfBirth());
        return Integer.compare(comparison, 0);
    }


    /**
     * Returns a string representation of this profile, including the first name,
     * last name, and date of birth.
     *
     * @return a string representation of the profile
     */
    @Override
    public String toString() {
        return firstName + " " + lastName + " " + dateOfBirth.toString();
    }

    /**
     * Checks if this profile is equal to another object.
     *
     * @param obj the object to compare with
     * @return true if the other object is a Profile and has the same first name,
     *         last name, and date of birth; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Profile)) {
            return false;
        }
        Profile targetProfile = (Profile) obj;
        return firstName.equalsIgnoreCase(targetProfile.getFirstName()) &&
                lastName.equalsIgnoreCase(targetProfile.getLastName()) &&
                dateOfBirth.equals(targetProfile.getDateOfBirth());
    }
    /**
     * Generates a hash code for this object using its first name, last name, and date of birth.
     * @return an integer hash code representing the object based on its first name, last name, and date of birth.
     */
    @Override
    public int hashCode(){
        int result = 17;
        result = 31 * result + (firstName != null ? firstName.toLowerCase().hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.toLowerCase().hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        return result;
    }
}