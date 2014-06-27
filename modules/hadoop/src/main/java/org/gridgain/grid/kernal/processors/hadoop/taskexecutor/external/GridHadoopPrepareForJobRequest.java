/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.hadoop.taskexecutor.external;

import org.gridgain.grid.hadoop.*;
import org.gridgain.grid.kernal.processors.hadoop.message.*;

/**
 * Child process initialization request.
 */
public class GridHadoopPrepareForJobRequest implements GridHadoopMessage {
    /** */
    private static final long serialVersionUID = 0L;

    /** Job ID. */
    private GridHadoopJobId jobId;

    /** Job info. */
    private GridHadoopJobInfo jobInfo;

    /** Has mappers flag. */
    private boolean hasMappers;

    /** */
    private int reducers;

    /**
     * @param jobId Job ID.
     * @param jobInfo Job info.
     * @param hasMappers Has mappers flag.
     * @param reducers Number of reducers in job.
     */
    public GridHadoopPrepareForJobRequest(GridHadoopJobId jobId, GridHadoopJobInfo jobInfo, boolean hasMappers,
        int reducers) {
        this.jobId = jobId;
        this.jobInfo = jobInfo;
        this.hasMappers = hasMappers;
        this.reducers = reducers;
    }

    /**
     * @return Job info.
     */
    public GridHadoopJobInfo jobInfo() {
        return jobInfo;
    }

    /**
     * @return Job ID.
     */
    public GridHadoopJobId jobId() {
        return jobId;
    }

    /**
     * @return Has mappers flag.
     */
    public boolean hasMappers() {
        return hasMappers;
    }

    /**
     * @return Number of reducers in job.
     */
    public int reducers() {
        return reducers;
    }
}