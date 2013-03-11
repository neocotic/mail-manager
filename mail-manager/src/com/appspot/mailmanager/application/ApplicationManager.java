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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import com.appspot.mailmanager.send.SendServlet;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;

/**
 * The class responsible for managing {@code Application Applications}.
 * 
 * @author Alasdair Mercer <mercer.alasdair@gmail.com>
 */
public class ApplicationManager {

    private static final String CLASS_NAME = SendServlet.class.getName();
    private static final Logger log = Logger.getLogger(CLASS_NAME);

    private static ApplicationManager instance;

    /**
     * Retrieves the singleton instance of {@link ApplicationManager}.
     * 
     * @return The singleton instance.
     */
    public static ApplicationManager getInstance() {
        if (instance == null) {
            instance = new ApplicationManager();
        }
        return instance;
    }

    /**
     * Creates a new instance of {@link ApplicationManager}.
     */
    private ApplicationManager() {
    }

    /**
     * Creates and persists a new application with the specified {@code name}.
     * <p>
     * A unique API key is automatically generated and assigned to the new application.
     * 
     * @param name
     *            the name for the new application
     * @return The newly persisted {@link Application}.
     * @throws IllegalArgumentException
     *             If {@code name} is {@code null} or empty.
     */
    public Application add(String name) {
        log.entering(CLASS_NAME, "add", name);

        Application application = new Application(generateApiKey(), name);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(application.toEntity());

        log.exiting(CLASS_NAME, "add", application);
        return application;
    }

    /**
     * Indicates whether the specified {@code apiKey} is valid.
     * 
     * @param apiKey
     *            the API key to be checked
     * @return {@code true} if the API key exists; otherwise {@code false}.
     * @throws IllegalArgumentException
     *             If {@code apiKey} is {@code null} or empty.
     */
    public boolean existsWithApiKey(String apiKey) {
        return getByApiKey(apiKey) != null;
    }

    /**
     * Indicates whether the specified {@code name} is valid.
     * 
     * @param name
     *            the name to be checked
     * @return {@code true} if the name exists; otherwise {@code false}.
     * @throws IllegalArgumentException
     *             If {@code name} is {@code null} or empty.
     */
    public boolean existsWithName(String name) {
        return getByName(name) != null;
    }

    /**
     * Generates a unique API key to be allocated to an individual application prior to being persisted.
     * 
     * @return The unique API key.
     * @throws RuntimeException
     *             If a {@code NoSuchAlgorithmException} is thrown.
     */
    private String generateApiKey() {
        log.entering(CLASS_NAME, "generateApiKey");

        String apiKey = UUID.randomUUID().toString();

        log.exiting(CLASS_NAME, "generateApiKey", apiKey);
        return apiKey;
    }

    /**
     * Returns the {@link Application} associated with the specified {@code apiKey}.
     * 
     * @param apiKey
     *            the API key of the {@link Application} to be retrieved
     * @return The {@link Application} with the {@code apiKey} provided or {@code null} if none could be found.
     * @throws IllegalArgumentException
     *             If {@code apiKey} is {@code null} or empty.
     */
    public Application getByApiKey(String apiKey) {
        log.entering(CLASS_NAME, "getByApiKey", apiKey);

        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("Invalid application API key: " + apiKey);
        }

        Application application = getWithProperty("apiKey", apiKey);

        log.exiting(CLASS_NAME, "getByApiKey", application);
        return application;
    }

    /**
     * Returns the {@link Application} associated with the specified {@code name}.
     * 
     * @param name
     *            the name of the {@link Application} to be retrieved
     * @return The {@link Application} with the {@code name} provided or {@code null} if none could be found.
     * @throws IllegalArgumentException
     *             If {@code name} is {@code null} or empty.
     */
    public Application getByName(String name) {
        log.entering(CLASS_NAME, "getByName", name);

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid application name: " + name);
        }

        Application application = getWithProperty("name", name);

        log.exiting(CLASS_NAME, "getByName", application);
        return application;
    }

    /**
     * Returns the {@link Application} whose {@code property} has the specified {@code value}.
     * 
     * @param property
     *            the property to be queried
     * @param value
     *            the value to match {@code property}
     * @return The {@link Application} with the matching property or {@code null} if none could be found.
     * @throws IllegalArgumentException
     *             If {@code property} is {@code null} or empty.
     */
    private Application getWithProperty(String property, Object value) {
        log.entering(CLASS_NAME, "getWithProperty", new Object[] { property, value });

        if (property == null || property.isEmpty()) {
            throw new IllegalArgumentException("Invalid property: " + property);
        }

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(Application.KIND).setFilter(new Query.FilterPredicate(property, Query.FilterOperator.EQUAL, value));
        Entity entity = datastore.prepare(query).asSingleEntity();

        Application application = entity == null ? null : Application.fromEntity(entity);

        log.exiting(CLASS_NAME, "getWithProperty", application);
        return application;
    }

    /**
     * Removes the persisted {@code application} provided.
     * 
     * @param application
     *            the {@link Application} to be deleted
     * @throws NullPointerException
     *             If {@code application} is {@code null}.
     */
    public void remove(Application application) {
        removeByApiKey(application.getApiKey());
    }

    /**
     * Removes the persisted {@link Application} with the specified {@code apiKey}.
     * 
     * @param apiKey
     *            the API key of the {@link Application} to be deleted
     * @throws IllegalArgumentException
     *             If {@code apiKey} is {@code null} or empty.
     */
    public void removeByApiKey(String apiKey) {
        log.entering(CLASS_NAME, "removeByApiKey", apiKey);

        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("Invalid application API key: " + apiKey);
        }

        removeWithProperty("apiKey", apiKey);

        log.exiting(CLASS_NAME, "removeByApiKey");
    }

    /**
     * Removes the persisted {@link Application} with the specified {@code name}.
     * 
     * @param name
     *            the name of the {@link Application} to be deleted
     * @throws IllegalArgumentException
     *             If {@code name} is {@code null} or empty.
     */
    public void removeByName(String name) {
        log.entering(CLASS_NAME, "removeByName", name);

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid application name: " + name);
        }

        removeWithProperty("name", name);

        log.exiting(CLASS_NAME, "removeByName");
    }

    /**
     * Removes the persisted {@link Application} with a {@code property} matching the specified {@code value}.
     * 
     * @param property
     *            the property to be queried
     * @param value
     *            the value to match {@code property}
     * @throws IllegalArgumentException
     *             If {@code property} is {@code null} or empty.
     */
    private void removeWithProperty(String property, Object value) {
        log.entering(CLASS_NAME, "removeWithProperty", new Object[] { property, value });

        if (property == null || property.isEmpty()) {
            throw new IllegalArgumentException("Invalid property: " + property);
        }

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(Application.KIND).setFilter(new Query.FilterPredicate(property, Query.FilterOperator.EQUAL, value)).setKeysOnly();

        Set<Key> keys = new HashSet<>();
        for (Entity entity : datastore.prepare(query).asIterable()) {
            keys.add(entity.getKey());
        }
        datastore.delete(keys);

        log.exiting(CLASS_NAME, "removeWithProperty");
    }
}