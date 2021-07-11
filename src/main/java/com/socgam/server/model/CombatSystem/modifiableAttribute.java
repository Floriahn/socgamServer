package com.socgam.server.model.CombatSystem;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class modifiableAttribute implements modifiableAttributeInterface {
    // attributes
    private int value;
    private int modifier;
    private int maxValue;
    private String displayName;
    private int lastValue;

    // For the beans observer pattern
    private PropertyChangeSupport support;

    // Constructor
    public modifiableAttribute(String displayName)  {
        support = new PropertyChangeSupport(this);
        this.displayName = displayName;
        this.value = 0;
        this.maxValue = Integer.MAX_VALUE;
    }
    public modifiableAttribute(String displayName, int initialValue)  {
        support = new PropertyChangeSupport(this);
        this.displayName = displayName;
        this.value = initialValue;
        this.maxValue = Integer.MAX_VALUE;
    }
    public modifiableAttribute(String displayName,  int initialValue, int maxValue)  {
        support = new PropertyChangeSupport(this);
        this.displayName = displayName;
        this.value = initialValue;
        this.maxValue = maxValue;
    }

    // Public methods
    public int getMaxValue() {
        return maxValue;
    }

    public int getValue() { return actualValue(); }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void changeAttribute(int amount) {
        setValue(value+amount);
    }

    public void increaseModifier(int amount) {
        modifier += amount;
        updateValue();
    }

    public void setMaxValue(int value) {
        maxValue = value;
    }

    // Private methods
    private void setValue(int i){
        int newValue = i;
        if(newValue < 0) {
            newValue = 0;
        } else if(newValue > maxValue) {
            newValue = maxValue;
        }
        value = newValue;
        updateValue();
    }

    private int actualValue() {
        int r = value + modifier;
        if(r < 0) {
            r = 0;
        } else if(r > maxValue) {
            r = maxValue;
        }
        return r;
    }

    private void updateValue() {
        int newValue = actualValue();
        support.firePropertyChange("value", lastValue, newValue);
        lastValue = newValue;
    }

    // Add or remove listeners
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
}
