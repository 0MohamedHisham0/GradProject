package com.hti.Grad_Project.Network.Remote;


import com.hti.Grad_Project.Model.AnswerList_Model;
import com.hti.Grad_Project.Model.Pdf_Model;
import com.hti.Grad_Project.Model.Pdf_List_Model;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final static String BASE_URL = "http://b91e-45-240-189-104.ngrok.io/";
    private final RemoteDB_Dao remoteDB_dao;
    private static RetrofitClient retrofitClient;
    private static Retrofit retrofit;

    public RetrofitClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(new OkHttpClient().newBuilder()
                        .connectTimeout(2, TimeUnit.MINUTES)
                        .readTimeout(2, TimeUnit.MINUTES)
                        .writeTimeout(2, TimeUnit.MINUTES)
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        remoteDB_dao = retrofit.create(RemoteDB_Dao.class);
    }

    public static RetrofitClient getInstance() {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient();
        }
        return retrofitClient;
    }

    public Call<Pdf_List_Model> getAllBooks() {
        return remoteDB_dao.getAllBooks();
    }

    public Call<Pdf_Model> getBookById(String id) {
        return remoteDB_dao.getBookById(id);
    }

    public Call<AnswerList_Model> getQuestionAnswer(String question, String predictions, String model, String book) {
        return remoteDB_dao.getQuestionAnswer(question, predictions, model, book);
    }

    public Call<Pdf_Model> postNewBook(File pdf, String title) throws UnsupportedEncodingException {

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/pdf"), pdf);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", URLEncoder.encode(pdf.getName(), "utf-8"), requestBody);

        return remoteDB_dao.postPdf(title.replace(".pdf", ""), filePart);
    }

}






