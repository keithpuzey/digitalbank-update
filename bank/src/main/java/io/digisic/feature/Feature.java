package io.digisic.bank.feature;

public enum Feature {
    // key, menu label, font-awesome icon, badge lifetime (minutes)
    NEW_DEPOSIT_BADGE("new-deposit-badge", "Highlight New Deposits", "fa-star-o", 2);
    // Future features: add a line here

    private final String key, label, icon;
    private final int durationMinutes;

    Feature(String key, String label, String icon, int durationMinutes) {
        this.key = key; this.label = label; this.icon = icon; this.durationMinutes = durationMinutes;
    }
    public String getKey() { return key; }
    public String getLabel() { return label; }
    public String getIcon() { return icon; }
    public int getDurationMinutes() { return durationMinutes; }

    public static Feature fromKey(String key) {
        for (Feature f : values()) if (f.key.equals(key)) return f;
        throw new IllegalArgumentException("Unknown feature: " + key);
    }
}