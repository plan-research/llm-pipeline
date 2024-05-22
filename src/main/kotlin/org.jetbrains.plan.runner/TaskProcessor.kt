package org.jetbrains.plan.runner

import org.jetbrains.plan.runner.context.AbstractTask
import org.jetbrains.plan.runner.context.Context
import org.jetbrains.plan.runner.context.DatasetLoader
import org.jetbrains.plan.runner.context.ProcessingStatus
import org.jetbrains.plan.runner.storage.MutableTypeSafeStorage

/**
 * Represents a task processor that can process tasks, e.g. MetricCalculator or Lexical Filtering
 */
interface TaskProcessor<C: Context, T: AbstractTask, I, D: DatasetLoader<T, I>> {
    /**
     * Applies a dataset specific properties to the task processor.
     * @param datasetProvider The dataset to get property from.
     */
    fun setup(context: C, datasetProvider: D) = Unit
    /**
     * Applies a task to a list of solutions with specified entry names and returns the processing status and result.
     *
     * @param task The task to be applied.
     * @param solutions The list of solutions for the task.
     * @return A Pair object containing the processing status and the result of applying the task.
     */
    fun apply(task: T, solutions: List<I>, experimentContext: MutableTypeSafeStorage) : ProcessingStatus
}