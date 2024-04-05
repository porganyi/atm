import org.junit.Test;

public class AtmTest {

    Atm atm = new Atm();

    Account account01 = new Account("JOHN DOE", "1111222233334444", 2000000);

    @Test
    public void firstTest() {
        System.out.println(account01.balance + ", " + account01.currency);
    }

}
