public class Atm {

    public final long SHOW_BALANCE_LIMIT = 1000000;
    public final String UPPER_SHOW_BALANCE_LIMIT_TEXT = "*****";
    public final String OK = "OK";
    public final String OK_WITH_LOG_ALERT = "OK WITH LOG ALERT";
    public final String NOT_ENOUGH_MONEY = "NOT ENOUGH MONEY";
    public final long DEPOSIT_LOG_ALERT_LIMIT = 1000000;
    public final long WITHDRAW_LOG_ALERT_LIMIT = 1000000;
    public final String LOG_ALERT_TEXT = "LOG ALERT";

    private final AtmLog atmLog;
    private final AtmPrint atmPrint;

    public Atm(AtmLog atmLog, AtmPrint atmPrint) {
        this.atmLog = atmLog;
        this.atmPrint = atmPrint;
    }

    public String showBalance(Account account) {
        String balanceText = account.balance < SHOW_BALANCE_LIMIT
                ? showBalanceWithinLimit(account)
                : showBalanceBeyondLimit();
        logShowBalance(account, balanceText);
        return balanceText;
    }

    private String showBalanceWithinLimit(Account account) {
        return account.balance + " " + account.DEFAULT_CURRENCY;
    }

    private String showBalanceBeyondLimit() {
        return UPPER_SHOW_BALANCE_LIMIT_TEXT;
    }

    public String makeDeposit(Account account, long amount) {
        return makeDeposit(account, amount, Currency.HUF);
    }

    public String makeDeposit(Account account, double amount, Currency currency) {
        account.makeDeposit(amount, currency);
        logMakeDeposit(account, amount, currency);
        return getDepositReturnString(amount, currency);
    }

    private String getDepositReturnString(double amount, Currency currency) {
        long amountInHuf = Math.round(amount / currency.hufToCurrency);
        return amountInHuf > DEPOSIT_LOG_ALERT_LIMIT ? OK_WITH_LOG_ALERT : OK;
    }

    public String makeWithdraw(Account account, long amount) {
        return makeWithdraw(account, amount, Currency.HUF);
    }

    public String makeWithdraw(Account account, double amount, Currency currency) {
        long amountInHuf = Math.round(amount / currency.hufToCurrency);
        if (amountInHuf > account.balance) {
            logMakeWithdraw(account, amount, currency, NOT_ENOUGH_MONEY);
            return NOT_ENOUGH_MONEY;
        } else {
            account.makeWithdraw(amount, currency);
            logMakeWithdraw(account, amount, currency, OK);
            return getWithdrawReturnString(amountInHuf);
        }
    }

    private String getWithdrawReturnString(long amount) {
        return amount > WITHDRAW_LOG_ALERT_LIMIT ? OK_WITH_LOG_ALERT : OK;
    }

    public String transfer(Account fromAccount, Account toAccount, long amount) {
        if (amount > fromAccount.balance) {
            logTransfer(fromAccount, toAccount, amount, NOT_ENOUGH_MONEY);
            return NOT_ENOUGH_MONEY;
        } else {
            fromAccount.makeWithdraw(amount);
            toAccount.makeDeposit(amount);
            logTransfer(fromAccount, toAccount, amount, OK);
            return getTransferReturnString(amount);
        }
    }

    public void printBalance(Account account) {
        logPrintBalance(account);
        atmPrint.printLine("====================");
        atmPrint.printLine("NAME: " + account.ownerName);
        atmPrint.printLine("ACCOUNT NR.: " + account.accountNumber);
        atmPrint.printLine("BALANCE: " + account.balance + " " + account.DEFAULT_CURRENCY);
        atmPrint.printLine("====================");
    }

    private String getTransferReturnString(long amount) {
        return amount > WITHDRAW_LOG_ALERT_LIMIT ? OK_WITH_LOG_ALERT : OK;
    }

    private void logShowBalance(Account account, String balanceText) {
        atmLog.log("SHOW " + account.accountNumber + " -> " + balanceText);
    }

    private void logMakeDeposit(Account account, double amount, Currency currency) {
        String amountString = currency == Currency.HUF ? String.valueOf(Math.round(amount)) : String.valueOf(amount);
        String logAlertString = amount > DEPOSIT_LOG_ALERT_LIMIT ? " -> " + LOG_ALERT_TEXT : "";
        atmLog.log("DEPOSIT " + account.accountNumber + ", " + amountString + ", " + currency + logAlertString);
    }

    private void logMakeWithdraw(Account account, double amount, Currency currency, String result) {
        String amountString = currency == Currency.HUF ? String.valueOf(Math.round(amount)) : String.valueOf(amount);
        String logAlertString = amount > WITHDRAW_LOG_ALERT_LIMIT ? " -> " + LOG_ALERT_TEXT : "";
        atmLog.log("WITHDRAW " + account.accountNumber + ", " + amountString + ", " + currency +
                (result.equals("OK") ? logAlertString : " -> " + "\"" + result + "\""));
    }

    private void logTransfer(Account fromAccount, Account toAccount, long amount, String result) {
        String logAlertString = amount > WITHDRAW_LOG_ALERT_LIMIT ? " -> " + LOG_ALERT_TEXT : "";
        atmLog.log("TRANSFER " + fromAccount.accountNumber + ", " + toAccount.accountNumber + ", " + amount + ", " + fromAccount.DEFAULT_CURRENCY +
                (result.equals("OK") ? logAlertString : " -> " + "\"" + result + "\""));
    }

    private void logPrintBalance(Account account) {
        atmLog.log("PRINT " + account.accountNumber + " ->");
    }
}
