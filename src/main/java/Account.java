public class Account {

    public static final Currency DEFAULT_CURRENCY = Currency.HUF;
    public String ownerName;
    public String accountNumber;
    public int balance;

    private Account(String ownerName, String accountNumber, int balance) {
        this.ownerName = ownerName;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public static Account create(String ownerName, String accountNumber, int balance) {
        if (ownerName.replaceAll(" ", "").isEmpty()) {
            throw new Error("Wrong owner name!");
        }
        if (!isAccountNumberGood(accountNumber)) {
            throw new Error("Wrong account number");
        }
        if (balance < 0) {
            throw new Error("Wrong account balance");
        }
        return new Account(ownerName, accountNumber, balance);
    }

    private static boolean isAccountNumberGood(String accountNumber) {
        if (accountNumber.length() != 16) {
            return false;
        }
        for (char c : accountNumber.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public void makeDeposit(int amount) {
        if (amount < 0) {
            throw new Error("Deposit amount is negative!");
        }
        balance += amount;
    }

    public void makeWithdraw(int amount) {
        if (amount < 0) {
            throw new Error("Withdraw amount is negative!");
        }
        balance -= amount;
    }
}
