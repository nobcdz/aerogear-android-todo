package org.jboss.aerogear.todo.activities;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.http.HeaderAndBody;
import org.jboss.aerogear.todo.R;
import org.jboss.aerogear.todo.ToDoApplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class RegisterActivity extends SherlockActivity {

	protected static final String TAG = "RegisterActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home :
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void submit(View submitButton) {
		if (valid()) {
			String firstName = text(R.id.first_name_field);
			String lastName = text(R.id.last_name_field);
			String emailAddress = text(R.id.email_field);
			String username = text(R.id.username_field);
			String password = text(R.id.password_field);
			String role = ((Spinner) findViewById(R.id.role_spinner))
					.getSelectedItem().toString();

			((ToDoApplication) getApplication()).enroll(this, firstName, lastName,
					emailAddress, username, password, role,
					new Callback<HeaderAndBody>() {

						@Override
						public void onSuccess(HeaderAndBody data) {
							startActivity(new Intent(getApplicationContext(),
									TodoActivity.class));
							finish();
						}

						@Override
						public void onFailure(Exception e) {
							Log.e(TAG, "There was an error enrolling", e);
							Toast.makeText(getApplicationContext(),
									"There was an error enrolling",
									Toast.LENGTH_LONG).show();
						}
					});

		}
	}

	private String text(int fieldId) {
		return ((EditText) findViewById(fieldId)).getText().toString();
	}

	private boolean valid() {
		String firstName = text(R.id.first_name_field);
		if (firstName.equals("")) {
			setError(R.id.first_name_field);
			return false;
		}

		String lastName = text(R.id.last_name_field);
		if (lastName.equals("")) {
			setError(R.id.last_name_field);
			return false;
		}

		String emailAddress = text(R.id.email_field);
		if (emailAddress.equals("")) {
			setError(R.id.email_field);
			return false;
		}

		String username = text(R.id.username_field);
		if (username.equals("")) {
			setError(R.id.username_field);
			return false;
		}

		String password = text(R.id.password_field);
		if (password.equals("")) {
			setError(R.id.password_field);
			return false;
		}

		return true;
	}

	private void setError(int fieldId) {
		((EditText) findViewById(fieldId)).setError("This is required");
	}

	public void cancel(View cancelButton) {
		finish();
	}

}
