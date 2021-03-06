/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.aerogear.todo.data;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.aerogear.todo.data.Task;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gson.Gson;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class SerializationTest {
	Gson gson = new Gson();

	List<Task> items = new ArrayList<Task>();

	@Test
	public void testRoundTripSerialization() throws Exception {
		items = new ArrayList<Task>();
		items.add(new Task("Wash the car"));
		items.add(new Task("Walk the dog"));
		items.add(new Task("Finish the Android example app"));

		String jsonString = gson.toJson(items);

		StringReader reader = new StringReader(jsonString);
		final Task[] results = gson.fromJson(reader, Task[].class);

		Assert.assertEquals(3, results.length);
		Task item1 = results[0];
		Assert.assertEquals(Task.class.getName(), item1.getClass().getName());
		Assert.assertEquals("Wash the car",item1.getTitle());
	}

	@Test
	public void testRobolectricHTTP() throws Exception {
		String TEST = "adf goi g ";
		Robolectric.addPendingHttpResponse(200, TEST);
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet("url");
		final HttpResponse response = client.execute(get);
		final InputStream inputStream = response.getEntity().getContent();
		final String next = new Scanner(inputStream).useDelimiter("\\A").next();
		Assert.assertEquals(TEST,next);
	}

}
