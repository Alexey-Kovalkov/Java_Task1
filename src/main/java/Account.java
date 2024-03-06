import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;

public class Account {
    private String ownerName;

    private AccType accType;
    private HashMap<Currency, Integer> accSaldo = new HashMap<>();

    private Deque<Command> commands = new ArrayDeque<>();

    private Account(){}
    public Account(String ownerName) {
        this.setOwnerName(ownerName);
    }

    public Loadable save() {return new Snapshot();}

    private class Snapshot implements Loadable {
        private String ownerName;
        private HashMap<Currency, Integer> accSaldo = new HashMap<>();
        private Deque<Command> commands = new ArrayDeque<>();

        public Snapshot (){
            this.ownerName = Account.this.ownerName;
            this.accSaldo = new HashMap<>(Account.this.accSaldo);
            this.commands = new ArrayDeque<>(Account.this.commands);
        }

        public void load () {
            Account.this.ownerName = this.ownerName;
            Account.this.accSaldo = new HashMap<>(this.accSaldo);
            Account.this.commands = new ArrayDeque<>(this.commands);
        }
    }

    public void setOwnerName(String ownerName) {
        if (ownerName == null || ownerName.isEmpty()) throw new IllegalArgumentException("Имя д.б. указано!");
        String oldOwner = this.ownerName;
        this.commands.push(() -> {this.ownerName = oldOwner;});
        this.ownerName = ownerName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setSaldo(Currency curr, Integer accS) {
        if (accS < 0) throw new IllegalArgumentException("Количество валюты не может быть отрицательным!");
        if (this.accSaldo.containsKey(curr)) {
            Integer oldSum = this.accSaldo.get(curr);
            this.commands.push(() -> {this.accSaldo.put(curr, oldSum);});
        }
        else {
            this.commands.push(() -> {this.accSaldo.remove(curr);});
        }
        this.accSaldo.put(curr, accS);
    }

    public AccType getAccType() {
        return accType;
    }

    public void setAccType(AccType accType) {
        AccType oldType = this.accType;
        this.commands.push(() -> {this.accType = oldType;});
        this.accType = accType;
    }

    public String printAccType(){
        if (this.accType == null) {
            return "Не присвоен";
        }
        else {
            return this.accType.toString();
        }
    }
    public HashMap<Currency, Integer> getAccSaldo() {
        return new HashMap<Currency, Integer>(this.accSaldo);
    }

    public Account undo(){
        if (this.commands.isEmpty()) {throw new NothingToUndo();}
        commands.pop().perform();
        return this;
    }

    public void printSaldo(){
        for (Currency curr: this.accSaldo.keySet()){
            System.out.println(curr.toString() + " - " + this.accSaldo.get(curr));
        }
    }
}
