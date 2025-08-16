package se2203b.iGlobal;

/**
 * The City class represents a city in the iGlobal system.
 * It holds the name of the city and the corresponding province code.
 */
public class City {
    // The name of the city.
    private final String cityName;
    // The code representing the province to which the city belongs.
    private final String provinceCode;

    /**
     * Constructs a City object with the specified city name and province code.
     *
     * @param cityName     The name of the city.
     * @param provinceCode The code of the province.
     */
    public City(String cityName, String provinceCode) {
        this.cityName = cityName;
        this.provinceCode = provinceCode;
    }

    /**
     * Returns the name of the city.
     *
     * @return A String representing the city name.
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * Returns the province code for the city.
     *
     * @return A String representing the province code.
     */
    public String getProvinceCode() {
        return provinceCode;
    }
}
