package com.quintus.labs.datingapp.rest;

import android.content.Intent;

import com.google.gson.Gson;
import com.quintus.labs.datingapp.Login.Login;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.AppConstants;
import com.quintus.labs.datingapp.Utils.AppContext;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.Utils.ToastUtils;
import com.quintus.labs.datingapp.rest.Response.ResponseModel;
import com.quintus.labs.datingapp.xmpp.utils.NetworkUtil;

import java.io.IOException;

import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by MyU10 on 1/21/2017.
 */

public abstract class RestCallBack<T> implements Callback<T> {

    public abstract void onFailure(Call<T> call, String message);

    public abstract void onResponse(Call<T> call, Response<T> restResponse, T response);

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (call.isCanceled()) {
            onFailure(call, "");
            return;
        }

        if (NetworkUtil.isInternetAvailable) {
            if (t.getLocalizedMessage() != null)
                onFailure(call, t.getLocalizedMessage());
            else
                onFailure(call, "");
        } else
            onFailure(call, AppContext.getInstance().getContext().getString(R.string.no_internet));

        try {
            ToastUtils.showErrorOnLive(AppContext.getInstance().getContext(), t.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        int index = call.request().url().toString().indexOf("?");
        if (index == -1)
            index = call.request().url().toString().length();


    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            try {
                if(response.headers().get("x-auth")!=null){
                    TempStorage.setAuthToken(response.headers().get("x-auth"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                onResponse(call, response, response.body());
            }
            onResponse(call, response, response.body());
        } else {
            try {
                Gson gson = new Gson();
                ResponseModel responseModel = gson.fromJson(response.errorBody().string(), ResponseModel.class);

                //Send user to login screen if authentication error comes...
                if (responseModel.statusCode.equals(AppConstants.ApiParamValue.AUTHENTICATION_ERROR)) {
                    Intent intent;
//                    if (AppSharedPreferences.getInstance().getUserName() != null && !AppSharedPreferences.getInstance().getUserName().equalsIgnoreCase("")) {
//                        intent = new Intent(AppContext.getInstance().getContext(), RegisterLoginActivity.class);
//                    } else {
//                        intent = new Intent(AppContext.getInstance().getContext(), LoginScreenActivity.class);
//                    }
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//
                    TempStorage.logoutUser(AppContext.getInstance().getContext());
                    Intent in = new Intent(AppContext.getInstance().getContext(), Login.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    AppContext.getInstance().getContext().startActivity(in);

                } else if (responseModel.statusCode.equals(AppConstants.ApiParamValue.FORCE_UPDATE_ERROR)) {

                } else if (responseModel.statusCode.equals(AppConstants.ApiParamValue.RESPONSE_ERROR)) {

                }

                onFailure(call, responseModel.errorMessage);

            } catch (Exception e) {
                e.printStackTrace();
                onFailure(call, response.code() + " : " + response.message());
            }
        }
    }

    public static boolean isSuccessFull(ResponseModel responseModel) {
        if (responseModel.statusCode.equals(AppConstants.ApiParamValue.SUCCESS_RESPONSE_CODE))
            return true;
        else
            return false;
    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "response can't be converted into a string";
        }
    }
}
