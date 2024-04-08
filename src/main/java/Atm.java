public class Atm {

    public static final int SHOW_BALANCE_LIMIT = 1000000;
    public static final String UPPER_SHOW_BALANCE_LIMIT_TEXT = "*****";
    public static final String OK = "OK";
    public static final String NOT_ENOUGH_MONEY = "NOT ENOUGH MONEY";
    public static final int WITHDRAW_LOG_ALERT_LIMIT = 1000000;
    public static final int DEPOSIT_LOG_ALERT_LIMIT = 1000000;
    public static final String LOG_ALERT_TEXT = "LOG ALERT";

    private final AtmLog atmLog;

    public Atm(AtmLog atmLog) {
        this.atmLog = atmLog;
    }

    public String showBalance(Account account) {
        String balanceText = account.balance < SHOW_BALANCE_LIMIT
                ? showBalanceWithinLimit(account)
                : showBalanceBeyondLimit();
        logShowBalance(account, balanceText);
        return balanceText;
    }

    private String showBalanceWithinLimit(Account account) {
        return account.balance + " " + Account.DEFAULT_CURRENCY;
    }

    private String showBalanceBeyondLimit() {
        return UPPER_SHOW_BALANCE_LIMIT_TEXT;
    }

    public String makeDeposit(Account account, int amount) {
        account.makeDeposit(amount);
        logMakeDeposit(account, amount);
        return OK;
    }

    public String makeWithdraw(Account account, int amount) {
        if (amount > account.balance) {
            logMakeWithdraw(account, amount, NOT_ENOUGH_MONEY);
            return NOT_ENOUGH_MONEY;
        } else {
            account.makeWithdraw(amount);
            logMakeWithdraw(account, amount, OK);
            return OK;
        }
    }

    public String transfer(Account fromAccount, Account toAccount, int amount) {
        if (amount > fromAccount.balance) {
            logTransfer(fromAccount, toAccount, amount, NOT_ENOUGH_MONEY);
            return NOT_ENOUGH_MONEY;
        } else {
            fromAccount.makeWithdraw(amount);
            toAccount.makeDeposit(amount);
            logTransfer(fromAccount, toAccount, amount, OK);
            return OK;
        }

    }

    private void logShowBalance(Account account, String balanceText) {
        atmLog.log("SHOW " + account.accountNumber + " -> " + balanceText);
    }

    private void logMakeDeposit(Account account, int amount) {
        atmLog.log("DEPOSIT " + account.accountNumber + ", " + amount + ", " + Account.DEFAULT_CURRENCY);
    }

    private void logMakeWithdraw(Account account, int amount, String result) {
        atmLog.log("WITHDRAW " + account.accountNumber + ", " + amount + ", " + Account.DEFAULT_CURRENCY +
                (result.equals("OK") ? "" : " -> " + "\"" + result + "\""));
    }

    private void logTransfer(Account fromAccount, Account toAccount, int amount, String result) {
        atmLog.log("TRANSFER " + fromAccount.accountNumber + ", " + toAccount.accountNumber + ", " + amount + ", " + Account.DEFAULT_CURRENCY +
                (result.equals("OK") ? "" : " -> " + "\"" + result + "\""));
    }

}
