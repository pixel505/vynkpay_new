package com.vynkpay.activity.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.vynkpay.R;
import com.vynkpay.adapter.MessageListAdapter;
import com.vynkpay.databinding.ActivitySupportChatBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.CloseTicketResponse;
import com.vynkpay.retrofit.model.GetChatResponse;
import com.vynkpay.retrofit.model.ReplyChatResponse;
import com.vynkpay.utils.M;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportChatActivity extends AppCompatActivity {
    private static final int ACTIVITY_CHOOSE_FILE = 1;
    ActivitySupportChatBinding binding;
    SupportChatActivity ac;
    Dialog dialog1;
    String cheqeImagepath;
    private int mInterval = 10000; // 5 seconds by default, can be changed later
    private Handler mHandler;
    String realPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_support_chat);
        ac = SupportChatActivity.this;
        dialog1 = M.showDialog(SupportChatActivity.this, "", false, false);
        init();
    }

    private void init() {
        if(getIntent().getStringExtra("mark")!=null){
            if(getIntent().getStringExtra("mark").equals("can")){
                binding.markLn.setVisibility(View.VISIBLE);
                binding.markCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.show();
                        MainApplication.getApiService().closeTicket(Prefes.getAccessToken(ac), getIntent().getStringExtra("ticketId")).enqueue(new Callback<CloseTicketResponse>() {
                            @Override
                            public void onResponse(Call<CloseTicketResponse> call, Response<CloseTicketResponse> response) {
                                if(response.isSuccessful()){
                                    dialog1.dismiss();
                                    startActivity(new Intent(SupportChatActivity.this,SupportTicketActivity.class));
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<CloseTicketResponse> call, Throwable t) {
                            dialog1.dismiss();
                            }
                        });

                    }
                });
            }

            else {
                binding.btmLn.setVisibility(View.GONE);
            }
        }
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.supportchat);
        getChat();
        mHandler = new Handler();
        startRepeatingTask();

        binding.attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  Intent chooseFile;
                Intent intent;
                chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
                chooseFile.setType("*/*");
                intent = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
            }
        });

        binding.sendattachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.msgEdt.getText().toString().isEmpty()) {
                    Toast.makeText(ac, "Please Enter Message", Toast.LENGTH_SHORT).show();
                }


                else {
                    dialog1.show();


              if(realPath==null){

                  Log.e("null","yes");
                  RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), binding.msgEdt.getText().toString());
                  RequestBody ticketId = RequestBody.create(MediaType.parse("multipart/form-data"), getIntent().getStringExtra("ticketId"));
                  MainApplication.getApiService().replyChat(Prefes.getAccessToken(ac), null, body, ticketId)
                          .enqueue(new Callback<ReplyChatResponse>() {
                              @Override
                              public void onResponse(Call<ReplyChatResponse> call, Response<ReplyChatResponse> response) {
                                  if (response.isSuccessful() && response.body() != null) {


                                      dialog1.dismiss();
                                      if (response.body().getStatus().equals("true")) {
                                          binding.msgEdt.setText("");
                                          binding.sendImage.setVisibility(View.GONE);
                                          getChat();

                                      } else if (response.body().getStatus().equals("false")) {

                                      }

                                  }
                              }

                              @Override
                              public void onFailure(Call<ReplyChatResponse> call, Throwable t) {
                                  dialog1.dismiss();
                                  Toast.makeText(ac, t.getMessage(), Toast.LENGTH_SHORT).show();
                              }
                          });
               }else  {
                  Log.e("null","no");

                  List<MultipartBody.Part> parts = new ArrayList<>();
                  File file = new File(realPath);
                  compress(realPath);
                  RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                  parts.add(MultipartBody.Part.createFormData("image_id[]", file.getName(), imageBody));
                  RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), binding.msgEdt.getText().toString());
                  RequestBody ticketId = RequestBody.create(MediaType.parse("multipart/form-data"), getIntent().getStringExtra("ticketId"));
                  MainApplication.getApiService().replyChat(Prefes.getAccessToken(ac), parts, body, ticketId)
                          .enqueue(new Callback<ReplyChatResponse>() {
                              @Override
                              public void onResponse(Call<ReplyChatResponse> call, Response<ReplyChatResponse> response) {
                                  if (response.isSuccessful() && response.body() != null) {

                                      dialog1.dismiss();
                                      if (response.body().getStatus().equals("true")) {
                                          binding.msgEdt.setText("");
                                          binding.sendImage.setVisibility(View.GONE);
                                          getChat();
                                          realPath=null;

                                      } else if (response.body().getStatus().equals("false")) {

                                      }
                                  }
                              }

                              @Override
                              public void onFailure(Call<ReplyChatResponse> call, Throwable t) {
                                  dialog1.dismiss();
                                  Toast.makeText(ac, t.getMessage(), Toast.LENGTH_SHORT).show();
                              }
                          });
              }

                }


            }

        });


    }

    private void getChat() {
        MainApplication.getApiService().getChat(Prefes.getAccessToken(ac), getIntent().getStringExtra("ticketId")).enqueue(new Callback<GetChatResponse>() {
            @Override
            public void onResponse(Call<GetChatResponse> call, Response<GetChatResponse> response) {
                if (response.isSuccessful() && response != null) {
                    if (response.body().getStatus().equals("true")) {
                        binding.chatList.setAdapter(null);

                        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                        MessageListAdapter adapter = new MessageListAdapter(getApplicationContext(), response.body().getData().getConversation());

                        binding.chatList.setLayoutManager(manager);
                        binding.chatList.setAdapter(adapter);
                        binding.chatList.setNestedScrollingEnabled(false);
                        binding.chatList.scrollToPosition(response.body().getData().getConversation().size() - 1);
                    } else if (response.body().getStatus().equals("false")) {

                    }
                }
            }

            @Override
            public void onFailure(Call<GetChatResponse> call, Throwable t) {

            }
        });

    }

    private File compress(String mProfileImage) {
        File compressedFile = null;
        File file = new File(mProfileImage);

        try {
            Bitmap bitmap = BitmapFactory.decodeFile(mProfileImage);

            compressedFile = new File(Environment.getExternalStorageDirectory().toString() + "/health4cash" + file.getName());
            if (!compressedFile.exists())
                compressedFile.mkdirs();

            FileOutputStream out = new FileOutputStream(compressedFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compressedFile;
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                getChat();//this function can change value of mInterval.

            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    @Override
    public void onBackPressed() {

        stopRepeatingTask();
        // startActivity(new Intent(MessageActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();

        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    @Override
    protected void onStop() {
        stopRepeatingTask();
        super.onStop();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
        if(requestCode == ACTIVITY_CHOOSE_FILE) {
            /*
            Uri uri = data.getData();
            String FilePath = getRealPathFromURI(uri); // should the path be here in this string
            System.out.print("Path  = " + FilePath);*/
            Uri uri = data.getData();
             realPath = ImageFilePath.getPath(SupportChatActivity.this, data.getData());





          try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                binding.sendImage.setVisibility(View.VISIBLE);
                binding.sendImage.setText(realPath);
                // binding.sendImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String [] proj      = {MediaStore.Images.Media.DATA};
        Cursor cursor       = getContentResolver().query( contentUri, proj, null, null,null);
        if (cursor == null) return null;
        int column_index    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
