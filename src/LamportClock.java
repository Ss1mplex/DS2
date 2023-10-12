public class LamportClock {
    private long time;

    public LamportClock() {
        this.time = 0;
    }

    public synchronized long getTime() {
        return time;
    }

    public synchronized void tick() {

        time++;
    }

    public synchronized void updateClock(long receivedTime) {

        time = Math.max(time, receivedTime) + 1;
    }
}
