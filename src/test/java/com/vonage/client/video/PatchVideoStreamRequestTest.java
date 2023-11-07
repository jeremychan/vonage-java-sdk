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
package com.vonage.client.video;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.UUID;

public class PatchVideoStreamRequestTest {
	private final String streamId = UUID.randomUUID().toString();

	@Test
	public void testSerializeAddStreamAllParams() {
		PatchComposedStreamsRequest request = new PatchComposedStreamsRequest(streamId, false, true);
		String expectedJson = "{\"addStream\":\""+streamId+"\",\"hasAudio\":false,\"hasVideo\":true}";
		assertEquals(expectedJson, request.toJson());
	}

	@Test
	public void testSerializeAddStreamRequiredParams() {
		PatchComposedStreamsRequest request = new PatchComposedStreamsRequest(streamId, null, null);
		String expectedJson = "{\"addStream\":\""+streamId+"\"}";
		assertEquals(expectedJson, request.toJson());
	}

	@Test
	public void testSerializeRemoveStream() {
		PatchComposedStreamsRequest request = new PatchComposedStreamsRequest(streamId);
		String expectedJson = "{\"removeStream\":\""+streamId+"\"}";
		assertEquals(expectedJson, request.toJson());
	}
}