package com.renren.mobile.chat.ui.contact.feed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.common.network.DomainUrl;
import com.core.json.JsonArray;
import com.core.json.JsonObject;
import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.base.inter.NEWSFEED_TYPE;
import com.renren.mobile.chat.base.views.NewImageView;
import com.renren.mobile.chat.base.views.NotSynImageView;
import com.renren.mobile.chat.base.views.NotSynImageView.DownloadImageAbleListener;
import com.renren.mobile.chat.common.DateFormat;
import com.renren.mobile.chat.newsfeed.NewsFeedWarpper;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.ContactBaseModel;
import com.renren.mobile.chat.ui.contact.IDownloadContactListener;
import com.renren.mobile.chat.ui.contact.UserInfoActivity;
import com.renren.mobile.chat.ui.groupchat.D_SelectGroupChatContactActivity;
import com.renren.mobile.chat.ui.groupchat.D_SelectGroupChatContactActivity.COMEFROM;
import com.renren.mobile.chat.ui.groupchat.D_SelectGroupChatContactActivity.PARAMS;
import com.renren.mobile.chat.ui.photo.PhotoNew;
import com.renren.mobile.chat.webview.RenRenWebView;

/**
 * @author liuchao
 */

public class ChatFeedAdapter extends BaseAdapter implements OnScrollListener,
		DownloadImageAbleListener, ChatFeedPicRecycleObserverable {

	public static final String IMAGE_BROWSER_URLS = "urls";

	public static final String IMAGE_BROWSER_NUM = "num";

	// //用于记录上一次可见的item序号,图片回收用~
	// public static int LAST_FIRST_VISIBLE_ITEM = -1;
	//
	// //用于记录列表显示时第一个展示的位置,当滑动停止时回收图片用~
	// public static int FIRST_VISIABLE_ITEM = -1;
	//
	// public static int VISIBLE_ITEM_COUNT = 0;

	private ChatFeedScreen mScreen;

	// private Animation mFeedPhotoLoadingBiggerAnimation,
	// mFeedPhotoLoadingSmallerAnimation;

	/**
	 * 用于图片回收~单张图片比较大,一个boolean表示这单张图片是否展示(内存中有). 被观察者~
	 */
	// private ArrayList<ChatFeedPicRecycleObserver> mImageList = new
	// ArrayList<ChatFeedPicRecycleObserver>();

	public static List<ChatFeedModel> mDataList = new ArrayList<ChatFeedModel>();
	private boolean mIsScrolling = false;

	static class ViewHolder {
		@ViewMapping(ID = R.id.lc_feed_head)
		public NotSynImageView feed_head;
		@ViewMapping(ID = R.id.lc_feed_name)
		public TextView feed_name;
		@ViewMapping(ID = R.id.lc_feed_time)
		public TextView feed_time;
		@ViewMapping(ID = R.id.lc_feed_content)
		public TextView feed_content;
		@ViewMapping(ID = R.id.lc_feed_location_layout)
		public LinearLayout feed_location_layout;
		@ViewMapping(ID = R.id.lc_feed_location_name)
		public TextView feed_location_name;
		@ViewMapping(ID = R.id.lc_feed_location_double_layout)
		public LinearLayout feed_location_double_layout;
		@ViewMapping(ID = R.id.lc_feed_location_double_name)
		public TextView feed_location_double_name;
		@ViewMapping(ID = R.id.lc_feed_location_single_layout)
		public LinearLayout feed_location_single_layout;
		@ViewMapping(ID = R.id.lc_feed_location_single_name)
		public TextView feed_location_single_name;
		@ViewMapping(ID = R.id.lc_feed_bottom_photo_single_layout)
		public FrameLayout feed_photo_single_layout;
		@ViewMapping(ID = R.id.lc_feed_photo_single)
		public NewImageView feed_photo_single;
		@ViewMapping(ID = R.id.lc_feed_single_img_loading)
		public ImageView feed_photo_single_loading_animation;
		@ViewMapping(ID = R.id.lc_feed_bottom_double_photo_layout)
		public LinearLayout feed_photo_double_layout;
		@ViewMapping(ID = R.id.lc_feed_photo1)
		public NewImageView feed_photo1;
		@ViewMapping(ID = R.id.lc_feed_photo1_layout)
		public FrameLayout feed_photo1_layout;
		@ViewMapping(ID = R.id.lc_feed_photo2)
		public NewImageView feed_photo2;
		@ViewMapping(ID = R.id.lc_feed_bottom_reblog_layout)
		public LinearLayout feed_reblog_layout;
		@ViewMapping(ID = R.id.lc_feed_reblog_name)
		public TextView feed_reblog_name;
		@ViewMapping(ID = R.id.lc_feed_reblog_content)
		public TextView feed_reblog_content;
		@ViewMapping(ID = R.id.lc_feed_item_bottom_reblog_btn)
		public LinearLayout feed_reblog_btn;
		@ViewMapping(ID = R.id.lc_feed_item_bottom_reply_btn)
		public LinearLayout feed_reply_btn;
	}

	public ChatFeedAdapter(ArrayList<ChatFeedModel> data, View header,
			View footer, ListView listView, ChatFeedScreen screen) {
		this.mScreen = screen;
		if (null != data) {
			mDataList.addAll(data);
		}
		if (header != null) {
			listView.addHeaderView(header);
			listView.setHeaderDividersEnabled(false);
		}
		if (footer != null) {
			listView.addFooterView(footer);
			listView.setFooterDividersEnabled(false);
		}
		// mFeedPhotoLoadingBiggerAnimation =
		// AnimationUtils.loadAnimation(mScreen.context,
		// R.anim.lc_feed_loading_bigger);
		// mFeedPhotoLoadingSmallerAnimation =
		// AnimationUtils.loadAnimation(mScreen.context,
		// R.anim.lc_feed_loading_smaller);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = SystemService.sInflaterManager.inflate(
					R.layout.lc_feed_item, null);
			convertView.setBackgroundResource(R.drawable.lc_feed_item_bg_color);
			viewHolder = new ViewHolder();
			ViewMapUtil.getUtil().viewMapping(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		setChatFeedConvertView(viewHolder, position);
		return convertView;
	}

	private void setChatFeedConvertView(ViewHolder viewHolder,
			final int position) {
		final ChatFeedModel chatFeedModel = mDataList.get(position);
		// 名字
		viewHolder.feed_name.setText(chatFeedModel.getUserName());
		// 文字内容
		if (!TextUtils.isEmpty(chatFeedModel.getTitle())) {
			viewHolder.feed_content.setVisibility(View.VISIBLE);
			viewHolder.feed_content.setMovementMethod(LinkMovementMethod
					.getInstance());
			viewHolder.feed_content.setText(Html.fromHtml(chatFeedModel
					.getTitle()));
			Spanned spanned = setUrl(viewHolder.feed_content);
			viewHolder.feed_content.setText(new EmotionStringOnClick(spanned,
					mScreen.context));
		} else {
			viewHolder.feed_content.setVisibility(View.GONE);
		}
		// 头像
		this.setHeadImage(viewHolder, chatFeedModel);
		// 时间
		String date = DateFormat.getDateByChatSession(chatFeedModel.getTime());
		if (!date.equals("") && !date.equals("昨天") && !date.equals("前天")) {
			viewHolder.feed_time.setText(date);
		} else {
			viewHolder.feed_time.setText(date + " "
					+ DateFormat.getNowStrByChat(chatFeedModel.getTime()));
		}
		// 清空图片
		viewHolder.feed_photo_single.setImageDrawable(null);
		viewHolder.feed_photo1.setImageDrawable(null);
		viewHolder.feed_photo2.setImageDrawable(null);
		// 清空位置信息
		viewHolder.feed_location_layout.setVisibility(View.GONE);
		viewHolder.feed_location_single_layout.setVisibility(View.GONE);
		viewHolder.feed_location_double_layout.setVisibility(View.GONE);
		// 主要内容区域(转发、图片)
		switch (chatFeedModel.getType()) {
		// 转发
		case NEWSFEED_TYPE.FEED_STATUS_UPDATE:
			viewHolder.feed_photo_single_layout.setVisibility(View.GONE);
			viewHolder.feed_photo_double_layout.setVisibility(View.GONE);
			viewHolder.feed_reblog_layout.setVisibility(View.VISIBLE);
			if (chatFeedModel.mIsReblog) {
				ChatFeedReblogContent chatFeedReblogContent = (ChatFeedReblogContent) chatFeedModel
						.getChatFeedAttachmentContent().get(0);
				viewHolder.feed_reblog_name.setText(chatFeedReblogContent
						.getOwnerName());
				viewHolder.feed_reblog_content
						.setMovementMethod(LinkMovementMethod.getInstance());
				viewHolder.feed_reblog_content.setText(Html
						.fromHtml(chatFeedReblogContent.getStatus()));
				Spanned spanned = setUrl(viewHolder.feed_reblog_content);
				viewHolder.feed_reblog_content
						.setText(new EmotionStringOnClick(spanned,
								mScreen.context));// ,imgGetter,null)));
			} else {
				viewHolder.feed_reblog_layout.setVisibility(View.GONE);
			}
			break;
		// 单图
		case NEWSFEED_TYPE.FEED_PHOTO_PUBLISH_ONE:
			viewHolder.feed_reblog_layout.setVisibility(View.GONE);
			viewHolder.feed_photo_double_layout.setVisibility(View.GONE);
			viewHolder.feed_photo_single_layout.setVisibility(View.VISIBLE);
			viewHolder.feed_photo_single.setDownloadAbleListener(this);
			if (chatFeedModel.getChatFeedAttachmentContent().size() > 0) {

				ChatFeedPhotoContent chatFeedPhotoContent = (ChatFeedPhotoContent) chatFeedModel
						.getChatFeedAttachmentContent().get(0);
				if (!TextUtils.isEmpty(chatFeedPhotoContent.getDigest())) {
					viewHolder.feed_content
							.setMovementMethod(LinkMovementMethod.getInstance());
					viewHolder.feed_content.setText(Html
							.fromHtml(chatFeedPhotoContent.getDigest()));
					Spanned spanned = setUrl(viewHolder.feed_content);
					viewHolder.feed_content.setText(new EmotionStringOnClick(
							spanned, mScreen.context));// ,imgGetter,null)));
				} else {
					viewHolder.feed_content.setVisibility(View.GONE);
				}
				final String[] temp = new String[] { chatFeedPhotoContent
						.getLargeUrl() };
				viewHolder.feed_photo_single
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								PhotoNew.show(mScreen.context, temp);
							}
						});
				this.warpImage(viewHolder.feed_photo_single,
						viewHolder.feed_photo_single_loading_animation,
						chatFeedPhotoContent);
				// 注册图片，用于回收~
				// mImageList.add(new
				// ChatFeedPicRecycleObserver(viewHolder.feed_photo_single,
				// position));
			} else {
				viewHolder.feed_photo_single_layout.setVisibility(View.GONE);
			}
			break;
		// 多图
		case NEWSFEED_TYPE.FEED_PHOTO_PUBLISH_MORE:
			viewHolder.feed_reblog_layout.setVisibility(View.GONE);
			viewHolder.feed_photo_single_layout.setVisibility(View.GONE);
			viewHolder.feed_photo_double_layout.setVisibility(View.VISIBLE);
			viewHolder.feed_photo1.setDownloadAbleListener(this);
			viewHolder.feed_photo2.setDownloadAbleListener(this);
			if (chatFeedModel.getChatFeedAttachmentContent().size() > 0) {
				final ChatFeedPhotoContent chatFeedPhotoContent1 = (ChatFeedPhotoContent) chatFeedModel
						.getChatFeedAttachmentContent().get(0);
				final ChatFeedPhotoContent chatFeedPhotoContent2 = (ChatFeedPhotoContent) chatFeedModel
						.getChatFeedAttachmentContent().get(1);
				this.warpImageNotLarge(viewHolder.feed_photo1,
						chatFeedPhotoContent1);
				this.warpImageNotLarge(viewHolder.feed_photo2,
						chatFeedPhotoContent2);
				int count = chatFeedModel.getChatFeedAttachmentContent().size();
				final String[] temp = new String[count];
				for (int num = 0; num < count; num++) {
					temp[num] = ((ChatFeedPhotoContent) chatFeedModel
							.getChatFeedAttachmentContent().get(num))
							.getLargeUrl();
				}
				viewHolder.feed_photo1
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								PhotoNew.show(mScreen.context, temp, 1);
							}
						});
				viewHolder.feed_photo2
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								PhotoNew.show(mScreen.context, temp, 2);
							}
						});
				// mImageList.add(new
				// ChatFeedPicRecycleObserver(viewHolder.feed_photo1,
				// position));
				// mImageList.add(new
				// ChatFeedPicRecycleObserver(viewHolder.feed_photo2,
				// position));
			} else {
				viewHolder.feed_photo_double_layout.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
		// 位置展示
		if (chatFeedModel.isHasLocation()) {
			String location = chatFeedModel.getChatFeedLocation()
					.getPlaceName();
			switch (chatFeedModel.getType()) {
			case NEWSFEED_TYPE.FEED_STATUS_UPDATE:
				viewHolder.feed_location_layout.setVisibility(View.VISIBLE);
				viewHolder.feed_location_name.setText(location);
				break;
			case NEWSFEED_TYPE.FEED_PHOTO_PUBLISH_ONE:
				viewHolder.feed_location_single_layout
						.setVisibility(View.VISIBLE);
				viewHolder.feed_location_single_name.setText(location);
				break;
			case NEWSFEED_TYPE.FEED_PHOTO_PUBLISH_MORE:
				viewHolder.feed_location_double_layout
						.setVisibility(View.VISIBLE);
				viewHolder.feed_location_double_name.setText(location);
				break;
			default:
				break;
			}
		}
		// 转发按钮
		viewHolder.feed_reblog_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mScreen.context,
						D_SelectGroupChatContactActivity.class);
				ChatFeedModel model = ChatFeedAdapter.mDataList.get(position);
				NewsFeedWarpper newsFeed = new NewsFeedWarpper();
				newsFeed.adapter(model);
				i.putExtra(PARAMS.COMEFROM, COMEFROM.CHAT_FEED_REBOLG);
				i.putExtra(RenRenChatActivity.PARAM_NEED.CONTEXT_FROM,
						mScreen.context.getClass().getSimpleName());
				i.putExtra(RenRenChatActivity.PARAM_NEED.TO_CHAT_FEED_MODEL,
						newsFeed);
				mScreen.context.startActivity(i);
			}
		});
		// 回复按钮
		viewHolder.feed_reply_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final ChatFeedModel chatFeedModel = ChatFeedAdapter.mDataList
						.get(position);
				final NewsFeedWarpper m = new NewsFeedWarpper();
				m.adapter(chatFeedModel);
				ContactBaseModel contactmodel = C_ContactsData.getInstance()
						.getRenRenContactBySiXinID(chatFeedModel.getUserId(),
								new IDownloadContactListener() {
									@Override
									public void onSussess(ContactBaseModel model) {
										ContactBaseModel contactmodel = C_ContactsData
												.getInstance()
												.getRenRenContactByRenRenID(
														chatFeedModel
																.getUserId(),
														null);
										if (contactmodel == null) {
											return;
										}
										RenRenChatActivity.show(
												mScreen.context, contactmodel,
												m);
									}

									@Override
									public void onError() {
									}

									@Override
									public void onDowloadOver() {
									}

								});
				if (contactmodel != null) {
					RenRenChatActivity.show(mScreen.context, contactmodel, m);
				} else {
				}
			}
		});

		/**
		 * 自动刷新~
		 */
		if (position == getCount() - 1 && getCount() != 0) {
			if (getCount() < ChatFeedDataManager.CHAT_FEED_MAX_NUM
					&& ChatFeedDataManager.hasMoreData) {
				mScreen.mChatFeedHolder.mBaseFooterLayout
						.setFeedFooterLoading(true);
				mScreen.mChatFeedDataManager.getFeedList(false);
			}
		}
	}

	private void warpImage(final NewImageView newImageView,
			final ImageView imageview, ChatFeedPhotoContent content) {
		// 如果没有下载,渐变动画
		// if(!newImageView.onShow){
		// imageview.startAnimation(mFeedPhotoLoadingSmallerAnimation);
		// RenrenChatApplication.sHandler.postDelayed((new Runnable() {
		// @Override
		// public void run() {
		// imageview.startAnimation(mFeedPhotoLoadingBiggerAnimation);
		// RenrenChatApplication.sHandler.postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// imageview.startAnimation(mFeedPhotoLoadingSmallerAnimation);
		// }
		// }, 500);
		// }
		// }),500);
		// }
		if (content.getLargeUrl() != null) {
			setImageImg(newImageView, content.getLargeUrl());
		} else if (content.getMainUrl() != null) {
			setImageImg(newImageView, content.getMainUrl());
		} else {
			setImageImg(newImageView, content.getUrl());
		}
	}

	private void warpImageNotLarge(NewImageView imageview,
			ChatFeedPhotoContent content) {
		if (content.getMainUrl() != null) {
			setImageImg(imageview, content.getMainUrl());
		} else {
			setImageImg(imageview, content.getUrl());
		}
	}

	/**
	 * 设置item的头像
	 * */
	private void setHeadImage(ViewHolder holder, final ChatFeedModel feedModel) {
		holder.feed_head.clear();
		holder.feed_head.setDownloadAbleListener(this);
		holder.feed_head.addUrl(feedModel.getHeadUrl());
		holder.feed_head.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserInfoActivity.show(mScreen.context, feedModel.getUserId(),
						DomainUrl.RENREN_SIXIN_DOMAIN);
			}
		});
	}

	private void setImageImg(final NewImageView imageView, final String url) {
		// if (!mIsScrolling) {
		imageView.setURL(url, null);
		// }
	}

	public boolean isContainItem(ChatFeedModel chatFeedModel) {
		boolean contain = false;
		for (ChatFeedModel chatFeedModelItem : mDataList) {
			if (chatFeedModel.getId() == chatFeedModelItem.getId()) {
				return true;
			}
		}
		return contain;
	}

	public void updateList() {
		notifyDataSetChanged();
	}

	/**
	 * 假如有数据到来，更新显示
	 * 
	 * @param array
	 * @param isRefresh
	 * @throws IOException
	 */
	public void reset(final JsonArray array, final boolean isRefresh)
			throws IOException {
		RenrenChatApplication.mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (isRefresh) {
					clear();
				}
				JsonObject[] objs = new JsonObject[array.size()];
				array.copyInto(objs);
				for (JsonObject o : objs) {
					ChatFeedModel c = ChatFeedModelFactory.createFeedModel(o);
					if (c != null) {
						if (isRefresh) {
							addItem(c);
						} else {
							if (getCount() < ChatFeedDataManager.CHAT_FEED_MAX_NUM - 1) {
								if (!isContainItem(c)) {
									addItem(c);
								}
							} else if (getCount() == ChatFeedDataManager.CHAT_FEED_MAX_NUM - 1) {
								addItem(c);
							}
						}
					}
				}
				updateList();
			}
		});
	}

	public void addItem(ChatFeedModel data) {
		mDataList.add(data);
	}

	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	public void clear() {
		mDataList.clear();
	}

	public List<ChatFeedModel> getData() {
		return mDataList;
	}

	public void addList(ArrayList<ChatFeedModel> data) {
		mDataList.addAll(data);
		notifyDataSetChanged();
	}

	public void addListFront(ArrayList<ChatFeedModel> data) {
		// 获取的新鲜事列表本来就是倒叙的，因为最早来的放列表前面，最先展示的应该是第一个
		for (ChatFeedModel chatFeedModel : data) {
			if (!ChatFeedDataManager.hasItemFeed(mDataList, chatFeedModel)) {
				mDataList.add(0, chatFeedModel);
			}
		}
		// mDataList.addAll(0, data);
		// 获取新鲜事以后超过最大数目的话，删除最后的部分
		if (this.getCount() > ChatFeedDataManager.CHAT_FEED_MAX_NUM) {
			// int index = 0;
			// // mDataList.subList(0, end)
			// while(index<deleteNumber && mDataList.size()>0){
			// mDataList.remove(this.getCount()-1);
			// index++;
			// }
			mDataList = mDataList.subList(0,
					ChatFeedDataManager.CHAT_FEED_MAX_NUM);
		}
		notifyDataSetChanged();
	}

	public void removeItem(Object data) {
		mDataList.remove(data);
		notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public ChatFeedModel getFeedModel(int position) {
		return mDataList.get(position);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
			mIsScrolling = false;
			// if(FIRST_VISIABLE_ITEM > LAST_FIRST_VISIBLE_ITEM ||
			// FIRST_VISIABLE_ITEM < LAST_FIRST_VISIBLE_ITEM){
			// //向上或者向下滑动，导致显示的item变化的时候
			// System.out.println("firstVisibleItem ---" + FIRST_VISIABLE_ITEM);
			// final int min = FIRST_VISIABLE_ITEM - 1;
			// final int max = FIRST_VISIABLE_ITEM - 1 + VISIBLE_ITEM_COUNT;
			// RenrenChatApplication.sHandler.post(new Runnable() {
			// @Override
			// public void run() {
			// for(ChatFeedPicRecycleObserver chatFeedPicRecycleObserver :
			// mImageList){
			// chatFeedPicRecycleObserver.recycle(min, max);
			// }
			// ArrayList<ChatFeedPicRecycleObserver> mImageListCopy =
			// (ArrayList<ChatFeedPicRecycleObserver>) mImageList.clone();
			// for(ChatFeedPicRecycleObserver
			// chatFeedPicRecycleObserver:mImageListCopy){
			// if(chatFeedPicRecycleObserver.isRecycle){
			// mImageList.remove(chatFeedPicRecycleObserver);
			// }
			// }
			// System.gc();
			// }
			// });
			// }
			// LAST_FIRST_VISIBLE_ITEM = FIRST_VISIABLE_ITEM;
		} else {
			mIsScrolling = true;
		}
		if (!mIsScrolling) {
			this.notifyDataSetChanged();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (totalItemCount <= 0) {
			return;
		}
	}

	@Override
	public boolean enable() {
		return !mIsScrolling;
	}

	public static interface FeedPicRecycleObserver {
		public void recycle(int min, int max);
	}

	@Override
	public void register(ChatFeedPicRecycleObserver chatFeedPicRecycleObserver) {
		// mImageList.add(chatFeedPicRecycleObserver);
	}

	//
	// //统一处理规格图片,(注意bmp格式图片的处理)
	// ImageGetter imgGetter = new Html.ImageGetter(){
	//
	// @Override
	// public Drawable getDrawable(String source) {
	//
	//
	// // int id = Integer.parseInt(source);
	// Drawable drawable = null;
	// Bitmap map = null;
	// try
	// {
	// InputStream obj = new URL(source).openStream();
	// if (source.endsWith(".bmp"))
	// {
	// byte[] data = readStream(obj);
	// if (data != null)
	// {
	// map = BitmapFactory.decodeByteArray(data,0,data.length);
	// }
	// }
	// else
	// {
	// map = BitmapFactory.decodeStream(obj);
	// }
	//
	// drawable = new BitmapDrawable(map);
	// }
	// catch (IOException e)
	// {
	// return null;
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	//
	// if (null != drawable)
	// {
	// drawable.setBounds(0, 0, 40, 40);
	// }
	// return drawable;
	// }
	// };
	// private static byte[] readStream(InputStream inStream) throws Exception
	// {
	// ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	// byte[] buffer = new byte[1024];
	// int len = 0;
	// while ((len = inStream.read(buffer)) != -1)
	// {
	// outStream.write(buffer, 0, len);
	// }
	// outStream.close();
	// inStream.close();
	// return outStream.toByteArray();
	// }
	//
	private SpannableStringBuilder setUrl(TextView tv) {
		CharSequence text = tv.getText();
		SpannableStringBuilder style = null;
		if (text instanceof Spannable) {
			int end = text.length();
			Spannable sp = (Spannable) tv.getText();
			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
			style = new SpannableStringBuilder(text);
			style.clearSpans();
			for (URLSpan url : urls) {
				MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
				style.setSpan(myURLSpan, sp.getSpanStart(url),
						sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			// tv.setText(style);
		}
		return style;
	}

	//
	// ImageGetter FeedImageGetter = new ImageGetter() {
	// @Override
	// public Drawable getDrawable(String source) {
	// int id = Integer.parseInt(source);
	//
	// //�规�id浠��婧��浠朵腑�峰��剧�瀵硅薄
	// Drawable d = mActivity.getResources().getDrawable(id);
	// d.setBounds(0, 0, d.getIntrinsicWidth(),d.getIntrinsicHeight());
	// return d;
	// }
	// };
	private class MyURLSpan extends ClickableSpan {
		private String mUrl;

		MyURLSpan(String url) {
			mUrl = url;
		}

		@Override
		public void onClick(View widget) {
			RenRenWebView.show(mScreen.context, "", "", mUrl);
		}
	}

}
