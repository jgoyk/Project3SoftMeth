package com.example.project3softmeth;

import clinic.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Scanner;

import util.CircularLinkedList;
import util.Date;
import java.io.File;
import util.List;
import util.Node;
import util.Sort;

import static util.Sort.appointment;
import static util.Sort.provider;


public class ClinicManagerController {
    private static final int INDEX_COMMAND = 0;
    private static final int INDEX_APPOINTMENT_DATE = 1;
    private static final int INDEX_TIMESLOT = 2;
    private static final int INDEX_FIRST_NAME = 3;
    private static final int INDEX_LAST_NAME = 4;
    private static final int INDEX_DATE_OF_BIRTH = 5;
    private static final int INDEX_NPI = 6;
    private static final int INDEX_IMAGING_TYPE = 6;
    private static final int INDEX_NEWTIMESLOT = 6;
    private static final int D_OR_T_COMMAND_LENGTH = 7;
    private static final int VALID_C_COMMAND_LENGTH = 6;
    private static final int VALID_R_COMMAND_LENGTH = 7;

    private static final int DOCTOR_COMMAND_LENGTH = 7;
    private static final int TECHNICIAN_COMMAND_LENGTH = 6;
    private static final int PROVIDER_FIRST_NAME_INDEX = 1;
    private static final int PROVIDER_LAST_NAME_INDEX = 2;
    private static final int PROVIDER_DOB_INDEX = 3;
    private static final int PROVIDER_LOCATION_INDEX = 4;
    private static final int DOCTOR_SPECIALTY_INDEX = 5;
    private static final int TECHNICIAN_RATE_INDEX = 5;
    private static final int DOCTOR_NPI_INDEX = 6;

    private static final int MIN_TIMESLOT_INDEX = 1;
    private static final int MAX_TIMESLOT_INDEX = 6;

    private static final String[] OUTPUT_HEADER_ARRAY = {"** List of appointments, ordered by date/time/provider.", "** Appointments ordered by patient/date/time **",
            "** List of appointments, ordered by county/date/time.", "** List of office appointments ordered by county/date/time.",
            "** List of radiology appointments ordered by county/date/time."};
    private static final int PRINT_APPOINTMENT_VALUE = 0;
    private static final int PRINT_PATIENT_VALUE = 1;
    private static final int PRINT_LOCATION_VALUE = 2;
    private static final int PRINT_OFFICE_VALUE = 3;
    private static final int PRINT_IMAGING_VALUE = 4;

    private static final int DATE_IS_TODAY = 0;
    private static final int DATE_BEFORE_TODAY = -1;
    private static final int DATE_IS_VALID = 1;
    private static final int DATE_NOT_WITHIN_SIX_MONTHS = 2;

    private static final int DATE_INDEX_MONTH = 0;
    private static final int DATE_INDEX_DAY = 1;
    private static final int DATE_INDEX_YEAR = 2;

    private static final int CALENDAR_OFFSET = 1;
    private static final int NO_OFFSET_VALUE = 0;
    private static final int SIX_MONTH_OFFSET = 6;
    private static final int MONTH_INDEX_OFFSET = 1;

    private static final int BOOKED_VALUE = 1;
    private static final int RESCHEDULE_VALUE = 2;


    private static final char PROVIDER_NAME_DOB = 'N';
    private static final char DATE_TIME_PROVIDER_NAME = 'A'; //Used for PA Command
    private static final char PATIENT_DATE_TIME = 'P'; // Used for PP and PS commands
    private static final char COUNTY_DATE_TIME = 'L'; // Used for PL, PO, and PI commands
    private static final int APPOINTMENT_TYPE_BOTH = 0;
    private static final int APPOINTMENT_TYPE_OFFICE = 1;
    private static final int APPOINTMENT_TYPE_IMAGING = 2;

    private static List<Provider> providerList = new List<Provider>();
    private static CircularLinkedList technicianList = new CircularLinkedList();
    private static List<Doctor> doctorList = new List<Doctor>();
    private static List<Appointment> appointmentList = new List<Appointment>();
    private static boolean listEmptied = false;

    @FXML
    private DatePicker dateOfAppt;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private DatePicker dateOfBirth;
    @FXML
    private ToggleGroup visitType;
    @FXML
    private TextArea outputArea;
    @FXML
    private Button loadProviders;
    @FXML
    private ComboBox<String> providersCombo;

    private RadioButton selectedVisitType;

    @FXML
    private void newApptOnClick() {
        System.out.println("Clicked New Appointment");
        System.out.println(dateOfAppt.getValue().toString());
        System.out.println(firstName.getText());
        System.out.println(lastName.getText());
        System.out.println(dateOfBirth.getValue().toString());
        selectedVisitType = (RadioButton) visitType.getSelectedToggle();
        if(selectedVisitType != null){
            System.out.println(selectedVisitType.getText());
        }

    }

    @FXML
    private void cancelApptOnClick() {
        System.out.println("Clicked Cancel Appointment");

    }

    @FXML
    private void clearOnClick() {
        System.out.println("Clicked Clear");
        dateOfAppt.setValue(null);
        firstName.setText(null);
        lastName.setText(null);
        dateOfBirth.setValue(null);
        selectedVisitType = (RadioButton) visitType.getSelectedToggle();
        selectedVisitType.setSelected(false);
        outputArea.setText(null);
    }

    @FXML
    private void loadProvidersOnClick() {
        System.out.println("Clicked LoadProviders");
        try {
            File providerFile = new File("src/main/java/providers.txt");
            Scanner fileScanner = new Scanner(providerFile);
            while (fileScanner.hasNextLine()) {
                String commandArray = fileScanner.nextLine();
                providerCreator(commandArray);
            }
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
        provider(providerList);
        technicianList.reverse();
        outputArea.setText("Providers loaded to the list." + "\n");
        for (int i = 0; i < providerList.size(); i++) {
            providersCombo.getItems().add(providerList.get(i).getProfile().getFirstName() + " " + providerList.get(i).getProfile().getLastName());
            outputArea.appendText(providerList.get(i).toString() + "\n");
        }
        outputArea.appendText("Rotation list for the technicians." + "\n");
        Node currNode = technicianList.getHead();
        for (int i = technicianList.getSize() - 1; i >= 0; i--) {
            Technician technician = currNode.getTechnician();
            String name = technician.getProfile().getFirstName() + " " + technician.getProfile().getLastName();
            String location = technician.getLocation().toString();
            outputArea.appendText(name + " (" + location + ")");

            if (i != 0) {
                outputArea.appendText(" --> ");
            }

            currNode = currNode.getNext();
        }
        loadProviders.setDisable(true);

    }

    /**
     * Creates a new provider based on the input command received from the user.
     * @param inputLine A string representing the input command from the user.
     */
    private void providerCreator(String inputLine){
        if(inputLine.isEmpty()){
            return;
        }
        String[] inputList = inputLine.split("  ");
        switch (inputList[INDEX_COMMAND]) {
            case "D": //Create new doctor
                doctorCreator(inputList);
                break;
            case "T": //Create new technician
                technicianCreator(inputList);
                break;
            default: // Command not recognized
                System.out.println("Invalid command!");
                break;
        }

    }
    /**
     * Creates a Doctor object from the provided command array and adds it to the provider and doctor lists.
     *
     * The command array must contain the required number of elements defined by `DOCTOR_COMMAND_LENGTH`.
     * It extracts the doctor's profile information, NPI number, location, and specialty,
     * and initializes a Doctor instance which is then added to the `providerList` and `doctorList`.
     *
     * @param commandArray An array containing the doctor's details.
     */
    private void doctorCreator(String[] commandArray) {
        if (commandArray.length != DOCTOR_COMMAND_LENGTH) {
            return;
        }

        Date dateOfBirth = new Date(commandArray[PROVIDER_DOB_INDEX]);
        Profile profile = new Profile(commandArray[PROVIDER_FIRST_NAME_INDEX], commandArray[PROVIDER_LAST_NAME_INDEX], dateOfBirth);
        String npi = commandArray[DOCTOR_NPI_INDEX];
        String locationString = commandArray[PROVIDER_LOCATION_INDEX];
        String specialtyString = commandArray[DOCTOR_SPECIALTY_INDEX];
        Location providerLocation = null;
        Specialty doctorSpecialty = null;

        for (Location location : Location.values()) {
            if (location.name().equals(locationString.trim())) {
                providerLocation = location;
            }
        }
        for (Specialty specialty : Specialty.values()) {
            if (specialty.name().equals(specialtyString.trim())) {
                doctorSpecialty = specialty;
            }
        }
        Doctor doctor = new Doctor(profile, providerLocation, npi, doctorSpecialty);
        providerList.add(doctor);
        doctorList.add(doctor);
    }

    /**
     * Creates a Technician object from the provided command array and adds it to the provider and technician lists.
     *
     * The command array must contain the required number of elements defined by `TECHNICIAN_COMMAND_LENGTH`.
     * It extracts the technician's profile information, rate, and location,
     * and initializes a Technician instance which is then added to the `providerList` and `technicianList`.
     *
     * @param commandArray An array containing the technician's details.
     */
    private void technicianCreator(String[] commandArray) {
        if (commandArray.length != TECHNICIAN_COMMAND_LENGTH) {
            return;
        }

        Date dateOfBirth = new Date(commandArray[PROVIDER_DOB_INDEX]);
        Profile profile = new Profile(commandArray[PROVIDER_FIRST_NAME_INDEX], commandArray[PROVIDER_LAST_NAME_INDEX], dateOfBirth);
        int rate = Integer.parseInt(commandArray[TECHNICIAN_RATE_INDEX]);
        String locationString = commandArray[PROVIDER_LOCATION_INDEX];
        Location providerLocation = null;

        for (Location location : Location.values()) {
            if (location.name().equals(locationString.trim())) {
                providerLocation = location;
            }
        }

        Technician technician = new Technician(profile, providerLocation, rate);
        providerList.add(technician);
        technicianList.add(technician);
    }



}