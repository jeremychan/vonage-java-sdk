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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;


public class TalkResponseTest {
    private TalkResponse response;

    @BeforeEach
    public void setUp() {
        response = TalkResponse.fromJson("{\n" +
                "  \"message\": \"Talk stopped\",\n" +
                "  \"uuid\": \"ssf61863-4a51-ef6b-11e1-w6edebcf93bb\"\n" +
                "}");
    }

    @Test
    public void testBasics() {
        assertEquals("Talk stopped", response.getMessage());
        assertEquals("ssf61863-4a51-ef6b-11e1-w6edebcf93bb", response.getUuid());
    }

    @Test
    public void testUnknownJson() throws IOException {
        String json = "{\n" +
                "    \"unknownProperty\": \"unknown\"\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        TalkResponse tr = mapper.readValue(json, TalkResponse.class);
        assertNull(tr.getUuid());
    }
}