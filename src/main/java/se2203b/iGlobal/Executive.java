package se2203b.iGlobal;

import javafx.beans.property.*;

/**
 * The Executive class represents an executive user in the iGlobal system.
 * It contains properties for the executive's personal information.
 * This class uses JavaFX properties to support data binding in the UI.
 */
public class Executive {
    // JavaFX properties for executive attributes
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty emailAddress = new SimpleStringProperty();
    private final StringProperty phoneNumber = new SimpleStringProperty();
    private final StringProperty privatePhoneNumber = new SimpleStringProperty();

    /**
     * Default constructor.
     * Initializes an empty Executive instance.
     */
    public Executive() {}

    /**
     * Parameterized constructor.
     *
     * @param firstName      the first name of the executive.
     * @param lastName       the last name of the executive.
     * @param emailAddress   the email address of the executive.
     * @param phoneNumber    the public phone number of the executive.
     * @param privatePhone   the private phone number of the executive.
     */
    public Executive(String firstName, String lastName, String emailAddress, String phoneNumber, String privatePhone) {
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.emailAddress.set(emailAddress);
        this.phoneNumber.set(phoneNumber);
        this.privatePhoneNumber.set(privatePhone);
    }

    /**
     * Gets the first name of the executive.
     *
     * @return the first name.
     */
    public String getFirstName() {
        return firstName.get();
    }

    /**
     * Sets the first name of the executive.
     *
     * @param value the first name to set.
     */
    public void setFirstName(String value) {
        firstName.set(value);
    }

    /**
     * Gets the first name property.
     *
     * @return the first name property.
     */
    public StringProperty firstNameProperty() {
        return firstName;
    }

    /**
     * Gets the last name of the executive.
     *
     * @return the last name.
     */
    public String getLastName() {
        return lastName.get();
    }

    /**
     * Sets the last name of the executive.
     *
     * @param value the last name to set.
     */
    public void setLastName(String value) {
        lastName.set(value);
    }

    /**
     * Gets the last name property.
     *
     * @return the last name property.
     */
    public StringProperty lastNameProperty() {
        return lastName;
    }

    /**
     * Gets the email address of the executive.
     *
     * @return the email address.
     */
    public String getEmailAddress() {
        return emailAddress.get();
    }

    /**
     * Sets the email address of the executive.
     *
     * @param value the email address to set.
     */
    public void setEmailAddress(String value) {
        emailAddress.set(value);
    }

    /**
     * Gets the email address property.
     *
     * @return the email address property.
     */
    public StringProperty emailAddressProperty() {
        return emailAddress;
    }

    /**
     * Gets the public phone number of the executive.
     *
     * @return the public phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    /**
     * Sets the public phone number of the executive.
     *
     * @param value the public phone number to set.
     */
    public void setPhoneNumber(String value) {
        phoneNumber.set(value);
    }

    /**
     * Gets the public phone number property.
     *
     * @return the phone number property.
     */
    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    /**
     * Gets the private phone number of the executive.
     *
     * @return the private phone number.
     */
    public String getPrivatePhoneNumber() {
        return privatePhoneNumber.get();
    }

    /**
     * Sets the private phone number of the executive.
     *
     * @param value the private phone number to set.
     */
    public void setPrivatePhoneNumber(String value) {
        privatePhoneNumber.set(value);
    }

    /**
     * Gets the private phone number property.
     *
     * @return the private phone number property.
     */
    public StringProperty privatePhoneNumberProperty() {
        return privatePhoneNumber;
    }
}
