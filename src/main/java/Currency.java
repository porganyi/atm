public enum Currency {
    HUF(1),
    EUR(0.0025),
    USD(0.0027),
    GBP(0.0022);
    public final double hufToCurrency;

    Currency(double hufToCurrency) {
        this.hufToCurrency = hufToCurrency;
    }
}
