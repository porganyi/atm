public class Atm {

    public static final float SHOW_BALANCE_LIMIT = 1000000;
    public static final String UPPER_LIMIT_TEXT = "*****";
    public static final float WITHDRAW_ALERT_LIMIT = 1000000;
    public static final String WITHDRAW_ALERT_TEXT = "WITHDRAW ALERT";
    public static final String WITHDRAW_ERROR_MESSAGE = "WITHDRAW ERROR MESSAGE";
    public static final float DEPOSIT_ALERT_LIMIT = 1000000;
    public static final String DEPOSIT_ALERT_TEXT = "DEPOSIT ALERT";

    public void showBalance(Account account) {
        if (account.balance > SHOW_BALANCE_LIMIT) {
            System.out.println(account.balance + ", " + account.currency);
        } else {
            System.out.println(UPPER_LIMIT_TEXT);
        }
    }

    public void makeDeposit(Account account, float amount) {
        account.balance += amount;
        if (amount > DEPOSIT_ALERT_LIMIT) {
            System.out.println(DEPOSIT_ALERT_TEXT);
        }
    }

    public void makeWithdraw(Account account, float amount) {
        if (amount > account.balance) {

        } else {
            account.balance += amount;
        }
    }

}
