/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.hadoop.taskexecutor;

import org.gridgain.grid.*;
import org.gridgain.grid.hadoop.*;
import org.gridgain.grid.kernal.processors.hadoop.*;
import org.gridgain.grid.kernal.processors.hadoop.shuffle.collections.*;
import org.gridgain.grid.util.lang.*;
import org.gridgain.grid.util.offheap.unsafe.*;

import java.util.*;

import static org.gridgain.grid.hadoop.GridHadoopJobProperty.*;
import static org.gridgain.grid.hadoop.GridHadoopTaskType.*;

/**
 * Runnable task.
 */
public abstract class GridHadoopRunnableTask implements GridPlainCallable<Void> {
    /** */
    private final GridUnsafeMemory mem;

    /** */
    private final GridHadoopJob job;

    /** Task to run. */
    private final GridHadoopTaskInfo info;

    /** */
    private final GridHadoopJobClassLoadingContext clsLdrCtx;

    /** Submit time. */
    private long submitTs = System.currentTimeMillis();

    /** Execution start timestamp. */
    private long execStartTs;

    /** Execution end timestamp. */
    private long execEndTs;

    /** */
    private GridHadoopMultimap local;

    /** */
    private volatile GridHadoopTask task;

    /** Set if task is to cancelling. */
    private volatile boolean cancelled;

    /**
     * @param job Job.
     * @param mem Memory.
     * @param info Task info.
     * @param clsLdrCtx Class loading context.
     */
    public GridHadoopRunnableTask(GridHadoopJob job, GridUnsafeMemory mem, GridHadoopTaskInfo info,
        GridHadoopJobClassLoadingContext clsLdrCtx) {
        this.job = job;
        this.mem = mem;
        this.info = info;
        this.clsLdrCtx = clsLdrCtx;
    }

    /**
     * @return Wait time.
     */
    public long waitTime() {
        return execStartTs - submitTs;
    }

    /**
     * @return Execution time.
     */
    public long executionTime() {
        return execEndTs - execStartTs;
    }

    /** {@inheritDoc} */
    @Override public Void call() throws GridException {
        execStartTs = System.currentTimeMillis();

        boolean runCombiner = info.type() == MAP && job.hasCombiner() &&
            !get(job, SINGLE_COMBINER_FOR_ALL_MAPPERS, false);

        ClassLoader old = GridHadoopJobClassLoadingContext.prepareClassLoader(clsLdrCtx, job.info());

        GridHadoopTaskState state = GridHadoopTaskState.COMPLETED;
        Throwable err = null;

        try {
            runTask(info, runCombiner);

            if (runCombiner)
                runTask(new GridHadoopTaskInfo(info.nodeId(), COMBINE, info.jobId(), info.taskNumber(), info.attempt(),
                    null), runCombiner);
        }
        catch (GridHadoopTaskCancelledException e) {
            state = GridHadoopTaskState.CANCELED;
        }
        catch (Throwable e) {
            state = GridHadoopTaskState.FAILED;
            err = e;
        }
        finally {
            execEndTs = System.currentTimeMillis();

            onTaskFinished(state, err);

            Thread.currentThread().setContextClassLoader(old);

            if (runCombiner)
                local.close();
        }

        return null;
    }

    /**
     * @param info Task info.
     * @param localCombiner If we have mapper with combiner.
     * @throws GridException If failed.
     */
    private void runTask(GridHadoopTaskInfo info, boolean localCombiner) throws GridException {
        if (cancelled)
            throw new GridHadoopTaskCancelledException("Task cancelled.");

        try (GridHadoopTaskOutput out = createOutput(info, localCombiner);
             GridHadoopTaskInput in = createInput(info, localCombiner)) {

            GridHadoopTaskContext ctx = new GridHadoopTaskContext(job, in, out);

            task = job.createTask(info);

            if (cancelled)
                throw new GridHadoopTaskCancelledException("Task cancelled.");

            task.run(ctx);
        }
    }

    /**
     * Cancel the executed task.
     */
    public void cancel() {
        cancelled = true;

        if (task != null)
            task.cancel();
    }

    /**
     * @param state State.
     * @param err Error.
     */
    protected abstract void onTaskFinished(GridHadoopTaskState state, Throwable err);

    /**
     * @param info Task info.
     * @param localCombiner If we have mapper with combiner.
     * @return Task input.
     * @throws GridException If failed.
     */
    @SuppressWarnings("unchecked")
    private GridHadoopTaskInput createInput(GridHadoopTaskInfo info, boolean localCombiner) throws GridException {
        switch (info.type()) {
            case MAP:
            case COMMIT:
            case ABORT:
                return null;

            case COMBINE:
                if (localCombiner) {
                    assert local != null;

                    return local.input((Comparator<Object>)job.combineGroupComparator());
                }

            default:
                return createInput(info);
        }
    }

    /**
     * @param info Task info.
     * @return Input.
     * @throws GridException If failed.
     */
    protected abstract GridHadoopTaskInput createInput(GridHadoopTaskInfo info) throws GridException;

    /**
     * @param info Task info.
     * @return Output.
     * @throws GridException If failed.
     */
    protected abstract GridHadoopTaskOutput createOutput(GridHadoopTaskInfo info) throws GridException;

    /**
     * @param info Task info.
     * @param localCombiner If we have mapper with combiner.
     * @return Task output.
     * @throws GridException If failed.
     */
    private GridHadoopTaskOutput createOutput(GridHadoopTaskInfo info, boolean localCombiner) throws GridException {
        switch (info.type()) {
            case REDUCE:
            case COMMIT:
            case ABORT:
                return null;

            case MAP:
                if (localCombiner) {
                    assert local == null;

                    local = get(job, SHUFFLE_COMBINER_NO_SORTING, false) ?
                        new GridHadoopHashMultimap(job, mem, get(job, COMBINER_HASHMAP_SIZE, 8 * 1024)):
                        new GridHadoopSkipList(job, mem, job.sortComparator()); // TODO replace with red-black tree

                    return local.startAdding();
                }

            default:
                return createOutput(info);
        }
    }

    /**
     * @return Task info.
     */
    public GridHadoopTaskInfo taskInfo() {
        return info;
    }
}
