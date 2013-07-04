/**
 * Copyright 2013 DuraSpace, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fcrepo.exceptionhandlers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.fcrepo.exception.TransactionMissingException;

/**
 * If a transaction is requested that has been closed (or never existed),
 * just return an HTTP 410 Gone.
 */
@Provider
public class TransactionMissingExceptionMapper implements
        ExceptionMapper<TransactionMissingException> {

    @Override
    public Response toResponse(TransactionMissingException exception) {
        return Response.status(Response.Status.GONE).build();
    }
}
