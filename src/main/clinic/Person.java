package clinic;

/**
 * Represents a person in the clinic system. A person can either be a patient or a provider.
 * This class holds a Profile that stores the person's information such as name and date of birth.
 * 
 * The Person class implements Comparable to allow sorting based on the profile.
 * 
 * @author Joshua Goykhman (Netid: jg1986)
 */
public class Person implements Comparable<Person> {
    protected Profile profile;  // The profile of the person, containing their details like name and DOB

    /**
     * Default constructor that initializes the person with a null profile.
     */
    public Person() {
        this.profile = null;
    }

    /**
     * Constructor to create a Person with a specified profile.
     *
     * @param profile The profile of the person, containing details like name and date of birth.
     */
    public Person(Profile profile) {
        this.profile = new Profile(profile);  // Creates a deep copy of the profile
    }

    /**
     * Gets the profile of the person.
     *
     * @return The profile of the person.
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Sets the profile of the person.
     *
     * @param profile The profile to set.
     */
    public void setProfile(Profile profile) {
        this.profile = new Profile(profile);  // Sets a deep copy of the profile
    }

    /**
     * Compares this person with another person based on their profiles.
     *
     * @param other The person to compare against.
     * @return A negative integer, zero, or a positive integer as this person is less than,
     * equal to, or greater than the specified person.
     */
    @Override
    public int compareTo(Person other) {
        return this.profile.compareTo(other.profile);  // Comparison based on profile details (like name, DOB)
    }

    /**
     * Returns a string representation of the person, which is the string representation of their profile.
     *
     * @return The string representation of the person.
     */
    @Override
    public String toString() {
        return profile.toString();
    }

    /**
     * Checks if this person is equal to another object based on their profiles.
     *
     * @param obj The object to compare against.
     * @return true if the profiles are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Person other = (Person) obj;
        return this.profile.equals(other.profile);
    }

    /**
     * Generates a hash code for the person based on their profile.
     *
     * @return The hash code of the person.
     */
    @Override
    public int hashCode() {
        return profile.hashCode();
    }
}
