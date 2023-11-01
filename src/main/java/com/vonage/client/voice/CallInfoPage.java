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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.vonage.client.Jsonable;
import java.util.Iterator;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CallInfoPage implements Iterable<CallInfo>, Jsonable {
    private int count, pageSize, recordIndex;
    private PageLinks links;
    private EmbeddedCalls embedded;

    @JsonProperty("count")
    public int getCount() {
        return count;
    }

    @JsonProperty("page_size")
    public int getPageSize() {
        return pageSize;
    }

    @JsonProperty("record_index")
    public int getRecordIndex() {
        return recordIndex;
    }

    @JsonProperty("_links")
    public PageLinks getLinks() {
        return links;
    }

    @JsonProperty("_embedded")
    public EmbeddedCalls getEmbedded() {
        return embedded;
    }
    
    @Override
    public Iterator<CallInfo> iterator() {
        return new ArrayIterator<>(embedded.getCallInfos());
    }

    /**
     * Creates an instance of this class from a JSON payload.
     *
     * @param json The JSON string to parse.
     *
     * @return An instance of this class with the fields populated, if present.
     */
    public static CallInfoPage fromJson(String json) {
        return Jsonable.fromJson(json);
    }
}