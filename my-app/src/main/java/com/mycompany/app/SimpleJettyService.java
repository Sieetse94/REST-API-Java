package com.mycompany.app;

import javax.servlet.*;
import org.eclipse.jetty.servlet.*;
import org.eclipse.jetty.server.*;

public class SimpleJettyService {
    
    public static void run(final Class<? extends Servlet> servlet) {
        final Server server = new Server(8000);
        final ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(servlet, "/*");
        server.setHandler(servletHandler);
        try {
            server.dumpStdErr();
            server.start();
            server.join();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
