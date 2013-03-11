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

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * TODO: JavaDoc
 * 
 * @author Alasdair Mercer <mercer.alasdair@gmail.com>
 */
public class Application {

    /** The kind name used to represent this object in the datastore. */
    public static final String KIND = Application.class.getSimpleName();

    /**
     * Creates a new instance of {@link Application} based on the values derived from the specified {@code entity}.
     * 
     * @param entity
     *            the {@code Entity} from which the details are to be derived
     * @return The {@link Application} derived from {@code entity}.
     * @throws NullPointerException
     *             If {@code entity} is {@code null}.
     */
    public static Application fromEntity(Entity entity) {
        return new Application((String) entity.getProperty("apiKey"), (String) entity.getProperty("name"));
    }

    /**
     * Creates a new instance of {@link Application} based on the values derived from the specified {@code json}.
     * 
     * @param json
     *            the {@code JSONObject} from which the details are to be derived
     * @return The {@link Application} derived from {@code json}.
     * @throws IllegalArgumentException
     *             If any of the required derived values are invalid.
     * @throws JSONException
     *             If {@code json} is malformed.
     * @throws NullPointerException
     *             If {@code json} is {@code null}.
     */
    public static Application fromJSON(JSONObject json) throws JSONException {
        return new Application(json.getString("apiKey"), json.getString("name"));
    }

    private String apiKey;
    private String name;

    /**
     * Creates a new instance of {@link Application} with the {@code apiKey} and {@code name} provided.
     * 
     * @param apiKey
     *            the API key to be used
     * @param name
     *            the name to be used
     * @throws IllegalArgumentException
     *             If either {@code apiKey} or {@code name} are {@code null} or empty.
     */
    public Application(String apiKey, String name) {
        setApiKey(apiKey);
        setName(name);
    }

    /**
     * Returns the API key for this {@link Application}.
     * 
     * @return The API key.
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Sets the API key for this {@link Application} to {@code apiKey}.
     * 
     * @param apiKey
     *            the API key to be set
     * @throws IllegalArgumentException
     *             If {@code apiKey} is {@code null} or empty.
     */
    public void setApiKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("Invalid application API key: " + apiKey);
        }
        this.apiKey = apiKey;
    }

    /**
     * Returns the name of this {@link Application}.
     * 
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this {@link Application} to {@code name}.
     * 
     * @param name
     *            the name to be set
     * @throws IllegalArgumentException
     *             If {@code name} is {@code null} or empty.
     */
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid application name: " + name);
        }
        this.name = name;
    }

    /**
     * Creates an {@code Entity} based on this {@link Application}.
     * 
     * @return The derived {@code Entity}.
     */
    public Entity toEntity() {
        Entity entity = new Entity(KIND);
        entity.setProperty("apiKey", apiKey);
        entity.setProperty("name", name);

        return entity;
    }

    /**
     * Creates a {@code JSONObject} based on this {@link Application}.
     * 
     * @return The derived {@code JSONObject}.
     * @throws JSONException
     *             If this {@link Application} is malformed.
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("apiKey", apiKey);
        json.put("name", name);

        return json;
    }

    /*
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((apiKey == null) ? 0 : apiKey.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /*
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Application other = (Application) obj;
        if (apiKey == null) {
            if (other.apiKey != null)
                return false;
        } else if (!apiKey.equals(other.apiKey))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}