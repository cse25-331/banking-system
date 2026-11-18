package bank.model;

public class Organisation extends Customer {
    private final String organisationName;
    private final String registrationNumber;
    private final String organisationType;      // NGO or BUSINESS

    public Organisation(String customerId, String username, String password,
                        String address, String contactDetails,
                        String organisationName, String registrationNumber,
                        String organisationType) {
        super(customerId, username, password, address, contactDetails);
        this.organisationName = organisationName;
        this.registrationNumber = registrationNumber;
        this.organisationType = organisationType;
    }

    @Override
    public String getDisplayName() {
        return organisationName;
    }

    public String getRegistrationNumber() { return registrationNumber; }
    public String getOrganisationType() { return organisationType; }
}