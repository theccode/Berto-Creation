package com.android.berto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.app.ActivityCompat.invalidateOptionsMenu;
import static android.widget.AdapterView.*;

public class ImageFragment extends Fragment  {
    private ImageView mMasterImageView;
    private ImageView mAddImageView;
    private ImageView mUploadImageView;
    private ImageView mExitButton;
    private HorizontalScrollView mHorizontalScrollView;
    private LinearLayout mHSVLinearLayout;
    private ImageView mHSVImageView;
    private TextView mTVImageWidth;
    private TextView mTVImageHeight;
    private TextView mTVImageResolution;
    private TextView mTVCost;
    private Spinner mMaterialTypeSpinner;
    private View iv;
    private  double imageWidth;
    private  double imageHeight;
    private  double imageResolution;
    private double price;
    private final int REQUEST_CODE = 0;
    private String[] mMaterialItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.imageWidth = 0;
        this.imageHeight = 0;
        this.imageResolution = 0;
        this.mMaterialItems = new String[]{"Sticker", "Flexy"};
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        iv = new View(getContext());

        mMasterImageView = (ImageView) view.findViewById(R.id.ivImage);
        mHorizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.hsvImage);
        mAddImageView = (ImageView) view.findViewById(R.id.addImageButton);
        mUploadImageView = (ImageView) view.findViewById(R.id.uploadImageButton);
        mExitButton = (ImageView) view.findViewById(R.id.exitButton);
        mHSVLinearLayout = (LinearLayout) view.findViewById(R.id.hsvLinearLayout);
        mTVImageWidth = (TextView) view.findViewById(R.id.tvImageWidth);
        mTVImageHeight = (TextView) view.findViewById(R.id.tvImageHeight);
        mTVImageResolution = (TextView) view.findViewById(R.id.tvImageResolution);
        mTVCost = (TextView) view.findViewById(R.id.tvcost);
        mMaterialTypeSpinner = (Spinner) view.findViewById(R.id.spMaterialType);
        mMaterialTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Toast.makeText(getContext(), "Sticker chosed", Toast.LENGTH_LONG).show();
                    case 1:
                        Toast.makeText(getContext(), "Flexy chosed", Toast.LENGTH_LONG).show();
                    default:
                        Toast.makeText(getContext(), "Please choose a material type", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

       mAddImageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
               pictureIntent.setType("image/*");
               startActivityForResult(Intent.createChooser(pictureIntent, "Select An Image"), REQUEST_CODE);
           }
       });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CODE){

            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                double widthInPixels = selectedImage.getWidth();
                this.imageWidth =  selectedImage.getWidth()/300.0;
                this.imageHeight =  selectedImage.getHeight()/300.0;
                this.imageResolution = widthInPixels /imageWidth;
                this.price = (this.imageWidth * this.imageHeight)/144.00;

//                Log.d("WIDTH", String.valueOf(imageWidth));
//                Log.d("HEIGHT", String.valueOf(imageHeight));
//                Log.d("RESOLUTION", String.valueOf(imageResolution));
                setImageDimensions(imageWidth, imageHeight, imageResolution, price);
                if (selectedImage == null ){
                    mMasterImageView.setImageDrawable(null);
                }else {
                    mHSVImageView = new ImageView(getActivity());
                    registerForContextMenu(mHSVImageView);
                    mMasterImageView.setImageBitmap(selectedImage);
                    mHSVImageView.setAdjustViewBounds(true);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 0, 30, 0);
                    mHSVImageView.setLayoutParams(lp);
                    mHSVImageView.setImageBitmap(selectedImage);
                    updateMasterImageView(mHSVImageView, imageWidth, imageHeight, imageResolution, price);
                    mHSVLinearLayout.addView(mHSVImageView);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateMasterImageView(final ImageView imageView, final double imageWidth, final double imageHeight, final double imageResolution, final double price){
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                BitmapDrawable bitmap = (BitmapDrawable) imageView.getDrawable();
                mMasterImageView.setImageBitmap(bitmap.getBitmap());
                setImageDimensions(imageWidth, imageHeight, imageResolution, price);
            }
        });
    }

    private void setImageDimensions(double imageWidth, double imageHeight, double imageResolution, double price){
        mTVImageWidth.setText(MessageFormat.format("{0}\"", String.format("%.3f",imageWidth)));
        mTVImageHeight.setText(MessageFormat.format("{0}\"", String.format("%.3f",imageHeight)));
        mTVImageResolution.setText(String.valueOf(Math.round(imageResolution)));
        mTVCost.setText(MessageFormat.format("{0}\"",String.format("%.3f", price)));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout_file, mMaterialItems);
        mMaterialTypeSpinner.setAdapter(adapter);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.remove_item_menu, menu);
         iv = v;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.delete:
                    mHSVLinearLayout.removeView(iv);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
