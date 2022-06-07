/*
 *   Copyright 2022 Vonage
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
package com.vonage.client.messages;

import com.vonage.client.VonageUnexpectedException;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class MessageResponseTest {

	@Test
	public void testConstructFromValidJson() {
		String uuid = UUID.randomUUID().toString();
		MessageResponse response = MessageResponse.fromJson("{\"message_uuid\":\""+uuid+"\"}");
		assertEquals(uuid, response.getMessageUuid());
	}

	@Test(expected = VonageUnexpectedException.class)
	public void testConstructFromInvalidJson() {
		MessageResponse.fromJson("{_malformed_}");
	}
}
