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
import java.time.LocalDate;
import java.time.ZoneId;
import static util.Sort.appointment;
import static util.Sort.provider;


public class ClinicManagerController {
    private static final int INDEX_COMMAND = 0;
    private static final int INDEX_APPOINTMENT_DATE = 0;
    private static final int INDEX_TIMESLOT = 1;
    private static final int INDEX_FIRST_NAME = 2;
    private static final int INDEX_LAST_NAME = 3;
    private static final int INDEX_DATE_OF_BIRTH = 4;
    private static final int INDEX_NPI = 5;
    private static final int INDEX_IMAGING_TYPE = 5;
    private static final int INDEX_NEWTIMESLOT = 5;
    private static final int D_OR_T_COMMAND_LENGTH = 6;
    private static final int VALID_C_COMMAND_LENGTH = 5;
    private static final int VALID_R_COMMAND_LENGTH = 6;

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

    private static final int COMBO_NOT_SELECTED_VALUE = -1;

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
    private static final String[] TIMESLOTS = {"9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM"};


    @FXML
    private ComboBox<String> timeslotsCombo;
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
    private DatePicker existingDateOfAppt, existingDateOfBirth;
    @FXML
    private TextField existingFirstName, existingLastName;
    @FXML
    private ComboBox<String> existingTimeslotCombo, newTimeslotCombo;
    @FXML
    private TextArea outputArea;

    @FXML
    private void newApptOnClick() {
        System.out.println("Clicked New Appointment");
        if(!submissionValidator()) {
            return;
        }
        String[] commandArray = {dateOfAppt.getValue().toString(), Integer.toString(timeslotsCombo.getSelectionModel().getSelectedIndex()), firstName.getText(), lastName.getText(), dateOfBirth.getValue().toString(), Integer.toString(providersCombo.getSelectionModel().getSelectedIndex())};
        if(selectedVisitType.getText().equals("Imaging Service")){
            technicianCreator(commandArray);
        } else {
            Appointment newOfficeAppointment = createOfficeAppointment(commandArray);
            if(newOfficeAppointment != null){
                addAppointmentToList(newOfficeAppointment, BOOKED_VALUE);
            }
        }
    }

    @FXML
    private void cancelApptOnClick() {
        System.out.println("Clicked Cancel Appointment");
        if(!submissionValidator()) {
            return;
        }
        String[] commandArray = {dateOfAppt.getValue().toString(), Integer.toString(timeslotsCombo.getSelectionModel().getSelectedIndex()), firstName.getText(), lastName.getText(), dateOfBirth.getValue().toString(), Integer.toString(providersCombo.getSelectionModel().getSelectedIndex())};
        cancelAppointment(commandArray);
    }

    @FXML
    private void clearOnClick() {
        System.out.println("Clicked Clear");
        dateOfAppt.setValue(null);
        firstName.setText(null);
        lastName.setText(null);
        dateOfBirth.setValue(null);
        selectedVisitType = (RadioButton) visitType.getSelectedToggle();
        outputArea.setText(null);
        if(selectedVisitType != null){
            selectedVisitType.setSelected(false);
        }
        timeslotsCombo.getSelectionModel().clearSelection();
        providersCombo.getSelectionModel().clearSelection();
        timeslotsCombo.setValue(null);
        providersCombo.setValue(null);
        timeslotsCombo.setPromptText("Timeslot");
        providersCombo.setPromptText("Provider");
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
        for(String timeslot : TIMESLOTS){
            timeslotsCombo.getItems().add(timeslot);
        }
        loadProviders.setDisable(true);

    }


    private boolean submissionValidator(){
        selectedVisitType = (RadioButton) visitType.getSelectedToggle();
        if(selectedVisitType == null){
            outputArea.setText("Please select an appointment type." + "\n");
            return false;
        }
        if(dateOfAppt == null){
            outputArea.setText("Please select an appointment date." + "\n");
            return false;
        }
        if(firstName == null || lastName == null || dateOfBirth == null){
            outputArea.setText("Please enter patients first and last name, and date of birth" + "\n");
            return false;
        }
        if(timeslotsCombo.getSelectionModel().getSelectedIndex() == COMBO_NOT_SELECTED_VALUE){
            outputArea.setText("Please select an appointment timeslot." + "\n");
            return false;
        }
        if(providersCombo.getSelectionModel().getSelectedIndex() == COMBO_NOT_SELECTED_VALUE){
            outputArea.setText("Please select an appointment timeslot." + "\n");
            return false;
        }
        return true;
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


    /**
     * Creates an appointment based off commandLine input array
     * Returns the new appointment object if successful, null otherwise
     * @param commandArray an array from command line input with data to create appointment
     @return an appointment or null object based on provided commandLine input array
     */
    private Appointment createOfficeAppointment(String[] commandArray){


        Date date = null;
        Timeslot slot = null;
        Person patient = null;
        Person provider = null;
        try{
            String[] dateArr = commandArray[INDEX_APPOINTMENT_DATE].split("-");
            date = new Date(Integer.parseInt(dateArr[1]), Integer.parseInt(dateArr[2]), Integer.parseInt(dateArr[0]));
        }catch(Exception e){
            outputArea.setText("Appointment date: " + commandArray[INDEX_APPOINTMENT_DATE] + " is not a valid calendar date");
            return null;
        }
        if(!appointmentDateValidator(date)){
            return null;
        }
        patient = profileCreator(commandArray[INDEX_FIRST_NAME], commandArray[INDEX_LAST_NAME], commandArray[INDEX_DATE_OF_BIRTH]);
        if(patient == null){
            return null;
        }
        try{
            slot = timeslotCreator(commandArray[INDEX_TIMESLOT]);
            provider = doctorList.get(Integer.parseInt(commandArray[INDEX_IMAGING_TYPE]));
        }catch(Exception e){
            return null;
        }
        if(!date.isValid()){
            System.out.println("Appointment date: " + commandArray[INDEX_APPOINTMENT_DATE] + " is not a valid calendar date");
        }
        if( slot == null || provider == null){
            return null;
        }
        return new Appointment(date, slot, patient, provider);
    }

    /**
     * Creates an appointment based off commandLine input array
     * Returns the new appointment object if successful, null otherwise
     * @param commandArray an array from command line input with data to create appointment
     @return an appointment or null object based on provided commandLine input array
     */
    private Appointment createTechnicianAppointment(String[] commandArray){

        if(commandArray.length != D_OR_T_COMMAND_LENGTH){
            System.out.println("Missing data tokens.");
            return null;
        }
        Date date = null;
        Timeslot slot = null;
        Person patient = null;
        Person provider = null;
        Radiology room = null;
        try{
            date = new Date(commandArray[INDEX_APPOINTMENT_DATE]);
        }catch(Exception e){
            System.out.println("Appointment date: " + commandArray[INDEX_APPOINTMENT_DATE] + " is not a valid calendar date");
            return null;
        }
        if(!appointmentDateValidator(date)){
            return null;
        }
        patient = profileCreator(commandArray[INDEX_FIRST_NAME], commandArray[INDEX_LAST_NAME], commandArray[INDEX_DATE_OF_BIRTH]);
        if(patient == null){
            return null;
        }
        slot = timeslotCreator(commandArray[INDEX_TIMESLOT]);
        if( slot == null){
            return null;
        }
        room = radiologyCreator(commandArray[INDEX_IMAGING_TYPE]);
        if( room == null){
            return null;
        }
        if(!imagingAppointmentValid(date,slot, patient)){
            System.out.println(patient.toString() +" has an existing appointment at the same time slot.");
            return null;
        }
        provider = technicianAssigner(room, slot);
        if( provider == null){
            return null;
        }
        if(!date.isValid()){
            System.out.println("Appointment date: " + commandArray[INDEX_APPOINTMENT_DATE] + " is not a valid calendar date");
        }

        return new Imaging(date, slot, patient, provider, room);
    }

    /**
     * Cancels an appointment based on the provided command array.
     *
     * The command array must contain the required number of elements defined by `VALID_C_COMMAND_LENGTH`.
     * It extracts the appointment date, timeslot, and patient profile from the command array.
     * If a matching appointment is found, it is removed from the appointment list.
     * Otherwise, a message indicating that the appointment does not exist is printed.
     *
     * @param commandArray An array containing the appointment details.
     */
    private void cancelAppointment(String[] commandArray) {
        Date date = null;
        Timeslot slot = null;
        Profile profile = null;
        try {
            String[] apptDateArr = commandArray[INDEX_APPOINTMENT_DATE].split("-");
            String[] dateOfBirthArr = commandArray[INDEX_DATE_OF_BIRTH].split("-");
            date = new Date(Integer.parseInt(apptDateArr[1]), Integer.parseInt(apptDateArr[2]), Integer.parseInt(apptDateArr[0]));
            slot = new Timeslot(Integer.parseInt(commandArray[INDEX_TIMESLOT]));
            profile = new Profile(commandArray[INDEX_FIRST_NAME], commandArray[INDEX_LAST_NAME], new Date(Integer.parseInt(dateOfBirthArr[1]), Integer.parseInt(dateOfBirthArr[2]), Integer.parseInt(dateOfBirthArr[0])));
        } catch (Exception e) {
            outputArea.setText(date + " " + slot + " " + profile + " - appointment does not exist.");
        }
        Iterator<Appointment> iterator = appointmentList.iterator();
        while (iterator.hasNext()) {
            Appointment apptToCheck = iterator.next();
            if (apptToCheck.getPatient().getProfile().equals(profile) && apptToCheck.getTimeslot().equals(slot) && apptToCheck.getDate().equals(date)) {
                appointmentList.remove(apptToCheck);
                outputArea.setText(date + " " + slot + " " + profile + " - appointment has been canceled.");
                return;
            }
        }
        outputArea.setText(date + " " + slot + " " + profile + " - appointment does not exist.");
    }

    /**
     * Reschedules an appointment based on the provided command array.
     *
     * The command array must contain the required number of elements defined by `VALID_R_COMMAND_LENGTH`.
     * It extracts the appointment date, old timeslot, new timeslot, and patient profile.
     * If a matching appointment is found, it is removed and a new appointment is added with the updated timeslot.
     * A message indicating success or failure is printed accordingly.
     *
     * @param commandArray An array containing the appointment details.
     */
    private void rescheduleAppointment(String[] commandArray) {
        if (commandArray.length != VALID_R_COMMAND_LENGTH) {
            System.out.println("Missing data tokens.");
            return;
        }
        Date date = null;
        Timeslot slot = null;
        Timeslot newTimeslot = null;
        Profile profile = null;
        try {
            date = new Date(commandArray[INDEX_APPOINTMENT_DATE]);
            slot = new Timeslot(Integer.parseInt(commandArray[INDEX_TIMESLOT]));
            profile = new Profile(commandArray[INDEX_FIRST_NAME], commandArray[INDEX_LAST_NAME], new Date(commandArray[INDEX_DATE_OF_BIRTH]));
            newTimeslot = new Timeslot(Integer.parseInt(commandArray[INDEX_NEWTIMESLOT]));
        } catch (Exception e) {
            System.out.println(date + " " + slot + " " + profile + " does not exist.");
        }

        Iterator<Appointment> iterator = appointmentList.iterator();
        while (iterator.hasNext()) {
            Appointment apptToCheck = iterator.next();
            if (apptToCheck.getPatient().getProfile().equals(profile) && apptToCheck.getTimeslot().equals(slot) && apptToCheck.getDate().equals(date)) {
                if (addAppointmentToList(new Appointment(date, newTimeslot, new Person(profile), apptToCheck.getProvider()), RESCHEDULE_VALUE)) {
                    appointmentList.remove(apptToCheck);
                }
                return;
            }
        }
        System.out.println(date + " " + slot + " " + profile + " does not exist.");
    }


    /**
     * Outputs the appointment list in sorted order based on the specified criteria.
     *
     * If the appointment list is not empty, it sorts the appointments according to the given order and type,
     * then prints them with appropriate headers. If the list is empty, a message indicating that the schedule is empty is printed.
     *
     * @param order The sorting criteria (e.g., patient date time).
     * @param apptType The type of appointment (e.g., office, imaging, both).
     */
    private void outputInSortedOrder(char order, int apptType) {
        if (!appointmentList.isEmpty() && !listEmptied) {
            Sort.appointment(appointmentList, order);
            switch (apptType) {
                case APPOINTMENT_TYPE_OFFICE:
                    System.out.println(OUTPUT_HEADER_ARRAY[PRINT_OFFICE_VALUE]);
                    break;
                case APPOINTMENT_TYPE_IMAGING:
                    System.out.println(OUTPUT_HEADER_ARRAY[PRINT_IMAGING_VALUE]);
                    break;
                case APPOINTMENT_TYPE_BOTH:
                    switch (order) {
                        case PATIENT_DATE_TIME:
                            System.out.println(OUTPUT_HEADER_ARRAY[PRINT_PATIENT_VALUE]);
                            break;
                        case DATE_TIME_PROVIDER_NAME:
                            System.out.println(OUTPUT_HEADER_ARRAY[PRINT_APPOINTMENT_VALUE]);
                            break;
                        case COUNTY_DATE_TIME:
                            System.out.println(OUTPUT_HEADER_ARRAY[PRINT_LOCATION_VALUE]);
                            break;
                    }
                    break;
            }
            for (int i = 0; i < appointmentList.size(); i++) {
                if (apptType == APPOINTMENT_TYPE_BOTH) System.out.println(appointmentList.get(i).toString());
                if (apptType == APPOINTMENT_TYPE_OFFICE && !(appointmentList.get(i) instanceof Imaging)) System.out.println(appointmentList.get(i).toString());
                if (apptType == APPOINTMENT_TYPE_IMAGING && appointmentList.get(i) instanceof Imaging) System.out.println(appointmentList.get(i).toString());
            }
            System.out.println("** end of list **");
        } else {
            System.out.println("Schedule calendar is empty.");
        }
    }


    /**
     * Validates if a patient can schedule an imaging appointment at a specific date and timeslot.
     *
     * It checks if the patient already has an appointment at the given date and timeslot.
     *
     * @param date The date of the desired imaging appointment.
     * @param slot The timeslot of the desired imaging appointment.
     * @param patient The patient for whom the appointment is being scheduled.
     * @return true if the appointment can be scheduled, false otherwise.
     */
    private boolean imagingAppointmentValid(Date date, Timeslot slot, Person patient) {
        Iterator<Appointment> iterator = appointmentList.iterator();
        while (iterator.hasNext()) {
            Appointment apptToCheck = iterator.next();
            if (apptToCheck.getPatient().equals(patient) && apptToCheck.getTimeslot().equals(slot) && apptToCheck.getDate().equals(date)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Creates a Radiology service based on the provided service string.
     *
     * If the service string matches a defined Radiology service, it returns the corresponding service.
     * Otherwise, it prints a message indicating the service is not provided and returns null.
     *
     * @param serviceString The name of the desired radiology service.
     * @return The corresponding Radiology service, or null if not found.
     */
    private Radiology radiologyCreator(String serviceString) {
        Radiology room = null;
        for (Radiology service : Radiology.values()) {
            if (service.name().equalsIgnoreCase(serviceString.trim())) {
                room = service;
            }
        }
        if (room == null) {
            System.out.println(serviceString + " - imaging service not provided.");
            return null;
        }
        return room;
    }

    /**
     Creates a profile object based off inputs
     If any errors occur they are printed and null is return
     @param room a string representing patient first name
     @return Profile object based off input data
     */
    private Person technicianAssigner(Radiology room, Timeslot timeslot){
        Person technician = null;
        Node head = technicianList.getHead();
        Node ptr = technicianList.getHead();
        Iterator<Appointment> iterator;
        boolean found = false;
        do{
            iterator = appointmentList.iterator();
            while (iterator.hasNext()){
                Appointment apptToCheck = iterator.next();
                if (apptToCheck instanceof Imaging){
                    Imaging imagingAppt = (Imaging)(apptToCheck);
                    Technician currTechnician = (Technician)(imagingAppt.getProvider());
                    if (imagingAppt.getTimeslot().equals(timeslot)){
                        if (currTechnician.getLocation().equals(ptr.getTechnician().getLocation()) && imagingAppt.getRoom().equals(room)){
                            found = true;
                        } else if (currTechnician.getProfile().equals(ptr.getTechnician().getProfile())){
                            found = true;
                        }
                    }

                }
            }
            if (found){
                found = false;
                ptr = ptr.getNext();
            } else {
                technicianList.setHead(ptr.getNext());
                return ptr.getTechnician();
            }
        } while(ptr != head);
        System.out.println("Cannot find an available technician at all locations for " + room.name() + " at slot "+ timeslot.getTimeslotInt() +".");
        return null;

    }


    /**
     Creates a profile object based off inputs
     If any errors occur they are printed and null is return
     @param firstName a string representing patient first name
     @param lastName a string representing patient last name
     @param dateOfBirthString a string representing patient date of birth
     @return Profile object based off input data
     */
    private Person profileCreator(String firstName, String lastName, String dateOfBirthString){
        Date date = null;
        String[] dateArr = dateOfBirthString.split("-");
        date = new Date(Integer.parseInt(dateArr[1]), Integer.parseInt(dateArr[2]), Integer.parseInt(dateArr[0]));

        if(date == null || !date.isValid()){
            System.out.println("Patient dob: " + dateOfBirthString + " is not a valid calendar date");
            return null;
        }
        if(checkDateValid(date) > 0){
            System.out.println("Patient dob: " + dateOfBirthString + " is today or a date after today.");
            return null;
        }
        Profile profile;
        Person person;
        try{
            profile = new Profile(firstName, lastName, date);
        }catch(Exception e){
            return null;
        }
        try{
            person = new Person(profile);
        }catch(Exception e){
            return null;
        }

        return person;
    }


    /**
     Returns a timeslot object based off string input, returns null if the string is invalid

     @param timeslotString a string representing the timeslot object to be created
     @return A timeslot object created from input string
     */
    private Timeslot timeslotCreator(String timeslotString){
        int timeslot = 0;
        try {
            timeslot = Integer.parseInt(timeslotString);
        } catch(Exception e){
            System.out.println(timeslotString + " is not a valid time slot.");
            return null;
        }
        if (timeslot > 0 && timeslot < 13){
            return new Timeslot(timeslot);
        } else {
            System.out.println(timeslotString + " is not a valid time slot.");
            return null;
        }
    }
    /**
     Returns a provider object based off input string,
     If provider doesn't exist, it prints the error and returns null
     @param npi a string representing the doctors NPI
     @return A provider object based off input string
     */
    private Person doctorFinder(String npi){

        for(int i = 0; i < doctorList.size(); i++){
            if(doctorList.get(i).getNpi().equals(npi)){
                return doctorList.get(i);
            }
        }
        System.out.println(npi + " - provider doesn't exist.");
        return null;
    }
    /**
     Checks if given date is not today or before, and is within 6 months
     Returns an integer value based on the validity of the date
     @param targetDate the date object to be checked
     @return an integer based on if the date is valid, or what the issue is
     */
    private int checkDateValid(Date targetDate){
        if(targetDate.compareTo(getSystemDate(NO_OFFSET_VALUE)) == DATE_IS_TODAY){
            return DATE_IS_TODAY;
        }
        if(targetDate.compareTo(getSystemDate(NO_OFFSET_VALUE)) == DATE_BEFORE_TODAY){
            return DATE_BEFORE_TODAY;
        }
        if(targetDate.compareTo(getSystemDate(SIX_MONTH_OFFSET)) != 1){
            return DATE_IS_VALID;
        }else{
            return DATE_NOT_WITHIN_SIX_MONTHS;
        }

    }
    /**
     Gets current system date and returns a Date object of the current date with the month offset added
     @param monthOffset An integer value based on how much the
     @return A date object created from the system date and month offset
     */
    public Date getSystemDate(int monthOffset){

        Calendar appointmentCalendar = Calendar.getInstance();
        appointmentCalendar.add(Calendar.DATE, NO_OFFSET_VALUE);
        appointmentCalendar.add(Calendar.YEAR, NO_OFFSET_VALUE);
        appointmentCalendar.add(Calendar.MONTH, monthOffset + MONTH_INDEX_OFFSET);


        return new Date(appointmentCalendar.get(Calendar.MONTH), appointmentCalendar.get(Calendar.DAY_OF_MONTH), appointmentCalendar.get(Calendar.YEAR));
    }

    /**
     * Adds an appointment to list and prints a status based on statusValue
     * Returns true or false based on success of appointment list addition
     * @param appointment The new appointment to be added to the list
     * @param statusValue An integer value to differentiate which status to print after adding new appointment
     * @return a true or false boolean value depending on if the appointment was successfully added to list
     */
    boolean addAppointmentToList(Appointment appointment, int statusValue){
        listEmptied = false;
        if(appointment instanceof Imaging){
            appointmentList.add(appointment);
            if (statusValue == BOOKED_VALUE){
                outputArea.setText(appointment.toString() + " booked.");
            } else {
                outputArea.setText( "Rescheduled to " + appointment.toString());
            }
            return true;
        }
        if (appointmentValidator(appointment)){
            appointmentList.add(appointment);
            if (statusValue == BOOKED_VALUE){
                outputArea.setText(appointment.toString() + " booked.");
            } else {
                outputArea.setText( "Rescheduled to " + appointment.toString());
            }
            return true;
        }
        return false;

    }

    /**
     * Checks for timeslot conflicts for appointment input and checks if appointment date is a valid date
     * Prints error if there is a conflict or validity issue, and returns true or false based on success
     * @param appointment An appointment object to be validated
     * @return a boolean value based on if the appointment is valid or not
     */
    private boolean appointmentValidator(Appointment appointment){

        if(appointmentList.contains(appointment)){
            outputArea.setText(appointment.getPatient().toString()+" has an existing appointment at the same time slot.");
            return false;
        }
        Iterator<Appointment> iterator = appointmentList.iterator();
        while (iterator.hasNext()){
            Appointment apptToCheck = iterator.next();
            if (apptToCheck.getProvider().equals(appointment.getProvider()) && apptToCheck.getTimeslot().equals(appointment.getTimeslot()) && apptToCheck.getDate().equals(appointment.getDate())){
                if (!(appointment.getProvider() instanceof Technician)){
                    Doctor apptTechnician = (Doctor)(appointment.getProvider());
                    outputArea.setText(apptTechnician.toString()+ " is not available at slot " + appointment.getTimeslot().getTimeslotInt());
                }
                return false;
            }
            if (apptToCheck.getPatient().equals(appointment.getPatient()) && apptToCheck.getTimeslot().equals(appointment.getTimeslot()) && apptToCheck.getDate().equals(appointment.getDate())){
                outputArea.setText(appointment.getPatient().toString() + " has an existing appointment at "+appointment.getDate().toString()+" "+appointment.getTimeslot().toString());
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if appointment object date is valid, not today or before today, is within six months, and is not a weekend
     * Prints any errors and returns true if successful, false otherwise
     * @param date A date object to be checked for date validity
     * @return a boolean value based on if the appointment date is a valid appointment date
     */
    private boolean appointmentDateValidator(Date date){
        //Check appointment date is valid calendar date
        if(!date.isValid()){
            outputArea.setText("Appointment date: " + date.toString() + " is not a valid calendar date");
            return false;
        }
        //Check if appointment date is today
        if(checkDateValid(date) <= DATE_IS_TODAY){
            outputArea.setText("Appointment date: " + date.toString() + " is today or a date before today.");
            return false;
        }
        //Check if appointment date is within 6 months
        if(checkDateValid(date) == DATE_NOT_WITHIN_SIX_MONTHS){
            outputArea.setText("Appointment date: " + date.toString() + " is not within six months.");
            return false;
        }
        //Check if appointment date is a weekend
        Calendar appointmentCalendar = Calendar.getInstance();
        try{
            appointmentCalendar.set(date.getYear(), date.getMonth() - CALENDAR_OFFSET, date.getDay());
        }catch(Exception e){
            outputArea.setText("Date can not be convert to Calendar object");
            return false;
        }
        int day = appointmentCalendar.get(Calendar.DAY_OF_WEEK);
        if(day == Calendar.SATURDAY || day == Calendar.SUNDAY){
            outputArea.setText("Appointment date: " + date.toString() + " is Saturday or Sunday.");
            return false;
        }
        return true;
    }

    /**
     * Generates and prints billing statements for patients based on appointments in the appointment list.
     *
     * The method accumulates the total billing amounts for each patient by iterating through the
     * appointments, using the provider's rate for billing. The results are printed in an ordered format
     * showing each patient's profile information along with the total amount due. If the appointment list
     * is empty, a message indicating that the schedule is empty is printed.
     */
    private void printBillingStatements() {
        if (!appointmentList.isEmpty()) {
            listEmptied = true;
            appointment(appointmentList, PATIENT_DATE_TIME);
            int[] patientBills = new int[appointmentList.size()];
            int[] patientHashCodeList = new int[appointmentList.size()];
            Person[] patientList = new Person[appointmentList.size()];
            Iterator<Appointment> iterator = appointmentList.iterator();
            boolean found = false;
            int idx = -1;
            int size = 0;
            while (iterator.hasNext()) {
                found = false;
                idx = -1;
                Appointment appointment = iterator.next();
                Person patient = appointment.getPatient();
                Provider provider = (Provider) appointment.getProvider();
                int billingAmount = provider.rate();
                for (int i = 0; i < appointmentList.size(); i++) {
                    if (patientHashCodeList[i] != 0) {
                        if (patientHashCodeList[i] == patient.hashCode()) {
                            found = true;
                            idx = i;}}}
                if (found) {
                    patientBills[idx] = patientBills[idx] + billingAmount;
                } else {
                    patientList[size] = patient;
                    patientHashCodeList[size] = patient.hashCode();
                    patientBills[size] = billingAmount;
                    size++;}}
            outputArea.setText("** Billing statement ordered by patient. **");
            int count = 1;
            for (int i = 0; i < size; i++) {
                Person patient = patientList[i];
                int dueAmount = patientBills[i];
                String profileInfo = patient.getProfile().toString();
                System.out.printf("(%d) %s [due: $%,.2f]%n", count++, profileInfo, (double) dueAmount);}

            System.out.println("** end of list **");
        } else {System.out.println("Schedule calendar is empty.");}
    }



    /**
     * Calculates and prints the expected credit amounts for each provider based on the appointments in the appointment list.
     *
     * If the appointment list is not empty, the method accumulates billing amounts for each provider,
     * stores them in respective arrays, and prints the credit amounts ordered by provider.
     * The output includes each provider's profile information along with their total credit amount.
     * If the appointment list is empty, a message indicating that the schedule is empty is printed.
     */
    private void printExpectedCredits() {
        if (!appointmentList.isEmpty()) {
            listEmptied = true;
            // Use an iterator for the appointmentList
            appointment(appointmentList, PROVIDER_NAME_DOB);
            int[] providerCredits = new int[appointmentList.size()];
            int[] providerHashCodeList = new int[appointmentList.size()];
            Provider[] providerList = new Provider[appointmentList.size()];
            Iterator<Appointment> iterator = appointmentList.iterator();
            boolean found = false;
            int idx = -1;
            int size = 0;
            // Accumulate total billing amounts per provider
            while (iterator.hasNext()) {
                found = false;
                idx = -1;
                Appointment appointment = iterator.next();
                Provider provider = (Provider) appointment.getProvider();
                int billingAmount = provider.rate();
                for (int i = 0; i < appointmentList.size(); i++) {
                    if (providerHashCodeList[i] != 0) {
                        if (providerHashCodeList[i] == provider.hashCode()) {
                            found = true;
                            idx = i;
                        }
                    }
                }
                if (found) {
                    providerCredits[idx] = providerCredits[idx] + billingAmount;
                } else {
                    providerList[size] = provider;
                    providerHashCodeList[size] = provider.hashCode();
                    providerCredits[size] = billingAmount;
                    size++;
                }}
            // Print in the required format
            System.out.println("** Credit amount ordered by provider. **");
            int count = 1;
            for (int i = 0; i < size; i++) {
                Person patient = providerList[i];
                int creditAmount = providerCredits[i];
                String profileInfo = patient.getProfile().toString();
                System.out.printf("(%d) %s [credit amount: $%.2f]%n", count++, profileInfo, (double) creditAmount);
            }
            System.out.println("** end of list **");
        } else {
            System.out.println("Schedule calendar is empty.");
        }
    }

    @FXML
    protected void rescheduleApptOnClick() {
        try {
            // Collect existing appointment details
            LocalDate apptDate = existingDateOfAppt.getValue();
            LocalDate dob = existingDateOfBirth.getValue();
            String firstName = existingFirstName.getText();
            String lastName = existingLastName.getText();
            String oldTimeslot = existingTimeslotCombo.getValue();
            String newTimeslot = newTimeslotCombo.getValue();

            // Validate inputs
            if (apptDate == null || dob == null || firstName.isEmpty() || lastName.isEmpty() ||
                    oldTimeslot == null || newTimeslot == null) {
                outputArea.setText("All fields must be filled to reschedule an appointment.");
                return;
            }

            // Convert LocalDate to Date
            Date apptDateConverted = Date.from(apptDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date dobConverted = Date.from(dob.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Prepare data for ClinicManager's reschedule method
            String[] commandArray = {
                    apptDateConverted.toString(), // Appointment date
                    oldTimeslot,                  // Existing timeslot
                    firstName,                    // First name
                    lastName,                     // Last name
                    dobConverted.toString(),      // Date of birth
                    newTimeslot                   // New timeslot
            };

            clinicManager.rescheduleAppointment(commandArray);

            // Clear the form
            clearRescheduleForm();

        } catch (Exception e) {
            outputArea.setText("Error rescheduling appointment. Please check your inputs.");
            e.printStackTrace();
        }
    }

    private void clearRescheduleForm() {
        existingDateOfAppt.setValue(null);
        existingDateOfBirth.setValue(null);
        existingFirstName.clear();
        existingLastName.clear();
        existingTimeslotCombo.setValue(null);
        newTimeslotCombo.setValue(null);
    }

}