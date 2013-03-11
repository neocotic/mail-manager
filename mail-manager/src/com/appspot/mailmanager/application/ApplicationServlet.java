/* Copyright (C) 2013 Alasdair Mercer, http://neocotic.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.appspot.mailmanager.application;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.mailmanager.send.SendServlet;
import com.google.appengine.labs.repackaged.org.json.JSONException;

/**
 * The servlet responsible for managing registered {@link Application Applications}. The responses are very simple JSON strings or errors.
 * <p>
 * Possible management functions include adding, retrieving, and removing {@link Application Applications}. Only administrators should be able to access this servlet.
 * 
 * @author Alasdair Mercer <mercer.alasdair@gmail.com>
 */
@SuppressWarnings("serial")
public class ApplicationServlet extends HttpServlet {

    private static final String CLASS_NAME = SendServlet.class.getName();
    private static final Logger log = Logger.getLogger(CLASS_NAME);

    /*
     * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.entering(CLASS_NAME, "doDelete", new Object[] { req, resp });

        String apiKey = getInput("apiKey", req);
        String name = getInput("name", req);

        if (apiKey != null && !apiKey.isEmpty()) {
            ApplicationManager.getInstance().removeByApiKey(apiKey);
        } else if (name != null && !name.isEmpty()) {
            ApplicationManager.getInstance().removeByName(name);
        } else {
            resp.sendError(500);
        }

        log.exiting(CLASS_NAME, "doDelete");
    }

    /*
     * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.entering(CLASS_NAME, "doGet", new Object[] { req, resp });

        String apiKey = getInput("apiKey", req);
        String name = getInput("name", req);

        Application application = null;

        if (apiKey != null && !apiKey.isEmpty()) {
            application = ApplicationManager.getInstance().getByApiKey(apiKey);
        } else if (name != null && !name.isEmpty()) {
            application = ApplicationManager.getInstance().getByName(name);
        } else {
            resp.sendError(500);
        }

        if (application == null) {
            resp.sendError(404);
        } else {
            try {
                resp.setContentType("application/json");
                resp.getWriter().println(application.toJSON().toString());
            } catch (JSONException e) {
                throw new IOException("Failed to write response", e);
            }
        }

        log.exiting(CLASS_NAME, "doGet");
    }

    /*
     * @see HttpServlet#doPut(HttpServletRequest, http.HttpServletResponse)
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.entering(CLASS_NAME, "doPut", new Object[] { req, resp });

        String name = getInput("name", req);

        if (name == null || name.isEmpty()) {
            resp.sendError(500);
        }

        Application application = ApplicationManager.getInstance().add(name);

        if (application == null) {
            resp.sendError(500);
        } else {
            try {
                resp.setContentType("application/json");
                resp.getWriter().println(application.toJSON().toString());
            } catch (JSONException e) {
                throw new IOException("Failed to write response", e);
            }
        }

        log.exiting(CLASS_NAME, "doPut");
    }

    /**
     * Attempts to extract the named input value from the specified {@code req}.
     * <p>
     * The request is checked with the following precedence;
     * <ol>
     * <li>Parameter</li>
     * <li>Attribute</li>
     * </ol>
     * 
     * @param name
     *            the name of the input whose value is to be retrieved
     * @param req
     *            the {@code HttpServletRequest} being processed
     * @return The named input value or {@code null} if none could be extracted.
     */
    private String getInput(String name, HttpServletRequest req) {
        String input = req.getParameter(name);
        if (input == null) {
            input = (String) req.getAttribute(name);
        }
        return input;
    }
}