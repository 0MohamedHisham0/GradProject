package com.hti.Grad_Project.Adapter;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hti.Grad_Project.Activities.Pages_NewBook_TextRec_Activity;
import com.hti.Grad_Project.Model.book_page_Model;
import com.hti.Grad_Project.Utilities.passUriToActivity;
import com.hti.Grad_Project.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import carbon.widget.ImageView;

public class page_adapter extends RecyclerView.Adapter<VH_OCR> {
    passUriToActivity mCallback;
    View view;
    List<book_page_Model> list;
    Context context;
    String bookName;

    private Uri image_uri;
    private final int RESULT_LOAD_IMAGE = 123;
    private final int IMAGE_CAPTURE_CODE = 654;

    public page_adapter(passUriToActivity mCallback, List<book_page_Model> list, Context context, String bookName) {
        this.mCallback = mCallback;
        this.list = list;
        this.context = context;
        this.bookName = bookName;
    }

    @NonNull
    @Override
    public VH_OCR onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.book_page_item, parent, false);
        return new VH_OCR(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH_OCR holder, int position) {
        book_page_Model model = list.get(position);

        if (position != 0) {
            holder.cl_textAndPages.setVisibility(View.VISIBLE);
            holder.cl_Buttons.setVisibility(View.GONE);

            holder.mainText.setText(model.getMainText());
            holder.pageNum.setText(model.getPageNum());

            holder.bt_deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deletePage(model, holder.getAdapterPosition());

                }
            });


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
        ((Pages_NewBook_TextRec_Activity) context).startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);

    }

    //Open Gallery
    private void openGalleryToPickImage() {
        Intent galleryIntent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Pages_NewBook_TextRec_Activity) context).startActivityForResult(galleryIntent, this.RESULT_LOAD_IMAGE);
    }

    //DeleteItem
    private void deletePage(book_page_Model page, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();

        //
        Map<String, Object> updates = new HashMap<>();
        updates.put(page.getPageNum(), FieldValue.delete());

        if (!bookName.equals("")) {
            DocumentReference documentReference = db.collection("UsersBooks").document("UsersBooks").collection(mAuth.getCurrentUser().getUid()).document(bookName);
            documentReference.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(@NonNull Void unused) {
                    removeAt(position, list);
                    Toast.makeText(context, "Page " + page.getPageNum() + " Deleted Successfully", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            removeAt(position, list);

        }

    }

    public void removeAt(int position, List<book_page_Model> list) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }
}

class VH_OCR extends RecyclerView.ViewHolder {
    TextView mainText, pageNum;
    ConstraintLayout cl_textAndPages, cl_Buttons;
    Button bt_captureImage, bt_browseImage;
    ImageView bt_deleteItem;

    public VH_OCR(@NonNull View itemView) {
        super(itemView);
        //TextAndPages
        mainText = itemView.findViewById(R.id.tv_mainText_book_page_item);
        pageNum = itemView.findViewById(R.id.tv_pageNum_book_page_item);
        cl_textAndPages = itemView.findViewById(R.id.cl_textAndPage_book_page_item);

        //Buttons
        cl_Buttons = itemView.findViewById(R.id.cl_Buttons_book_page_item);
        bt_captureImage = itemView.findViewById(R.id.bt_captureImage_book_page_item);
        bt_browseImage = itemView.findViewById(R.id.bt_browseImage_book_page_item);
        bt_deleteItem = itemView.findViewById(R.id.bt_delete_book_page_item);

    }


}

