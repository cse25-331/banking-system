package bank.model;

public class Individual extends Customer {
    private final String firstName;
    private final String surname;
    private final boolean employed;

    public Individual(String customerId, String username, String password,
                      String address, String contactDetails,
                      String firstName, String surname, boolean employed) {
        super(customerId, username, password, address, contactDetails);
        this.firstName = firstName;
        this.surname = surname;
        this.employed = employed;
    }

    @Override
    public String getDisplayName() {
        return firstName + " " + surname;
    }

    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public boolean isEmployed() { return employed; }
}