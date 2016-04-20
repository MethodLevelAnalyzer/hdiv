/**
 * Copyright 2005-2015 hdiv.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hdiv.urlProcessor;

import org.hdiv.AbstractHDIVTestCase;
import org.hdiv.util.HDIVUtil;
import org.hdiv.util.Method;

public class UriTemplateTest extends AbstractHDIVTestCase {

	private LinkUrlProcessor linkUrlProcessor;

	private String urlWithoutUriTemplate;

	private String uriTemplate;

	private String url;

	@Override
	protected void onSetUp() throws Exception {
		linkUrlProcessor = getApplicationContext().getBean(LinkUrlProcessor.class);
		urlWithoutUriTemplate = "/testAction.do";
		uriTemplate = "{?filter, projection, search}";
		url = urlWithoutUriTemplate + uriTemplate;
	}

	private UrlData create(final String url) {
		return linkUrlProcessor.createUrlData(url, Method.GET, HDIVUtil.getHDIVParameter(getMockRequest()),
				getMockRequest());
	}

	public void testCreateUrlDataSimple() {
		UrlData urlData = create(urlWithoutUriTemplate);
		assertEquals(false, urlData.hasUriTemplate());
		assertEquals(urlWithoutUriTemplate, urlData.getUrlWithOutUriTemplate());
		assertEquals("", urlData.getUriTemplate());
	}

	public void testCreateUrlDataWithUriTemplate() {
		UrlData urlData = create(url);
		assertEquals(true, urlData.hasUriTemplate());
		assertEquals(urlWithoutUriTemplate, urlData.getUrlWithOutUriTemplate());
		assertEquals(uriTemplate, urlData.getUriTemplate());
	}

	public void testGetProcessedUrlWithHdivState() {
		UrlData urlData = create(url);
		String stateParam = "1-12-123123123123";
		String urlProcessed = linkUrlProcessor
				.getProcessedUrlWithHdivState(HDIVUtil.getHDIVParameter(getMockRequest()), urlData, stateParam);
		assertEquals(urlWithoutUriTemplate + "?_HDIV_STATE_=" + stateParam + uriTemplate.replace("?", "&"),
				urlProcessed);
	}

	public void testJSURLData() {
		UrlData data = new UrlData("javascript:myMethod()", Method.GET);
		assertEquals(true, data.isJS());
		data = new UrlData("hhhhhhhhhh:myMethod()", Method.GET);
		assertEquals(false, data.isJS());
	}
}
