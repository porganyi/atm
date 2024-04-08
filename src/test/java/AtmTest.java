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

    private static void logMakeDeposit(Account account, int amount) {
        atmLog.log("DEPOSIT " + account.accountNumber + ", " + amount + ", " + Account.DEFAULT_CURRENCY);
        logAccountState(account);
    }

    private static void logMakeWithdraw(Account account, int amount) {
        atmLog.log("WITHDRAW " + account.accountNumber + ", " + amount + ", " + Account.DEFAULT_CURRENCY);
        logAccountState(account);
    }

    private static void logTransfer(Account fromAccount, Account toAccount, int amount) {
        atmLog.log("TRANSFER " + fromAccount.accountNumber + ", " + toAccount.accountNumber + ", " + amount + ", " + Account.DEFAULT_CURRENCY);
        logAccountState(fromAccount);
        logAccountState(toAccount);
    }

    @BeforeEach
    void beforeEach() {
        atmLog.log("");
    }

    @Test
    public void show_balance_beyond_limit_test() {
        Account account = createAccount("X Y", "1234567890123456", 1000000);
        Assertions.assertEquals("*****", atm.showBalance(account));
    }

    @Test
    public void show_balance_within_limit_test() {
        Account account = createAccount("X Y", "1234567890123456", 999999);
        Assertions.assertEquals("999999 HUF", atm.showBalance(account));
    }

    @Test
    public void make_deposit_test() {
        Account account = createAccount("X Y", "1234567890123456", 10);
        int amount = 1;

        atm.makeDeposit(account, amount);

        logMakeDeposit(account, amount);
        Assertions.assertEquals(11, account.balance);
    }

    @Test
    public void make_withdraw_with_enough_balance_test() {
        Account account = createAccount("X Y", "1234567890123456", 10);
        int amount = 1;

        atm.makeWithdraw(account, amount);

        logMakeWithdraw(account, amount);
        Assertions.assertEquals(9, account.balance);
    }

    @Test
    public void make_withdraw_with_not_enough_balance_test() {
        Account account = createAccount("X Y", "1234567890123456", 10);
        int amount = 11;

        atm.makeWithdraw(account, amount);

        logMakeWithdraw(account, amount);
        Assertions.assertEquals(10, account.balance);
    }

    @Test
    public void make_transfer_test() {
        Account fromAccount = createAccount("X Y", "1234567890123456", 10);
        Account toAccount = createAccount("Z W", "6543210987654321", 20);
        int amount = 1;

        atm.transfer(fromAccount, toAccount, amount);

        logTransfer(fromAccount, toAccount, amount);
        Assertions.assertEquals(9, fromAccount.balance);
        Assertions.assertEquals(21, toAccount.balance);
    }

    @Test
    public void complex_test_with_two_accounts() {
        Account account01 = createAccount("JOHN DOE", "1111222233334444", 2000000);
        Account account02 = createAccount("JACK BLACK", "0000111122223333", 10000);
    }

}
