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
package com.appspot.mailmanager.send;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.mailmanager.Contact;
import com.appspot.mailmanager.MailException;
import com.appspot.mailmanager.application.ApplicationManager;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * The servlet responsible for sending emails on behalf of registered {@link Application Applications}. The responses are very simple JSON strings.
 * <p>
 * Only requests containing a valid API key will be completed.
 * 
 * @author Alasdair Mercer <mercer.alasdair@gmail.com>
 */
@SuppressWarnings("serial")
public class SendServlet extends HttpServlet {

    private static final String CLASS_NAME = SendServlet.class.getName();
    private static final Logger log = Logger.getLogger(CLASS_NAME);

    /**
     * Adds the plain text or HTML contents to the {@code message} accordingly.
     * <p>
     * Either plain text or HTML contents may be added to the {@code message}, but never both.
     * 
     * @param message
     *            the {@code Message} to which the contents are to be added
     * @param request
     *            the {@link SendRequest} containing the contents
     * @throws MessagingException
     *             If an error occurs while adding the contents.
     */
    private void addContent(Message message, SendRequest request) throws MessagingException {
        log.entering(CLASS_NAME, "addContent", new Object[] { message, request });

        if (request.getHtml() != null) {
            Multipart content = new MimeMultipart();

            MimeBodyPart html = new MimeBodyPart();
            html.setContent(request.getHtml(), "text/html");
            content.addBodyPart(html);

            message.setContent(content);
        } else if (request.getText() != null) {
            message.setText(request.getText());
        }

        log.exiting(CLASS_NAME, "addContent");
    }

    /**
     * 
     * Builds a {@link SendRequest} based on JSON data contained within the body of {@code req}.
     * 
     * @param req
     *            the {@code HttpServletRequest} to be read
     * @return The {@link SendRequest} derived from the body of {@code req}.
     * @throws MailException
     *             If an {@code IOException} occurs when reading the body of {@code req} or a {@code JSONException} occurs while parsing the body into JSON.
     */
    private SendRequest deriveSendRequest(HttpServletRequest req) throws MailException {
        log.entering(CLASS_NAME, "deriveSendRequest", req);

        StringBuffer buff = new StringBuffer();
        String line = null;
        BufferedReader reader = null;

        try {
            reader = req.getReader();

            while ((line = reader.readLine()) != null) {
                buff.append(line);
            }
        } catch (IOException e) {
            throw new MailException("Bad request", e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                log.log(Level.FINER, "Swallowing exception", e);
            }
        }

        SendRequest request = null;

        try {
            request = SendRequest.fromJSON(new JSONObject(buff.toString()));
        } catch (IllegalArgumentException | JSONException e) {
            throw new MailException("Invalid data", e);
        }

        log.exiting(CLASS_NAME, "deriveSendRequest", request);
        return request;
    }

    /*
     * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.entering(CLASS_NAME, "doGet", new Object[] { req, resp });

        SendRequest request = null;
        SendResponse response = null;

        try {
            request = deriveSendRequest(req);

            if (!ApplicationManager.getInstance().existsWithApiKey(request.getApiKey())) {
                throw new MailException("Unrecognized API key");
            } else if (request.getHtml() == null && request.getText() == null) {
                throw new MailException("Missing content");
            }

            sendMail(request);

            response = new SendResponse(200);
        } catch (MailException e) {
            response = new SendResponse(500, e.getMessage());
        }

        try {
            resp.setStatus(response.getStatus());
            resp.setContentType("application/json");
            resp.getWriter().println(response.toJSON().toString());
        } catch (JSONException e) {
            throw new IOException("Failed to write response", e);
        }

        log.exiting(CLASS_NAME, "doGet");
    }

    /**
     * Sends an email based on the information contained within the specified {@code request}.
     * 
     * @param request
     *            the {@link SendRequest} to be used
     * @throws MailException
     *             If an error occurs while constructing or sending the email.
     */
    private void sendMail(SendRequest request) throws MailException {
        log.entering(CLASS_NAME, "sendMail", request);

        try {
            Message message = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
            message.setFrom(request.getSender().toInternetAddress());
            for (Contact recipient : request.getRecipients()) {
                message.addRecipient(Message.RecipientType.TO, recipient.toInternetAddress());
            }
            message.setSubject(request.getSubject());

            addContent(message, request);

            Transport.send(message);
        } catch (AddressException | UnsupportedEncodingException e) {
            throw new MailException("Invalid address", e);
        } catch (MessagingException e) {
            throw new MailException("Message could not be sent", e);
        }

        log.exiting(CLASS_NAME, "sendEmail");
    }
}