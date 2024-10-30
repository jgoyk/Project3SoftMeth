package clinic;

/**
 * Represents various locations along with their corresponding county and zip code.
 *
 * Each location is defined by a name, county, and zip code.
 *
 * @author Joshua Goykhman (Netid: jg1986)
 */
public enum Location {
    BRIDGEWATER ("Somerset", "08807"),
    EDISON ("Middlesex", "08817"),
    PISCATAWAY ("Middlesex", "08854"),
    PRINCETON ("Mercer", "08542"),
    MORRISTOWN ("Morris", "07960"),
    CLARK ("Union", "07066");

    private final String county;
    private final String zip;

    /**
     * Constructs a Location enum with the specified county and zip code.
     *
     * @param county the county associated with the location
     * @param zip the zip code of the location
     */
    Location(String county, String zip) {
        this.county = county;
        this.zip = zip;
    }

    /**
     * Gets the county associated with this location.
     *
     * @return the county of the location
     */
    public String getCounty() {
        return county;
    }

    /**
     * Gets the zip code associated with this location.
     *
     * @return the zip code of the location
     */
    public String getZip() {
        return zip;
    }

}
