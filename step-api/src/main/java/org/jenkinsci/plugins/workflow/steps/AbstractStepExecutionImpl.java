package org.jenkinsci.plugins.workflow.steps;

import com.google.inject.Inject;
import java.io.Serializable;

/**
 * Partial implementation of {@link StepExecution} that injects {@link StepContextParameter} upon resume.
 * Declare any {@code transient} fields with {@link StepContextParameter} that you might need.
 * <p>The originating {@link Step} may also be {@link Inject}ed, but you must mark it {@link Inject#optional} since it will <em>not</em> be restored here.
 * If it is only used for the benefit of {@link #start}, mark it {@code transient}.
 * If you need any information from the step definition after a restart, make sure the {@link Step} is {@link Serializable} and do not mark it {@code transient}.
 * (For a {@link AbstractSynchronousStepExecution} these considerations are irrelevant.)
 * @author Kohsuke Kawaguchi
 */
public abstract class AbstractStepExecutionImpl extends StepExecution {

    protected AbstractStepExecutionImpl() {
    }

    protected AbstractStepExecutionImpl(StepContext context) {
        super(context);
    }


    /**
     * Reinject {@link StepContextParameter}s.
     */
    @Override
    public void onResume() {
        inject();
    }

    protected void inject() {
        try {
            AbstractStepImpl.prepareInjector(getContext(), null).injectMembers(this);
        } catch (Exception e) {
            getContext().onFailure(e);
        }
    }
}
