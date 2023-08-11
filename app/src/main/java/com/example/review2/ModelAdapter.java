package com.example.review2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ModelViewHolder> {

    private Context context;
    private List<Model> list;

    public ModelAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<Model> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ModelViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ModelViewHolder holder, int position) {

        Model current = list.get(holder.getAdapterPosition());
        if (current == null) {
            return;
        }

        Glide.with(context).load(current.getImage()).error(R.drawable.i8).into(holder.avatar);
        holder.tvName.setText(current.getName());
        holder.tvPrice.setText(current.getPrice() + "");
        holder.tvDescription.setText(current.getDescription());

        holder.edit.setOnClickListener(v -> {

        });
        holder.delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("do you delete this item?");
            builder.setMessage("Are you sure you want to delete this item?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ApiService.instance.deleteData(current.getId()).enqueue(new Callback<Model>() {
                        @Override
                        public void onResponse(Call<Model> call, Response<Model> response) {
                            list.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                            Toast.makeText(context, "xóa thành công", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Model> call, Throwable t) {

                        }
                    });
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.show();
        });

        holder.edit.setOnClickListener(v -> {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_add);

            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            EditText edtName = dialog.findViewById(R.id.edt_name);
            EditText edtColor = dialog.findViewById(R.id.edt_color);
            EditText edtImage = dialog.findViewById(R.id.edt_image);
            EditText edtPrice = dialog.findViewById(R.id.edt_price);
            EditText edtDesc = dialog.findViewById(R.id.edt_description);
            Button btnCancel = dialog.findViewById(R.id.btn_cancel);
            Button btnSave = dialog.findViewById(R.id.btn_save);

            edtName.setText(current.getName());
            edtColor.setText(current.getColor());
            edtImage.setText(current.getImage());
            edtPrice.setText(current.getPrice()+"");
            edtDesc.setText(current.getDescription());

            btnCancel.setOnClickListener(view->dialog.dismiss());
            btnSave.setOnClickListener(view->{
               String name = edtName.getText().toString();
               String image = edtImage.getText().toString();
               String color = edtColor.getText().toString();
               int price = Integer.parseInt(edtPrice.getText().toString());
               String desc = edtDesc.getText().toString();

               current.setName(name);
               current.setColor(color);
               current.setDescription(desc);
               current.setImage(image);
               current.setPrice(price);

               updateCurrentData(current.getId(), current, holder.getAdapterPosition());
               dialog.dismiss();
            });

            dialog.show();
        });

    }

    private void updateCurrentData(String id, Model current, int i) {

        ApiService.instance.updateData(id, current).enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                Toast.makeText(context, "update success", Toast.LENGTH_SHORT).show();
                notifyItemChanged(i);
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Toast.makeText(context, "update fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (list != null) return list.size();
        return 0;
    }

    class ModelViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatar, edit, delete;
        private TextView tvName, tvDescription, tvPrice;

        public ModelViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.img_avatar);
            edit = itemView.findViewById(R.id.tc_edit);
            delete = itemView.findViewById(R.id.ic_delete);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }
    }

}
