/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package ee.jakarta.tck.concurrent.api.ContextService;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import ee.jakarta.tck.concurrent.common.fixed.counter.CounterRunnableTask;
import ee.jakarta.tck.concurrent.common.fixed.counter.WorkInterface;
import ee.jakarta.tck.concurrent.framework.TestConstants;
import ee.jakarta.tck.concurrent.framework.junit.anno.Assertion;
import ee.jakarta.tck.concurrent.framework.junit.anno.Common;
import ee.jakarta.tck.concurrent.framework.junit.anno.Common.PACKAGE;
import ee.jakarta.tck.concurrent.framework.junit.anno.Web;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ContextService;
import jakarta.enterprise.concurrent.ManagedTaskListener;

@Web
@Common({ PACKAGE.FIXED_COUNTER })
public class ContextServiceTests {

    // TODO deploy as EJB and JSP artifacts
    @Deployment(name = "ContextServiceTests")
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class);
    }

    @Resource(lookup = TestConstants.defaultContextService)
    private ContextService context;

    @Assertion(id = "JAVADOC:5", strategy = "Lookup default ContextService object and create proxy object using instance and interface.")
    public void contextServiceWithIntf() {
        assertAll(() -> {
            Runnable proxy = (Runnable) context.createContextualProxy(new CounterRunnableTask(), Runnable.class);
            assertNotNull(proxy);
        });
    }

    @Assertion(id = "JAVADOC:6", strategy = "Lookup default ContextService object and create proxy object using instance and interface."
            + " If the instance does not implement the specified interface, IllegalArgumentException will be thrown")
    public void contextServiceWithIntfAndIntfNoImplemented() {
        assertThrows(IllegalArgumentException.class, () -> {
            context.createContextualProxy(new Object(), Runnable.class);
        });
    }

    @Assertion(id = "JAVADOC:6", strategy = "Lookup default ContextService object and create proxy object using instance and interface."
            + " If the instance is null, IllegalArgumentException will be thrown")
    public void contextServiceWithIntfAndInstanceIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            context.createContextualProxy(null, Runnable.class);
        });
    }

    @Assertion(id = "JAVADOC:7", strategy = "Lookup default ContextService object and create proxy object using instance and multiple interfaces.")
    public void contextServiceWithMultiIntfs() {
        assertAll(() -> {
            Object proxy = context.createContextualProxy(new CounterRunnableTask(), Runnable.class,
                    WorkInterface.class);
            assertNotNull(proxy);
            assertTrue(proxy instanceof Runnable);
            assertTrue(proxy instanceof WorkInterface);
        });
    }

    @Assertion(id = "JAVADOC:8", strategy = "Lookup default ContextService object and create proxy object using instance and multi interfaces."
            + "If the instance does not implement the specified interface, IllegalArgumentException will be thrown")
    public void contextServiceWithMultiIntfsAndIntfNoImplemented() {
        assertThrows(IllegalArgumentException.class, () -> {
            context.createContextualProxy(new CounterRunnableTask(), Runnable.class, WorkInterface.class,
                    ManagedTaskListener.class);
        });
    }

    @Assertion(id = "JAVADOC:8", strategy = "Lookup default ContextService object and create proxy object using object and multi interfaces."
            + " If the instance is null, IllegalArgumentException will be thrown")
    public void contextServiceWithMultiIntfsAndInstanceIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            context.createContextualProxy(null, Runnable.class, WorkInterface.class);
        });
    }

    @Assertion(id = "JAVADOC:9", strategy = "Lookup default ContextService object and create proxy object using ExecutionProperties and interface.")
    public void contextServiceWithIntfAndProperties() {
        assertAll(() -> {
            Map<String, String> execProps = new HashMap<String, String>();
            execProps.put("vendor_a.security.tokenexpiration", "15000");
            execProps.put("USE_PARENT_TRANSACTION", "true");

            Runnable proxy = (Runnable) context.createContextualProxy(new CounterRunnableTask(), execProps,
                    Runnable.class);
            assertNotNull(proxy);
        });
    }

    @Assertion(id = "JAVADOC:11",
            strategy = "Lookup default ContextService object and create proxy object using ExecutionProperties and multiple interfaces.")
    public void contextServiceWithMultiIntfsAndProperties() {
        assertAll(() -> {
            Map<String, String> execProps = new HashMap<String, String>();
            execProps.put("vendor_a.security.tokenexpiration", "15000");
            execProps.put("USE_PARENT_TRANSACTION", "true");

            Object proxy = context.createContextualProxy(new CounterRunnableTask(), execProps, Runnable.class,
                    WorkInterface.class);
            assertNotNull(proxy);
            assertTrue(proxy instanceof Runnable);
            assertTrue(proxy instanceof WorkInterface);
        });
    }

    @Assertion(id = "JAVADOC:10",
            strategy = "Lookup default ContextService object and create proxy object using ExecutionProperties and interface."
                    + " If the instance does not implement the specified interface, IllegalArgumentException will be thrown")
    public void contextServiceWithIntfAndPropertiesAndIntfNoImplemented() {
        assertThrows(IllegalArgumentException.class, () -> {
            Map<String, String> execProps = new HashMap<String, String>();
            execProps.put("vendor_a.security.tokenexpiration", "15000");
            execProps.put("USE_PARENT_TRANSACTION", "true");

            context.createContextualProxy(new CounterRunnableTask(), execProps, Runnable.class,
                    ManagedTaskListener.class);
        });
    }

    @Assertion(id = "JAVADOC:10",
            strategy = "Lookup default ContextService object and create proxy object using ExecutionProperties and interfaces."
            + "If the instance is null, IllegalArgumentException will be thrown")
    public void contextServiceWithIntfsAndPropertiesAndInstanceIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            Map<String, String> execProps = new HashMap<String, String>();
            execProps.put("vendor_a.security.tokenexpiration", "15000");
            execProps.put("USE_PARENT_TRANSACTION", "true");

            context.createContextualProxy(null, execProps, Runnable.class);
        });
    }

    @Assertion(id = "JAVADOC:12",
            strategy = "Lookup default ContextService object and create proxy object using ExecutionProperties and multiple interfaces."
            + "If the instance does not implement the specified interface, IllegalArgumentException will be thrown")
    public void contextServiceWithMultiIntfsAndPropertiesAndIntfNoImplemented() {
        assertThrows(IllegalArgumentException.class, () -> {
            Map<String, String> execProps = new HashMap<String, String>();
            execProps.put("vendor_a.security.tokenexpiration", "15000");
            execProps.put("USE_PARENT_TRANSACTION", "true");

            context.createContextualProxy(new CounterRunnableTask(), execProps, Runnable.class, WorkInterface.class,
                    ManagedTaskListener.class);
        });
    }

    @Assertion(id = "JAVADOC:12",
            strategy = "Lookup default ContextService object and create proxy object using ExecutionProperties and multiple interfaces."
            + " If the instance is null, IllegalArgumentException will be thrown")
    public void contextServiceWithMultiIntfsAndPropertiesAndInstanceIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            Map<String, String> execProps = new HashMap<String, String>();
            execProps.put("vendor_a.security.tokenexpiration", "15000");
            execProps.put("USE_PARENT_TRANSACTION", "true");

            context.createContextualProxy(null, execProps, Runnable.class, CounterRunnableTask.class);
        });
    }

    @Assertion(id = "JAVADOC:13",
            strategy = "Lookup default ContextService object and create proxy object using ExecutionProperties and multiple interfaces."
            + " Retrieve ExecutionProperties from proxy object and verify property value.")
    public void getExecutionProperties() {
        assertAll(() -> {
            Map<String, String> execProps = new HashMap<String, String>();
            execProps.put("USE_PARENT_TRANSACTION", "true");

            Object proxy = context.createContextualProxy(new CounterRunnableTask(), execProps, Runnable.class,
                    WorkInterface.class);
            assertNotNull(proxy);

            Map<String, String> returnedExecProps = context.getExecutionProperties(proxy);
            assertEquals("true", returnedExecProps.get("USE_PARENT_TRANSACTION"));
        });
    }

    @Assertion(id = "JAVADOC:14",
            strategy = "Lookup default ContextService object. Retrieve ExecutionProperties from plain object.")
    public void getExecutionPropertiesNoProxy() {
        assertAll(() -> {
            try {
                context.getExecutionProperties(new Object());
            } catch (IllegalArgumentException ie) {
                // Pass if IAE is thrown, but fail otherwise.
            }
        });
    }
}
