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
package com.vonage.client.proactiveconnect;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.common.HalLinks;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Map;

public class ListListsEndpointTest {
	ListListsEndpoint endpoint;
	
	@Before
	public void setUp() {
		endpoint = new ListListsEndpoint(new HttpWrapper());
	}
	
	@Test
	public void testAuthMethod() {
		Class<?>[] authMethods = endpoint.getAcceptableAuthMethods();
		assertEquals(1, authMethods.length);
		assertEquals(JWTAuthMethod.class, authMethods[0]);
	}
	
	@Test
	public void testMakeRequestAllParams() throws Exception {
		HalRequestWrapper request = new HalRequestWrapper(3, 12, SortOrder.DESC, null);
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("GET", builder.getMethod());
		String expectedUri = "https://api-eu.vonage.com/v0.1/bulk/lists" +
				"?page=" + request.page + "&page_size=" + request.pageSize + "&order=" + request.order;
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
		assertEquals(3, params.size());
	}

	@Test
	public void testDefaultUri() throws Exception {
		HalRequestWrapper request = new HalRequestWrapper(null, null, null, null);
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("GET", builder.getMethod());
		String expectedUri = "https://api-eu.vonage.com/v0.1/bulk/lists";
		assertEquals(expectedUri, builder.build().getURI().toString());
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build());
		endpoint = new ListListsEndpoint(wrapper);
		HalRequestWrapper request = new HalRequestWrapper(5, null, null, null);
		String expectedUri = baseUri + "/v0.1/bulk/lists?page=" + request.page;
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("GET", builder.getMethod());
	}

	@Test
	public void testEmptyResponse() throws Exception {
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, "{}");
		ListListsResponse parsed = endpoint.parseResponse(mockResponse);
		assertNotNull(parsed);
		assertNull(parsed.getLists());
		assertNull(parsed.getLinks());
		assertNull(parsed.getPage());
		assertNull(parsed.getPageSize());
		assertNull(parsed.getTotalItems());
		assertNull(parsed.getTotalPages());
	}

	@Test
	public void testFullResponse() throws Exception {
		String expectedResponse = "{\n" +
				"   \"page_size\": 50,\n" +
				"   \"page\": 7,\n" +
				"   \"total_pages\": 9,\n" +
				"   \"total_items\": 42,\n" +
				"   \"_links\": {\n" +
				"      \"first\": {\n" +
				"         \"href\": \"https://api-eu.vonage.com/v0.1/bulk/lists?page=5&page_size=10\"\n" +
				"      },\n" +
				"      \"self\": {\n" +
				"         \"href\": \"https://api-eu.vonage.com/v0.1/bulk/lists?page=5&page_size=10\"\n" +
				"      },\n" +
				"      \"prev\": {\n" +
				"         \"href\": \"https://api-eu.vonage.com/v0.1/bulk/lists?page=5&page_size=10\"\n" +
				"      },\n" +
				"      \"next\": {\n" +
				"         \"href\": \"https://api-eu.vonage.com/v0.1/bulk/lists?page=5&page_size=10\"\n" +
				"      }\n" +
				"   },\n" +
				"   \"_embedded\": {\n" +
				"      \"lists\": [\n" +
				"         {},{},{}\n" +
				"      ]\n" +
				"   }\n" +
				"}";
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, expectedResponse);
		ListListsResponse parsed = endpoint.parseResponse(mockResponse);
		assertNotNull(parsed);
		HalLinks links = parsed.getLinks();
		assertNotNull(links);
		assertNotNull(links.getSelfUrl());
		assertNotNull(links.getFirstUrl());
		assertNotNull(links.getNextUrl());
		assertNotNull(links.getPrevUrl());
		assertEquals(7, parsed.getPage().intValue());
		assertEquals(50, parsed.getPageSize().intValue());
		assertEquals(9, parsed.getTotalPages().intValue());
		assertEquals(42, parsed.getTotalItems().intValue());
		List<ContactsList> lists = parsed.getLists();
		assertNotNull(lists);
		assertEquals(3, lists.size());
		lists.forEach(Assert::assertNotNull);
	}

	@Test(expected = VonageResponseParseException.class)
	public void testInvalidResponse() {
		ListListsResponse.fromJson("{malformed]");
	}

	@Test(expected = ProactiveConnectResponseException.class)
	public void test400Response() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(400, "{}"));
	}
	
	@Test(expected = ProactiveConnectResponseException.class)
	public void test500Response() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(500, "{}"));
	}
}