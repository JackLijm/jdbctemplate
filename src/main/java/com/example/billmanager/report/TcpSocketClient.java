//package com.example.jdbcteplate.report;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.Socket;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
///**
// * tcp socket通信客户端 User: Lib Date: 2018/04/10 Time: 18:29
// */
//public class TcpSocketClient {
//
//	protected Log logger = LogFactory.getLog(this.getClass());
//
//	/**
//	 * socket服务器地址
//	 */
//	private String host;
//
//	/**
//	 * socket服务器端口
//	 */
//	private Integer port;
//
//	/**
//	 * socket唯一标识
//	 */
//	private String token;
//
//	/**
//	 * 客户端版本
//	 */
//	private Integer versionCode;
//
//	/**
//	 * 消息处理器
//	 */
//	private MsgHandle handle;
//
//	/**
//	 * 异常处理器
//	 */
//	private ExceptionHandle exceptionHandle;
//
//	/**
//	 * 全局变量 socket
//	 */
//	private Socket socket;
//
//	/**
//	 * 发送消息队列
//	 */
//	private BlockingQueue<String> queue;
//
//	/**
//	 * 线程池 启动socket服务 处理心跳 重连， 发送消息 接受消息 corepoolsize：5
//	 */
//	/**
//	 * socket start
//	 */
//	private ScheduledExecutorService socketStartScheduledExecutorService;
//
//	/**
//	 * socket heartbeat
//	 */
//	private ScheduledExecutorService heartbeatgScheduledExecutorService;
//
//	/**
//	 * socket sendmsg
//	 */
//	private ScheduledExecutorService sendMsgScheduledExecutorService;
//
//	/**
//	 * socket receive
//	 */
//	private ScheduledExecutorService receiveScheduledExecutorService;
//
//	// 处理服务端消息的线程池（为了防止正在处理消息的时候来不及接收消息，所以对消息处理都需要另起一个线程）
//	private ExecutorService handleServerMsgService;
//
//	private Long lastHeartbeatTime = null;
//
//	public TcpSocketClient(String host, Integer port, String token) {
//		this.host = host;
//		this.port = port;
//		this.token = token;
//		socketStartScheduledExecutorService = Executors.newScheduledThreadPool(1);
//	}
//
//	public TcpSocketClient(String host, Integer port, String token, MsgHandle msgHandle) {
//		this.host = host;
//		this.port = port;
//		this.token = token;
//		this.handle = msgHandle;
//		socketStartScheduledExecutorService = Executors.newScheduledThreadPool(1);
//	}
//
//	public TcpSocketClient(String host, Integer port, String token, Integer versionCode) {
//		this.host = host;
//		this.port = port;
//		this.token = token;
//		this.versionCode = versionCode;
//		socketStartScheduledExecutorService = Executors.newScheduledThreadPool(1);
//	}
//
//	public void start() {
//		startServer();
//	}
//
//	/**
//	 * 启动服务
//	 */
//	public void startServer() {
//		socketStartScheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					if (!isConnection()) {
//						socket = new Socket(host, port);
//						heartbeatgScheduledExecutorService = Executors.newScheduledThreadPool(1);
//						sendMsgScheduledExecutorService = Executors.newScheduledThreadPool(1);
//						receiveScheduledExecutorService = Executors.newScheduledThreadPool(1);
//						// 初始化处理服务端消息的线程池
//						handleServerMsgService = Executors.newCachedThreadPool();
//						queue = new ArrayBlockingQueue<String>(100, true);
//						// 初始化之后马上发送token给服务端
//						sendMsg(TcpSocketUtil.getTokenMsg(host, null, token, versionCode));
//						if (handle != null) {
//							handle.startConnected(TcpSocketClient.this.getToken());
//						}
//						heartbeating();
//						sendMsg();
//						reciveMsgHandler();
//					}
//				} catch (IOException e) {
//					logger.debug("socket服务启动失败", e);
//					if (exceptionHandle != null) {
//						exceptionHandle.doHandleException(true, e.getMessage());
//					}
//					stopSocket();
//				}
//			}
//		}, 0, 10 * 1000, TimeUnit.MILLISECONDS);
//	}
//
//	/**
//	 * 心跳
//	 */
//	private void heartbeating() {
//		heartbeatgScheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
//			@Override
//			public void run() {
//				if (isConnection()) {
//					MsgCommonReq req = TcpSocketUtil.getHeartMsg(host, null, token, versionCode);
//					sendMsg(req);
//				} else {
//					stopSocket();
//				}
//			}
//		}, 0, 5 * 1000, TimeUnit.MILLISECONDS);
//	}
//
//	/**
//	 * 判断是否已连接
//	 *
//	 * @return
//	 */
//	public boolean isConnection() {
//		return (socket != null && socket.isConnected() && !socket.isClosed());
//	}
//
//	private void sendMsg() {
//		sendMsgScheduledExecutorService.schedule(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					while (true) {
//						String message = queue.take();
//						if (isConnection()) {
//							OutputStream out = socket.getOutputStream();
//							out.write((message + "\r\n").getBytes());
//							out.flush();
//						}
//					}
//				} catch (Exception e) {
//					logger.debug("发送消息失败，原因：" + e.getMessage(), e);
//					if (exceptionHandle != null) {
//						exceptionHandle.doHandleException(true, e.getMessage());
//					}
//					stopSocket();
//				}
//			}
//		}, 0, TimeUnit.MILLISECONDS);
//	}
//
//	/**
//	 * 消息处理器
//	 */
//	private void reciveMsgHandler() {
//		receiveScheduledExecutorService.schedule(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					if (isConnection()) {
//						InputStream is = socket.getInputStream();
//						// 阻塞室接收，防止一直死循环去读取，否则出现CPU占用率过高
//						BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//						while (isConnection()) {
//							String line = reader.readLine();
//							if (line != null) {
//								logger.debug("收到服务端消息(client receive)：" + line);
//							}
//							try {
//								final MsgCommonReq req = TcpSocketUtil.getMsgCommonReqFromXml(line);
//								if (req != null && req.getMsgHeader() != null
//										&& req.getMsgHeader().getMsgType() != null) {
//									// 接收到心跳
//									if (SocketCorestans.MSGTYPE_SOCKET_HEART
//											.equals(req.getMsgHeader().getMsgType())) {
//										Long nowTime = System.currentTimeMillis();
//										if (lastHeartbeatTime != null) {
//											if (nowTime - lastHeartbeatTime > 15 * 1000) {
//												stopSocket();
//												break;
//											}
//										}
//										lastHeartbeatTime = nowTime;
//									} else if (SocketCorestans.MSGTYPE_SOCKET_CLOSE
//											.equals(req.getMsgHeader().getMsgType())) {
//										// 服务端主动关闭连接，客户端也相应断开连接
//										logger.debug("服务端已断开连接，客户端也主动断开连接..............");
//										stopSocket();
//										break;
//									} else {
//										// 消息处理
//										if (handle != null && req.getMsgBody() != null) {
//											// 回复服务端客户端收到消息
//											//responseServer(req);
//											handleServerMsgService.submit(new Runnable() {
//												@Override
//												public void run() {
//													handle.doHandle(req.getMsgBody().toString());
//												}
//											});
//											// 暂时没有消息内容的结构体
//											// handle.doHandle(req);
//										}
//										logger.debug("[收到消息]" + line);
//									}
//								}
//							} catch (Exception e) {
//								logger.debug("处理接收到服务端的消息失败，原因：" + e.getMessage(), e);
//							}
//						}
//					}
//				} catch (Exception e) {
//					logger.debug("接收服务端的消息失败，原因：" + e.getMessage(), e);
//					stopSocket();
//				}
//			}
//		}, 0, TimeUnit.MILLISECONDS);
//	}
//
//	/**
//	 * 向所连接的服务端发送消息
//	 *
//	 * @param msg
//	 */
//	public void sendMsg(String msg) {
//		if (msg == null || msg.isEmpty())
//			return;
//		MsgCommonReq req = TcpSocketUtil.getMsgCommonReq(host, null, SocketCorestans.MSGTYPE_SOCKET_COMMON);
//		req.setMsgBody(msg);
//		// 不管发送成不成功，先保存消息记录，然后收到消息确认包之后再删除该记录
//		sendMsg(req);
//	}
//
//	public void sendMsg(String msg, boolean isXml) {
//		if (msg == null || msg.isEmpty())
//			return;
//		MsgCommonReq req = null;
//		if (isXml) {
//			req = TcpSocketUtil.getMsgCommonReqFromXml(msg);
//		} else {
//			req = TcpSocketUtil.getMsgCommonReq(host, null, SocketCorestans.MSGTYPE_SOCKET_COMMON);
//			req.setMsgBody(msg);
//		}
//		sendMsg(req);
//	}
//
//	public void sendMsg(MsgCommonReq req) {
//		String msg = TcpSocketUtil.getReqXml(req);
//		// 将消息转化为一行发送
//		msg = msg.replaceAll("[\n\r\t]+", "");
//		sendMsgByString(msg);
//	}
//
//	private void shutdownScheduleExecutorServer(ExecutorService service) {
//		if (service != null) {
//			service.shutdownNow();
//			service = null;
//		}
//
//	}
//
//	/**
//	 * 停止服务
//	 */
//	public void stopSocket() {
//		shutdownScheduleExecutorServer(receiveScheduledExecutorService);
//		receiveScheduledExecutorService = null;
//		shutdownScheduleExecutorServer(sendMsgScheduledExecutorService);
//		sendMsgScheduledExecutorService = null;
//		shutdownScheduleExecutorServer(heartbeatgScheduledExecutorService);
//		heartbeatgScheduledExecutorService = null;
//		shutdownScheduleExecutorServer(handleServerMsgService);
//		handleServerMsgService = null;
//		queue = null;
//		if (socket != null) {
//			try {
//				if (!socket.isClosed()) {
//					socket.close();
//				}
//			} catch (IOException e) {
//				logger.error(e.getMessage(), e);
//			}
//		}
//		lastHeartbeatTime = null;
//		socket = null;
//	}
//
//	private void sendMsgByString(String message) {
//		// 将消息转化为一行发送
//		message = message.replaceAll("[\n\r\t]+", "");
//		try {
//			queue.put(message);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public String getHost() {
//		return host;
//	}
//
//	public void setHost(String host) {
//		this.host = host;
//	}
//
//	public Integer getPort() {
//		return port;
//	}
//
//	public void setPort(Integer port) {
//		this.port = port;
//	}
//
//	public String getToken() {
//		return token;
//	}
//
//	public void setToken(String token) {
//		this.token = token;
//	}
//
//	public Integer getVersionCode() {
//		return versionCode;
//	}
//
//	public void setVersionCode(Integer versionCode) {
//		this.versionCode = versionCode;
//	}
//
//	public MsgHandle getHandle() {
//		return handle;
//	}
//
//	public void setHandle(MsgHandle handle) {
//		this.handle = handle;
//	}
//
//	public ExceptionHandle getExceptionHandle() {
//		return exceptionHandle;
//	}
//
//	public void setExceptionHandle(ExceptionHandle exceptionHandle) {
//		this.exceptionHandle = exceptionHandle;
//	}
//
//	/**
//	 * 给服务器器发送收到消息
//	 * @param req
//	 * @return void
//	 */
//	public void responseServer(MsgCommonReq req) {
//		// 回复收到信息包
//		MsgCommonReq msgCommonReq = new MsgCommonReq();
//		msgCommonReq.setMsgBody(req.getMsgBody());
//		msgCommonReq.setMsgCommonReqId(req.getMsgCommonReqId());
//		MsgHeader header = new MsgHeader();
//		header.setMsgID(req.getMsgHeader().getMsgID());
//		header.setMsgType(SocketCorestans.MSGTYPE_SOCKET_COMFIRM);
//		header.setVersionCode(versionCode);
//		msgCommonReq.setMsgHeader(header);
//		sendMsg(msgCommonReq);
//	}
//
//	public static void main(String[] args) throws IOException, InterruptedException {
//		File file = new File("D:\\test.txt");
//		InputStream fileInputStream = new FileInputStream(file);
//		InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//		while (true) {
//			//Thread.sleep(1000);
//			BufferedReader reader = new BufferedReader(inputStreamReader);
//			String line = reader.readLine();
//			System.out.println(line);
//
//		}
//	}
//}
