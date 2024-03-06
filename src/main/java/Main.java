import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        Account acc = new Account("O. Bender");
        acc.setSaldo (Currency.RUB, 100);
        printAcc(acc);

        acc.setOwnerName("Михаил Паниковский");
        acc.setAccType(AccType.USUAL);
        acc.setSaldo (Currency.RUB, 300);
        acc.setSaldo (Currency.EUR, 4);

        printAcc(acc);

        Loadable sn1 = acc.save();  // Сохранение

        for (int i = 0; i < 3; i++) {  // Откат на 3 шага
            acc.undo();
            printAcc(acc);
        }

        System.out.println("========Восстановление========");

        acc.setAccType(AccType.PREMIUM);

        sn1.load();      // Восстановление
        printAcc(acc);

        acc.undo();      // Проверка восстановления undo
        printAcc(acc);

        acc.setAccType(AccType.PREMIUM);
        printAcc(acc);

        acc.undo();
        printAcc(acc);
    }

    public static void printAcc(Account acc){
        System.out.println("");
        System.out.println("Владелец - " + acc.getOwnerName());
        System.out.println("Тип счёта - " + acc.printAccType());
        acc.printSaldo();
    }
}
