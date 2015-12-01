package com.renren.mobile.chat.ui.contact.feed;

import android.content.Context;
import android.text.Spanned;

import com.common.emotion.emotion.EmotionString;
import com.renren.mobile.chat.webview.RenRenWebView;

public class EmotionStringOnClick extends EmotionString {
	Context mContext;

	public void runBrowser(String url) {
		RenRenWebView.show(mContext, "", "", url);
		// Uri uri = Uri.parse(url);
		// Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// AbstractRenrenApplication.getAppContext().startActivity(intent);
	}

	public EmotionStringOnClick() {
		super();
	}

	public EmotionStringOnClick(Spanned spanned) {
		super(spanned);
	}

	public EmotionStringOnClick(Spanned spanned, Context context) {
		super(spanned);
		this.mContext = context;
	}

	public EmotionStringOnClick(String text) {
		super(text);
	}

}
