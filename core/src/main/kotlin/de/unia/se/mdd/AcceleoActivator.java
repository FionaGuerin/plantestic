/*******************************************************************************
 * Copyright (c) 2008, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package de.unia.se.mdd;

        import org.eclipse.core.runtime.Plugin;
        import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
        public class AcceleoActivator extends Plugin {

        /**
         * The plug-in ID.
         */
        public static final String PLUGIN_ID = "de.unia.se.mdd";

        /**
         * The shared instance.
         */
        private static AcceleoActivator plugin;

        public AcceleoActivator() {
        }

        public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        }

        public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
        }

        /**
         * Returns the shared instance.
         *
         * @return the shared instance
         */
        public static AcceleoActivator getDefault() {
        return plugin;
        }

        }
