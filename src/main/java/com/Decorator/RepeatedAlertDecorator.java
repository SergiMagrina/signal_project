package com.Decorator;

import java.util.Timer;
import java.util.TimerTask;

public class RepeatedAlertDecorator extends AlertDecorator {

    private int repeatCount;
    private long repeatIntervalMs;

    public RepeatedAlertDecorator(Alert alert, int repeatCount, long repeatIntervalMs) {
        super(alert);
        this.repeatCount = repeatCount;
        this.repeatIntervalMs = repeatIntervalMs;
    }

    @Override
    public void trigger() {
        // Trigger the alert immediately
        super.trigger();

        // Setup repeated alert triggering using a Timer
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            int timesTriggered = 1;

            @Override
            public void run() {
                if (timesTriggered < repeatCount) {
                    System.out.println("Repeated alert for patient " + getPatientId());
                    superTrigger();
                    timesTriggered++;
                } else {
                    timer.cancel();
                }
            }

            private void superTrigger() {
                // call the wrapped alert's trigger method
                wrappedAlert.trigger();
            }
        };

        timer.schedule(task, repeatIntervalMs, repeatIntervalMs);
    }
}
