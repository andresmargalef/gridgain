/* 
 Copyright (C) GridGain Systems. All Rights Reserved.
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.hadoop.v1;

import org.apache.hadoop.mapred.Counters;
import org.apache.hadoop.mapreduce.*;
import org.gridgain.grid.hadoop.*;
import org.gridgain.grid.kernal.processors.hadoop.v2.*;

import java.io.*;

import static org.apache.hadoop.mapreduce.util.CountersStrings.*;

/**
 * Hadoop counter implementation for v1 API.
 */
class GridHadoopV1Counter extends Counters.Counter {
    /** Delegate. */
    private final GridHadoopCounter counter;

    /**
     * Creates new instance.
     *
     * @param counter Delegate counter.
     */
    public GridHadoopV1Counter(GridHadoopCounter counter) {
        this.counter = counter;
    }

    /** {@inheritDoc} */
    @Override public void setDisplayName(String displayName) {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public String getName() {
        return counter.name();
    }

    /** {@inheritDoc} */
    @Override public String getDisplayName() {
        return getName();
    }

    /** {@inheritDoc} */
    @Override public long getValue() {
        return counter.value();
    }

    /** {@inheritDoc} */
    @Override public void setValue(long value) {
        counter.value(value);
    }

    /** {@inheritDoc} */
    @Override public void increment(long incr) {
        counter.increment(incr);
    }

    /** {@inheritDoc} */
    @Override public void write(DataOutput out) throws IOException {
        throw new UnsupportedOperationException("not implemented");
    }

    /** {@inheritDoc} */
    @Override public void readFields(DataInput in) throws IOException {
        throw new UnsupportedOperationException("not implemented");
    }

    /** {@inheritDoc} */
    @Override public String makeEscapedCompactString() {
        return toEscapedCompactString(new GridHadoopV2Counter(counter));
    }

    /** {@inheritDoc} */
    @Override public boolean contentEquals(Counters.Counter counter) {
        return getUnderlyingCounter().equals(counter.getUnderlyingCounter());
    }

    /** {@inheritDoc} */
    @Override public long getCounter() {
        return counter.value();
    }

    /** {@inheritDoc} */
    @Override public Counter getUnderlyingCounter() {
        return this;
    }
}
