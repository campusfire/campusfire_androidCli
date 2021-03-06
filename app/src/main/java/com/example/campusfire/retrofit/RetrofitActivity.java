package com.example.campusfire.retrofit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.campusfire.R;
import com.example.campusfire.drawing.DrawingActivity;
import com.example.campusfire.gesture.DetectSwipeGestureListener;
import com.example.campusfire.network.NetworkController;
import com.example.campusfire.utils.FileService;
import com.example.campusfire.utils.FileUtils;
import com.example.campusfire.utils.ServiceGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.example.campusfire.network.UrlConstants.GET_URL_REQUEST_CODE;
import static com.example.campusfire.network.UrlConstants.POST_URL;
import static com.example.campusfire.network.UrlConstants.POST_URL_AUTHPLAYER1_REQUEST_CODE;
import static com.example.campusfire.network.UrlConstants.POST_URL_LOGOUT_CODE;
import static com.example.campusfire.network.UrlConstants.POST_URL_REQUEST_CODE;
import static com.example.campusfire.network.UrlConstants.POST_URL_TEXT_REQUEST_CODE;

public class RetrofitActivity extends AppCompatActivity implements GestureDetector.OnDoubleTapListener, SensorEventListener, View.OnClickListener, NetworkController.ResultListener, RetrofitContract.View {

    private RetrofitPresenter rPresenter;
    //These are the components used for the image upload
    private int REQ_CODE=100;
    private int DRAW_CODE=200;
    private String image="data";
    private String imageName="Image.jpg";
    private Button uploadButton, btnselectpic, downloadButton, selectGetButton, uploadTextButton;
    private ImageButton logoutButton, drawingButton;
    private EditText txtUpload;
    private ImageView imageview;
    private ProgressDialog dialog = null;
    private JSONObject jsonObject;
    private Uri selectedImageUri;
    private String urlGetImage;

    // tags
    private String TAGDL = "DownloadImage";
    private String TAGUPDTXT = "UploadText";
    private static final String DEBUG_TAG = "Gestures";

    //To display player information
    private TextView textViewPlayer;
    private String Player;

    // This is the gesture detector compat instance.
    private GestureDetectorCompat gestureDetectorCompat = null;

    private long lastUpdate;
    private SensorManager sensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        rPresenter = new RetrofitPresenter(this);

        //Retrieve information on the player
        Player = this.getIntent().getStringExtra("player");
        textViewPlayer = (TextView)findViewById(R.id.textViewPlayer);
        textViewPlayer.setText(Player);

        // Create the buttons and other components for the upload by click
        uploadButton = (Button)findViewById(R.id.button_upload);
        btnselectpic = (Button)findViewById(R.id.button_choose);
        downloadButton = (Button)findViewById(R.id.button_download);
        selectGetButton = (Button)findViewById(R.id.button_select_get);
        imageview = (ImageView)findViewById(R.id.imageView);
        txtUpload = (EditText)findViewById(R.id.txtUpload);
        uploadTextButton = (Button)findViewById(R.id.button_upload_text);
        logoutButton = (ImageButton) findViewById(R.id.button_logout);
        drawingButton = (ImageButton) findViewById(R.id.button_drawing);

        btnselectpic.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
        downloadButton.setOnClickListener(this);
        selectGetButton.setOnClickListener(this);
        uploadTextButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        drawingButton.setOnClickListener(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);

        jsonObject = new JSONObject();

        // Create a common gesture listener object.
        DetectSwipeGestureListener gestureListener = new DetectSwipeGestureListener();

        // Set activity in the listener.
        gestureListener.setActivity(this);

        // Create the gesture detector with the gesture listener.
        gestureDetectorCompat = new GestureDetectorCompat(this, gestureListener);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            float gravity = SensorManager.GRAVITY_EARTH;
            long actualTime = event.timestamp;
            rPresenter.getAccelerometer(values, gravity, actualTime);
            lastUpdate = actualTime;
        }
    }

    @Override
    public void toaster(String txtToast){
        Toast.makeText(this,txtToast,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void logger(String type, String tag, String logMessage){
        switch (type){
            case "v":
                Log.v(tag,logMessage);
                break;
            case "e":
                Log.e(tag,logMessage);
                break;
            case "i":
                Log.i(tag,logMessage);
                break;
            case "d":
                Log.d(tag,logMessage);
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (this.gestureDetectorCompat.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_choose:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE);
                break;
            case R.id.button_upload:
                uploadFile(selectedImageUri);
                break;
            case R.id.button_download:
                Log.v(TAGDL,"DL button pressed");
                getRetrofitImage();
                break;
            case R.id.button_upload_text:
                String sentText = txtUpload.getText().toString();
                rPresenter.uploadText(sentText);
                break;
            case R.id.button_logout:
                rPresenter.logout(Player);
            case R.id.button_drawing:
                Intent drawingIntent = new Intent(this, DrawingActivity.class);
                drawingIntent.putExtra("Player",Player);
                startActivityForResult(drawingIntent,DRAW_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void uploadFile(Uri selectedImageUri) {
        // create upload service client
        FileService.Upload service = ServiceGenerator.createService(FileService.Upload.class);

        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, selectedImageUri);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(selectedImageUri)), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("Image", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description = RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString);

        // finally, execute the request
        Call<ResponseBody> call = service.upload(description, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.v("Upload", "success");
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageview.setImageURI(selectedImageUri);
        }
    }

    void getRetrofitImage() {
        Log.v(TAGDL,"getRetrofitImage accessed");
        // create upload service client
        FileService.Get service = ServiceGenerator.createService(FileService.Get.class);
        Log.v(TAGDL,"FileGetService créé");
        Call<ResponseBody> call = service.getImageDetails();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.v(TAGDL,"onResponse accessed");
                try {
                    Log.d(TAGDL, "Response came from server");
                    boolean FileDownloaded = DownloadImage(response.body());
                    Log.d(TAGDL, "Image is downloaded and saved : " + FileDownloaded);
                } catch (Exception e) {
                    Log.d(TAGDL, "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAGDL, t.toString());
            }
        });
    }

    private boolean DownloadImage(ResponseBody body) {

        try {
            File file = new File(getExternalFilesDir(null) + File.separator + "TotemImage.jpg");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAGDL, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                //creation of bitmap and put it into preview
                Bitmap bMap = BitmapFactory.decodeFile(getExternalFilesDir(null) + File.separator + "TotemImage.jpg");
                bMap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream);
                int width, height;
                ImageView image = (ImageView) findViewById(R.id.imageView);
                width = bMap.getWidth();
                height = bMap.getHeight();
                Bitmap bMap2 = Bitmap.createScaledBitmap(bMap, width, height, false);
                image.setImageBitmap(bMap2);

                //store as image, get it accessible from gallery
                MediaStore.Images.Media.insertImage(getContentResolver(), bMap, "TotemImage" , "Totem");
                Toast.makeText(this, "Image enregistrée", Toast.LENGTH_SHORT).show();

                outputStream.flush();

            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public void network_connect(int requestCode, HashMap<String, String> params){
        switch (requestCode){
            case GET_URL_REQUEST_CODE:
                break;
            case POST_URL_REQUEST_CODE:
                break;
            case POST_URL_AUTHPLAYER1_REQUEST_CODE:
                break;
            case POST_URL_TEXT_REQUEST_CODE:
                NetworkController.getInstance().connect(this, requestCode, Request.Method.POST, params, this);
                break;
            case POST_URL_LOGOUT_CODE:
                NetworkController.getInstance().connect(this,requestCode,Request.Method.POST,params,this);
                finish();
                break;
        }
    }

    @Override
    public void onResult(int requestCode, boolean isSuccess, JSONObject jsonObject, VolleyError volleyError, ProgressDialog progressDialog) throws JSONException {
        if (requestCode == POST_URL_TEXT_REQUEST_CODE)
        {
            if (isSuccess)
            {
                toaster("Texte envoyé");
                txtUpload.getText().clear();
            }
            else
            {
                toaster("Erreur d'envoi du texte");
            }
        }
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }
}