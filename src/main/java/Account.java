public class Account {

    public static final Currency DEFAULT_CURRENCY = Currency.HUF;

    public String ownerName;
    public String accountNumber;
    public int balance;

    public Account(String ownerName, String accountNumber, int balance) {
        this.ownerName = ownerName;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }
}
