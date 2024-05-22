package org.jetbrains.plan.runner.context

/**
 * AbstractTask is an abstract class that represents a task.
 * Basically, the task should contain only information that distributes across all solutions.
 * It could be a title of the task, its prompt and some necessary information
 *
 * @property title The title of the task.
 */
abstract class AbstractTask {
    abstract val title: String
    override fun toString(): String = "Task \"$title\""
}