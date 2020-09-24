package com.pinandpassword;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.networking.FastAndroidWrapper;
import com.thailiapp.mnepalmos.LoginActivity;
import com.thailiapp.mnepalmos.R;
import com.thailiapp.mnepalmos.RecoveryOTPActivity;
import com.utils.AlertDialogUtil;
import com.utils.ApiUrl;
import com.utils.CommonWords;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPwdActivity extends AppCompatActivity {

    private Button btnRequestRecoveryPwd;
    private EditText editText_mobileNumber;
    private TextView txtlink_login;
    private String username;
    private String android_id, channel, uniqueID;
    private ImageButton backbtn;

    private Dialog nDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_pwd);
        initializeUIElements();

        channel = "gprs";
        android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        uniqueID = UUID.randomUUID().toString();

        Log.i("DeviceId", android_id);
        Log.i("UniqueId", uniqueID);

        //SUBMIT
        submitButtonClick();
        //backbtn
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ForgotPwdActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)) {
            Intent intent = new Intent(ForgotPwdActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
    }

    private void initializeUIElements() {

        editText_mobileNumber = findViewById(R.id.editText_mobileNumber);
        btnRequestRecoveryPwd = findViewById(R.id.btn_request_recovery_pwd);
        txtlink_login = findViewById(R.id.link_login);
        nDialog = new Dialog(this);
        nDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nDialog.setCancelable(false);
        nDialog.setContentView(R.layout.progressbar_layout);

    }

    public void getEditTextValue() {

        username = editText_mobileNumber.getText().toString();
    }

    /*** Start Validation Part **/

    public boolean isValidation() {

        getEditTextValue();

        if (username.isEmpty()) {
            editText_mobileNumber.setError("Please enter the mobile number");
            return false;
        } else if (username.length() != 10) {
            editText_mobileNumber.setError("Mobile Number must be 10 digit");
            editText_mobileNumber.requestFocus();
            return false;
        } else if (!username.startsWith("98")) {
            editText_mobileNumber.setError("Mobile Number must start from 98");
            editText_mobileNumber.requestFocus();
            return false;
        } else if (!isValidPhone(username)) {
            editText_mobileNumber.setError("Mobile Number must start from 98");
            editText_mobileNumber.requestFocus();
            return false;
        }
        return true;
    }

    /*** End Validation Part **/

    /*Submit Button*/
    public void submitButtonClick() {

        btnRequestRecoveryPwd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getEditTextValue();
                if (isValidation()) {
                    if (editText_mobileNumber.getText().toString().startsWith("98")) {
                        submitForgotPwdForm("");
                        //submitForgotPwdForm("");
                    } else {
                        editText_mobileNumber.setError("Mobile Number must start from 98");
                        editText_mobileNumber.requestFocus();
                    }
                    // Submit form is valid
                    //submitForm();

                } else {
                    AlertDialogUtil.alertDialogBox(ForgotPwdActivity.this, CommonWords.no_require);
                }
            }
        });


        // set on-click listener
        txtlink_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ForgotPwdActivity.this,
                        LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public static boolean isValidPhone(String phone) {
        String expression = "^([9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{9,9}$";
        CharSequence inputString = phone;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputString);
        return matcher.matches();
    }

    /* Click the Forgot Button*/
    public void submitForgotPwdForm(String relogin) {
        // Submit form is valid
        getEditTextValue();
        nDialog.show();


        FastAndroidWrapper.FastAndroidWrapperCallback fastAndroidWrapperCallback = new FastAndroidWrapper.FastAndroidWrapperCallback() {
            @Override
            public void fastAndroidCallBack(String json) {
                nDialog.dismiss();
                if (json != null) {
                    Log.d("nirajan", json);
                    try {

                        json = json.replaceAll("'", "");
                        json = json.replace("\\\"", "'");

                        System.out.println("Dynamic response=="
                                + json);
                        Log.i("Dynamic response==", json);

                        String jsondata = json;

                        JSONObject messageObject = new JSONObject(jsondata);
                        Log.i("", messageObject.toString());
                        System.out.println("fund transfer response=="
                                + messageObject.toString());

                        // get d json object
                        String s = messageObject.getString("d");
                        Log.i("", s);

                        String jsonStr = s;

                        if (jsonStr != null) {
                            try {

                                JSONObject jsonObj = new JSONObject(jsonStr);
                                Log.i("", jsonObj.toString());

                                int statusCode = jsonObj.getInt("StatusCode");
                                Log.i("", "" + statusCode);

                                if (statusCode == 200) {
                                    Log.i("", "" + statusCode);

                                 //   String statusMessage = jsonObj.getString("StatusMessage");
                                   // Log.i("", statusMessage);
                                        sendToRecoveryOTP();
                                } else {

                                    String statusMessage = jsonObj.getString("StatusMessage");
                                    Log.i("", statusMessage);

                                    AlertDialogUtil.alertDialogBox(ForgotPwdActivity.this,
                                            statusMessage);

                                }
                                if (nDialog.isShowing()) {
                                    nDialog.dismiss();
                                }

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                                if (nDialog.isShowing()) {
                                    nDialog.dismiss();
                                }
                            }
                        } else {
                            Log.e("ServiceHandler", CommonWords.no_response);
                            if (nDialog.isShowing()) {
                                nDialog.dismiss();
                            }
                            AlertDialogUtil.alertDialogBox(ForgotPwdActivity.this,
                                    CommonWords.no_response);
                        }

                    } catch (Exception e1) {
                        e1.printStackTrace();
                        AlertDialogUtil.alertDialogBox(ForgotPwdActivity.this,
                                CommonWords.no_network);
                        if (nDialog.isShowing()) {
                            nDialog.dismiss();
                        }
                    }
                }
                if (json == null) {
                    AlertDialogUtil.alertDialogBox(ForgotPwdActivity.this,
                            CommonWords.no_response);
                    if (nDialog.isShowing()) {
                        nDialog.dismiss();
                    }
                }
            }

            @Override
            public void fastAndroidError(ANError anError) {
                AlertDialogUtil.alertDialogBox(ForgotPwdActivity.this,
                        CommonWords.no_response);

                nDialog.dismiss();

            }
        };
        HashMap<String, String> bodyParam = new HashMap<>();
        /// must remove this parameter

        bodyParam.put("mobile", username);
        bodyParam.put("src", channel);
//        bodyParam.put("DeviceID", android_id);
//        bodyParam.put("DeviceUID", UUID.randomUUID().toString());
        FastAndroidWrapper.FastAndroidWrapperPost(bodyParam, fastAndroidWrapperCallback, ApiUrl.SENDOTP, nDialog);

//        AndroidNetworking.post(ApiUrl.serverCheckUserNameUrl)
//                .addHeaders("contentType", "application/json")
//                .addBodyParameter("mobile", username)
//                .addBodyParameter("src", channel)
//                .addBodyParameter("DeviceID", android_id)
//                .addBodyParameter("DeviceUID", UUID.randomUUID().toString())
//              .build().getAsString(new StringRequestListener() {
//            @Override
//            public void onResponse(String json) {
//
//
//                        if (json != null) {
//                            Log.i("RESPONSE JSON =", json);
//                            try {
//
//                                json = json.replaceAll("'", "");
//                                json = json.replace("\\\"", "'");
//
//                                System.out.println("Dynamic response=="
//                                        + json);
//                                Log.i("Dynamic response==", json);
//
//                                String jsondata = json;
//
//                                JSONObject messageObject = new JSONObject(jsondata);
//                                Log.i("", messageObject.toString());
//                                System.out.println("fund transfer response=="
//                                        + messageObject.toString());
//
//                                // get d json object
//                                String s = messageObject.getString("d");
//                                Log.i("", s);
//
//                                String jsonStr = s;
//
//                                if (jsonStr != null) {
//                                    try {
//
//                                        JSONObject jsonObj = new JSONObject(jsonStr);
//                                        Log.i("", jsonObj.toString());
//
//                                        int statusCode = jsonObj.getInt("StatusCode");
//                                        Log.i("", "" + statusCode);
//
//                                        if (statusCode == 200) {
//                                            Log.i("", "" + statusCode);
//
//                                            String statusMessage = jsonObj.getString("StatusMessage");
//                                            Log.i("", statusMessage);
//
//                                            if (statusMessage != null) {
//
//                                                sendToRecoveryOTP(statusMessage);
//
//                                            }
//                                        } else {
//
//                                            String statusMessage = jsonObj.getString("StatusMessage");
//                                            Log.i("", statusMessage);
//
//                                             AlertDialogUtil.alertDialogBox( this,
//                                                    statusMessage);
//
//                                        }
//                                        if(nDialog.isShowing()){ nDialog.dismiss();}
//
//                                    } catch (JSONException e1) {
//                                        e1.printStackTrace();
//                                        if(nDialog.isShowing()){ nDialog.dismiss();}
//                                    }
//                                } else {
//                                    Log.e("ServiceHandler", CommonWords.no_response);
//                                    if(nDialog.isShowing()){ nDialog.dismiss();}
//                                     AlertDialogUtil.alertDialogBox( this,
//                                            CommonWords.no_response);
//                                }
//
//                            } catch (Exception e1) {
//                                e1.printStackTrace();
//                                 AlertDialogUtil.alertDialogBox( this,
//                                        CommonWords.no_network);
//                                if(nDialog.isShowing()){ nDialog.dismiss();}
//                            }
//                        }
//                        if (json == null) {
//                             AlertDialogUtil.alertDialogBox( this,
//                                    CommonWords.no_response);
//                            if(nDialog.isShowing()){ nDialog.dismiss();}
//                        }
//                    }
//
//            @Override
//            public void onError(ANError anError) {
//                AlertDialogUtil.alertDialogBox( this,
//                        CommonWords.no_response);
//                if(nDialog.isShowing()){ nDialog.dismiss();}
//            }
//        });

    }

//
//    public void  AlertDialogUtil.alertDialogBox(final Context context, final String message) {
//        final Dialog view = new Dialog(context);
//        view.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        view.setCancelable(false);
//        view.setContentView(R.layout.alert_dialog_layout);
//
//        TextView textMessage = view
//                .findViewById(R.id.textViewMessage);
//
//        textMessage.setText(message);
//
//        view.findViewById(R.id.rl_ok).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // your business logic
//                if (message.equalsIgnoreCase("")) {
//                    //(getApplicationContext()).accessDialogBox(context);
//                    view.dismiss();
//                } else {
//                    view.dismiss();
//                }
//            }
//        });
//
//        view.show();
//    }
//

    private void sendToRecoveryOTP() {
            Intent intent = new Intent(this,
                    RecoveryOTPActivity.class);
            intent.putExtra("MOBILE", username);
            startActivity(intent);
            finish();
        }
    }

    /******** Start Server Checking **********/

    /*** Async Task to check whether internet connection is working.**/
    /******** Start Server is working Checking **********/

    /*** Async Task to check whether Server is working.**/

//    private class NetCheck extends AsyncTask<String, Void, Boolean> {
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            nDialog = new ProgressDialog(this);
//            nDialog.setMessage("Loading..");
//            nDialog.setIndeterminate(false);
//            nDialog.setCancelable(true);
//            nDialog.show();
//        }
//
//        @Override
//        protected Boolean doInBackground(String... args) {
//
//            /**
//             * Gets current device state and checks for working internet connection by trying Google.
//             **/
//            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo netInfo = cm.getActiveNetworkInfo();
//            if (netInfo != null && netInfo.isConnected()) {
//                try {
//                    URL url = new URL("http://www.google.com");
//                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
//                    urlc.setConnectTimeout(3000);
//                    urlc.connect();
//                    if (urlc.getResponseCode() == 200) {
//                        return true;
//                    }
//                } catch (MalformedURLException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//            return false;
//
//        }
//
//        @Override
//        protected void onPostExecute(Boolean th) {
//
//            if (th == true) {
//
//
//                /*if (ccode != "") { //Verified Verification Code
//                    //new AsyncListViewSessionLoaderS().execute();
//                    getLoginWithCode("");
//                } else if (ccode == "") { //Get Verification Code
//                    LoginWithOutCode();
//                }
//                */
//                new ServerCheck().execute();
//
//            } else {
//                nDialog.dismiss();
//                 AlertDialogUtil.alertDialogBox(this, CommonWords.no_network);
//            }
//        }
//    }

    /******** End Internet Connection Checking **********/
//    private class ServerCheck extends AsyncTask<String, Void, Boolean> {
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected Boolean doInBackground(String... args) {
//
//            /**
//             * Gets current device state and checks for working internet connection by trying Google.
//             **/
//            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo netInfo = cm.getActiveNetworkInfo();
//            if (netInfo != null && netInfo.isConnected()) {
//                try {
//                    URL url = new URL(ApiUrl.Urlroot); //"http://www.google.com"
//                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
//                    urlc.setConnectTimeout(3000);
//                    urlc.connect();
//                    if (urlc.getResponseCode() == 200) {
//                        return true;
//                    }
//                } catch (MalformedURLException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//            return false;
//
//        }
//
//        @Override
//        protected void onPostExecute(Boolean th) {
//
//            if (th == true) {
//
//
//
//
//            } else {
//                nDialog.dismiss();
//                AlertDialogUtil.alertDialogBox(this, CommonWords.not_response);
//            }
//        }
//    }

