import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AtmTest {

    private final AtmLog atmLog = new AtmLog();
    private final AtmPrint atmPrint = new AtmPrint();
    private final Atm atm = new Atm(atmLog, atmPrint);

    private Account createAccount(String ownerName, String accountNumber, long balance) {
        Account account = new Account(ownerName, accountNumber, balance);
        logAccountState(account);
        return account;
    }

    private void logAccountState(Account account) {
        atmLog.log(account.getOwnerName() + ", " + account.getAccountNumber() + ", "
                + account.getBalance() + " " + Account.DEFAULT_CURRENCY);
    }

    private void logResult(String result) {
        atmLog.log("Result: " + result);
    }

    private void logExchangeRates() {
        StringBuilder exchangeRatesString = new StringBuilder("1 HUF");
        for (Currency currency : Currency.values()) {
            if (currency != Currency.HUF) {
                exchangeRatesString.append(", ").append(currency.hufToCurrency).append(" ").append(currency);
            }
        }
        atmLog.log(exchangeRatesString.toString());
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
        long amount = 1;

        String result = atm.makeDeposit(account, amount);
        logResult(result);
        logAccountState(account);

        Assertions.assertEquals("OK", result);
        Assertions.assertEquals(11, account.getBalance());
    }

    @Test
    public void make_deposit_in_currency_test() {
        Account account = createAccount("X Y", "1234567890123456", 10);
        double amount = Currency.EUR.hufToCurrency;

        String result = atm.makeDeposit(account, amount, Currency.EUR);
        logResult(result);
        logAccountState(account);

        Assertions.assertEquals("OK", result);
        Assertions.assertEquals(11, account.getBalance());
    }

    @Test
    public void make_deposit_with_log_alert_test() {
        Account account = createAccount("X Y", "1234567890123456", 10);
        long amount = 1000001;

        String result = atm.makeDeposit(account, amount);
        logResult(result);
        logAccountState(account);

        Assertions.assertEquals("OK WITH LOG ALERT", result);
        Assertions.assertEquals(1000011, account.getBalance());
    }

    @Test
    public void make_withdraw_test() {
        Account account = createAccount("X Y", "1234567890123456", 10);
        long amount = 1;

        String result = atm.makeWithdraw(account, amount);
        logResult(result);
        logAccountState(account);

        Assertions.assertEquals("OK", result);
        Assertions.assertEquals(9, account.getBalance());
    }

    @Test
    public void make_withdraw_with_log_alert_test() {
        Account account = createAccount("X Y", "1234567890123456", 1000010);
        long amount = 1000001;

        String result = atm.makeWithdraw(account, amount);
        logResult(result);
        logAccountState(account);

        Assertions.assertEquals("OK WITH LOG ALERT", result);
        Assertions.assertEquals(9, account.getBalance());
    }

    @Test
    public void make_withdraw_with_not_enough_balance_test() {
        Account account = createAccount("X Y", "1234567890123456", 10);
        long amount = 11;

        String result = atm.makeWithdraw(account, amount);
        logResult(result);
        logAccountState(account);

        Assertions.assertEquals("NOT ENOUGH MONEY", result);
        Assertions.assertEquals(10, account.getBalance());
    }

    @Test
    public void make_transfer_test() {
        Account fromAccount = createAccount("X Y", "1234567890123456", 10);
        Account toAccount = createAccount("Z W", "6543210987654321", 20);
        long amount = 1;

        String result = atm.transfer(fromAccount, toAccount, amount);
        logResult(result);
        logAccountState(fromAccount);
        logAccountState(toAccount);

        Assertions.assertEquals("OK", result);
        Assertions.assertEquals(9, fromAccount.getBalance());
        Assertions.assertEquals(21, toAccount.getBalance());
    }

    @Test
    public void make_transfer_with_log_alert_test() {
        Account fromAccount = createAccount("X Y", "1234567890123456", 1000010);
        Account toAccount = createAccount("Z W", "6543210987654321", 20);
        long amount = 1000001;

        String result = atm.transfer(fromAccount, toAccount, amount);
        logResult(result);
        logAccountState(fromAccount);
        logAccountState(toAccount);

        Assertions.assertEquals("OK WITH LOG ALERT", result);
        Assertions.assertEquals(9, fromAccount.getBalance());
        Assertions.assertEquals(1000021, toAccount.getBalance());
    }

    @Test
    public void make_transfer_with_not_enough_balance_test() {
        Account fromAccount = createAccount("X Y", "1234567890123456", 10);
        Account toAccount = createAccount("Z W", "6543210987654321", 20);
        long amount = 11;

        String result = atm.transfer(fromAccount, toAccount, amount);
        logResult(result);
        logAccountState(fromAccount);
        logAccountState(toAccount);

        Assertions.assertEquals("NOT ENOUGH MONEY", result);
        Assertions.assertEquals(10, fromAccount.getBalance());
        Assertions.assertEquals(20, toAccount.getBalance());
    }

    @Test
    public void complex_test_with_two_accounts() {
        Account account01 = createAccount("JOHN DOE", "1111222233334444", 2000000);
        long balance1 = 2000000;
        Account account02 = createAccount("JACK BLACK", "0000111122223333", 10000);
        long balance2 = 10000;
        atm.makeDeposit(account02, 12000);
        balance2 += 12000;
        atm.makeWithdraw(account01, 9000);
        balance1 -= 9000;
        atm.showBalance(account02);
        atm.showBalance(account01);
        atm.transfer(account01, account02, 20000);
        balance1 -= 20000;
        balance2 += 20000;
        atm.transfer(account02, account01, 90000);
        atm.makeDeposit(account01, 1500000);
        atm.makeWithdraw(account01, 1500000);
        logExchangeRates();
        atm.makeDeposit(account01, 500, Currency.USD);
        balance1 += Math.round(500 / Currency.USD.hufToCurrency);
        atm.makeWithdraw(account01, 20, Currency.EUR);
        balance1 -= Math.round(20 / Currency.EUR.hufToCurrency);
        atm.printBalance(account01);
        Assertions.assertEquals(balance1, account01.getBalance());
        Assertions.assertEquals(balance2, account02.getBalance());
    }

}
