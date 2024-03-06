public enum AccType {

    USUAL("Обычный счёт"),
    PREMIUM("Премиальный счёт");

    final private String name;

    AccType(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
