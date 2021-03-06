/**
 * Copyright 2015 DuraSpace, Inc.
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
package org.fcrepo.integration;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.ResultSet;
import org.fcrepo.kernel.models.Container;
import org.fcrepo.kernel.impl.rdf.impl.DefaultIdentifierTranslator;
import org.fcrepo.kernel.impl.rdf.impl.PropertiesRdfContext;
import org.fcrepo.kernel.services.ContainerService;
import org.fcrepo.kernel.utils.iterators.RdfStream;
import org.fcrepo.transform.transformations.SparqlQueryTransform;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.fcrepo.kernel.RdfLexicon.REPOSITORY_NAMESPACE;
import static org.junit.Assert.assertTrue;


/**
 * <p>SparqlQueryTransformIT class.</p>
 *
 * @author cbeer
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring-test/master.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SparqlQueryTransformIT extends AbstractResourceIT {

    @Inject
    ContainerService containerService;
    private SparqlQueryTransform testObj;

    @Test
    public void shouldDoStuff() throws RepositoryException {
        final Session session = repo.login();

        final Container object = containerService.findOrCreate(session, "/testObject");

        final String s = "SELECT ?x ?uuid\n" +
                "WHERE { ?x  <" + REPOSITORY_NAMESPACE + "uuid> ?uuid }";
        final InputStream stringReader = new ByteArrayInputStream(s.getBytes());

        testObj = new SparqlQueryTransform(stringReader);

        final RdfStream stream = object.getTriples(new DefaultIdentifierTranslator(session),
                PropertiesRdfContext.class);
        try (final QueryExecution qexec = testObj.apply(stream)) {
            assert (qexec != null);
            final ResultSet results = qexec.execSelect();
            assert (results != null);
            assertTrue(results.hasNext());
        }
    }
}
