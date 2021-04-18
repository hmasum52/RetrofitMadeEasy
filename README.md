# Retrofit Made Easy
Networking is a core part of an android part if it includes some external API call. Most APIs responses are in JSON format nowadays. We can HTTP request using android framework provided class like [HttpUrlConnection](https://developer.android.com/reference/java/net/HttpURLConnection) and [Volly](https://developer.android.com/training/volley). But [retrofit](https://square.github.io/retrofit/) is the most popular API for calling network calls in android nowadays.    

I use retrofit for API calls in all my android project. But I have to write same code for different project and different API again and again. So I have simplified retrofit network calls code so that I can easily use them in my projects. Now all I have to do is create a JsonApiCaller class object all call the GET and POST methods to complete my network call. 

Retrofit network call have the following steps:

- Step - 1 : Create a interface with relative endpoints and response types.

  As I need  to do only POST and GET requests most of the time and the responses are JSON object or array, so I have created the following interface.

  ```java
  public interface JsonApiEndPoints {
      @POST
      Call<JsonElement> POST(@HeaderMap Map<String,String> headerMap,
                                        @Url String relativePath, @Body Object object);
      @POST
      Call<JsonElement> POST(@Url String relativePath, @Body Object object);
  
      @GET
      Call<JsonElement> GET(@HeaderMap Map<String,String> headerMap, @Url String relativePath);
  
      @GET
      Call<JsonElement> GET(@Url String relativePath);
  }
  ```

- Step -2 : Build the retrofit instance with base URL:

  So I have created ApiService class which is a singleton but I have used map so that I can create instances for different BASE URL.

  ```java
  public class ApiService {
      public static final String TAG = "ApiService->";
      private static final Map<String, ApiService> instanceMap = new HashMap<>();
      private final JsonApiEndPoints jsonApiEndPoints;
  
      private ApiService(String baseUrl){
          HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
          interceptor.level(HttpLoggingInterceptor.Level.BODY);
          OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
  
          Retrofit retrofit = new Retrofit.Builder()
                  .baseUrl(baseUrl)
                  .client(client)
                  .addConverterFactory(GsonConverterFactory.create())
                  .build();
  
          jsonApiEndPoints = retrofit.create(JsonApiEndPoints.class);
          System.out.println(TAG+" Retrofit Api is created successfully");
      }
  
      public static synchronized ApiService getInstance(String baseUrl) {
          if(instanceMap.get(baseUrl) == null){
              instanceMap.put(baseUrl,new ApiService(baseUrl));
          }
          return instanceMap.get(baseUrl);
      }
  
      public JsonApiEndPoints getJsonApiEndPoints() {
          return jsonApiEndPoints;
      }
  }
  ```

- Step - 3: Enqueue calls:

  ```java
  public interface OnFinishListener<T>{
      void onSuccess(T t);
      void onFailure(Exception e);
  }
  
  public class JsonApiCaller<T>{
      public static final String TAG = "JsonApiCaller:-->";
      private final JsonApiEndPoints apiEndPoints; //api endpoints
      private OnFinishListener<T> onFinishListener;
      private final Gson gson = new Gson();
      private final Type type;
  
      /**
       *  if we have a class name "Dummy"
       *  <ul>
       *      <li>type is Dummy.class for Dummy object</li>
       *      <li>type is Dummy[].class for a array of Dummy class</li>
       *      <li>type is new TypeToken<List<Dummy>>(){}.getType() for a list of Dummy class</li>
       *  </ul>
       *
      // * @param type is the type of response we want to get
       * @param baseUrl is baseUrl of the API endpoint
       */
      public JsonApiCaller(Type type, String baseUrl){
          this.type = type;
          apiEndPoints = ApiService.getInstance(baseUrl).getJsonApiEndPoints();
      }
      public void addOnFinishListener(OnFinishListener<T> onFinishListener) {
          this.onFinishListener = onFinishListener;
      }
  
      public JsonApiCaller<T> GET(String relativePath){
          Call<JsonElement> call = apiEndPoints.GET(relativePath);
          this.enqueueRequest(call);
          return this;
      }
  
      public JsonApiCaller<T> GET(Map<String,String> headerMap, String relativePath){
          Call<JsonElement> call = apiEndPoints.GET(headerMap,relativePath);
          this.enqueueRequest(call);
          return this;
      }
  
      public JsonApiCaller<T> POST(String relativePath, Object object) {
          Call<JsonElement> call = apiEndPoints.POST(relativePath, object);
          Log.d(TAG, "POST()=> posting json element ");
          this.enqueueRequest(call);
          return this;
      }
  
      public JsonApiCaller<T> POST(Map<String,String> headerMap,String relativePath, Object object) {
          Call<JsonElement> call = apiEndPoints.POST(headerMap,relativePath, object);
          Log.d(TAG, "POST()=> posting json element ");
          this.enqueueRequest(call);
          return this;
      }
  
      private void enqueueRequest(Call<JsonElement> call){
          //for debugging
          //start
          Request request = call.request();
          Log.d(TAG,"enqueueRequest>method: "+request.method());
          Log.d(TAG,"enqueueRequest>url: "+request.url());
          Log.d(TAG,"enqueueRequest>headers: "+request.headers());
         // Log.d(TAG,"callForObject>body: "+request.body());
          //end
  
          call.enqueue(new Callback<JsonElement>() {
              @Override
              public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                  if(response.isSuccessful()){
                      JsonElement json = response.body();
                      Log.d(TAG,"enqueueRequest>onResponse: "+call.request().url()+" call is successful");
                      Log.d(TAG,"enqueueRequest>onResponse:"+type);
                      onFinishListener.onSuccess(gson.fromJson(json,type));
                  }else{
                      onFinishListener.onFailure(new Exception("Error "+response.code()));
                      Log.d(TAG,"enqueueRequest>onResponse:  process is not successful "+response.code());
                  }
              }
  
              @Override
              public void onFailure(Call<JsonElement> call, Throwable t) {
                  String className = t.getClass().toString();
                  onFinishListener.onFailure(new Exception(t));
                  if(className.endsWith("UnknownHostException") )
                      Log.d(TAG,"enqueueRequest > Server is not responding");
                  t.printStackTrace();
              }
          });
      }
  }
  ```

  