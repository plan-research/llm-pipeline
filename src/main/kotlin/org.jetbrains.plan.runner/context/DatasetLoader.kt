package org.jetbrains.plan.runner.context

/**
 * DatasetLoader is an interface that defines the contract for loading datasets for experiments.
 *
 * @param E a subclass of [AbstractTask] representing the type of tasks in the dataset
 * @param I the type of solutions or information associated with each task in the dataset
 */
interface DatasetLoader<E: AbstractTask, I> {
    fun getTasks(): Iterable<E>

    /**
     * Get all solutions (or information) for the [task].
     */
    fun getTaskSolutions(task: E): List<I>
}