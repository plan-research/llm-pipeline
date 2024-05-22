package org.jetbrains.plan.runner

import org.jetbrains.plan.runner.context.AbstractTask
import org.jetbrains.plan.runner.context.Context

/**
 * TaskPreprocessor is an interface that defines the contract for task preprocessing operations.
 *
 * @param C a context for the experiment, that could be provided to the preprocessor during the setup
 * @param T a task, that is used in the experiment, extends [AbstractTask]
 * @param I solutions (or information) that is provided for each task by [org.jetbrains.plan.runner.context.DatasetLoader]
 */
interface TaskPreprocessor<C : Context, T : AbstractTask, I> {
    /**
     * Applies a preprocessing operation on a task solution.
     *
     * @param task The task to be applied.
     * @param taskSolutions The list of task solutions to be processed.
     * @return A list of processed task solutions.
     */
    fun apply(task: T, taskSolutions: List<I>): List<I>


    /**
     * Sets up the TaskPreprocessor with the provided context.
     *
     * @param context The context for the experiment.
     */
    fun setup(context: C) = Unit
}