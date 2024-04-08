public class Atm {

    public static final int SHOW_BALANCE_LIMIT = 1000000;
    public static final String UPPER_LIMIT_TEXT = "*****";
    public static final int WITHDRAW_ALERT_LIMIT = 1000000;
    public static final String WITHDRAW_ALERT_TEXT = "WITHDRAW ALERT";
    public static final String WITHDRAW_ERROR_MESSAGE = "WITHDRAW ERROR MESSAGE";
    public static final int DEPOSIT_ALERT_LIMIT = 1000000;
    public static final String DEPOSIT_ALERT_TEXT = "DEPOSIT ALERT";

    private final AtmLog atmLog;

    public Atm(AtmLog atmLog) {
        this.atmLog = atmLog;
    }

    public String showBalance(Account account) {
        String balanceText = account.balance < SHOW_BALANCE_LIMIT
                ? showBalanceWithinLimit(account)
                : showBalanceBeyondLimit();
        atmLog.log("SHOW " + account.accountNumber + " -> " + balanceText);
        return balanceText;
    }

    private String showBalanceWithinLimit(Account account) {
        return account.balance + " " + Account.DEFAULT_CURRENCY;
    }

    private String showBalanceBeyondLimit() {
        return UPPER_LIMIT_TEXT;
    }

    public void makeDeposit(Account account, int amount) {
        account.makeDeposit(amount);
    }

    public void makeWithdraw(Account account, int amount) {
        account.makeWithdraw(amount);
    }

    public void transfer(Account fromAccount, Account toAccount, int amount) {
        fromAccount.makeWithdraw(amount);
        toAccount.makeDeposit(amount);
    }

}
