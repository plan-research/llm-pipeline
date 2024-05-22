package org.jetbrains.plan.runner

import org.jetbrains.plan.runner.context.AbstractTask
import org.jetbrains.plan.runner.context.Context
import org.jetbrains.plan.runner.context.DatasetLoader
import org.jetbrains.plan.runner.context.ProcessingStatus
import org.jetbrains.plan.runner.storage.MutableTypeSafeStorage

/**
 * Represents a task processor that can process tasks, e.g. MetricCalculator or Lexical Filtering
 */
interface TaskProcessor<C : Context, T : AbstractTask, I, D : DatasetLoader<T, I>> {
    /**
     * Sets up the task processor with the provided context and dataset provider.
     *
     * @param context The context for the task processor.
     * @param datasetProvider The dataset provider used in the task processor.
     */
    fun setup(context: C, datasetProvider: D) = Unit

    /**
     * Applies a task to a list of solutions with specified entry names and returns the processing status and result.
     *
     * @param task The task to be applied.
     * @param solutions The list of solutions for the task.
     * @param experimentContext The storage of the intermediate results.
     * The results could be stored via [org.jetbrains.plan.runner.storage.TypeSafeStorage.Key]
     * @return [org.jetbrains.plan.runner.context.ProcessingStatus] of the result of the operation
     */
    fun apply(task: T, solutions: List<I>, experimentContext: MutableTypeSafeStorage): ProcessingStatus
}