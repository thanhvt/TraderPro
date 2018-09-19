package com.traderpro.my_interface;

//import com.mobotechnology.bipinpandey.retrofit_handdirty.model.UserDevice;
//import com.mobotechnology.bipinpandey.retrofit_handdirty.model.UserDeviceList;

import com.traderpro.thanhvt.UserDevice;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface GetUserDeviceDataService {

    @GET("UserDevices/")
    Call<List<UserDevice>> Get();


    /**
     * URL MANIPULATION
     * @since Not used, Just to know how to use @query to get JSONObject
     * */
    //@GET("bins/path/")
    //Call<UserDeviceList> getUserDeviceDataData(@Query("company_no") int companyNo);


    /**
     * URL MANIPULATION
     * A request URL can be updated dynamically using replacement blocks and parameters on the method.
     * A replacement block is an alphanumeric string surrounded by { and }
     * A corresponding parameter must be annotated with @Path using the same string.
     */
    @GET("group/{id}/users")
    Call<List<UserDevice>> groupList(@Path("id") int groupId);


    /**
     * URL MANIPULATION
     * Using Query parameters.
     */
    @GET("group/{id}/users")
    Call<List<UserDevice>> groupList(@Path("id") int groupId, @Query("sort") String sort);


    /**
     * URL MANIPULATION
     * complex query parameter combinations a Map can be used
     */
    @GET("group/{id}/noticelist")
    Call<List<UserDevice>> groupList(@Path("id") int groupId, @QueryMap Map<String, String> options);


    /**
     * URL MANIPULATION
     * HTTP request body with the @Body annotation
     */
    @POST("UserDevices/")
    Call<Boolean> Post(@Body UserDevice devices);


    /**
     * FORM ENCODED AND MULTIPART
     * Multipart requests are used when @Multipart is present on the method.
     * Parts are declared using the @Part annotation.
     */
    @PUT("UserDevices/")
    Call<Boolean> Put(@Body UserDevice devices);

    /**
     * FORM ENCODED AND MULTIPART
     * Form-encoded data is sent when @FormUrlEncoded is present on the method.
     * Each key-value pair is annotated with @Field containing the name and the object providing the value
     */
    @FormUrlEncoded
    @POST("notice/edit")
    Call<UserDevice> updateUserDevice(@Field("id") String id, @Field("title") String title);


    /**
     * HEADER MANIPULATION
     * Set static headers for a method using the @Headers annotation.
     */
    @Headers("Cache-Control: max-age=640000")
    @GET("notice/list")
    Call<List<UserDevice>> UserDeviceList();


    /**
     * HEADER MANIPULATION
     */
    @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "User-Agent: Retrofit-Sample-App"
    })
    @GET("noticelist/{title}")
    Call<UserDevice> getUserDevice(@Path("title") String title);


    /**
     * HEADER MANIPULATION
     * A request Header can be updated dynamically using the @Header annotation.
     * A corresponding parameter must be provided to the @Header.
     * If the value is null, the header will be omitted. Otherwise, toString will be called on the value, and the result used.
     */
    @GET("userdevice")
    Call<UserDevice> getUserDeviceUsingHeader(@Header("Authorization") String authorization);
}