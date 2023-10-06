package com.tsj2023.todobucketlist.network;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface RetrofitService {

    @Multipart
    @POST("TodoBucketlist/completeList.php")
    Call<String> postDataToServer(@PartMap Map<String, String> dataPart,
                                  @Part MultipartBody.Part filePart);
}
