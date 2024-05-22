package org.jetbrains.plan.runner.context

abstract class AbstractTask {
    abstract val title: String
    override fun toString(): String = "Task \"$title\""
}