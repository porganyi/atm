public class Account {

    public final Currency DEFAULT_CURRENCY = Currency.HUF;
    public final int ACCOUNT_NUMBER_LENGTH = 16;
    public String ownerName;
    public String accountNumber;
    public long balance;

    public Account(String ownerName, String accountNumber, long balance) {
        if (!isValidOwnerName(ownerName)) {
            throw new Error("Wrong owner name!");
        }
        if (!isAccountNumberGood(accountNumber)) {
            throw new Error("Wrong account number");
        }
        if (balance < 0) {
            throw new Error("Wrong account balance");
        }
        this.ownerName = ownerName;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    //     Todo végiggondolni, hol private,
//     kiemelni a create-et
//     hibakezelés lejjebb
    private boolean isValidOwnerName(String ownerName) {
        return !ownerName.replaceAll(" ", "").isEmpty();
    }

    private boolean isAccountNumberGood(String accountNumber) {
        if (accountNumber.length() != this.ACCOUNT_NUMBER_LENGTH) {
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
