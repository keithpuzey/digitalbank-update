package io.digisic.bank.controller.web;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.digisic.bank.feature.FeatureFlagService;

@RestController
@RequestMapping("/user/features")
public class FeatureController {

    @Autowired private FeatureFlagService featureFlags;

    @PostMapping("/{key}/toggle")
    public Map<String, Object> toggle(@PathVariable String key) {
        return Map.of("key", key, "enabled", featureFlags.toggle(key));
    }
}