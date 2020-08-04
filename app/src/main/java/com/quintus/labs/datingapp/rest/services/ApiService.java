package com.quintus.labs.datingapp.rest.services;


import com.quintus.labs.datingapp.BoostPaidPlans.PlanModel;
import com.quintus.labs.datingapp.Utils.AppConstants;
import com.quintus.labs.datingapp.rest.RequestModel.AcceptRejectModel;
import com.quintus.labs.datingapp.rest.RequestModel.AddAddressRequest;
import com.quintus.labs.datingapp.rest.RequestModel.ChangePasswordModel;
import com.quintus.labs.datingapp.rest.RequestModel.ChangepasswordRequest;
import com.quintus.labs.datingapp.rest.RequestModel.EditProfileUpdateRequest;
import com.quintus.labs.datingapp.rest.RequestModel.FcmTokenUpdateRequest;
import com.quintus.labs.datingapp.rest.RequestModel.HelpCenterRequestModel;
import com.quintus.labs.datingapp.rest.RequestModel.LoginRequest;
import com.quintus.labs.datingapp.rest.RequestModel.PaymentIntentRequest;
import com.quintus.labs.datingapp.rest.RequestModel.PaymentRSARequest;
import com.quintus.labs.datingapp.rest.RequestModel.PaymentRequest;
import com.quintus.labs.datingapp.rest.RequestModel.PaymentResultRequest;
import com.quintus.labs.datingapp.rest.RequestModel.RegisterRequest;
import com.quintus.labs.datingapp.rest.RequestModel.SaveBookingModel;
import com.quintus.labs.datingapp.rest.RequestModel.SuperLikeRequest;
import com.quintus.labs.datingapp.rest.Response.AddressList;
import com.quintus.labs.datingapp.rest.Response.CardList;
import com.quintus.labs.datingapp.rest.Response.CatagoryList;
import com.quintus.labs.datingapp.rest.Response.ImageModel;
import com.quintus.labs.datingapp.rest.Response.MatchedFriend;
import com.quintus.labs.datingapp.rest.Response.MyBooking;
import com.quintus.labs.datingapp.rest.Response.RSAList;
import com.quintus.labs.datingapp.rest.Response.ResponseModel;
import com.quintus.labs.datingapp.rest.Response.ServiceproviderList;
import com.quintus.labs.datingapp.rest.Response.SubCategoryList;
import com.quintus.labs.datingapp.rest.Response.SuperLikeModel;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.quintus.labs.datingapp.rest.model.PaymentIntentModel;
import com.quintus.labs.datingapp.rest.model.PaymentResultModel;

import java.util.List;
import java.util.Map;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface ApiService {




    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.LOGIN)
    Call<ResponseModel<UserData>> login(@Body LoginRequest loginRequest);


    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.SIGNUP)
    Call<ResponseModel<UserData>> signup(@Body RegisterRequest registerRequest);


    @Headers("Content-type: application/json")
    @PUT(AppConstants.Url.GETUSER)
    Call<ResponseModel<UserData>> updateUser(@Body EditProfileUpdateRequest editProfileUpdateRequest);


    @Multipart
    @POST(AppConstants.Url.CHATUPLOAD)
    Call<ResponseModel<ImageModel>> chatUpload(@Part MultipartBody.Part fileType,@Part MultipartBody.Part file);

    @Multipart
    @PUT(AppConstants.Url.GETUSER)
    Call<ResponseModel<UserData>> fileUpload(@Part("fullName") RequestBody bodyfullName, @Part("gender") RequestBody bodygender,
                                             @Part("dob") RequestBody bodydob, @Part("interested") RequestBody bodyintrested,
                                             @Part("minRange") RequestBody bodyminrange, @Part("maxRange") RequestBody bodymaxrange,
                                             @Part("distance") RequestBody bodydistance, @Part MultipartBody.Part file);

    @Multipart
    @POST(AppConstants.Url.UPLOADIMAGE)
    Call<ResponseModel<ImageModel>> uploadImage(@Part MultipartBody.Part file);

    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    @Headers("Content-type: application/json")
    @DELETE(AppConstants.Url.DELETEIMAGE)
    Call<ResponseModel<ImageModel>> deleteImage(@Path(AppConstants.ApiParamKey.ID) long mediaId);


    //
    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.CATEGORYLIST)
    Call<ResponseModel<List<CatagoryList>>> getCategoryList();

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.SUBCATEGORYLIST + "/{CategoryId}")
    Call<ResponseModel<SubCategoryList>> getSubCategoryList(@Path(ServiceConstants.CATEGORYID) int categoryid);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.ADDADRESS)
    Call<ResponseModel<AddressList>> addAddress(@Body AddAddressRequest addAddressRequest);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.ADDADRESS)
    Call<ResponseModel<List<AddressList>>> getAddress();

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.PROVIDERLIST )
    Call<ResponseModel<List<ServiceproviderList>>> getProviderList(@Query(ServiceConstants.OBJECTID) int objectId
            , @Query("serviceType") String serviceType);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.SAVEBOOKING)
    Call<ResponseModel<Map<String, String>>> savebooking(@Body SaveBookingModel saveBookingModel);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.FEEDS)
    Call<ResponseModel<List<CardList>>> myFeeds();

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.REQUEST_FRIEND)
    Call<ResponseModel<MatchedFriend>> requestFriend(@Body AcceptRejectModel acceptRejectModel);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.UPDATE_DEVICE_TOKEN)
    Call<ResponseModel<Object>> updateDeviceToken(@Body FcmTokenUpdateRequest fcmTokenUpdateRequest);


    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.BLOCK)
    Call<ResponseModel<Object>> block(@Body SuperLikeRequest superLikeRequest);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.UNBLOCK)
    Call<ResponseModel<Object>> unblock(@Body SuperLikeRequest superLikeRequest);


    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.SUPER_LIKE)
    Call<ResponseModel<Object>> superLike(@Body SuperLikeRequest superLikeRequest);


    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.SUPER_LIKE)
    Call<ResponseModel<List<SuperLikeModel>>> getSuperLikeUser();

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.GETUSER)
    Call<ResponseModel<UserData>> getUserDetails();

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.GET_PAYMENT_INTENT)
    Call<ResponseModel<PaymentIntentModel>> getPaymentIntent(@Body PaymentIntentRequest paymentIntentRequest);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.PAYMENT_URL)
    Call<ResponseModel<PaymentResultModel>> payment(@Body PaymentResultRequest paymentResultRequest);




    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.FRIEND_LIST)
    Call<ResponseModel<List<MatchedFriend>>> getFriendsList();

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.PLAN_LIST)
    Call<ResponseModel<List<PlanModel>>> getPlanList();



    @Headers("Content-type: application/json")
    @PUT(AppConstants.Url.GETUSER)
    Call<ResponseModel<UserData>> setUserDetails(@Body EditProfileUpdateRequest editProfileUpdateRequest);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.SENDFEEDBACK)
    Call<ResponseModel<Map<String, String>>> sendHelpCenter(@Body HelpCenterRequestModel helpCenterRequestModel);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.LOGOUT)
    Call<ResponseModel<Map<String, String>>> logOut();

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.CHANGEPASSWORD)
    Call<ResponseModel<Map<String, String>>> ChangePassword(@Body ChangepasswordRequest changepasswordRequest);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.INITIATEPAYMENT+ "/{id}")
    Call<ResponseModel<Map<String, String>>> initiatePayment(@Path("id") int bookingid);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.FINALPAYMENT+ "/{id}")
    Call<ResponseModel<MyBooking>> paymentToServer(@Path("id") int bookingid, @Body PaymentRequest paymentRequest);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.INITIATE)
    Call<ResponseModel<Map<String, String>>> initiatePaymentSubscribe();

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.RSA)
    Call<ResponseModel<List<RSAList>>> rsaList();

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.RSAPayment)
    Call<ResponseModel<UserData>> paymentToRsaServer(@Body PaymentRSARequest paymentRequest);


    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.CHANGEPASSWORD)
    Call<ResponseModel> changePassword(@Body ChangePasswordModel changePasswordModel);

}
