/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.aries.jax.rs.rest.management.internal;

import static org.apache.aries.jax.rs.rest.management.RestManagementConstants.APPLICATION_BUNDLE_JSON;
import static org.apache.aries.jax.rs.rest.management.RestManagementConstants.APPLICATION_BUNDLE_XML;

import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.aries.jax.rs.rest.management.schema.BundleSchema;
import org.osgi.framework.BundleContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public class FrameworkResource extends BaseResource {

    public FrameworkResource(BundleContext bundleContext) {
        super(bundleContext);
    }

    @GET
    @Path("framework{ext: (\\.json|\\.xml)*}")
    @Produces({APPLICATION_BUNDLE_JSON, APPLICATION_BUNDLE_XML})
    @Operation(
        summary = "Get the framework bundle by extension type",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "The framework bundle",
                content = @Content(schema = @Schema(implementation = BundleSchema.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Framework not found"
            ),
            @ApiResponse(
                responseCode = "406",
                description = "The REST management service does not support any of the requested representations"
            )
        }
    )
    public Response framework(
        @Parameter(allowEmptyValue = true, schema = @Schema(allowableValues = {".json", ".xml"}))
        @PathParam("ext") String ext) {
        ResponseBuilder builder = Response.status(
            Response.Status.OK
        ).entity(
            bundleSchema(framework)
        );

        return Optional.ofNullable(
            ext
        ).map(
            String::trim
        ).map(
            t -> ".json".equals(t) ? APPLICATION_BUNDLE_JSON : APPLICATION_BUNDLE_XML
        ).map(t -> builder.type(t)).orElse(
            builder
        ).build();
    }

}
