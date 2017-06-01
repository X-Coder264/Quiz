package hr.tvz.quiz.rest;

        import retrofit2.Retrofit;

        import retrofit2.converter.gson.GsonConverterFactory;



public class APIClient {

    private static final String BASE_URL = "http://10.0.3.2:8080/"; //URL is not localhost because emulator doesn't see it

    private static APIClient instance = null;
    private APIInterface apiService;

    private APIClient(){
        buildRetrofit();
    }

    public static APIClient getInstance() {
        if (instance == null) {
            instance = new APIClient();
        }
        return instance;
    }

    private void buildRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.apiService = retrofit.create(APIInterface.class);
    }

    public APIInterface getApiService() {
        return apiService;
    }
}