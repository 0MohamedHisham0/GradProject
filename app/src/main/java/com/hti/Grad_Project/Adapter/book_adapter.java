package com.hti.Grad_Project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hti.Grad_Project.Activities.Pages_NewBook_TextRec_Activity;
import com.hti.Grad_Project.Model.book_Model;
import com.hti.Grad_Project.Model.book_page_Model;
import com.hti.Grad_Project.R;
import com.hti.Grad_Project.Utilities.Constants;

import java.util.List;
import java.util.Objects;

import carbon.widget.ImageView;

public class book_adapter extends RecyclerView.Adapter<VH_BookSaved> {
    View view;
    List<book_Model> list;
    Context context;

    public book_adapter(List<book_Model> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public VH_BookSaved onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        return new VH_BookSaved(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH_BookSaved holder, int position) {
        book_Model model = list.get(position);
        holder.mainText.setText(model.getBookName());
        holder.pageNumber.setText("Pages : " + model.getPagesList().size());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Pages_NewBook_TextRec_Activity.class);
                intent.putExtra("bookPages", model);
                context.startActivity(intent);
            }
        });

        holder.bt_deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBook(model, holder.getAdapterPosition());

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void deleteBook(book_Model book, int position) {


        Constants.GetFireStoneDb().collection("UsersBooks").document("UsersBooks").collection(Objects.requireNonNull(Constants.GetAuth().getCurrentUser()).getUid()).document(book.getBookName()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void unused) {
                removeAt(position, list);
            }
        });

    }

    public void removeAt(int position, List<book_Model> list) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }
}

class VH_BookSaved extends RecyclerView.ViewHolder {
    TextView mainText, pageNumber;
    ImageView bt_deleteItem;

    public VH_BookSaved(@NonNull View itemView) {
        super(itemView);

        //TextAndPages
        mainText = itemView.findViewById(R.id.tv_mainText_book_item);
        pageNumber = itemView.findViewById(R.id.tv_pageNum_book_item);
        bt_deleteItem = itemView.findViewById(R.id.bt_delete_book_item);
    }
}

