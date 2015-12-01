package com.renren.mobile.chat.ui;

import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.common.binder.LocalBinderPool;
import com.common.utils.Config;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.base.util.SystemUtil;

public class ILoveSixinActivity extends BaseActivity {
	private Button loginBtn;
	private EditText urlEditText, hostEditText, socketEditText, httpEditText;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("测试");
		setContentView(R.layout.ilovesixin);
		loginBtn = (Button) findViewById(R.id.loginButton);
		urlEditText = (EditText) findViewById(R.id.urlEditText);
		if (Config.CURRENT_SERVER_URI != null) {
			urlEditText.setText(Config.CURRENT_SERVER_URI);
		}
		hostEditText = (EditText) findViewById(R.id.hostEditText);
		if (Config.HOST_NAME != null) {
			hostEditText.setText(Config.HOST_NAME);
		}
		socketEditText = (EditText) findViewById(R.id.socketEditText);
		String a = String.valueOf(Config.SOCKET_DEFAULT_PORT);
		if (a != null) {
			socketEditText.setText(a);
		}
		httpEditText = (EditText) findViewById(R.id.httpEditText);
		String b = String.valueOf(Config.HTTP_DEFAULT_PORT);
		if (b != null) {
			httpEditText.setText(b);
		}
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = urlEditText.getText().toString();
				String host = hostEditText.getText().toString();
				String http = httpEditText.getText().toString();
				int httpPort = Integer.parseInt(http);
				String socket = socketEditText.getText().toString();
				int socketPort = Integer.parseInt(socket);
				Config.CURRENT_SERVER_URI = url;
				Config.HOST_NAME = host;
				Config.HTTP_DEFAULT_PORT = httpPort;
				Config.SOCKET_DEFAULT_PORT = socketPort;

				try {
					LocalBinderPool.getInstance().obtainBinder()
					.changeURL(url, host, httpPort, socketPort);
				} catch (RemoteException e) {}
				ILoveSixinActivity.this.finish();
			}
		});

	}
}
