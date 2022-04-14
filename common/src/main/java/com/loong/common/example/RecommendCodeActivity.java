//package com.loong.common.example;
//
//import java.io.IOException;
//
///**
// * author : xuelong
// * e-mail : xuelong9009@qq.com
// * date   : 2020/7/2110:03 AM
// * desc   : 适用于保存View的图片到相册
// * version: 1.0
// */
//public class RecommendCodeActivity extends BaseActivity {
//    private ImageView ivCode;
//    String codeUrl = "";
//    private int codeId = 0;
//    private TextView tvId, tvSave;
//    private LinearLayout llMain;
//    private TextView mTvRight;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_recommend_code);
//        mLDialog.show();
//        findViews();
//        initData(savedInstanceState);
//        sendRecommendCode();
//        initViews(savedInstanceState);
//    }
//
//
//    @Override
//    protected void findViews() {
//        ivCode = findViewById(R.id.iv_code);
//        tvId = findViewById(R.id.tv_id);
//        llMain = findViewById(R.id.ll_main);
//        tvSave = findViewById(R.id.tv_save);
//    }
//
//    @Override
//    protected void initViews(Bundle savedInstanceState) {
//        tvSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                tvSave.setClickable(false);
//
////相关权限的申请 存储权限
//
//                try {
//                    if (ActivityCompat.checkSelfPermission(RecommendCodeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                            != PackageManager.PERMISSION_GRANTED
//                            || ActivityCompat.checkSelfPermission(RecommendCodeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                            != PackageManager.PERMISSION_GRANTED) {
//                        // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
//                        ActivityCompat.requestPermissions(RecommendCodeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                    } else {
//                        mLDialog.setDialogText("正在保存图片...");
//                        mLDialog.show();
//                        saveMyBitmap("AuthCode", createViewBitmap(llMain));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    //权限申请的回调
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case 1: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    mLDialog.setDialogText("正在保存图片...");
//                    mLDialog.show();
//                    try {
//                        saveMyBitmap("AuthCode", createViewBitmap(llMain));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    tvSave.setClickable(true);
//                    Toast.makeText(RecommendCodeActivity.this, "请先开启读写权限", Toast.LENGTH_SHORT).show();
//                }
//                return;
//            }
//        }
//    }
//
//    //使用IO流将bitmap对象存到本地指定文件夹
//    public void saveMyBitmap(final String bitName, final Bitmap bitmap) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String filePath = Environment.getExternalStorageDirectory().getPath();
//                File file = new File(filePath + "/DCIM/Camera/" + bitName + ".png");
//                try {
//                    file.createNewFile();
//
//
//                    FileOutputStream fOut = null;
//                    fOut = new FileOutputStream(file);
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
//
//
//                    Message msg = Message.obtain();
//                    msg.obj = file.getPath();
//                    handler.sendMessage(msg);
//                    //Toast.makeText(PayCodeActivity.this, "保存成功", Toast.LENGTH_LONG).show();
//                    fOut.flush();
//                    fOut.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            String picFile = (String) msg.obj;
//            String[] split = picFile.split("/");
//            String fileName = split[split.length - 1];
//            try {
//                MediaStore.Images.Media.insertImage(getApplicationContext()
//                        .getContentResolver(), picFile, fileName, null);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            // 最后通知图库更新
//            sendBroadcast(new Intent(
//                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
//                    + picFile)));
//            Toast.makeText(RecommendCodeActivity.this, "图片保存图库成功", Toast.LENGTH_LONG).show();
//            if (mLDialog != null && mLDialog.isShowing()) {
//                mLDialog.dismiss();
//            }
//            tvSave.setClickable(true);
//        }
//    };
//
//
////将要存为图片的view传进来 生成bitmap对象
//
//    public Bitmap createViewBitmap(View v) {
//        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
//                Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        v.draw(canvas);
//        return bitmap;
//    }
//
//    @Override
//    protected void initData(Bundle savedInstanceState) {
//
//
//    }
//
//    @Override
//    protected String initTitleCenterString() {
//        return "推荐码";
//    }
//
//}