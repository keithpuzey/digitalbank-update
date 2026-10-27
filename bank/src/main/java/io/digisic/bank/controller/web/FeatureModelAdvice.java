package io.digisic.bank.controller.web;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import io.digisic.bank.feature.FeatureFlagService;

@ControllerAdvice(basePackages = "io.digisic.bank.controller.web")
public class FeatureModelAdvice {

    @Autowired private FeatureFlagService featureFlags;

    @ModelAttribute("featureMenu")
    public List<Map<String, Object>> featureMenu() {
        return featureFlags.menuModel();
    }
}