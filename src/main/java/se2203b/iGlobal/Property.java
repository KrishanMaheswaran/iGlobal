package se2203b.iGlobal;

import javafx.beans.property.*;

/**
 * The Property class represents a real estate property record.
 * It includes attributes such as property type, lot size, square footage,
 * number of bedrooms and bathrooms, year built, price, location details,
 * and additional descriptive information.
 */
public class Property {
    // The type of property (e.g., residential, commercial)
    private final StringProperty propertyType = new SimpleStringProperty();

    // The lot size of the property in appropriate units (e.g., acres or square feet)
    private final DoubleProperty lotSize = new SimpleDoubleProperty();

    // The square footage of the property’s interior
    private final DoubleProperty squareFootage = new SimpleDoubleProperty();

    // The number of bedrooms in the property
    private final IntegerProperty bedrooms = new SimpleIntegerProperty();

    // The number of bathrooms (can be fractional for half baths) in the property
    private final DoubleProperty bathrooms = new SimpleDoubleProperty();

    // The year the property was built
    private final IntegerProperty yearBuilt = new SimpleIntegerProperty();

    // The listing price of the property
    private final DoubleProperty price = new SimpleDoubleProperty();

    // The province where the property is located
    private final StringProperty province = new SimpleStringProperty();

    // The city where the property is located
    private final StringProperty city = new SimpleStringProperty();

    // The street address of the property
    private final StringProperty address = new SimpleStringProperty();

    // The postal code of the property's location
    private final StringProperty postalCode = new SimpleStringProperty();

    // A list or description of amenities that the property offers
    private final StringProperty amenities = new SimpleStringProperty();

    // A general description of the property
    private final StringProperty description = new SimpleStringProperty();

    /**
     * Default constructor.
     */
    public Property() {}

    /**
     * Parameterized constructor to initialize a Property object with specific details.
     *
     * @param propertyType   The type of the property.
     * @param lotSize        The size of the lot.
     * @param squareFootage  The interior square footage.
     * @param bedrooms       The number of bedrooms.
     * @param bathrooms      The number of bathrooms.
     * @param yearBuilt      The year the property was built.
     * @param price          The listing price.
     * @param province       The province where the property is located.
     * @param city           The city where the property is located.
     * @param address        The street address of the property.
     * @param postalCode     The postal code.
     * @param amenities      The amenities offered by the property.
     * @param description    Additional description of the property.
     */
    public Property(String propertyType, double lotSize, double squareFootage, int bedrooms, double bathrooms,
                    int yearBuilt, double price, String province, String city, String address,
                    String postalCode, String amenities, String description) {
        this.propertyType.set(propertyType);
        this.lotSize.set(lotSize);
        this.squareFootage.set(squareFootage);
        this.bedrooms.set(bedrooms);
        this.bathrooms.set(bathrooms);
        this.yearBuilt.set(yearBuilt);
        this.price.set(price);
        this.province.set(province);
        this.city.set(city);
        this.address.set(address);
        this.postalCode.set(postalCode);
        this.amenities.set(amenities);
        this.description.set(description);
    }

    // Getters, setters, and property methods for each attribute

    public String getPropertyType() {
        return propertyType.get();
    }

    public void setPropertyType(String value) {
        propertyType.set(value);
    }

    public StringProperty propertyTypeProperty() {
        return propertyType;
    }

    public double getLotSize() {
        return lotSize.get();
    }

    public void setLotSize(double value) {
        lotSize.set(value);
    }

    public DoubleProperty lotSizeProperty() {
        return lotSize;
    }

    public double getSquareFootage() {
        return squareFootage.get();
    }

    public void setSquareFootage(double value) {
        squareFootage.set(value);
    }

    public DoubleProperty squareFootageProperty() {
        return squareFootage;
    }

    public int getBedrooms() {
        return bedrooms.get();
    }

    public void setBedrooms(int value) {
        bedrooms.set(value);
    }

    public IntegerProperty bedroomsProperty() {
        return bedrooms;
    }

    public double getBathrooms() {
        return bathrooms.get();
    }

    public void setBathrooms(double value) {
        bathrooms.set(value);
    }

    public DoubleProperty bathroomsProperty() {
        return bathrooms;
    }

    public int getYearBuilt() {
        return yearBuilt.get();
    }

    public void setYearBuilt(int value) {
        yearBuilt.set(value);
    }

    public IntegerProperty yearBuiltProperty() {
        return yearBuilt;
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double value) {
        price.set(value);
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public String getProvince() {
        return province.get();
    }

    public void setProvince(String value) {
        province.set(value);
    }

    public StringProperty provinceProperty() {
        return province;
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String value) {
        city.set(value);
    }

    public StringProperty cityProperty() {
        return city;
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String value) {
        address.set(value);
    }

    public StringProperty addressProperty() {
        return address;
    }

    public String getPostalCode() {
        return postalCode.get();
    }

    public void setPostalCode(String value) {
        postalCode.set(value);
    }

    public StringProperty postalCodeProperty() {
        return postalCode;
    }

    public String getAmenities() {
        return amenities.get();
    }

    public void setAmenities(String value) {
        amenities.set(value);
    }

    public StringProperty amenitiesProperty() {
        return amenities;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String value) {
        description.set(value);
    }

    public StringProperty descriptionProperty() {
        return description;
    }
}
