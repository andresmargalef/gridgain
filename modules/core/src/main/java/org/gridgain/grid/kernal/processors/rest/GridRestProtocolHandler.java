/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.rest;

import org.gridgain.grid.*;
import org.gridgain.grid.kernal.processors.rest.request.*;

/**
 * Command protocol handler.
 */
public interface GridRestProtocolHandler {
    /**
     * @param req Request.
     * @return Response.
     * @throws GridException In case of error.
     */
    public GridRestResponse handle(GridRestRequest req) throws GridException;

    /**
     * @param req Request.
     * @return Future.
     */
    public GridFuture<GridRestResponse> handleAsync(GridRestRequest req);
}