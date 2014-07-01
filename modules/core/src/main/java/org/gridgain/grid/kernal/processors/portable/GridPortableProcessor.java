/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.portable;

import org.gridgain.grid.kernal.processors.*;
import org.gridgain.grid.util.portable.*;
import org.gridgain.portable.*;
import org.jetbrains.annotations.*;

/**
 * Portable processor.
 */
public interface GridPortableProcessor extends GridProcessor {
    /**
     * @return Portable context.
     */
    public GridPortableContext portableContext();

    /**
     * @param obj Object.
     * @return Whether object is portable.
     */
    public boolean isPortable(Object obj) throws GridPortableException;

    /**
     * @param obj Object to marshal.
     * @return Portable object.
     * @throws GridPortableException In case of error.
     */
    public <T> GridPortableObject<T> marshal(@Nullable T obj) throws GridPortableException;

    /**
     * @param arr Byte array.
     * @return Portable object.
     * @throws GridPortableException
     */
    @Nullable public <T> T unmarshal(byte[] arr) throws GridPortableException;
}