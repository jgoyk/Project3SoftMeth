package projectthree;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import projectone.*;
import projecttwo.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Scanner;

public class ClinicManagerController {

    List<Provider> providerList = new List<Provider>();
    List<Appointment> AppointmentList = new List<Appointment>();
    TechnicianRotator technicianRotator;
    static MedicalRecord medicalRecord = new MedicalRecord();

    @FXML  TabPane tabPane;
    @FXML
    TextField dateField;
    @FXML
    TextField timeSlotField;
    @FXML
    TextField firstNameField;
    @FXML
    TextField lastNameField;
    @FXML  TextField dobField;
    @FXML  TextField providerField;
    @FXML  RadioButton officeRadio;
    @FXML  RadioButton imagingRadio;
    @FXML  TextArea scheduleOutputArea;

    @FXML  TextField manageDateField;
    @FXML  TextField manageTimeSlotField;
    @FXML  TextField manageFirstNameField;
    @FXML  TextField manageLastNameField;
    @FXML  TextField manageDobField;
    @FXML  TextField newTimeSlotField;
    @FXML  TextArea manageOutputArea;

    @FXML  TableView<Appointment> appointmentTableView;
    @FXML  TableColumn<Appointment, String> dateColumn;
    @FXML  TableColumn<Appointment, String> timeColumn;
    @FXML  TableColumn<Appointment, String> patientColumn;
    @FXML  TableColumn<Appointment, String> providerColumn;
    @FXML  TableColumn<Appointment, String> typeColumn;

    @FXML  TableView<FinancialEntry> financialTableView;
    @FXML  TableColumn<FinancialEntry, String> nameColumn;
    @FXML  TableColumn<FinancialEntry, String> amountColumn;

    @FXML
    public void initialize() {
        initializePreLoadData();
        initializeTableColumns();
        printProviderInfo();
        dateColumn.prefWidthProperty().bind(appointmentTableView.widthProperty().multiply(0.2));
        timeColumn.prefWidthProperty().bind(appointmentTableView.widthProperty().multiply(0.2));
        patientColumn.prefWidthProperty().bind(appointmentTableView.widthProperty().multiply(0.2));
        providerColumn.prefWidthProperty().bind(appointmentTableView.widthProperty().multiply(0.2));
        typeColumn.prefWidthProperty().bind(appointmentTableView.widthProperty().multiply(0.2));
        nameColumn.prefWidthProperty().bind(financialTableView.widthProperty().multiply(0.5));
        amountColumn.prefWidthProperty().bind(financialTableView.widthProperty().multiply(0.5));
    }

    void initializeTableColumns() {
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTimeslot().toString()));
        patientColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPatient().toString()));
        providerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProvider().toString()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue() instanceof Imaging ? "Imaging" : "Office"));

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        amountColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("$%.2f", cellData.getValue().getAmount())));
    }

    @FXML
    void handleScheduleAppointment(ActionEvent event) {
        String command = buildScheduleCommand();
        if (officeRadio.isSelected()) {
            scheduleAppointment_Doctor(command);
        } else {
            scheduleAppointment_Imaging(command);
        }
    }

    @FXML
    void handleCancelAppointment(ActionEvent event) {
        String command = buildCancelCommand();
        cancelAppointment(command);
    }

    @FXML
    void handleRescheduleAppointment(ActionEvent event) {
        String command = buildRescheduleCommand();
        rescheduleAppointment(command);
    }

    @FXML
    void handleDisplayAllAppointments(ActionEvent event) {
        displayAppointment_Sorted_Date();
    }

    @FXML
    void handleDisplayByPatient(ActionEvent event) {
        displayAppointment_Sorted_Patient();
    }

    @FXML
    void handleDisplayByLocation(ActionEvent event) {
        displayAppointment_Sorted_Location();
    }

    @FXML
    void handleDisplayOfficeAppointments(ActionEvent event) {
        displayAppointment_Office();
    }

    @FXML
    void handleDisplayImagingAppointments(ActionEvent event) {
        displayAppointment_Imaging();
    }

    @FXML
    void handleDisplayBillingStatements(ActionEvent event) {
        displayBillingStatements();
    }

    @FXML
    void handleDisplayProviderCredits(ActionEvent event) {
        displayProviderCredits();
    }

    String buildScheduleCommand() {
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                officeRadio.isSelected() ? "D" : "T",
                dateField.getText(),
                timeSlotField.getText(),
                firstNameField.getText(),
                lastNameField.getText(),
                dobField.getText(),
                providerField.getText());
    }

    String buildCancelCommand() {
        return String.format("C,%s,%s,%s,%s,%s",
                manageDateField.getText(),
                manageTimeSlotField.getText(),
                manageFirstNameField.getText(),
                manageLastNameField.getText(),
                manageDobField.getText());
    }

    String buildRescheduleCommand() {
        return String.format("R,%s,%s,%s,%s,%s,%s",
                manageDateField.getText(),
                manageTimeSlotField.getText(),
                manageFirstNameField.getText(),
                manageLastNameField.getText(),
                manageDobField.getText(),
                newTimeSlotField.getText());
    }

    void scheduleAppointment_Doctor(String commandLine) {
        // 实现预约医生的逻辑
        scheduleOutputArea.setText("");
        final int ProperCommandLine_Length = 7;
        String[] CommandLine_Sp = commandLine.split(",");
        //check length
        if(CommandLine_Sp.length != ProperCommandLine_Length){
            scheduleOutputArea.appendText("Missing date tokens.");
            return;
        }
        //check appointmentDate
        Date AppointmentDate = generateDate_FromString(CommandLine_Sp[1]);
        if(AppointmentDate == null){
            scheduleOutputArea.appendText(CommandLine_Sp[1] + " is not a valid calendar date");
            return;
        }
        //check timeslot
        Timeslot timeslot = generateTimeSlot_ByIndex(CommandLine_Sp[2]);
        if(timeslot == null){
            scheduleOutputArea.appendText(CommandLine_Sp[2] + " is not a valid Timeslot.");
            return;
        }
        //check PatientDob
        Date PatientDob = generateDate_FromString(CommandLine_Sp[5]);
        if(PatientDob == null){
            scheduleOutputArea.appendText("Patient dob: " + CommandLine_Sp[5] + " is not a valid calendar date");
            return;
        }
        //check patient
        Patient patient = generatePatient_ByString(CommandLine_Sp[3], CommandLine_Sp[4], PatientDob);
        if(patient == null){
            return;
        }
        //check npi
        Doctor doc = (Doctor)generateProvider_npi_ByString(CommandLine_Sp[6]);
        if(doc == null){
            return;
        }
        //final check
        Appointment finalcheckAppointment = scheduleAppointment_finalCheck(new Appointment(AppointmentDate, timeslot, patient, doc));
        if(finalcheckAppointment != null){scheduleOutputArea.appendText(finalcheckAppointment.toString() + " booked.");}
    }



    void scheduleAppointment_Imaging(String commandLine) {
        // 实现预约影像检查的逻辑
        scheduleOutputArea.setText("");
        final int ProperCommandLine_Length = 7;
        String[] CommandLine_Sp = commandLine.split(",");
        //check length
        if(CommandLine_Sp.length != ProperCommandLine_Length){scheduleOutputArea.appendText("Missing date tokens."); return;}
        //check appointmentDate
        Date AppointmentDate = generateDate_FromString(CommandLine_Sp[1]);
        if(AppointmentDate == null){scheduleOutputArea.appendText(CommandLine_Sp[1] + " is not a valid calendar date"); return;}
        //check timeslot
        Timeslot timeslot = generateTimeSlot_ByIndex(CommandLine_Sp[2]);
        if(timeslot == null){scheduleOutputArea.appendText(CommandLine_Sp[2] + " is not a valid Timeslot.");return;}
        //check PatientDob
        Date PatientDob = generateDate_FromString(CommandLine_Sp[5]);
        if(PatientDob == null){scheduleOutputArea.appendText("Patient dob: " + CommandLine_Sp[5] + " is not a valid calendar date");return;}
        //check patient
        Patient patient = generatePatient_ByString(CommandLine_Sp[3], CommandLine_Sp[4], PatientDob);
        if(patient == null){return;}
        //check radiology
        Radiology radio = generateRadiology_ByString(CommandLine_Sp[6]);
        if(radio == null){scheduleOutputArea.appendText(CommandLine_Sp[6] + " - imaging service not provided.");return;}
        //get technician
        Technician technician = generatePossibleTechnician(AppointmentDate, timeslot, radio);
        if(technician == null){scheduleOutputArea.appendText("Cannot find an available technician at all locations for " + CommandLine_Sp[6] + "at slot " + CommandLine_Sp[2]);return;}
        //final check
        Appointment finalcheckAppointment = scheduleAppointment_finalCheck(new Imaging(AppointmentDate, timeslot, patient, technician, radio));
        if(finalcheckAppointment != null){
            scheduleOutputArea.appendText(finalcheckAppointment.toString() + " booked.");
        }
    }

    void cancelAppointment(String commandLine) {
        manageOutputArea.setText("");
        final int ProperCommandLine_Length = 6;
        String[] CommandLine_Sp = commandLine.split(",");
        //check length
        if(CommandLine_Sp.length != ProperCommandLine_Length){
            manageOutputArea.appendText("Missing date tokens.");
            return;
        }
        //check appointmentDate
        Date AppointmentDate = generateDate_FromString(CommandLine_Sp[1]);
        if(AppointmentDate == null){
            manageOutputArea.appendText(CommandLine_Sp[1] + " is not a valid calendar date");
            return;
        }
        //check timeslot
        Timeslot timeslot = generateTimeSlot_ByIndex(CommandLine_Sp[2]);
        if(timeslot == null){
            manageOutputArea.appendText(CommandLine_Sp[2] + " is not a valid Timeslot.");
            return;
        }
        //check PatientDob
        Date PatientDob = generateDate_FromString(CommandLine_Sp[5]);
        if(PatientDob == null){
            manageOutputArea.appendText("Patient dob: " + CommandLine_Sp[5] + " is not a valid calendar date");
            return;
        }
        //check patient
        Patient patient = generatePatient_ByString(CommandLine_Sp[3], CommandLine_Sp[4], PatientDob);
        if(patient == null){return;}
        //find appointment
        Appointment TargetAppontment = findAppointment(patient, AppointmentDate, timeslot);
        if(TargetAppontment == null){
            manageOutputArea.appendText(CommandLine_Sp[1] + " " + timeslot.toString() + " " + patient.toString() + " - appointment does not exist.");
            return;
        }
        AppointmentList.remove(TargetAppontment);
        Provider provider = (Provider)TargetAppontment.getProvider();
        provider.changeCredit(-1);
        manageOutputArea.appendText(CommandLine_Sp[1] + " " + timeslot.toString() + " " + patient.toString() + " - appointment has been canceled.");

    }

    void rescheduleAppointment(String commandLine) {
        manageOutputArea.setText("");
        final int ProperCommandLine_Length = 7;
        String[] CommandLine_Sp = commandLine.split(",");
        //check length
        if(CommandLine_Sp.length != ProperCommandLine_Length){
            manageOutputArea.appendText("Missing date tokens.");
            return;
        }
        //check appointmentDate
        Date AppointmentDate = generateDate_FromString(CommandLine_Sp[1]);
        if(AppointmentDate == null){manageOutputArea.appendText(CommandLine_Sp[1] + " is not a valid calendar date"); return;}
        //check timeslot
        Timeslot timeslot = generateTimeSlot_ByIndex(CommandLine_Sp[2]);
        if(timeslot == null){manageOutputArea.appendText(CommandLine_Sp[2] + " is not a valid Timeslot.");return;}
        //check PatientDob
        Date PatientDob = generateDate_FromString(CommandLine_Sp[5]);
        if(PatientDob == null){manageOutputArea.appendText("Patient dob: " + CommandLine_Sp[5] + " is not a valid calendar date");return;}
        //check patient
        Patient patient = generatePatient_ByString(CommandLine_Sp[3], CommandLine_Sp[4], PatientDob);
        if(patient == null){return;}
        //find appointment
        Appointment TargetAppontment = findAppointment(patient, AppointmentDate, timeslot);
        if(TargetAppontment == null){manageOutputArea.appendText(CommandLine_Sp[1] + " " + timeslot.toString() + " " + patient.toString() + " - appointment does not exist.");return;}
        //get new Timeslot
        Timeslot timeslot_new = generateTimeSlot_ByIndex(CommandLine_Sp[6]);
        if(timeslot_new == null){manageOutputArea.appendText(CommandLine_Sp[6] + " is not a valid Timeslot.");return;}
        //find new imag
        //schedule new appointment
        Appointment modifiedAppointment = new Appointment(TargetAppontment.getDate(), timeslot_new, TargetAppontment.getPatient(), TargetAppontment.getProvider());
        //cancel old appointment
        Appointment finalcheckAppointment = scheduleAppointment_finalCheck(modifiedAppointment);
        if(finalcheckAppointment != null){AppointmentList.remove(TargetAppontment); Provider pro = (Provider)TargetAppontment.getProvider(); pro.changeCredit(-1); manageOutputArea.appendText("Rescheduled to " + finalcheckAppointment.toString());}
    }

    void displayAppointment_Sorted_Date() {
        Sort.appointment(AppointmentList, 'D');
        updateAppointmentTable(AppointmentList);
    }

    void displayAppointment_Sorted_Patient() {
        Sort.appointment(AppointmentList, 'P');
        updateAppointmentTable(AppointmentList);
    }

    void displayAppointment_Sorted_Location() {
        Sort.appointment(AppointmentList, 'L');
        updateAppointmentTable(AppointmentList);
    }

    void displayAppointment_Office() {
        List<Appointment> Appointment_Filted = new List<Appointment>();
        for(Appointment app : AppointmentList){
            if(!(app instanceof Imaging)){
                Appointment_Filted.add(app);
            }
        }
        Sort.appointment(Appointment_Filted, 'L');
        updateAppointmentTable(Appointment_Filted);

    }

    void displayAppointment_Imaging() {
        List<Appointment> Appointment_Filted = new List<Appointment>();
        for(Appointment app : AppointmentList){
            if(app instanceof Imaging){
                Appointment_Filted.add(app);
            }
        }
        Sort.appointment(Appointment_Filted, 'L');
        updateAppointmentTable(Appointment_Filted);
    }

    void displayBillingStatements() {
        updateFinancialTable(calculateBill());
    }

    void displayProviderCredits() {
        updateFinancialTable(getProviderCredits());
    }

    void updateAppointmentTable(List<Appointment> appointments) {
        ObservableList<Appointment> appointmentData = FXCollections.observableArrayList();
        for (Appointment appointment : appointments) {
            appointmentData.add(appointment);
        }
        appointmentTableView.setItems(appointmentData);
    }


    void updateFinancialTable(List<FinancialEntry> entries) {
        ObservableList<FinancialEntry> financialData = FXCollections.observableArrayList();
        for (FinancialEntry entry : entries) {
            financialData.add(entry);
        }
        financialTableView.setItems(financialData);
    }

    void initializePreLoadData() {
        readProvider_FromFile();
        technicianRotator = new TechnicianRotator(providerList);
    }

    void printProviderInfo() {
        scheduleOutputArea.appendText("Provider information:\n\n");
        scheduleOutputArea.appendText("Providers loaded to the list.\n");
        for(Provider pro : providerList){
            scheduleOutputArea.appendText(pro.toString()+"\n");
        }

        scheduleOutputArea.appendText("\nRotation list for the technicians.\n");
        String tech_Result = "";
        for(int i = 0; i < technicianRotator.getTechnicianList().length; i += 1){
            if(i != 0){
                tech_Result += " --> ";
            }

            tech_Result += technicianRotator.getTechnicianList()[i].getProfile().getFirstName() + " " +technicianRotator.getTechnicianList()[i].getProfile().getLastName() + " (" + technicianRotator.getTechnicianList()[i].getLocation().name() + ")";
        }

        scheduleOutputArea.appendText(tech_Result+"\n");

    }

    List<FinancialEntry> calculateBill() {
        List<FinancialEntry> billingStatements = new List<>();

        if(AppointmentList.size() != 0){
            medicalRecord.add(AppointmentList);
        }
        Patient[] PatientList = medicalRecord.getPatientsList();
        if(PatientList == null || PatientList.length == 0){
            return billingStatements;
        }

        List<Person> PersonList = new List<Person>();
        for(int i = 0; i < PatientList.length; i += 1){
            if(PatientList[i] != null){
                PersonList.add(PatientList[i]);
            }
        }
        Sort.Person(PersonList);

        int index = 1;
        for(Person per : PersonList){
            Patient pa = (Patient)per;
            for(int i = 0; i < PatientList.length; i += 1){
                if(PatientList[i] == null){
                    continue;
                }

                if(pa.equals(PatientList[i])){
                    String billingInfo = "(" + Integer.toString(index) + ") " + PatientList[i].toString() + " [due: $" + PatientList[i].charge() + ".00]";
                    billingStatements.add(new FinancialEntry(PatientList[i].toString(), PatientList[i].charge()));
                    index += 1;
                }
            }
        }

        AppointmentList = new List<Appointment>();

        return billingStatements;
    }



    List<FinancialEntry> getProviderCredits() {
        List<FinancialEntry> providerCredits = new List<FinancialEntry>();

        int providerIndex = 1;

        List<Person> PersonList = new List<Person>();
        for(Provider pro : providerList){
            PersonList.add(pro);
        }
        Sort.Person(PersonList);

        for(Person per : PersonList){
            Provider pro = (Provider)per;
            String output = "(" + Integer.toString(providerIndex) + ") " + pro.getProfile().toString() + " [Credit amount: $" + Integer.toString(pro.getCredit()) + ".00]";
            providerCredits.add(new FinancialEntry(pro.getProfile().toString(), pro.getCredit()));
            providerIndex += 1;
        }

        return providerCredits;
    }



    /**
     * finale check if an appointment is able to add into appointment List.
     * @param appointment
     */
    Appointment scheduleAppointment_finalCheck(Appointment appointment){

        //check if date is valid in calendar
        if(!checkvalidAppointmentDate_Calendar(appointment.getDate())){
            return null;
        }

        //check if appointment is conflict
        if(!checkvalidAppointment_Conflict(appointment)){
            return null;
        }

        AppointmentList.add(appointment);

        //change potential credit for provider
        Provider provider = (Provider)appointment.getProvider();
        provider.changeCredit(1);
        if(appointment instanceof Imaging){
            technicianRotator.jumpNext();
        }

        return appointment;
    }

    /**
     * try to read Provider File;
     */
    void readProvider_FromFile(){
        try {
            File myFile = new File("providers.txt");
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                decodeProviderLine(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            addTextToTextArea("An error occurred: ");
            e.printStackTrace();
        }
    }

    /**
     * try to get all the info from one line of providerFile
     * @param Provider_String
     */
    void decodeProviderLine(String Provider_String){

        //if line is empty
        if(Provider_String == ""){
            return;
        }
        String[] Provider_String_Seped = Provider_String.split("\\s+");

        switch (Provider_String_Seped[0]) {
            case "D":
                if(Provider_String_Seped.length != 7){
                    return;
                }
                try{
                    providerList.add(new Doctor(new Profile(Provider_String_Seped[1], Provider_String_Seped[2], generateDate_FromString(Provider_String_Seped[3])), generateLocation_FromString(Provider_String_Seped[4]), getSpecialty_FromString(Provider_String_Seped[5]), Provider_String_Seped[6]));
                }catch(Exception e){
                    addTextToTextArea("Invalid Provider information");
                }
                break;
            case "T":
                if(Provider_String_Seped.length != 6){
                    return;
                }
                try{
                    providerList.add(new Technician(new Profile(Provider_String_Seped[1], Provider_String_Seped[2], generateDate_FromString(Provider_String_Seped[3])), generateLocation_FromString(Provider_String_Seped[4]), Provider_String_Seped[5]));
                }catch(Exception e){
                    addTextToTextArea("Invalid Provider information");
                }
                break;
            default:
                addTextToTextArea("Invalid Provider information");
                return;
        }
    }

    /**
     * generate Date from string, format: m/d/y
     * @param DateString
     * @return date if success, return null if failed.
     */
    Date generateDate_FromString(String DateString){
        String[] dateString_Sep = DateString.split("/");
        if(dateString_Sep.length != 3){
            return null;
        }

        int month = -1;
        try{
            month = Integer.parseInt(dateString_Sep[0]);
        }catch(Exception e){
            return null;
        }

        int day = -1;
        try{
            day = Integer.parseInt(dateString_Sep[1]);
        }catch(Exception e){
            return null;
        }

        int year = -1;
        try{
            year = Integer.parseInt(dateString_Sep[2]);
        }catch(Exception e){
            return null;
        }

        Date date = new Date(month, day, year);

        if(date.isValid()){
            return date;
        }else{
            return null;
        }

    }

    /**
     * generate location ENUM from string
     * @param Location_String
     * @return location ENUM if exist, return null else
     */
    Location generateLocation_FromString(String Location_String){

        Location_String = Location_String.trim();

        for(Location loc_iterator : Location.values()){
            if(loc_iterator.name().toLowerCase().equals(Location_String.toLowerCase())){
                return loc_iterator;
            }
        }

        return null;
    }

    /**
     * generate Specialty ENUM from string
     * @param Specialty_String
     * @return Specialty ENUM if exist, return null else.
     */
    Specialty getSpecialty_FromString(String Specialty_String){

        Specialty_String = Specialty_String.trim();

        for(Specialty spe_iterator : Specialty.values()){
            if(spe_iterator.name().toLowerCase().equals(Specialty_String.toLowerCase())){
                return spe_iterator;
            }
        }

        return null;
    }

    /**
     * generate a TimeSlot with index in range 1-12
     * @param index_String
     * @return timeslop if success, return null else
     */
    Timeslot generateTimeSlot_ByIndex(String index_String){
        int index = -1;
        try{
            index = Integer.parseInt(index_String);
        }catch(Exception e){
            addTextToTextArea("Invalid TimeSlot: " + index_String);
            return null;
        }

        //if out of range
        if(index < 1 || index > 12){
            return null;
        }

        int hour = 0;
        //generate Hour
        if(index <= 6){
            hour = 9 + (index - 1) / 2;
        }else{
            hour = 14 + (index - 7) / 2;
        }

        int minus = 30 * (1 - index % 2);

        return new Timeslot(hour, minus);
    }

    /**
     * generatePatient from string, it will check first name, last name, and if birthday is befor today
     * @param fn
     * @param ln
     * @param Dob
     * @return return patient if all info correct, return null else.
     */
    Patient generatePatient_ByString(String fn, String ln, Date Dob){

        //check first name
        if(fn.matches(".*\\\\d.*") || fn == ""){
            addTextToTextArea(fn + " " + ln + " is not a valid name");
            return null;
        }
        //check last name
        if(ln.matches(".*\\\\d.*") || ln == ""){
            addTextToTextArea(fn + " " + ln + " is not a valid name");
            return null;
        }
        //check birthday
        if(Dob.compareTo(getSystemDate(0, 0, 0)) > 0){
            addTextToTextArea("Patient dob: " + Dob.toString() + " is not a valid date.");
            return null;
        }

        return new Patient(new Profile(fn, ln, Dob));
    }

    /**
     * try to find Doctor in provider list
     * @param npi_String
     * @return Doctor as Provider class, return null if not found or npi format wrong
     */
    Provider generateProvider_npi_ByString(String npi_String){
        int npi;
        try{
            npi = Integer.parseInt(npi_String);
        }catch(Exception e){
            addTextToTextArea(npi_String + " is not a valid NPI");
            return null;
        }

        for(Provider pro : providerList){
            if(pro.getClass() == projecttwo.Doctor.class){
                Doctor providerTemp = (Doctor)pro;
                if(providerTemp.getNPI_InNumber() == npi){
                    return pro;
                }
            }
        }

        addTextToTextArea("provider not found with npi: " + npi_String);
        return null;

    }

    /**
     * get Date by system, all offset with 0 means current date
     * @param MonthOffset
     * @param DayOffset
     * @param YearOffset
     * @return the Date with offset, that use system date as pivot
     */
    Date getSystemDate(int MonthOffset, int DayOffset, int YearOffset){
        int INDEX_OFFSET = 1;

        Calendar calndr = Calendar.getInstance();
        //calndr.setLenient(false);
        calndr.add(Calendar.MONTH, MonthOffset + INDEX_OFFSET);
        calndr.add(Calendar.DATE, DayOffset);
        calndr.add(Calendar.YEAR, YearOffset);

        return new Date(calndr.get(Calendar.MONTH), calndr.get(Calendar.DAY_OF_MONTH), calndr.get(Calendar.YEAR));
    }

    /**
     * get radiology from string
     * @param Radio_String
     * @return radiology class if founded, return null else.
     */
    Radiology generateRadiology_ByString(String Radio_String){

        Radio_String = Radio_String.trim();

        for(Radiology rad : Radiology.values()){
            if(Radio_String.toLowerCase().equals(rad.name().toLowerCase())){
                return rad;
            }
        }

        return null;
    }

    /**
     * generate valid technician from providerList
     * @param date
     * @param timeslot
     * @param rad
     * @return return possible technician, return null if not found
     */
    Technician generatePossibleTechnician(Date date, Timeslot timeslot, Radiology rad){

        Technician init = technicianRotator.getTechnician();
        if(checkTechnicianValid(date, timeslot, init, rad)){
            return init;
        }

        technicianRotator.jumpNext();

        while(technicianRotator.getTechnician() != init){

            if(checkTechnicianValid(date, timeslot, technicianRotator.getTechnician(), rad)){
                init = technicianRotator.getTechnician();
                //addTextToTextArea("good: " + init.toString());
                return init;
            }else{
                technicianRotator.jumpNext();
            }

        }
        return null;
    }

    /**
     * check if a technician is valid
     * @param date
     * @param timeslot
     * @param tech
     * @param rad
     * @return return true if technician is valid, return false else
     */
    boolean checkTechnicianValid(Date date, Timeslot timeslot, Technician tech, Radiology rad){
        for (Appointment appointment : AppointmentList) {
            if(appointment instanceof Imaging){
                Imaging imag_app = (Imaging)appointment;
                if(imag_app.checkAppointmentConflict(date, timeslot, tech, rad)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * find appointment with given condition
     * @param pa
     * @param da
     * @param ts
     * @return return appointment class if founded, return null else.
     */
    Appointment findAppointment(Patient pa, Date da, Timeslot ts){
        for(Appointment app : AppointmentList){
            if(app.findAppointment(pa, da, ts)){
                return app;
            }
        }
        return null;
    }

    /**
     * check if the appointment date is valid in calendar(not check conflict)
     * @param TargetDate
     * @return true if date is valid in calendar, false else
     */
    boolean checkvalidAppointmentDate_Calendar(Date TargetDate){

        //check date is possible in calendar
        if(!TargetDate.isValid()){
            addTextToTextArea("Appointment date: " + TargetDate.toString() + " is not a valid calendar date.");
            return false;
        }
        //check if is befor or today
        if(TargetDate.compareTo(getSystemDate(0,0,0)) == 0 || TargetDate.compareTo(getSystemDate(0,0,0)) == -1){
            addTextToTextArea("Appointment date: " + TargetDate.toString() + " is today or a date before today.");
            return false;
        }

        //check if is in six month
        if(TargetDate.compareTo(getSystemDate(6,0,0)) == 1){
            addTextToTextArea("Appointment date: " + TargetDate.toString() + " is not within six months.");
            return false;
        }

        //check if is weekend.
        Calendar calndr = Calendar.getInstance();
        try{
            calndr.set(TargetDate.getYear(), TargetDate.getMonth() - 1, TargetDate.getDay());
        }catch(Exception e){
            addTextToTextArea("Date can not be convert to Calendar object");
            return false;
        }
        int day = calndr.get(Calendar.DAY_OF_WEEK);
        if(day == Calendar.SATURDAY || day == Calendar.SUNDAY){
            addTextToTextArea("Appointment date: " + TargetDate.toString() + " is Saturday or Sunday.");
            return false;
        }

        return true;
    }

    /**
     * check if Appointment is conflict with exist appointment, it will iterate through the list and check one by one
     * @param TargetApp
     * @return true if no conflict, false else
     */
    boolean checkvalidAppointment_Conflict(Appointment TargetApp){

        for (Appointment app : AppointmentList) {
            if(app.checkAppointmentConflict_Provider(TargetApp)){
                addTextToTextArea(TargetApp.toString() + " is conflict");
                return false;
            }

            if(app.checkAppointmentConflict_Patient(TargetApp)){
                addTextToTextArea(TargetApp.getPatient().toString() + " has an existing appointment at the same time slot.");
                return false;
            }
        }

        return true;
    }

    /**
     * Add text to corresponding text area.
     * @param text text
     */
    void addTextToTextArea(String text) {
        if(tabPane==null){
            return;
        }
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        switch (selectedTab.getText()) {
            case "Schedule Appointment":
                scheduleOutputArea.setText("");
                scheduleOutputArea.appendText(text);
                break;
            case "Manage Appointments":
                manageOutputArea.setText("");
                manageOutputArea.appendText(text);
                break;
        }
    }
}

// 用于在TableView中显示财务信息的辅助类
class FinancialEntry {
    String name;
    double amount;

    public FinancialEntry(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() { return name; }
    public double getAmount() { return amount; }
}