package se2203b.iGlobal;

import javafx.beans.property.*;

/**
 * The PropertyType class represents a type of property (for example, residential, commercial, industrial)
 * in the iGlobal system. It uses JavaFX properties to allow easy binding to UI components.
 */
public class PropertyType {
    // The unique code for the property type, stored as a JavaFX StringProperty.
    private final StringProperty typeCode = new SimpleStringProperty();

    // The descriptive name for the property type, stored as a JavaFX StringProperty.
    private final StringProperty typeName = new SimpleStringProperty();

    /**
     * Default constructor for PropertyType.
     * Initializes an empty PropertyType instance.
     */
    public PropertyType() {}

    /**
     * Constructs a PropertyType with the specified code and name.
     *
     * @param code the unique code for the property type
     * @param name the descriptive name for the property type
     */
    public PropertyType(String code, String name) {
        this.typeCode.set(code);
        this.typeName.set(name);
    }

    /**
     * Returns the property type code.
     *
     * @return the type code as a String
     */
    public String getTypeCode() {
        return typeCode.get();
    }

    /**
     * Sets the property type code.
     *
     * @param code the new type code
     */
    public void setTypeCode(String code) {
        typeCode.set(code);
    }

    /**
     * Returns the StringProperty representing the type code.
     * This is useful for binding the property to UI controls.
     *
     * @return the typeCode property
     */
    public StringProperty typeCodeProperty() {
        return typeCode;
    }

    /**
     * Returns the property type name.
     *
     * @return the type name as a String
     */
    public String getTypeName() {
        return typeName.get();
    }

    /**
     * Sets the property type name.
     *
     * @param name the new type name
     */
    public void setTypeName(String name) {
        typeName.set(name);
    }

    /**
     * Returns the StringProperty representing the type name.
     * This is useful for binding the property to UI controls.
     *
     * @return the typeName property
     */
    public StringProperty typeNameProperty() {
        return typeName;
    }
}
