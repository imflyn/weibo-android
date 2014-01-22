package net.weibo.app.ui;

import net.weibo.api.NewsImpl;
import net.weibo.app.R;
import net.weibo.app.asynctask.MyAsyncTask;
import net.weibo.app.lib.ListViewTool;
import net.weibo.app.service.SendWeiboService;
import net.weibo.app.sp.SharedPreferencesConfig;
import net.weibo.common.ImageUtils;
import net.weibo.common.Utility;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;

public class WriteNewsActivity extends AbstractWriteActivity implements OnClickListener, AMapLocationListener
{

    public static final String   ACTION_DRAFT       = "DRAFT";
    public static final String   ACTION_SEND_FAILED = "SEND_FAILED";

    private String               picPath            = "";
    private Uri                  imageFileUri       = null;

    private LocationManagerProxy lm;

    private GetLocationInfo      locationTask;
    private double[]             myLocation         = new double[2];
    private boolean              hasLocation        = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initView();
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (!TextUtils.isEmpty(action))
        {
            if (action.equals(Intent.ACTION_SEND) && !TextUtils.isEmpty(type))
            {

            } else if (action.equals(ACTION_DRAFT))
            {

            } else if (action.equals(ACTION_SEND_FAILED))
            {
                initView();
                handleFailedOperation(intent);
            }
        } else
        {
            initView();
            if (SharedPreferencesConfig.getInstance(appContext).getBoolean(SharedPreferencesConfig.SP_ALLOW_LOCATION))
                addLocation();
        }

    }

    private void handleFailedOperation(Intent intent)
    {
        SpannableString span = ListViewTool.convertNormalStringToSpannableString(intent.getStringExtra("content"), this);

        content.setText(span);
        picPath = intent.getStringExtra("picPath");
        enablePicture();
        myLocation[0] = intent.getDoubleExtra("longitude", 0.0);
        myLocation[1] = intent.getDoubleExtra("latitude", 0.0);

        if (Utility.isTaskStopped(locationTask))
        {
            locationTask = new GetLocationInfo(myLocation);
            locationTask.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (lm != null)
            lm.removeUpdates(this);

        Utility.cancelTasks(locationTask);

    }

    private void initView()
    {
        menu_pic.setVisibility(View.VISIBLE);
        menu_geo.setVisibility(View.VISIBLE);
        registerListener();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("picPath", picPath);
        outState.putDoubleArray("myLocation", myLocation);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
        {
            picPath = savedInstanceState.getString("picPath");
            if (!TextUtils.isEmpty(picPath))
                enablePicture();

            myLocation = savedInstanceState.getDoubleArray("myLocation");
        }
    }

    private void registerListener()
    {
        menu_pic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addPic();
            }
        });

        menu_geo.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (hasLocation)
                {
                    menu_geo.setImageDrawable(getResources().getDrawable(R.drawable.wb_icon_write_lbs_nor));
                    tv_location.setVisibility(View.GONE);
                    hasLocation = false;
                } else
                {
                    addLocation();
                }
            }
        });
    }

    private void addPic()
    {

        new SelectPictureDialog().show(getSupportFragmentManager(), "");
    }

    public void deletePicture()
    {
        picPath = "";
        disablePicture();
    }

    private void disablePicture()
    {
        ((ImageButton) findViewById(R.id.menu_pic)).setImageDrawable(getResources().getDrawable(R.drawable.wb_icon_write_at_selector));
    }

    private void enablePicture()
    {

        Bitmap bitmap = ImageUtils.getBitmapByPath(picPath);
        if (bitmap != null)
        {
            ((ImageButton) findViewById(R.id.menu_pic)).setScaleType(ScaleType.FIT_CENTER);
            ((ImageButton) findViewById(R.id.menu_pic)).setImageBitmap(bitmap);
        }
    }

    public static Intent startBecauseSendFailed(Context context, String content, String picPath, double longitude, double latitude)
    {
        Intent intent = new Intent(context, WriteNewsActivity.class);
        intent.setAction(WriteNewsActivity.ACTION_SEND_FAILED);
        intent.putExtra("content", content);
        intent.putExtra("picPath", picPath);
        intent.putExtra("longitude", longitude);
        intent.putExtra("latitude", latitude);
        return intent;
    }

    @Override
    protected boolean canShowSaveDraftDialog()
    {
        return true;
    }

    @Override
    protected void sendNews()
    {

        if (!canSend())
            return;

        String value = content.getText().toString();

        Intent intent = new Intent(this, SendWeiboService.class);
        intent.putExtra("picPath", picPath);
        intent.putExtra("content", value);
        if (hasLocation)
        {
            intent.putExtra("longitude", myLocation[0]);
            intent.putExtra("latitude", myLocation[1]);
        }
        startService(intent);
        finish();
    }

    @Override
    public void saveToDraft()
    {

    }

    public static class SelectPictureDialog extends DialogFragment
    {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {

            String[] items = { getString(R.string.take_camera), getString(R.string.select_pic), getString(R.string.cancle) };

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.select)).setItems(items, (DialogInterface.OnClickListener) getActivity());

            return builder.create();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        switch (which)
        {
            case 0:
                imageFileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
                if (imageFileUri != null)
                {
                    Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
                    if (Utility.isIntentSafe(WriteNewsActivity.this, i))
                    {
                        startActivityForResult(i, CAMERA_RESULT);
                    } else
                    {
                        Toast.makeText(getApplicationContext(), getString(R.string.dont_have_camera_app), Toast.LENGTH_SHORT).show();
                    }
                } else
                {
                    Toast.makeText(getApplicationContext(), getString(R.string.cant_insert_album), Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                Intent choosePictureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(choosePictureIntent, PIC_RESULT);
                break;
            case 2:
                dialog.dismiss();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case CAMERA_RESULT:
                    if (TextUtils.isEmpty(content.getText().toString()))
                    {
                        content.setText(getString(R.string.share_pic));
                        content.setSelection(content.getText().toString().length());
                    }
                    picPath = Utility.getPicPathFromUri(imageFileUri, this);
                    enablePicture();
                    break;
                case PIC_RESULT:
                    if (TextUtils.isEmpty(content.getText().toString()))
                    {
                        content.setText(getString(R.string.share_pic));
                        content.setSelection(content.getText().toString().length());
                    }
                    Uri imageFileUri = intent.getData();
                    picPath = Utility.getPicPathFromUri(imageFileUri, this);
                    enablePicture();
                    break;
                case BROWSER_PIC:
                    boolean deleted = intent.getBooleanExtra("deleted", false);
                    if (deleted)
                        deletePicture();
                    break;
            }

        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    private void addLocation()
    {
        lm = LocationManagerProxy.getInstance(this);
        // Location
        // API定位采用GPS定位方式，第一个参数是定位provider，第二个参数时间最短是5000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者

        AMapLocation location = lm.getLastKnownLocation(LocationManagerProxy.GPS_PROVIDER);
        if (null != location)
        {
            final double latitude = location.getLatitude();
            final double longitude = location.getLongitude();

            myLocation[0] = longitude;
            myLocation[1] = latitude;

            if (Utility.isTaskStopped(locationTask))
            {
                locationTask = new GetLocationInfo(myLocation);
                locationTask.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
                lm.removeUpdates(this);
            }
        }
        lm.requestLocationUpdates(LocationManagerProxy.GPS_PROVIDER, 5000, 1, this);

        tv_location.setVisibility(View.VISIBLE);
        tv_location.setText("正在定位...");

        // UIUtils.ToastMessage(getApplicationContext(), "正在定位...");
    }

    @Override
    public void onLocationChanged(AMapLocation location)
    {
        if (location != null)
        {
            final double latitude = location.getLatitude();
            final double longitude = location.getLongitude();

            myLocation[0] = longitude;
            myLocation[1] = latitude;

            if (Utility.isTaskStopped(locationTask))
            {
                locationTask = new GetLocationInfo(myLocation);
                locationTask.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
                lm.removeUpdates(this);
            }
        }
    }

    private class GetLocationInfo extends MyAsyncTask<String, String, String>
    {
        double[] location;

        GetLocationInfo(double[] location)
        {
            this.location = location;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params)
        {
            NewsImpl newsImpl = new NewsImpl();
            String result = newsImpl.getLocation(String.valueOf(location[0]), String.valueOf(location[1]), String.valueOf(1), String.valueOf(200), "");
            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            if (TextUtils.isEmpty(result))
            {
                tv_location.setVisibility(View.GONE);
                menu_geo.setImageDrawable(getResources().getDrawable(R.drawable.wb_icon_write_lbs_nor));
                Toast.makeText(getApplicationContext(), "网络较差未找到位置!", Toast.LENGTH_SHORT).show();
                hasLocation = false;
            } else
            {
                tv_location.setVisibility(View.VISIBLE);
                tv_location.setText(result);
                menu_geo.setImageDrawable(getResources().getDrawable(R.drawable.wb_icon_write_lbs_press));
                // Toast.makeText(getApplicationContext(), "定位成功!",
                // Toast.LENGTH_SHORT).show();
                hasLocation = true;
            }
        }

    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onLocationChanged(Location location)
    {

    }

}
