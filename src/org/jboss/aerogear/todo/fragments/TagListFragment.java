/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.aerogear.todo.fragments;

import java.util.ArrayList;
import java.util.List;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.pipeline.Pipe;
import org.jboss.aerogear.todo.R;
import org.jboss.aerogear.todo.ToDoApplication;
import org.jboss.aerogear.todo.activities.TodoActivity;
import org.jboss.aerogear.todo.data.Tag;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class TagListFragment extends SherlockFragment {
	private ArrayAdapter<Tag> adapter;
	private List<Tag> tags = new ArrayList<Tag>();
	private Pipe<Tag> pipe;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.list, null);

		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(getResources().getString(R.string.tags));

		ImageView add = (ImageView) view.findViewById(R.id.add);
		add.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				((TodoActivity) getActivity()).showTagForm();
			}
		});

		adapter = new ArrayAdapter<Tag>(getActivity(),
				android.R.layout.simple_list_item_1, tags);
		ListView tagListView = (ListView) view.findViewById(R.id.list);
		tagListView.setAdapter(adapter);

		tagListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> adapterView,
							View view, int position, long id) {
						final Tag tag = tags.get(position);
						((TodoActivity) getActivity()).showTagForm(tag);
					}
				});

		tagListView
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> adapterView,
							View view, int position, long id) {
						final Tag tag = tags.get(position);
						new AlertDialog.Builder(getActivity())
								.setMessage("Delete '" + tag.getTitle() + "'?")
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialogInterface,
													int i) {
												startDelete(tag);
											}
										}).setNegativeButton("Cancel", null)
								.show();
						return true;
					}
				});

		return view;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pipe = ((ToDoApplication) getActivity().getApplication()).getPipeline()
				.get("tags", this, getActivity().getApplicationContext());
	}

	@Override
	public void onStart() {
		super.onStart();
		startRefresh();
	}

	private void startRefresh() {
		pipe.read(new Callback<List<Tag>>() {
			@Override
			public void onSuccess(List<Tag> data) {
				tags.clear();
				tags.addAll(data);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(Exception e) {
				Toast.makeText(getActivity(),
						"Error refreshing tags: " + e.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		});
	}

	private void startDelete(Tag tag) {
		pipe.remove(tag.getId(), new Callback<Void>() {
			@Override
			public void onSuccess(Void data) {
				startRefresh();
			}

			@Override
			public void onFailure(Exception e) {
				Toast.makeText(getActivity(),
						"Error removing tag: " + e.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		});
	}

}
