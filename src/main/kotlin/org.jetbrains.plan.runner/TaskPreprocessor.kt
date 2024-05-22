package org.jetbrains.plan.runner

import org.jetbrains.plan.runner.context.AbstractTask
import org.jetbrains.plan.runner.context.Context

/**
 * TaskPreprocessor is an interface that defines the contract for preprocessing tasks.
 * It provides the ability to apply a preprocessing operation on a task solution.
 *
 * This action will apply before the running of the metric collection.
 * This allows filtering solutions or gathering generated parts.
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
     * Sets up the [TaskPreprocessor] with the provided [dataset].
     *
     * @param dataset The dataset provider that contains the tasks and other necessary information.
     */
    fun setup(context: C) = Unit
}