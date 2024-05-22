package org.jetbrains.plan.runner

import org.jetbrains.plan.runner.context.AbstractTask
import org.jetbrains.plan.runner.context.Context
import org.jetbrains.plan.runner.context.DatasetLoader
import org.jetbrains.plan.runner.context.ProcessingStatus
import org.jetbrains.plan.runner.storage.StorageKey
import org.jetbrains.plan.runner.storage.mutableTypeSafeStorage
import kotlin.system.measureTimeMillis


/**
 * The ExperimentRunner class is responsible for running experiments on a dataset.
 * The current implementation is single-threaded.
 *
 * @param C a type that represents the experiment context
 * @param T a type that represents the tasks to be executed
 * @param I a type that represents the solutions or information provided for each task by the DatasetLoader
 * @param D a type that represents the DatasetLoader used to load tasks and solutions
 * @property context the experiment context
 * @property datasetLoader the DatasetLoader used to load tasks and solutions
 * @property preprocessors the list of TaskPreprocessors to be applied before running the tasks
 * @property processors the list of TaskProcessors to be applied for each task
 * @property postProcessors the list of TaskPostprocessors to be applied after running the tasks
 * @property breakOnFail a boolean value indicating whether to break the execution if a task fails
 */
class ExperimentRunner<C : Context, T: AbstractTask, I, D: DatasetLoader<T, I>>(
    private val context: C,
    private val datasetLoader: D,
    private val preprocessors: List<TaskPreprocessor<C, T, I>>,
    private val processors: List<TaskProcessor<C, T, I, D>>,
    private val postProcessors: List<TaskPostprocessor<C, T, I, D>>,
    private val breakOnFail: Boolean = false,
) {
    /**
     * List of all tasks. The order of the processing is derived by its title
     */
    private val tasks = datasetLoader.getTasks().associateBy { it.title }

    /**
     * Runs the experiment by performing preprocessing, processing, and postprocessing tasks.
     */
    fun run() {
        // Firstly, setup all the environment in the right order
        preprocessors.forEach { it.setup(context) }
        processors.forEach { it.setup(context, datasetLoader) }
        postProcessors.forEach { it.setup(context, datasetLoader) }

        // Process each task
        tasks.forEach { (_, task) ->
            val totalConsumed = mutableMapOf<String, Long>()
            val solutions =
                preprocessors.fold(datasetLoader.getTaskSolutions(task)) { acc, preprocessor ->
                    var result: List<I>
                    val timeConsumed = measureTimeMillis {
                        result = preprocessor.apply(task, acc)
                    }
                    totalConsumed[preprocessor.javaClass.name] = timeConsumed
                    result
                }

            val results = mutableTypeSafeStorage()
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
        /**
         * A storage key for storing time consumed by each (pre-)processor information.
         */
        val timeConsumed = StorageKey<Map<String, Long>>("timeConsumed")
    }
}