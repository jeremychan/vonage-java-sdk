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
package com.vonage.client.messages.sms;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SmsTextRequestTest {

	@Test
	public void testSerializeValid() {
		String from = "447900000001", to = "317900000002", msg = "Hello, World!";
		SmsTextRequest sms = SmsTextRequest.builder().from(from).to(to).text(msg).build();
		String json = sms.toJson();
		assertTrue(json.contains("\"text\":\""+msg+"\""));
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"text\""));
		assertTrue(json.contains("\"channel\":\"sms\""));

		assertEquals("SmsTextRequest "+json, sms.toString());
	}

	@Test
	public void testNullText() {
		assertThrows(NullPointerException.class, () -> SmsTextRequest.builder()
				.from("447900000001").to("317900000002").build()
		);
	}

	@Test
	public void testEmptyText() {
		assertThrows(IllegalArgumentException.class, () -> SmsTextRequest.builder()
				.from("447900000001").to("317900000002").text("").build()
		);
	}

	@Test
	public void testLongText() {
		StringBuilder text = new StringBuilder(1002);
		for (int i = 0; i < 999; i++) {
			text.append('*');
		}
		assertEquals(999, text.length());

		SmsTextRequest sms = SmsTextRequest.builder()
				.text(text.toString())
				.from("447900000001")
				.to("317900000002")
				.build();

		assertEquals(text.toString(), sms.getText());
		text.append("xy");
		assertEquals(1001, text.length());

		assertThrows(IllegalArgumentException.class, () -> SmsTextRequest.builder()
				.from(sms.getFrom()).text(text.toString()).to(sms.getTo()).build()
		);
	}
}
