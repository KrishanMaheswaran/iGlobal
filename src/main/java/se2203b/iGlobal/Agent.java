package se2203b.iGlobal;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The Agent class represents a licensed real estate professional in the iGlobal system.
 * It contains personal and professional details for an agent, such as first name, last name,
 * email address, phone number, license number, and specialization.
 *
 * This class uses JavaFX properties to facilitate data binding in the user interface.
 */
public class Agent {

    // JavaFX properties to hold the agent's details.
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty emailAddress = new SimpleStringProperty();
    private final StringProperty phoneNumber = new SimpleStringProperty();
    private final StringProperty licenseNumber = new SimpleStringProperty();
    private final StringProperty specialization = new SimpleStringProperty();

    /**
     * Default constructor.
     * Initializes an empty Agent with all properties set to default (null) values.
     */
    public Agent() {}

    /**
     * Parameterized constructor.
     *
     * @param firstName      the agent's first name.
     * @param lastName       the agent's last name.
     * @param emailAddress   the agent's email address.
     * @param phoneNumber    the agent's phone number.
     * @param licenseNumber  the agent's license number, which serves as a unique identifier.
     * @param specialization the agent's area of specialization.
     */
    public Agent(String firstName, String lastName, String emailAddress, String phoneNumber,
                 String licenseNumber, String specialization) {
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.emailAddress.set(emailAddress);
        this.phoneNumber.set(phoneNumber);
        this.licenseNumber.set(licenseNumber);
        this.specialization.set(specialization);
    }

    /**
     * Gets the agent's first name.
     *
     * @return the first name as a String.
     */
    public String getFirstName() {
        return firstName.get();
    }

    /**
     * Sets the agent's first name.
     *
     * @param value the new first name.
     */
    public void setFirstName(String value) {
        firstName.set(value);
    }

    /**
     * Returns the first name property.
     * This is useful for binding the first name value to UI components.
     *
     * @return the firstName property.
     */
    public StringProperty firstNameProperty() {
        return firstName;
    }

    /**
     * Gets the agent's last name.
     *
     * @return the last name as a String.
     */
    public String getLastName() {
        return lastName.get();
    }

    /**
     * Sets the agent's last name.
     *
     * @param value the new last name.
     */
    public void setLastName(String value) {
        lastName.set(value);
    }

    /**
     * Returns the last name property.
     * Useful for binding to UI components.
     *
     * @return the lastName property.
     */
    public StringProperty lastNameProperty() {
        return lastName;
    }

    /**
     * Gets the agent's email address.
     *
     * @return the email address as a String.
     */
    public String getEmailAddress() {
        return emailAddress.get();
    }

    /**
     * Sets the agent's email address.
     *
     * @param value the new email address.
     */
    public void setEmailAddress(String value) {
        emailAddress.set(value);
    }

    /**
     * Returns the email address property.
     * Useful for binding the email address value to UI components.
     *
     * @return the emailAddress property.
     */
    public StringProperty emailAddressProperty() {
        return emailAddress;
    }

    /**
     * Gets the agent's phone number.
     *
     * @return the phone number as a String.
     */
    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    /**
     * Sets the agent's phone number.
     *
     * @param value the new phone number.
     */
    public void setPhoneNumber(String value) {
        phoneNumber.set(value);
    }

    /**
     * Returns the phone number property.
     * Useful for binding the phone number value to UI components.
     *
     * @return the phoneNumber property.
     */
    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    /**
     * Gets the agent's license number.
     *
     * @return the license number as a String.
     */
    public String getLicenseNumber() {
        return licenseNumber.get();
    }

    /**
     * Sets the agent's license number.
     *
     * @param value the new license number.
     */
    public void setLicenseNumber(String value) {
        licenseNumber.set(value);
    }

    /**
     * Returns the license number property.
     * Useful for binding the license number value to UI components.
     *
     * @return the licenseNumber property.
     */
    public StringProperty licenseNumberProperty() {
        return licenseNumber;
    }

    /**
     * Gets the agent's specialization.
     *
     * @return the specialization as a String.
     */
    public String getSpecialization() {
        return specialization.get();
    }

    /**
     * Sets the agent's specialization.
     *
     * @param value the new specialization.
     */
    public void setSpecialization(String value) {
        specialization.set(value);
    }

    /**
     * Returns the specialization property.
     * Useful for binding the specialization value to UI components.
     *
     * @return the specialization property.
     */
    public StringProperty specializationProperty() {
        return specialization;
    }
}
