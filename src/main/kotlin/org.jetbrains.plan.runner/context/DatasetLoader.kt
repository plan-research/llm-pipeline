package org.jetbrains.plan.runner.context

interface DatasetLoader<E: AbstractTask, I> {
    fun getTasks(): Iterable<E>
    fun getTaskSolutions(task: E): List<I>
}