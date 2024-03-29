import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class AccTests {

    @Test
    @DisplayName("Create без имени кидает исключение")
    public void newEmptyName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Account(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Account(""));
    }
    @Test
    @DisplayName("Установка пустого имени кидает исключение")
    public void emptyName() {
        Account acc = new Account("O. Bender");
        Assertions.assertThrows(IllegalArgumentException.class, () -> acc.setOwnerName(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> acc.setOwnerName(""));
    }

    @Test
    @DisplayName("Установка отрицательного сальдо кидает исключение")
    public void minusSaldo() {
        Account acc = new Account("Светлана Непростая");
        Assertions.assertThrows(IllegalArgumentException.class, () -> acc.setSaldo (Currency.EUR, -100000000));
    }

    @Test
    @DisplayName("Undo имени и NothingToUndo")
    public void undoTst() {
        Account acc = new Account("O. Bender");
        String startName = acc.getOwnerName();
        acc.setOwnerName("А. Балаганов");
        Assertions.assertNotEquals(startName, acc.getOwnerName());
        acc.undo();
        Assertions.assertEquals(startName, acc.getOwnerName());
        acc.undo();
        Assertions.assertThrows(NothingToUndo.class, () -> acc.undo());
    }

    @Test
    @DisplayName("Undo сальдо")
    public void undoSaldo() {
        HashMap<Currency, Integer> mapSaldo = new HashMap<>();
        mapSaldo.put(Currency.RUB, 300);
        mapSaldo.put(Currency.EUR, 60);

        Account acc = new Account("Степан Относительный");
        acc.setSaldo(Currency.RUB, mapSaldo.get(Currency.RUB));
        acc.setSaldo(Currency.EUR, mapSaldo.get(Currency.EUR));
        Assertions.assertEquals(mapSaldo, acc.getAccSaldo());

        acc.undo();
        Assertions.assertNotEquals(mapSaldo, acc.getAccSaldo());

        mapSaldo.remove(Currency.EUR);
        Assertions.assertEquals(mapSaldo, acc.getAccSaldo());
    }

    @Test
    @DisplayName("Undo тип счёта")
    public void undoType() {
        Account acc = new Account("Виктор Однозначный");
        acc.setAccType(AccType.PREMIUM);
        acc.setAccType(AccType.USUAL);
        acc.undo();
        Assertions.assertEquals(AccType.PREMIUM, acc.getAccType());
        acc.undo();
        Assertions.assertNull(acc.getAccType());
    }

    @Test
    @DisplayName("Тест сохранения")
    public void accSave() {
        // исходные значения
        String name1 = "Генрих Поперечный";
        AccType type1 = AccType.USUAL;
        HashMap<Currency, Integer> mapSaldo = new HashMap<>();
        mapSaldo.put(Currency.RUB, 300);
        mapSaldo.put(Currency.EUR, 60);


        Account acc = new Account(name1);
        acc.setSaldo(Currency.RUB, mapSaldo.get(Currency.RUB));
        acc.setSaldo(Currency.EUR, mapSaldo.get(Currency.EUR));
        acc.setAccType(type1);

        // Сохранение
        Loadable sn1 = acc.save();

        // Изменение
        acc.setOwnerName("Артур Продольный");
        acc.setSaldo (Currency.RUB, 100);
        acc.setSaldo (Currency.EUR, 4);
        acc.setAccType(AccType.PREMIUM);

        // Восстановление
        sn1.load();

        Assertions.assertEquals(name1, acc.getOwnerName());
        Assertions.assertEquals(type1, acc.getAccType());
        Assertions.assertEquals(mapSaldo, acc.getAccSaldo());
    }
}