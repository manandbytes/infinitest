package org.infinitest.eclipse.prefs;

import static java.lang.Integer.*;
import static org.apache.commons.lang.StringUtils.*;
import static org.eclipse.jface.preference.FieldEditor.*;
import static org.infinitest.eclipse.prefs.PreferencesConstants.*;
import static org.infinitest.util.InfinitestGlobalSettings.*;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.infinitest.eclipse.PluginActivationController;
import org.infinitest.eclipse.markers.SlowMarkerRegistry;
import org.infinitest.eclipse.workspace.CoreSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PreferenceChangeHandler
{
    private final PluginActivationController controller;
    private final CoreSettings coreSettings;
    private SlowMarkerRegistry slowMarkerRegistry;
    private boolean clearSlowMarkerRegistry;

    @Autowired
    public PreferenceChangeHandler(PluginActivationController controller, CoreSettings coreSettings)
    {
        this.controller = controller;
        this.coreSettings = coreSettings;
    }

    public void propertyChange(PropertyChangeEvent event)
    {
        String preference = findChangedPreference(event);
        Object newValue = event.getNewValue();
        if (AUTO_TEST.equals(preference))
        {
            updateAutoTest((Boolean) newValue);
        }

        if (SLOW_TEST_WARNING.equals(preference))
        {
            updateSlowTestWarning((String) newValue);
        }

        if (PARALLEL_CORES.equals(preference))
        {
            updateConcurrency((String) newValue);
        }
    }

    private void updateConcurrency(String newValue)
    {
        if (!isBlank(newValue))
        {
            coreSettings.setConcurrentCoreCount(Integer.parseInt(newValue));
        }
    }

    private void updateSlowTestWarning(String newValue)
    {
        if (!isBlank(newValue))
        {
            setSlowTestTimeLimit(parseInt(newValue));
            // Remove markers created per previous value
            clearSlowMarkerRegistry = true;
        }

    }

    private void updateAutoTest(Boolean continuouslyTest)
    {
        if (continuouslyTest.booleanValue())
        {
            controller.enable();
        }
        else
        {
            controller.disable();
        }
    }

    private String findChangedPreference(PropertyChangeEvent event)
    {
        Object source = event.getSource();
        if (source instanceof FieldEditor && event.getProperty().equals(VALUE))
        {
            return ((FieldEditor) source).getPreferenceName();
        }
        return null;
    }

    public void setSlowMarkerRegistry(SlowMarkerRegistry bean)
    {
        slowMarkerRegistry = bean;
        clearSlowMarkerRegistry = false;
    }

    public void applyChanges()
    {
        if (clearSlowMarkerRegistry)
        {
            clearSlowMarkers();
        }

    }

    public void clearChanges()
    {
        clearSlowMarkerRegistry = false;
    }

    public void clearSlowMarkers()
    {
        clearSlowMarkerRegistry = false;
        if (slowMarkerRegistry != null)
        {
            slowMarkerRegistry.clear();
        }
    }
}
