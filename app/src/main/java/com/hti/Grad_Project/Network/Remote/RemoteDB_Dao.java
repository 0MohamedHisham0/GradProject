package com.hti.Grad_Project.Network.Remote;


import com.hti.Grad_Project.Model.AnswerList_Model;
import com.hti.Grad_Project.Model.Pdf_Model;
import com.hti.Grad_Project.Model.Pdf_List_Model;
import com.hti.Grad_Project.Model.Answer_Model;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RemoteDB_Dao {

    @GET("api/v2/files/")
    Call<Pdf_List_Model> getAllBooks();

    @GET("api/v2/files/")
    Call<Pdf_Model> getBookById(
            @Query("id") String id
    );

    @GET("api/v2/QA")
    Call<AnswerList_Model> getQuestionAnswer(
            @Query("question") String question,
            @Query("prediction") String predictions,
            @Query("model") String model,
            @Query("folder") String folder
    );

    @Multipart
    @POST("api/v2/files/add")
    Call<Pdf_Model> postPdf(
            @Part("title") String title,
            @Part MultipartBody.Part file

    );

}
