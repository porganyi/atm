public class Account {

    public static final Currency DEFAULT_CURRENCY = Currency.HUF;
    public String ownerName;
    public String accountNumber;
    public long balance;

    private Account(String ownerName, String accountNumber, long balance) {
        this.ownerName = ownerName;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public static Account create(String ownerName, String accountNumber, long balance) {
        if (!isOwnerNameGood(ownerName)) {
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

    private static boolean isOwnerNameGood(String ownerName) {
        return !ownerName.replaceAll(" ", "").isEmpty();
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

    public void makeDeposit(long amount) {
        makeDeposit(amount, Currency.HUF);
    }

    public void makeDeposit(double amount, Currency currency) {
        if (amount < 0) {
            throw new Error("Deposit amount is negative!");
        }
        long amountInHuf = Math.round(amount / currency.hufToCurrency);
        balance += amountInHuf;
    }

    public void makeWithdraw(long amount) {
        makeWithdraw(amount, Currency.HUF);
    }

    public void makeWithdraw(double amount, Currency currency) {
        if (amount < 0) {
            throw new Error("Withdraw amount is negative!");
        }
        long amountInHuf = Math.round(amount / currency.hufToCurrency);
        balance -= amountInHuf;
    }
}
