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

package org.gridgain.grid.kernal.processors.portable.os;

import org.gridgain.client.marshaller.*;
import org.gridgain.grid.kernal.*;
import org.gridgain.grid.kernal.processors.*;
import org.gridgain.grid.kernal.processors.portable.*;
import org.gridgain.portable.*;
import org.jetbrains.annotations.*;

/**
 * No-op implementation of {@link GridPortableProcessor}.
 */
public class GridOsPortableProcessor extends GridProcessorAdapter implements GridPortableProcessor {
    /**
     * @param ctx Kernal context.
     */
    public GridOsPortableProcessor(GridKernalContext ctx) {
        super(ctx);
    }

    /** {@inheritDoc} */
    @Override public int typeId(String typeName) {
        return 0;
    }

    /** {@inheritDoc} */
    @Nullable @Override public Object marshalToPortable(@Nullable Object obj) throws GridPortableException {
        return obj;
    }

    /** {@inheritDoc} */
    @Override public Object detachPortable(@Nullable Object obj) {
        return obj;
    }

    /** {@inheritDoc} */
    @Nullable @Override public GridClientMarshaller portableMarshaller() {
        return null;
    }

    /** {@inheritDoc} */
    @Override public boolean isPortable(GridClientMarshaller marsh) {
        return false;
    }
}
