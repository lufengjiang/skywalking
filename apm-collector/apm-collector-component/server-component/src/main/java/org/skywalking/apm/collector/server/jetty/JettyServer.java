/*
 * Copyright 2017, OpenSkywalking Organization All rights reserved.
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
 *
 * Project repository: https://github.com/OpenSkywalking/skywalking
 */

package org.skywalking.apm.collector.server.jetty;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.ServletMapping;
import org.skywalking.apm.collector.server.Server;
import org.skywalking.apm.collector.server.ServerException;
import org.skywalking.apm.collector.server.ServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import java.net.InetSocketAddress;

/**
 * @author peng-yongsheng
 */
public class JettyServer implements Server {

    private final Logger logger = LoggerFactory.getLogger(JettyServer.class);

    private final String host;
    private final int port;
    private final String contextPath;
    private org.eclipse.jetty.server.Server server;
    private ServletContextHandler servletContextHandler;

    public JettyServer(String host, int port, String contextPath) {
        this.host = host;
        this.port = port;
        this.contextPath = contextPath;
    }

    @Override public String hostPort() {
        return host + ":" + port;
    }

    @Override public String serverClassify() {
        return "Jetty";
    }

    @Override public void initialize() throws ServerException {
        // 创建 Jetty Server
        server = new org.eclipse.jetty.server.Server(new InetSocketAddress(host, port));

        // 创建 Jetty ServletContextHandler
        servletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        servletContextHandler.setContextPath(contextPath);
        logger.info("http server root context path: {}", contextPath);

        server.setHandler(servletContextHandler);
    }

    @Override public void addHandler(ServerHandler handler) {
        // 创建 ServletHolder
        ServletHolder servletHolder = new ServletHolder();
        servletHolder.setServlet((HttpServlet)handler);
        // 添加到 Jetty ServletContextHandler
        servletContextHandler.addServlet(servletHolder, ((JettyHandler)handler).pathSpec());
    }

    @Override public void start() throws ServerException {
        logger.info("start server, host: {}, port: {}", host, port);
        try {
            for (ServletMapping servletMapping : servletContextHandler.getServletHandler().getServletMappings()) {
                logger.info("jetty servlet mappings: {} register by {}", servletMapping.getPathSpecs(), servletMapping.getServletName());
            }
            server.start();
        } catch (Exception e) {
            throw new JettyServerException(e.getMessage(), e);
        }
    }
}
