package org.jetbrains.plan.runner

import org.jetbrains.plan.runner.context.AbstractTask
import org.jetbrains.plan.runner.context.Context
import org.jetbrains.plan.runner.context.DatasetLoader
import org.jetbrains.plan.runner.storage.TypeSafeStorage

/**
 * TaskPostprocessor is an interface that defines the contract for postprocessing tasks.
 * Mostly, postprocessors should save results or transform the results of the experiments
 * It provides the ability to apply a preprocessing operation on a task solution.
 *
 * This action will apply after the running of the metric collection.
 * @param C a context for the experiment, that could be provided to the post-processor during the setup
 * @param T a task, that is used in the experiment
 * @param I solutions (or information) that is provided for each task by [org.jetbrains.plan.runner.context.DatasetLoader]
 * @param D a dataset loader that provided during the setup
 */
interface TaskPostprocessor<C: Context, T: AbstractTask, I, D: DatasetLoader<T, I>> {
    /**
     * Applies the postprocessor to the provided task and its results.
     *
     * @param information a map containing key-value pairs of information to be collected from the task
     * @param task the task from the information was collected
     */
    fun apply(information: TypeSafeStorage, task: T)

    /**
     * This method represents a post-action to be performed after processing the tasks, collecting, saving the results.
     */
    fun postAction() = Unit

    /**
     * Sets up the TaskPostprocessor with the provided context and dataset loader.
     *
     * @param context The context for the experiment.
     * @param dataset The dataset loader used in the experiment.
     */
    fun setup(context: C, dataset: D) = Unit
}