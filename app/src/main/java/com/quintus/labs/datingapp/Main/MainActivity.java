package com.quintus.labs.datingapp.Main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.quintus.labs.datingapp.BoostPaidPlans.BoostPlans;
import com.quintus.labs.datingapp.Login.Login;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.AppConstants;
import com.quintus.labs.datingapp.Utils.FirebaseRemoteConfigHelper;
import com.quintus.labs.datingapp.Utils.Helper;
import com.quintus.labs.datingapp.Utils.ItsAMatchDialog;
import com.quintus.labs.datingapp.Utils.PulsatorLayout;
import com.quintus.labs.datingapp.Utils.RemoteConfigConst;
import com.quintus.labs.datingapp.Utils.SuperLikeDialog;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.Utils.ToastUtils;
import com.quintus.labs.datingapp.Utils.TopNavigationViewHelper;
import com.quintus.labs.datingapp.Utils.TransparentProgressDialog;
import com.quintus.labs.datingapp.rest.RequestModel.AcceptRejectModel;
import com.quintus.labs.datingapp.rest.RequestModel.SuperLikeRequest;
import com.quintus.labs.datingapp.rest.Response.CardList;
import com.quintus.labs.datingapp.rest.Response.ImageModel;
import com.quintus.labs.datingapp.rest.Response.Interest;
import com.quintus.labs.datingapp.rest.Response.MatchedFriend;
import com.quintus.labs.datingapp.rest.Response.ResponseModel;
import com.quintus.labs.datingapp.rest.RestCallBack;
import com.quintus.labs.datingapp.rest.RestServiceFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


/**
 * DatingApp
 * https://github.com/quintuslabs/DatingApp
 * Created on 25-sept-2018.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final int ACTIVITY_NUM = 1;
    final private int MY_PERMISSIONS_REQUEST_LOCATION = 123;
    ListView listView;
    //List<Cards> rowItems;
    //ArrayList<com.quintus.labs.datingapp.rest.Response.CardList> rowItems = new ArrayList<>();
    FrameLayout cardFrame, moreFrame;
    private Context mContext = MainActivity.this;
    private NotificationHelper mNotificationHelper;
    private CardList cards_data[];
    private PhotoAdapter arrayAdapter;
    Context context;
    private TransparentProgressDialog pd;
    private List<CardList> feedList;

    public static void open(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
public static Intent createIntent(Context mContext){
    Intent intent = new Intent(mContext, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
            Intent.FLAG_ACTIVITY_SINGLE_TOP);
    return intent;
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        cardFrame = findViewById(R.id.card_frame);
        moreFrame = findViewById(R.id.more_frame);
        // start pulsator
        PulsatorLayout mPulsator = findViewById(R.id.pulsator);
        mPulsator.start();
        mNotificationHelper = new NotificationHelper(this);
        pd = new TransparentProgressDialog(this, R.drawable.spinner);


        setupTopNavigationView();

        hitApiToGetFeed();
        checkForcrash();


    }



    private void hitApiToGetFeed() {
        pd.show();
        Call<ResponseModel<List<CardList>>> responseModelCall = RestServiceFactory.createService().myFeeds();

        responseModelCall.enqueue(new RestCallBack<ResponseModel<List<CardList>>>() {
            @Override
            public void onFailure(Call<ResponseModel<List<CardList>>> call, String message) {
                ToastUtils.show(MainActivity.this, message);
                dismissProgress();
            }

            @Override
            public void onResponse(Call<ResponseModel<List<CardList>>> call, Response<ResponseModel<List<CardList>>> restResponse, ResponseModel<List<CardList>> response) {
                if (RestCallBack.isSuccessFull(response)) {
                    feedList = response.data;
                    arrayAdapter = new PhotoAdapter(context, R.layout.item, feedList);
                    checkRowItem();
                    updateSwipeCard();
                    arrayAdapter.notifyDataSetChanged();

                }
                dismissProgress();

            }
        });


    }
    private String getAge (int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
    private void dismissProgress() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    private void checkRowItem() {
        if (feedList.isEmpty()) {
            moreFrame.setVisibility(View.VISIBLE);
            cardFrame.setVisibility(View.GONE);
        }
    }

    private void updateLocation() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        updateLocation();
                    } else {
                        Toast.makeText(MainActivity.this, "Location Permission Denied. You have to give permission inorder to know the user range ", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void updateSwipeCard() {
        final SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                feedList.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                CardList obj = (CardList) dataObject;
                checkRowItem();
                requestFriend(AppConstants.REJECTED,obj.getId());

            }

            @Override
            public void onRightCardExit(Object dataObject) {
                CardList obj = (CardList) dataObject;
                checkRowItem();

                requestFriend(AppConstants.ACCEPTED,obj.getId());


            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here


            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                CardList card= (CardList) dataObject;
                ProfileCheckinMain.open(context,card);

            }
        });
    }


    public void sendNotification() {
        NotificationCompat.Builder nb = mNotificationHelper.getChannel1Notification(mContext.getString(R.string.app_name), mContext.getString(R.string.match_notification));

        mNotificationHelper.getManager().notify(1, nb.build());
    }


    public void DislikeBtn(View v) {
        if (feedList.size() != 0) {
            CardList card_item = feedList.get(0);
            String url="";

            if(card_item.getMedia()!=null&&card_item.getMedia().size()>0){
                url = card_item.getMedia().get(0).getUrl();
            }

            feedList.remove(0);
            arrayAdapter.notifyDataSetChanged();

            Intent btnClick = new Intent(mContext, BtnDislikeActivity.class);
            btnClick.putExtra("url", url);
            startActivity(btnClick);
            requestFriend(AppConstants.REJECTED,card_item.getId());

        }
    }

    public void SuperLike(View v){
        if (feedList.size() != 0) {
            CardList card_item = feedList.get(0);
            if(!TempStorage.getUser().isPremiumUser()){
                BoostPlans.open(context,card_item);
            }
            else{
           ToastUtils.show(mContext,"Super Like");
                supperLike(AppConstants.ACCEPTED,card_item.getId(),card_item.getFullName());
            }
        }

    }

    public void LikeBtn(View v) {
        if (feedList.size() != 0) {
            CardList card_item = feedList.get(0);

            feedList.remove(0);
            arrayAdapter.notifyDataSetChanged();
            String url="";
            if(card_item.getMedia()!=null&&card_item.getMedia().size()>0){
                url = card_item.getMedia().get(0).getUrl();
            }
            Intent btnClick = new Intent(mContext, BtnLikeActivity.class);
            btnClick.putExtra("url", url);
            startActivity(btnClick);
            requestFriend(AppConstants.ACCEPTED,card_item.getId());

        }
    }


    /**
     * setup top tool bar
     */
    private void setupTopNavigationView() {
        Log.d(TAG, "setupTopNavigationView: setting up TopNavigationView");
        BottomNavigationViewEx tvEx = findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(tvEx);
        TopNavigationViewHelper.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        dismissProgress();
        super.onDestroy();
    }

    private void requestFriend(String status,int id){
       // pd.show();
        Call<ResponseModel<MatchedFriend>> responseModelCall = RestServiceFactory.createService().requestFriend( new AcceptRejectModel(id,status));


          responseModelCall.enqueue(new RestCallBack<ResponseModel<MatchedFriend>>() {
              @Override
              public void onFailure(Call<ResponseModel<MatchedFriend>> call, String message) {

              }

              @Override
              public void onResponse(Call<ResponseModel<MatchedFriend>> call, Response<ResponseModel<MatchedFriend>> restResponse, ResponseModel<MatchedFriend> response) {
                  if(isSuccessFull(response)){

                      if(response.data.getReceiverStatus().equalsIgnoreCase(AppConstants.ACCEPTED)&&response.data.getSenderStatus().equalsIgnoreCase(AppConstants.ACCEPTED)){

                          ItsAMatchDialog itsAMatchDialog = new ItsAMatchDialog(mContext, response.data, AppConstants.Navigation.HOME_PAGE);
                          try {
                              itsAMatchDialog.show();
                          } catch (Exception e) {
                              e.printStackTrace();
                          }

                      }

                  }

              }
          });



    }

    private void supperLike(String status,int id,String name){
        Call<ResponseModel<Object>> responseModelCall = RestServiceFactory.createService().superLike( new SuperLikeRequest(id));
        responseModelCall.enqueue(new RestCallBack<ResponseModel<Object>>() {
            @Override
            public void onFailure(Call<ResponseModel<Object>> call, String message) {

            }

            @Override
            public void onResponse(Call<ResponseModel<Object>> call, Response<ResponseModel<Object>> restResponse, ResponseModel<Object> response) {
                if(isSuccessFull(response)){
                    SuperLikeDialog superLikeDialog = new SuperLikeDialog(mContext, mContext.getString(R.string.super_like),String.format(mContext.getString(R.string.super_like_message),"<b>"+name+"</b>"),mContext.getString(R.string.got_it), AppConstants.Navigation.HOME_PAGE);
                    try {
                        superLikeDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }
    private void checkForcrash() {
        String crashkey = FirebaseRemoteConfigHelper.getFirebaseRemoteConfigHelper(mContext).getRemoteConfigValue(RemoteConfigConst.CRASH_KEY);
        if(crashkey.equalsIgnoreCase("true")){
            throw new RuntimeException("This is a crash");

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Helper.refreshDeviceToken();
    }
}
