import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AtmTest {

    private static final AtmLog atmLog = new AtmLog();
    private static final Atm atm = new Atm(atmLog);

    private static Account createAccount(String ownerName, String accountNumber, int balance) {
        Account account = Account.create(ownerName, accountNumber, balance);
        logAccountState(account);
        return account;
    }

    private static void logAccountState(Account account) {
        atmLog.log(account.ownerName + ", " + account.accountNumber + ", " + account.balance + " " + Account.DEFAULT_CURRENCY);
    }

    private static void logResult(String result) {
        atmLog.log("Result: " + result);
    }

    @BeforeEach
    void beforeEach() {
        atmLog.log("");
    }

    @Test
    public void show_balance_beyond_limit_test() {
        Account account = createAccount("X Y", "1234567890123456", 1000000);

        String message = atm.showBalance(account);
        logAccountState(account);

        Assertions.assertEquals("*****", message);
    }

    @Test
    public void show_balance_within_limit_test() {
        Account account = createAccount("X Y", "1234567890123456", 999999);

        String message = atm.showBalance(account);
        logAccountState(account);

        Assertions.assertEquals("999999 HUF", message);
    }

    @Test
    public void make_deposit_test() {
        Account account = createAccount("X Y", "1234567890123456", 10);
        int amount = 1;

        String result = atm.makeDeposit(account, amount);
        logResult(result);
        logAccountState(account);

        Assertions.assertEquals("OK", result);
        Assertions.assertEquals(11, account.balance);
    }

    @Test
    public void make_deposit_with_log_alert_test() {
        Account account = createAccount("X Y", "1234567890123456", 10);
        int amount = 1000001;

        String result = atm.makeDeposit(account, amount);
        logResult(result);
        logAccountState(account);

        Assertions.assertEquals("OK WITH LOG ALERT", result);
        Assertions.assertEquals(1000011, account.balance);
    }

    @Test
    public void make_withdraw_with_enough_balance_test() {
        Account account = createAccount("X Y", "1234567890123456", 10);
        int amount = 1;

        String result = atm.makeWithdraw(account, amount);
        logResult(result);
        logAccountState(account);

        Assertions.assertEquals("OK", result);
        Assertions.assertEquals(9, account.balance);
    }

    @Test
    public void make_withdraw_with_not_enough_balance_test() {
        Account account = createAccount("X Y", "1234567890123456", 10);
        int amount = 11;

        String result = atm.makeWithdraw(account, amount);
        logResult(result);
        logAccountState(account);

        Assertions.assertEquals("NOT ENOUGH MONEY", result);
        Assertions.assertEquals(10, account.balance);
    }

    @Test
    public void make_transfer_test() {
        Account fromAccount = createAccount("X Y", "1234567890123456", 10);
        Account toAccount = createAccount("Z W", "6543210987654321", 20);
        int amount = 1;

        String result = atm.transfer(fromAccount, toAccount, amount);
        logResult(result);
        logAccountState(fromAccount);
        logAccountState(toAccount);

        Assertions.assertEquals("OK", result);
        Assertions.assertEquals(9, fromAccount.balance);
        Assertions.assertEquals(21, toAccount.balance);
    }

    @Test
    public void make_transfer_with_not_enough_balance_test() {
        Account fromAccount = createAccount("X Y", "1234567890123456", 10);
        Account toAccount = createAccount("Z W", "6543210987654321", 20);
        int amount = 11;

        String result = atm.transfer(fromAccount, toAccount, amount);
        logResult(result);
        logAccountState(fromAccount);
        logAccountState(toAccount);

        Assertions.assertEquals("NOT ENOUGH MONEY", result);
        Assertions.assertEquals(10, fromAccount.balance);
        Assertions.assertEquals(20, toAccount.balance);
    }

    @Test
    public void complex_test_with_two_accounts() {
        Account account01 = createAccount("JOHN DOE", "1111222233334444", 2000000);
        Account account02 = createAccount("JACK BLACK", "0000111122223333", 10000);
        atm.makeDeposit(account02, 12000);
        atm.makeWithdraw(account01, 9000);
        atm.showBalance(account02);
        atm.showBalance(account01);
        atm.transfer(account01, account02, 20000);
        atm.transfer(account02, account01, 90000);
        atm.makeDeposit(account01, 1500000);
    }

}
