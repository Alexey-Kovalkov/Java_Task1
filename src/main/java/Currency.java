public enum Currency {
    USD("Доллар США"),
    EUR("Евро"),
    JOY("Йена"),
    TRY("Лира"),
    RUB("Российский рубль");

    private String name;


    Currency(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
