public class LamportClock {
    private int value;

    public LamportClock() {
        this.value = 0;
    }

    public int getValue() {
        return value;
    }

    public void tick() {

        value++;
    }

    public void sendEvent() {

        value++;
    }

    public void receiveAction(int receivedValue) {

        value = Math.max(value, receivedValue) + 1;
    }
}
