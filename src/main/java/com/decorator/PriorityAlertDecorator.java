package com.decorator;

public class PriorityAlertDecorator extends AlertDecorator {

    private String priorityLevel;

    public PriorityAlertDecorator(Alert alert, String priorityLevel) {
        super(alert);
        this.priorityLevel = priorityLevel;
    }

    @Override
    public void trigger() {
        System.out.println("Priority Level: " + priorityLevel);
        super.trigger();
    }

    // Optionally override getCondition to include priority
    @Override
    public String getCondition() {
        return "[" + priorityLevel + "] " + super.getCondition();
    }
}

