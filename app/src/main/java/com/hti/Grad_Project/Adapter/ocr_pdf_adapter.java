package com.hti.Grad_Project.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hti.Grad_Project.Activities.TextRecognitionActivity;
import com.hti.Grad_Project.Model.pdf_Model;
import com.hti.Grad_Project.Utilities.passUriToActivity;
import com.hti.myapplication.R;

import java.util.List;

public class ocr_pdf_adapter extends RecyclerView.Adapter<VH_OCR> {
    passUriToActivity mCallback;
    View view;
    List<pdf_Model> list;
    Context context;
    private Uri image_uri;
    private final int RESULT_LOAD_IMAGE = 123;
    private final int IMAGE_CAPTURE_CODE = 654;

    public ocr_pdf_adapter(passUriToActivity mCallback, List<pdf_Model> list, Context context) {
        this.mCallback = mCallback;
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public VH_OCR onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.pdf_item, parent, false);
        return new VH_OCR(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH_OCR holder, int position) {
        pdf_Model model = list.get(position);

        if (position != 0) {
            holder.cl_textAndPages.setVisibility(View.VISIBLE);
            holder.cl_Buttons.setVisibility(View.GONE);

            holder.mainText.setText(model.getMainText());
            holder.pageNum.setText(model.getPageNum());
        } else {
            holder.cl_Buttons.setVisibility(View.VISIBLE);
            holder.cl_textAndPages.setVisibility(View.GONE);

            holder.bt_browseImage.setOnClickListener(view -> {
                openGalleryToPickImage();
            });

            holder.bt_captureImage.setOnClickListener(view -> {
                validateCameraPermission();
            });

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //Capture Image
    private void validateCameraPermission() {
        Toast.makeText(context, "val", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission("android.permission.CAMERA") == PackageManager.PERMISSION_DENIED && context.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(context, "rong", Toast.LENGTH_SHORT).show();

                String[] permission = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
                ActivityCompat.requestPermissions((Activity) context, permission, 121);
            } else {
                Toast.makeText(context, "suc", Toast.LENGTH_SHORT).show();

                openCamera();

            }
        }

    }

    private void openCamera() {
        Toast.makeText(context, "open", Toast.LENGTH_SHORT).show();

        ContentValues values = new ContentValues();
        values.put("title", "New Picture");
        values.put("description", "From the Camera");
        image_uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        mCallback.onCaptureImage(image_uri);
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        cameraIntent.putExtra("output", (Parcelable) image_uri);
        ((TextRecognitionActivity) context).startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);

    }

    //Open Gallery
    private void openGalleryToPickImage() {
        Intent galleryIntent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((TextRecognitionActivity) context).startActivityForResult(galleryIntent, this.RESULT_LOAD_IMAGE);
    }
}

class VH_OCR extends RecyclerView.ViewHolder {
    TextView mainText, pageNum;
    ConstraintLayout cl_textAndPages, cl_Buttons;
    Button bt_captureImage, bt_browseImage;

    public VH_OCR(@NonNull View itemView) {
        super(itemView);
        //TextAndPages
        mainText = itemView.findViewById(R.id.tv_mainText_pdfItem);
        pageNum = itemView.findViewById(R.id.tv_pageNum_pdfItem);
        cl_textAndPages = itemView.findViewById(R.id.cl_textAndPage_pdfItem);

        //Buttons
        cl_Buttons = itemView.findViewById(R.id.cl_Buttons_pdfItem);
        bt_captureImage = itemView.findViewById(R.id.bt_captureImage_pdfItem);
        bt_browseImage = itemView.findViewById(R.id.bt_browseImage_pdfItem);

    }
}

