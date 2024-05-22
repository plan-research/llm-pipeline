package org.jetbrains.plan.runner

import org.jetbrains.plan.runner.context.AbstractTask
import org.jetbrains.plan.runner.context.Context
import org.jetbrains.plan.runner.context.DatasetLoader
import org.jetbrains.plan.runner.context.ProcessingStatus
import org.jetbrains.plan.runner.storage.StorageKey
import org.jetbrains.plan.runner.storage.mutableTypeSafeStorage
import kotlin.system.measureTimeMillis


class ExperimentRunner<C : Context, T: AbstractTask, I, D: DatasetLoader<T, I>>(
    private val context: C,
    private val datasetLoader: D,
    private val preprocessors: List<TaskPreprocessor<C, T, I>>,
    private val processors: List<TaskProcessor<C, T, I, D>>,
    private val postProcessors: List<TaskPostprocessor<C, T, I, D>>,
    val breakOnFail: Boolean = false,
) {
    private val tasks = datasetLoader.getTasks().associateBy { it.title }

    fun run() {
        preprocessors.forEach { it.setup(context) }
        processors.forEach { it.setup(context, datasetLoader) }
        postProcessors.forEach { it.setup(context, datasetLoader) }

        tasks.forEach { (_, task) ->
            val solutions =
                preprocessors.fold(datasetLoader.getTaskSolutions(task)) { acc, preprocessor ->
                    preprocessor.apply(task, acc)
                }

            val results = mutableTypeSafeStorage()
            val totalConsumed = mutableMapOf<String, Long>()
            for (processor in processors) {
                var status: ProcessingStatus
                val timeConsumed = measureTimeMillis {
                    status = processor.apply(task, solutions, results)
                }
                totalConsumed[processor.javaClass.name] = timeConsumed
                if (status == ProcessingStatus.Failed && breakOnFail) {
                    break
                }
            }
            results[timeConsumed] = totalConsumed
            for (postprocessor in postProcessors) {
                postprocessor.apply(results, task)
            }
        }
        postProcessors.forEach { it.postAction() }
    }

    companion object {
        val timeConsumed = StorageKey<Map<String, Long>>("timeConsumed")
    }
}