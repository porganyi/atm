import static java.util.Objects.isNull;

public class Account implements Checkable {

    public static final Currency DEFAULT_CURRENCY = Currency.HUF;
    public static final int ACCOUNT_NUMBER_LENGTH = 16;
    private final String ownerName;
    private final String accountNumber;
    private long balance;

    public String getOwnerName() {
        return ownerName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public long getBalance() {
        return balance;
    }

    public Account(String ownerName, String accountNumber, long balance) {
        check(!isValidOwnerName(ownerName), "Wrong owner name!");
        check(!isAccountNumberGood(accountNumber), "Wrong account number");
        check(balance < 0, "Wrong account balance");
        this.ownerName = ownerName;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    private boolean isValidOwnerName(String ownerName) {
        return !ownerName.replaceAll(" ", "").isEmpty();
    }

    private boolean isAccountNumberGood(String accountNumber) {
        if (accountNumber.length() != ACCOUNT_NUMBER_LENGTH) {
            return false;
        }
        for (char c : accountNumber.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public void makeDeposit(long amount) {
        makeDeposit(amount, Currency.HUF);
    }

    public void makeDeposit(double amount, Currency currency) {
        check(amount < 0, "Deposit amount is negative!");
        check(isNull(currency), "Cuurency is mandatory!");
        long amountInHuf = Math.round(amount / currency.hufToCurrency);
        balance += amountInHuf;
    }

    public void makeWithdraw(long amount) {
        makeWithdraw(amount, Currency.HUF);
    }

    public void makeWithdraw(double amount, Currency currency) {
        check(amount < 0, "Withdraw amount is negative!");
        long amountInHuf = Math.round(amount / currency.hufToCurrency);
        balance -= amountInHuf;
    }
}
