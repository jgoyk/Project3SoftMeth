package util;
import clinic.*;
import util.List;
/**
 * A utility class for sorting appointments and providers.
 * 
 * The class provides methods to sort lists of appointments and providers
 * based on various criteria using a selection sort algorithm.
 * 
 * @author Dhawal Arora (Netid: da812)
 */
public class Sort {
    public static final int EQUAL = 0; // Constant used to indicate equality
    public static final char DATE_TIME_PROVIDER_NAME = 'A'; // Sort by appointment date/time and provider name
    public static final char PATIENT_DATE_TIME = 'P'; // Sort by patient and appointment date/time
    public static final char COUNTY_DATE_TIME = 'L'; // Sort by provider county and appointment date/time
    public static final char PROVIDER_NAME_DOB = 'N'; // Sort by provider name and date of birth

    /**
     * Sorts a list of appointments based on the specified key.
     *
     * @param list The list of appointments to sort.
     * @param key The key to sort by (as specified by the defined constants).
     */
    public static void appointment(List<Appointment> list, char key) {
        for (int i = 0; i < list.size() - 1; i++) {
            int min = i; // Assume the minimum is the first element
            for (int j = i + 1; j < list.size(); j++) {
                Appointment current = list.get(j);
                Appointment minElement = list.get(min);
                if (compareAppointments(current, minElement, key) < 0) {
                    min = j; // Update min if current is less than minElement
                }
            }
            Appointment temp = list.get(min);
            list.set(min, list.get(i)); // Swap the found minimum element with the first element
            list.set(i, temp);
        }
    }

    /**
     * Sorts a list of providers.
     *
     * @param list The list of providers to sort.
     */
    public static void provider(List<Provider> list) { // Used for Providers.txt import and PC command
        for (int i = 0; i < list.size() - 1; i++) {
            int min = i; // Assume the minimum is the first element
            for (int j = i + 1; j < list.size(); j++) {
                Provider current = list.get(j);
                Provider minElement = list.get(min);
                if (current.getProfile().compareTo(minElement.getProfile()) < 0) {
                    min = j; // Update min if current provider profile is less than minElement
                }
            }
            Provider temp = list.get(min);
            list.set(min, list.get(i)); // Swap the found minimum element with the first element
            list.set(i, temp);
        }
    }

    /**
     * A helper method that compares two appointments based on the specified key.
     *
     * @param a1 The first appointment to compare.
     * @param a2 The second appointment to compare.
     * @param key The key(s) to compare by.
     * @return -1 if a1 is less than a2, 0 if they are equal, 1 if a1 is greater than a2.
     */
    private static int compareAppointments(Appointment a1, Appointment a2, char key) {
        int dateTimeComparison = a1.compareTo(a2);
        int patientComparison = a1.getPatient().getProfile().compareTo(a2.getPatient().getProfile());
        Provider provider1 = (Provider)(a1.getProvider());
        Provider provider2 = (Provider)(a2.getProvider());
        int providerComparison = provider1.compareTo(provider2);
        int countyComparison = provider1.getLocation().getCounty().compareTo(provider2.getLocation().getCounty());

        if (key == DATE_TIME_PROVIDER_NAME) {
            if (dateTimeComparison != EQUAL) {
                return dateTimeComparison; // Compare by date/time first
            } else {
                return a1.getProvider().getProfile().compareTo(a2.getProvider().getProfile()); // Then compare by provider
            }
        } else if (key == PATIENT_DATE_TIME) {
            if (patientComparison != EQUAL) {
                return patientComparison; // Compare by patient first
            } else {
                return dateTimeComparison; // Then compare by date/time
            }
        } else if (key == COUNTY_DATE_TIME) {
            if (countyComparison != EQUAL) {
                return countyComparison; // Compare by county first
            } else if (dateTimeComparison != EQUAL) {
                return dateTimeComparison; // Then compare by date/time
            } else {
                return patientComparison; // Finally compare by patient
            }
        } else if (key == PROVIDER_NAME_DOB) {
            return providerComparison; // Compare by provider name and date of birth
        }
        return EQUAL; // Default return for equal
    }
}
