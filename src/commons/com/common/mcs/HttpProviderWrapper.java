package com.common.mcs;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.common.R;
import com.common.app.AbstractRenrenApplication;
import com.common.utils.Config;
import com.common.utils.Methods;
import com.core.json.JsonObject;
import com.core.json.JsonParser;
import com.core.json.JsonValue;
import com.core.util.AbstractApplication;
import com.core.util.CommonUtil;

public class HttpProviderWrapper implements INetProvider {
	private static INetProvider instance = new HttpProviderWrapper();

	private HttpProviderWrapper() {
		System.setProperty("http.keepAlive", "true");
	}

	public static INetProvider getInstance() {
		return instance;
	}

	private Vector<INetRequest> reqTxtVec = new Vector<INetRequest>();
	private Vector<INetRequest> reqImgVec = new Vector<INetRequest>();
	private Vector<INetRequest> reqPostImgVec = new Vector<INetRequest>();
	private Vector<INetRequest> reqVoiceVec = new Vector<INetRequest>();
	private HttpThread[] txtThreads = null;
	private HttpThread[] imgThreads = null;
	private HttpThread postImgThreads = null;
	private HttpThread getVoiceThread = null;

	private void checkThreads() {

		if (getVoiceThread == null) {
			getVoiceThread = new HttpThread(reqVoiceVec);
			getVoiceThread.start();
		}

		if (txtThreads == null) {
			txtThreads = new HttpThread[Config.TEXT_THREAD_NUM];
			for (int i = 0; i < txtThreads.length; i++) {
				txtThreads[i] = new HttpThread(reqTxtVec);
				txtThreads[i].start();
			}
		}
		if (null == imgThreads) {
			imgThreads = new HttpThread[Config.IMG_THREAD_NUM];
			for (int i = 0; i < imgThreads.length; i++) {
				imgThreads[i] = new HttpThread(reqImgVec);
				imgThreads[i].setPriority(Thread.NORM_PRIORITY);
				imgThreads[i].start();
			}
		}
		if (null == postImgThreads) {
			postImgThreads = new HttpThread(reqPostImgVec);
			postImgThreads.start();
		}
	}

	public void addRequest(INetRequest req, int priority) {
		synchronized (HttpProviderWrapper.this) {
			checkThreads();
			// }
			if (INetRequest.TYPE_HTTP_GET_IMG == req.getType() || INetRequest.TYPE_HTTP_GET_EMONTICONS == req.getType()) {
				synchronized (reqImgVec) {
					switch (priority) {
					case INetRequest.PRIORITY_HIGH_PRIORITY:
						reqImgVec.insertElementAt(req, 0);
						reqImgVec.notify();
						break;
					case INetRequest.PRIORITY_LOW_PRIORITY:
						reqImgVec.addElement(req);
						reqImgVec.notify();
						break;
					}
				}
			} else if (INetRequest.TYPE_HTTP_POST_IMG == req.getType() || INetRequest.TYPE_HTTP_SYNC_CONTACT == req.getType()) {
				synchronized (reqPostImgVec) {
					reqPostImgVec.addElement(req);
					reqPostImgVec.notify();
				}

			} 
			else if (INetRequest.TYPE_HTTP_GET_VOICE == req.getType()) {
				synchronized (reqVoiceVec) {
					reqVoiceVec.addElement(req);
					reqVoiceVec.notify();
				}
			} else {
				synchronized (reqTxtVec) {
					switch (priority) {
					case INetRequest.PRIORITY_HIGH_PRIORITY:
						reqTxtVec.insertElementAt(req, 0);
						reqTxtVec.notify();
						break;
					case INetRequest.PRIORITY_LOW_PRIORITY:
						reqTxtVec.addElement(req);
						reqTxtVec.notify();
						break;
					}
				}
			}
		}
	}

	// 读取本地图片缓存
	// private boolean loadImageCache(final INetRequest currentRequest) {
	//
	// String url_str = currentRequest.getUrl();
	// if (INetRequest.TYPE_HTTP_GET_IMG == currentRequest.getType()) {
	// JsonObject resp = new JsonObject();
	// byte[] img = null;
	// try {
	// img = Data.getImageData(url_str);
	// if (img != null) {
	// resp.put(INetResponse.IMG_DATA, img);
	// INetResponse currentResponse = currentRequest.getResponse();
	// currentResponse.response(currentRequest, resp);
	// return true;
	// }
	// } catch (FileNotFoundException e) {
	// Data.delUnrelatedImgData(url_str);
	// return false;
	// }
	// }
	// return false;
	// }

	/**
	 * 增加一个请求，默认的文本请求优先级最高，会插入到队列顶部
	 */
	public void addRequest(INetRequest req) {
		// if(loadImageCache(req)){
		// return;
		// }
		synchronized (HttpProviderWrapper.this) {
			checkThreads();
		}
		if (INetRequest.TYPE_HTTP_GET_IMG == req.getType() || INetRequest.TYPE_HTTP_GET_EMONTICONS == req.getType()) {
			synchronized (reqImgVec) {
				reqImgVec.addElement(req);
				reqImgVec.notify();
			}
		} else if (INetRequest.TYPE_HTTP_POST_IMG == req.getType() || INetRequest.TYPE_HTTP_SYNC_CONTACT == req.getType()) {
			synchronized (reqPostImgVec) {
				reqPostImgVec.addElement(req);
				reqPostImgVec.notify();
			}
		} else if (INetRequest.TYPE_HTTP_GET_VOICE == req.getType()) {
			synchronized (reqVoiceVec) {
				// System.out.println("add to get voice");
				reqVoiceVec.addElement(req);
				reqVoiceVec.notify();
			}
		} else {
			synchronized (reqTxtVec) {
				reqTxtVec.insertElementAt(req, 0);
				// reqTxtVec.addElement(req);
				reqTxtVec.notify();
			}
		}
	}

	public void cancel() {
		synchronized (reqImgVec) {
			reqImgVec.clear();
		}
	}

	public void stop() {
		synchronized (HttpProviderWrapper.this) {
			if (null != txtThreads) {
				synchronized (reqTxtVec) {
					for (int i = 0; i < txtThreads.length; i++) {
						if (null != txtThreads[i]) {
							// synchronized (txtThreads[i].reqVec) {
							txtThreads[i].running = false;
							// txtThreads[i].reqVec.clear();
							// txtThreads[i].reqVec.notify();
							// }
						}
					}
					reqTxtVec.clear();
					reqTxtVec.notifyAll();
					txtThreads = null;
				}
			}

			// 为了保证退出后也能同步通讯录头像
			if (null != imgThreads) {
				synchronized (reqImgVec) {
					for (int i = 0; i < imgThreads.length; i++) {
						if (null != imgThreads[i]) {
							// synchronized (imgThreads[i].reqVec) {
							imgThreads[i].running = false;
							imgThreads[i].burnning = false;
							// txtThreads[i].reqVec.clear();
							// txtThreads[i].reqVec.notify();
							// }
						}
					}
					reqImgVec.clear();
					reqImgVec.notifyAll();
					imgThreads = null;
				}
			}

			if (null != postImgThreads) {
				synchronized (postImgThreads.reqVec) {
					postImgThreads.running = false;
					postImgThreads.burnning = true;
					postImgThreads.reqVec.notify();
				}
				postImgThreads = null;
			}
		}
	}

	static class HttpThread extends Thread {
		private Vector<INetRequest> reqVec = null;
		private JsonObject error = new JsonObject();

		public HttpThread(Vector<INetRequest> reqVec) {
			this.reqVec = reqVec;
			setPriority(Thread.NORM_PRIORITY);
			error.put("error_code", -99);
			error.put("error_msg", "连接服务器错误，请稍后再试！");
		}

		protected boolean running = true;

		/**
		 * 等所有请求都发送完成后关闭
		 */
		protected boolean burnning = false;

		@Override
		public void run() {
			INetRequest currentRequest = null;
			boolean reconnect = false;
			
			// httpClient = new DefaultHttpClient();
			while (running || burnning) {
				// Thread.yield();
				
				if (!reconnect) {
					synchronized (reqVec) {
						if (reqVec.size() > 0) {
							currentRequest = reqVec.firstElement();
							reqVec.removeElementAt(0);
						} else {
							if (burnning) {
								burnning = false;
								return;
							}
							try {
								reqVec.wait();
								continue;
							} catch (InterruptedException e) {
							}
						}
					}
				}
				HttpClient httpClient = null;
				if (currentRequest != null) {
					String url_str = currentRequest.getUrl();
					// data 为null 表示get方式获取图片
					INetResponse currentResponse = currentRequest.getResponse();
					final INetProgressResponse progressResponse = currentResponse instanceof INetProgressResponse ? (INetProgressResponse) currentResponse : null;
					try {

						HttpRequestBase httpRequest = null;
						if (INetRequest.TYPE_HTTP_GET_IMG == currentRequest.getType() || INetRequest.TYPE_HTTP_GET_EMONTICONS == currentRequest.getType()) {// 获取图片
							httpRequest = new HttpGet(url_str);
							httpRequest.addHeader("Referer", "http://www.renren.com/");
							httpRequest.addHeader("Accept", "*/*");
							JsonObject temp = new JsonObject();
							temp.put("error_code", -90);
							temp.put("error_msg", "获取图片失败");
							error = temp;
						} else if (INetRequest.TYPE_HTTP_GET_HTML == currentRequest.getType()) {
							httpRequest = new HttpGet(url_str);
							httpRequest.addHeader("Accept", "*/*");
							JsonObject temp = new JsonObject();
							temp.put("error_code", -91);
							temp.put("error_msg", "页面访问失败");
							error = temp;
						} else if (INetRequest.TYPE_HTTP_GET_VOICE == currentRequest.getType()) {
							httpRequest = new HttpGet(url_str);
							httpRequest.addHeader("Accept", "*/*");
							JsonObject temp = new JsonObject();
							// temp.put("error_code", -9000);
							// temp.put("error_msg",
							// "cannot get voice dingwei.chen");
							temp.put("error_code", -91);
							temp.put("error_msg", "下载语音文件失败");
							error = temp;
						} else {
							httpRequest = new HttpPost(url_str);
							JsonObject temp = new JsonObject();
							temp.put("error_code", -99);
							temp.put("error_msg", AbstractApplication.getAppContext().getString(R.string.no_internet_tip));
							
							error = temp;
							if (INetRequest.TYPE_HTTP_POST_IMG == currentRequest.getType() || INetRequest.TYPE_HTTP_POST_BIN_File == currentRequest.getType()) {
								httpRequest.addHeader("Content-Type", "multipart/form-data; charset=UTF-8; boundary=FlPm4LpSXsE");// 发送图像数据
							} else {
								httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");// 发送属性
							}
							// httpRequest.addHeader("Connection",
							// "Keep-Alive");
							httpRequest.addHeader("Connection", "keep-alive");
							httpRequest.addHeader("Accept", "*/*");
							byte[] requestBytes = currentRequest.serialize();
							HttpEntity entity = null;
							if (progressResponse != null) {
								progressResponse.uploadContentLength(requestBytes.length);
								entity = new ByteArrayEntity(requestBytes);
							} else {
								entity = new ByteArrayEntity(requestBytes);
							}
							((HttpPost) httpRequest).setEntity(entity);
						}

						BasicHttpParams httpParameters = new BasicHttpParams();
						if (INetRequest.TYPE_HTTP_POST_IMG == currentRequest.getType()) {
							httpParameters.setIntParameter(HttpConnectionParams.SO_TIMEOUT, Config.HTTP_POST_IMAGE_TIMEOUT); // 超时设置
							httpParameters.setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, Config.HTTP_POST_IMAGE_TIMEOUT);
							httpParameters.setIntParameter(HttpConnectionParams.SOCKET_BUFFER_SIZE, 8192 <<2);
						} else if (INetRequest.TYPE_HTTP_POST_BIN_File == currentRequest.getType()) {
							httpParameters.setIntParameter(HttpConnectionParams.SO_TIMEOUT, Config.HTTP_POST_BIN_TIMEOUT); // 超时设置
							httpParameters.setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, Config.HTTP_POST_BIN_TIMEOUT);
							httpParameters.setIntParameter(HttpConnectionParams.SOCKET_BUFFER_SIZE, 8192 <<2);
						} else {
							httpParameters.setIntParameter(HttpConnectionParams.SO_TIMEOUT, Config.HTTP_POST_TEXT_TIMEOUT); // 超时设置
							httpParameters.setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, Config.HTTP_POST_TEXT_TIMEOUT);
							httpParameters.setIntParameter(HttpConnectionParams.SOCKET_BUFFER_SIZE, 8192);
						}
						httpClient = new DefaultHttpClient(httpParameters);
						HttpHost host = HttpProxy.getProxyHttpHost(AbstractRenrenApplication.getAppContext());
						if (host != null) {
							httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
						}
						
						
						httpRequest.removeHeaders(HTTP.EXPECT_DIRECTIVE);
						HttpResponse httpResponse = httpClient.execute(httpRequest);
						int statusCode = httpResponse.getStatusLine().getStatusCode();
						if (statusCode == HttpStatus.SC_OK) {
							String headerField = null;
							try {
								headerField = httpResponse.getFirstHeader("Content-Type").getValue();
							} catch (Exception e) {
							}
							if (headerField != null) {
								if (headerField.startsWith("text/vnd.wap.wml") || headerField.startsWith("application/vnd.wap.wmlc")) {
									if (reconnect) {
										if (null != currentResponse) {
											currentResponse.response(currentRequest, error);
										}
										reconnect = false;
									} else {
										reconnect = true;
										continue;
									}
								}
							}
							InputStream is = httpResponse.getEntity().getContent();
							if (progressResponse != null) {
								progressResponse.downloadContentLength(httpResponse.getEntity().getContentLength());
								is = new FilterInputStream(is) {
									@Override
									public int read() throws IOException {
										int bytes = super.read();
										progressResponse.download(bytes);
										return bytes;
									}

									@Override
									public int read(byte[] buffer, int offset, int count) throws IOException {
										int bytes = super.read(buffer, offset, count);
										progressResponse.download(bytes);
										return bytes;
									}
								};
							}
							if (currentRequest.useGzip()) {
								is = new GZIPInputStream(is);
							}else{
							}
							byte[] buf = Methods.toByteArray(is);
							JsonValue resp = null;
							if (INetRequest.TYPE_HTTP_GET_HTML == currentRequest.getType()) {
								String s = new String(buf, "UTF-8");
								s = s.replace("\\r", "");
								JsonObject respObj = new JsonObject();
								respObj.put(INetResponse.HTML_DATA, s);
								resp = respObj;
							} else if (INetRequest.TYPE_HTTP_GET_IMG == currentRequest.getType() || INetRequest.TYPE_HTTP_GET_EMONTICONS == currentRequest.getType()) {// 获取图片
								JsonObject respObj = new JsonObject();
								respObj.put(INetResponse.IMG_DATA, buf);
								resp = respObj;
								if (INetRequest.TYPE_HTTP_GET_IMG == currentRequest.getType()) {
									// if (Data.saveImage(buf, url_str)) {
									// //TODO
									// // NewImagePool.addImageCache(url_str);
									// }
								}
							} else if (INetRequest.TYPE_HTTP_GET_VOICE == currentRequest.getType()) {
								JsonObject respObj = new JsonObject();
								respObj.put(INetResponse.VOICE_DATA, buf);
								respObj.put("url", currentRequest.getUrl());
								resp = respObj;
							} else {// 请求接口
								resp = deserialize(buf, currentRequest);
							}
							if (null != currentResponse) {
								try {// 避免网络回调异常引起的重复回调
									if (running) {
										currentResponse.response(currentRequest, resp);
									} else {
										// 为了在stop后还能够下载图片
										if (INetRequest.TYPE_HTTP_REST != currentRequest.getType()) {
											if (currentResponse != null)
												currentResponse.response(currentRequest, resp);
										}
									}
								} catch (Exception e) {}
							}
							reconnect = false;
						} else {
							if (reconnect) {
								if (null != currentResponse) {
									currentResponse.response(currentRequest, error);
								}
								reconnect = false;
							} else {
							    if (statusCode >= 400 && statusCode < 600) {
							    	if (null != currentResponse) {
										currentResponse.response(currentRequest, error);
									}
							        reconnect = false;
							    } else {
								    reconnect = true;
							    }
							}
							continue;
						}
						// 下面的处理过程不完整，没有给response返回，所以在response中关闭等待框的逻辑得不到处理
					} catch (UnknownHostException unKnownHost) {
//						System.out.println("异常1:"+unKnownHost);
						CommonUtil.error("cdw", "异常1:"+unKnownHost);
						JsonObject ret = new JsonObject();
						ret.put("error_code", -97);
						ret.put("error_msg", "无法连接网络，请检查您的手机网络设置...");
						if (null != currentResponse) {
							currentResponse.response(currentRequest, ret);
						}
						reconnect = false;
						continue;
					}

					catch (Exception e) {// 其他异常情况
						CommonUtil.error("cdw", "异常2:"+e);
						if (reconnect) {
							if (null != currentResponse) {
								if (currentRequest.getPriority() != INetRequest.PRIORITY_LOW_PRIORITY) { // 对于低优先级的请求错误不予提示
									currentResponse.response(currentRequest, error);
								}
							}
							reconnect = false;
						} else {
							reconnect = true;
							continue;
						}
					} finally {
						try {
							if (httpClient != null) {
								httpClient.getConnectionManager().shutdown();
							}
						} catch (Exception e) {
//						    Log.e("wt", "exception: " + e.toString());
						}
					}
				}
				
			}
		}
	}

	private static JsonValue deserialize(byte[] data, INetRequest currentRequest) {
		try {
			if (data == null) {
				return null;
			}
			String s = new String(data, "UTF-8");
			return JsonParser.parse(s);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
