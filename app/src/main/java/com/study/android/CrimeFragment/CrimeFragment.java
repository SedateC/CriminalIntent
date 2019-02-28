package com.study.android.CrimeFragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.study.utils.PictureUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * CrimeFragment 类是与模型及视图对象交互的控制器，用于显示特定crime的明细信息，并在
 用户修改这些信息后立即进行更新。
 import android.support.v4.app.Fragment;
 */


public class CrimeFragment extends Fragment {//选择支持库版Fragment V4
    //这里继承没有构造方法，调用父类的 后面在执行onCreate(savedInstanceState); onCreateView
    private Crime mCrime;
    private EditText mEditText;
    private Button mDateButton; //显示crime 发生的日期
    private Button mDateTimeButton;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallContact;
    private Button mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private static final String CRIME_POSITION_ID = "crime_position_id";
    private static final String DIALOG_DATE="DialogDate";
    private static final String DIALOG_TIME="DialogTime";
    //设置DATE 回传的目标Fragment this
    private static final int REQUEST_DATE= 0;
    private static final int REQUEST_TIME= 1;
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_PHOTO=3;


    @Override
    public void onCreate(Bundle savedInstanceState) {//创建覆盖方法
        //其次，类似于activity，fragment同样具有保存及获取状态的bundle。如同使用 Activity.
        //onSaveInstanceState(Bundle) 方法那样，你也可以根据需要覆盖 Fragment.onSaveInstance-
        //State(Bundle) 方法。
        super.onCreate(savedInstanceState);
        //这样传数据的话这个类不能给其他类用所以要传到其他类应该使用Bundle
        //每个fragment实例都可附带一个 Bundle 对象。该bundle包含键值对
       // UUID crimeId = (UUID)getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID); //切换成databases
        //  mCrime = CrimeLab.get(getActivity()).getCrime(crimeId); //这个是数据源的指针 切换成databases
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        setHasOptionsMenu(true);//设置是有标题栏的
    }


   //要附加argument bundle给fragment，需调用 Fragment.setArguments(Bundle) 方法。而且，
    //还必须在fragment创建后、添加给activity前完成。
    //为满足以上要求，Android开发人员采取的习惯做法是：添加名为 newInstance() 的静态方
    //法给 Fragment 类。使用该方法，完成fragment实例及 Dundle 对象的创建，然后将argument放入
    //bundle中，最后再附加给fragment。
    //托管activity需要fragment实例时，转而调用 newInstance() 方法，而非直接调用其构造方法。
    //而且，为满足fragment创建argument的要求，activity可给 newInstance() 方法传入任何需要的参数。
    //在 CrimeFragment 类中，编写可以接受 UUID 参数的 newInstance(UUID) 方法，创建argument
    //bundle和fragment实例，然后附加argument给fragment
    private static final String ARG_CRIME_ID = "crime_id";
    public static CrimeFragment newInstance(UUID crimeId){//先被调用
        Bundle bundldArgs = new Bundle();
        bundldArgs.putSerializable(ARG_CRIME_ID,crimeId);
        CrimeFragment crimeFragment = new CrimeFragment();
        //每个fragment实例都可附带一个 Bundle 对象。该bundle包含键值对
        crimeFragment.setArguments(bundldArgs);
        return crimeFragment;
    }

    /*
    * 该 方 法 实 例 化 fragment 视 图 的 布 局 ， 然 后 将 实 例 化 的 View 返 回 给 托 管 activity 。
    LayoutInflater 及 ViewGroup 是实例化布局的必要参数。 Bundle 用来存储恢复数据，可供该方
    法从保存状态下重建视图
    agment的视图是直接通过调用 LayoutInflater.inflate(...)
    方法并传入布局的资源ID生成的。第二个参数是视图的父视图，我们通常需要父视图来正确配置
    组件。第三个参数告诉布局生成器是否将生成的视图添加给父视图。这里，传入了 false 参数，
    因为我们将以代码的方式添加视图。
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime,container,false);
        mEditText = v.findViewById(R.id.crime_title);
        mEditText.setText(mCrime.getTitle());
        /*
        * 添加内容变换方法
        * */
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //s 为输入的
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = v.findViewById(R.id.crime_date);
        mDateTimeButton = v.findViewById(R.id.crime_date_time);
        updateDate();
        updateTime();
        // mDateButton.setEnabled(false);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment
                        .newInstance(mCrime.getDate());
                //设置传递数据返回对象为自己CrimeFragment.this
                datePickerFragment.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                datePickerFragment.show(fragmentManager,DIALOG_DATE);
            }
        });
        mDateTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mCrime.getDate());
                timePickerFragment.setTargetFragment(CrimeFragment.this,REQUEST_TIME);
                timePickerFragment.show(fragmentManager,DIALOG_TIME);
            }
        });
        mSolvedCheckBox = v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);//这里自动生成ischecked
            }
        });
        //隐式启动
        mReportButton = v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {/*
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);*/
                ShareCompat.IntentBuilder sc = ShareCompat.IntentBuilder.from(getActivity());
                sc.setType("text/plain");
                sc.setText(getCrimeReport());
                sc.setSubject(getString(R.string.crime_report_subject));
                sc.setChooserTitle(getString(R.string.send_report));
                sc.createChooserIntent();
                sc.startChooser();
            }
        });
        final Intent pickContact = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
       // pickContact.addCategory(Intent.CATEGORY_HOME);//只是阻止任何联系人应用和你的intent匹配
        mSuspectButton = v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact,REQUEST_CONTACT);
            }
        });
        if (mCrime.getSupect()!=null){
            mSuspectButton.setText(mCrime.getSupect());
        }
        mCallContact = v.findViewById(R.id.call_contact);
        if (mCrime.getCallContact()==null){
            mCallContact.setEnabled(false);
        }
        mCallContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+mCrime.getCallContact()));
                startActivity(intent);
            }
        });
        //Android设备上安装了哪些组件以及包括哪些activity， PackageManager 类全都知道。（本书
        //后续章节还会介绍更多组件。）调用 resolveActivity(Intent, int) 方法，可以找到匹配给定
        //Intent 任务的activity。flag标志 MATCH_DEFAULT_ONLY 限定只搜索带 CATEGORY_DEFAULT 标志的
        //activity。这和 startActivity(Intent) 方法类似。
        final PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,PackageManager.MATCH_DEFAULT_ONLY)==null){
         mSuspectButton.setEnabled(false);
        }
        mPhotoButton = v.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile!=null&&captureImage.resolveActivity(packageManager)!=null;
        mPhotoButton.setEnabled(canTakePhoto);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用 FileProvider.getUriForFile(...) 会把本地文件路径转换为相机能看见的 Uri 形
                //式。要实际写入文件，还需要给相机应用权限。为了授权，我们授予 FLAG_GRANT_WRITE_URI_
                //PERMISSION 给所有 cameraImage intent的目标activity，以此允许它们在 Uri 指定的位置写文件。
                //当然，还有个前提条件：在声明 FileProvider 的时候添加过android: grantUriPermissions 属性。
                Uri uri = FileProvider.getUriForFile(getActivity(),"com.study.android.criminalintent.fileprovider",mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                //要想获得全尺寸照片，就要让它使用文件系统存储照片。这可以通过传入保存在
                //MediaStore.EXTRA_OUTPUT 中的指向存储路径的 Uri 来完成。这个 Uri 会指向 FileProvider 提
                //供的位置。
                List<ResolveInfo> cameraActivities = getActivity().getPackageManager()
                        .queryIntentActivities(captureImage, packageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo acitivty:cameraActivities){
                        getActivity().grantUriPermission(acitivty.activityInfo.packageName,
                                uri,Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                }
                startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });
        mPhotoView = v.findViewById(R.id.crime_photo);
        updatePhotoView();
        //这里一个Fragment已经写好 用Fragmanager管理
        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_crime:
                CrimeLab.get(getActivity()).removeCrime(mCrime);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //list 获取返回结果
    // 从fragment中返回结果的处理稍有不同。fragment能够从activity中接收返回结果，但其自身无
    //法持有返回结果。只有activity拥有返回结果。因此，尽管 Fragment 有自己的 startActivityFor-
    //Result(...) 方法和 onActivityResult(...) 方法，但没有 setResult(...) 方法。
    public void returnResult(){
        getActivity().setResult(Activity.RESULT_OK,null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode!= Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
        if (requestCode == REQUEST_TIME){
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
            updateTime();
        }
        if (requestCode == REQUEST_CONTACT&&data!=null){
            Uri contactUri = data.getData();
            String callContactId = null;
            String[] queryFilds = new String[]{
              ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts._ID
            };
            Cursor cursor = getActivity().getContentResolver()
                    .query(contactUri,queryFilds,null,null,null);
            try {
                if (cursor.getCount()==0){
                    return;
                }
                cursor.moveToFirst();
                String suspect = cursor.getString(0);
                 callContactId = cursor.getString(1);
                mCrime.setSupect(suspect);
                mSuspectButton.setText(suspect);
            }finally {
                cursor.close();
            }
            Cursor c = getActivity().getContentResolver().
                    query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    +"=?",new String[]{callContactId},null
                            );
            try {
                if (c.getCount() == 0){
                    return;
                }
                c.moveToFirst();
                int index = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String callContact = c.getString(index);
                mCrime.setCallContact(callContact);
                mCallContact.setEnabled(true);
                mCallContact.setText(callContact);
            }finally {
                c.close();
            }

        }
        if (requestCode==REQUEST_PHOTO){
            Uri uri = FileProvider.getUriForFile(getActivity(),"com.study.android.criminalintent.fileprovider",mPhotoFile);
            getActivity().revokeUriPermission(uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }

    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }
    private void updateTime(){
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        mDateTimeButton.setText(df.format(mCrime.getDate()));
    }
    private String getCrimeReport(){
        String solvedString = null;
        if (mCrime.isSolved()){
            solvedString = getString(R.string.crime_report_solved);
        }else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFomat = "EEE,MMM dd";
       // String dateString = new SimpleDateFormat(dateFomat).format(mCrime.getDate());
        String dateString = android.text.format.DateFormat.format(dateFomat,mCrime.getDate()).toString();
        String suspect = mCrime.getSupect();
        if (suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        }else {
            suspect = getString(R.string.crime_report_suspect,suspect);
        }
        String report  = getString(R.string.crime_report,mCrime.getTitle(),dateString,solvedString,suspect);
        return report;
    }

    private void updatePhotoView(){
        if (mPhotoFile == null||!mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }


}
