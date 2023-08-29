/*
 *   Copyright 2023 Vonage
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.vonage.client.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import java.util.Objects;

/**
 * Represents the main attributes of a User.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseUser implements Jsonable {
    String id, name;

    BaseUser() {
    }

    void setId(String id) {
        this.id = id;
    }

    /**
     * Unique user ID.
     *
     * @return The user ID as a string, or {@code null} if unknown.
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Unique name of the user.
     *
     * @return The user's name.
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseUser baseUser = (BaseUser) o;
        return Objects.equals(id, baseUser.id) && Objects.equals(name, baseUser.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}