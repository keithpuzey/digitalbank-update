package io.digisic.bank.feature;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service("featureFlags")   // bean name lets Thymeleaf call @featureFlags.xxx
public class FeatureFlagService {

    private final Map<Feature, Boolean> state = new ConcurrentHashMap<>();

    public boolean isEnabled(String key) {
        return state.getOrDefault(Feature.fromKey(key), false);
    }

    /** Flips the flag and returns the new value. */
    public boolean toggle(String key) {
        return state.merge(Feature.fromKey(key), true, (old, v) -> !old);
    }

    /** True if the feature is on AND the event happened within the feature's window. */
    public boolean isRecent(String key, Date when) {
        return remainingMs(key, when) > 0;
    }

    /** Milliseconds left before the badge should disappear (0 if off/expired). */
    public long remainingMs(String key, Date when) {
        Feature f = Feature.fromKey(key);
        if (when == null || !isEnabled(key)) return 0;
        long left = f.getDurationMinutes() * 60_000L - (System.currentTimeMillis() - when.getTime());
        return Math.max(left, 0);
    }

    /** Model for the avatar menu. */
    public List<Map<String, Object>> menuModel() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Feature f : Feature.values()) {
            Map<String, Object> m = new HashMap<>();
            m.put("key", f.getKey());
            m.put("label", f.getLabel());
            m.put("icon", f.getIcon());
            m.put("enabled", isEnabled(f.getKey()));
            list.add(m);
        }
        return list;
    }
}