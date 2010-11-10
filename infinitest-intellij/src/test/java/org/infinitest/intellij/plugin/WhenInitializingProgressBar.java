package org.infinitest.intellij.plugin;

import org.infinitest.FakeInfinitestCore;
import org.infinitest.ResultCollector;
import org.infinitest.intellij.FakeInfinitestAnnotator;
import org.infinitest.intellij.FakeTestControl;
import org.infinitest.intellij.plugin.launcher.InfinitestPresenter;
import org.infinitest.intellij.plugin.swingui.InfinitestView;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import static java.awt.Color.BLACK;

public class WhenInitializingProgressBar
{
    @Test
    public void shouldSetBackgroundColorToBlack()
    {
        InfinitestView view = mock(InfinitestView.class);
        createPresenterWith(view);
        verify(view).setProgressBarColor(BLACK);
    }

    @Test
    public void shouldSetMaximumProgress()
    {
        InfinitestView view = mock(InfinitestView.class);
        createPresenterWith(view);
        verify(view).setMaximumProgress(1);
        verify(view).setProgress(1);
    }

    private void createPresenterWith(InfinitestView view)
    {
        new InfinitestPresenter(new ResultCollector(), new FakeInfinitestCore(), view,
                new FakeTestControl(), new FakeInfinitestAnnotator());
    }
}
