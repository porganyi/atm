public class Account {

    public String ownerName;
    public String accountNumber;
    public float balance;
    public Currency currency = Currency.HUF;

    public Account(String ownerName, String accountNumber, float balance) {
        this.ownerName = ownerName;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }
}
