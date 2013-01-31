AndoridSupport
==============

android开发框架

#前言
AndroidSupport 
只是把一些会用到的东西整理了一下，还谈不上框架，顶多就是写源代码

里面包含了很多会用到的源代码，比如
* Task （封装了AsyncTask，更加简单，也加了loading）
* ImageLoader(异步取图片，并缓存下来)
* LoadViewActivity（封装了ActivityGroup，做有nativtebar应用的时候会方便许多）
* ActivityUtils(封了一些常用的界面方法，比如dp->px转换，颜色转换，窗体条状等)
* AbstractHttpApi(父类的http模块，需要自己继承实现，封了大部分的代码)
* ActionBar（其实就是在一个分隔符和一个button）
* SharedPreferencesUtils（数据存储的帮助类）

#如何安装
1. 请在github下载源代码后，import进你的eclipse后，然后在Properties->Android->Libary is Libary打勾
2. 然后在你的项目里面Properties->Android->Libary->Add选择AndroidSupport

#Task使用范例
##示例代码
  	task = new Task(ProductsActivity.this);
		task.setOnInvokeBeforeListener(new OnInvokeBeforeListener() {

			@Override
			public void onInvokeBefore() {
				//调用开始前
			}
		}).setOnTaskRequestListener(new OnTaskRequestListener() {
			Group<Product> products;
			@Override
			public void onRequest() throws Exception{
				//耗时的在这做
			}
			
			@Override
			public Result getResult() {
				//返回你的对象
			}
		}).setOnInvokeAfterListener(new OnInvokeAterListener() {
			
			@Override
			public void onInvokeAter(MsgResult result) {
					//结束后更新UI等
			}
		})setOnInvokeErrorListener(new OnInvokeErrorListener(){
			@Override
			public void onInvokeError(MsgResult result){
				//返回失敗后調用
			}
		
		});

#HTTP模块的使用
##示例代码

         public class HttpApiImpl extends AbstractHttpApi {
	protected static final Logger LOG = Logger.getLogger(HttpApiImpl.class.getCanonicalName());
	private static boolean DEBUG = true;
	private static Gson gson = new Gson();
	
	
    public HttpApiImpl(DefaultHttpClient httpClient, String clientVersion) {
        super(httpClient, clientVersion);
    }

    
    public Result doHttpRequest(HttpRequestBase httpRequest,
    		Class clazz) throws Exception {
        return executeHttpRequest(httpRequest, clazz);
    }

    
    public Result executeHttpRequest(HttpRequestBase httpRequest,
    		Class clazz) throws Exception {
        InputStream is = executeHttpRequestSuccess(httpRequest);
        String responseString =  StringUtils.convertStreamToString(is);
        JSONObject jsonResponse = new JSONObject(responseString);
        if(DEBUG)Log.d("HttpApiImpl","responseString:" + responseString);
        String status = jsonResponse.getString("status");
        if("ok".equals(status)){
        	if(jsonResponse.has("result")){
        		Object obj = jsonResponse.get("result");
        		if(obj instanceof JSONObject){
        			return gson.fromJson(obj.toString(), clazz);
        		}else if(obj instanceof JSONArray){
        			JSONArray  array = (JSONArray) obj;
        			Group result = new Group();
        			for(int i=0;i<array.length();i++){
        				String json = ((JSONObject)array.get(i)).toString();
        				result.add(gson.fromJson(json, clazz));
        			}
        			return result;
        		}
        	}else{
        		return null;
        	}
        }else{
        	throw new SupportException("", jsonResponse.getString("message"));
        }
		return null;
    }



    
    public String doHttpPost(String url, NameValuePair... nameValuePairs) throws Exception {
        throw new RuntimeException("Haven't written this method yet.");
    }
       }

###API调用
       public Order order(Group<OrderItem> items) throws Exception{
		HttpPost httpPost = mHttpApi.createHttpPost(fullUrl("xxx.php"),
				new BasicNameValuePair("name", "sanvibyfish")
				);
		Order order = (Order) mHttpApi.doHttpRequest(httpPost, Order.class);
		return order;
	  }
