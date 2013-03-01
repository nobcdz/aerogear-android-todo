package org.jboss.aerogear.todo.activities;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.http.HeaderAndBody;
import org.jboss.aerogear.todo.R;
import org.jboss.aerogear.todo.ToDoApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	protected static final String TAG = LoginActivity.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.menu_register) {
			startActivity(new Intent(getApplicationContext(),
					RegisterActivity.class));
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	public void login(View loginButtonButton) {
		String username = text(R.id.username_field);
		String password = text(R.id.password_field);
		ToDoApplication app = (ToDoApplication) getApplication();
		app.login(this, username, password, new Callback<HeaderAndBody>() {

			@Override
			public void onSuccess(HeaderAndBody data) {
				startActivity(new Intent(getApplicationContext(),
						TodoActivity.class));
			}

			@Override
			public void onFailure(Exception e) {
				Log.e(TAG, e.getMessage(), e);
				Toast.makeText(LoginActivity.this, "Login failed",
						Toast.LENGTH_LONG).show();
			}
		});
	}

	private String text(int field_id) {
		EditText field = (EditText) findViewById(field_id);
		return field.getText().toString();
	}

}
