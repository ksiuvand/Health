package com.example.healthy.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthy.Listeners.RecipeClickListener;
import com.example.healthy.MarksItem;
import com.example.healthy.R;
import com.example.healthy.RecipeDetailsActivity;
import com.example.healthy.RequestManager;
import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DishMarksAdapter extends RecyclerView.Adapter<DishMarksAdapter.ViewHolder>{

    private List<MarksItem> listItems;
    ArrayList<String> marksM = new ArrayList<String>();
    public final static String PATH_MARKS_LOCATION = "marks";
    private Context mContext;
    RecipeClickListener listener;

    public DishMarksAdapter(List<MarksItem> listItems, Context mContext, RecipeClickListener listener) {
        this.listItems = listItems;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);
        return new ViewHolder(v);
    }

    public String getLikechislo(int n) {
        int result = n % 100;
        if (result >=10 && result <= 20) {
            // если окончание 11 - 20
            return mContext.getResources().getString(R.string.like);
        }
        // смотрим одну последнюю цифру
        result = n % 10;
        if (result == 0 || result > 4) {
            return mContext.getResources().getString(R.string.like);
        }
        if (result > 1) {
            return mContext.getResources().getString(R.string.like2);
        } if (result == 1) {
            return mContext.getResources().getString(R.string.like3);
        }
        return null;
    }
    public String getServingchislo(int n) {
        int result = n % 100;
        if (result >=10 && result <= 20) {
            // если окончание 11 - 20
            return mContext.getResources().getString(R.string.servings);
        }
        // смотрим одну последнюю цифру
        result = n % 10;
        if (result == 0 || result > 4) {
            return mContext.getResources().getString(R.string.servings);
        }
        if (result > 1) {
            return mContext.getResources().getString(R.string.servings2);
        } if (result == 1) {
            return mContext.getResources().getString(R.string.servings3);
        }
        return null;
    }
    public String getMinutechislo(int n) {
        int result = n % 100;
        if (result >=10 && result <= 20) {
            return mContext.getResources().getString(R.string.minute);
        }
        result = n % 10;
        if (result == 0 || result > 4) {
            return mContext.getResources().getString(R.string.minute);
        }
        if (result > 1) {
            return mContext.getResources().getString(R.string.minute2);
        } if (result == 1) {
            return mContext.getResources().getString(R.string.minute3);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MarksItem itemList = listItems.get(holder.getAdapterPosition());
        String country = Locale.getDefault().getCountry();
        if (country.equals("RU")){
            TranslateAPI translateAPI = new TranslateAPI(Language.ENGLISH, Language.RUSSIAN , itemList.getTitle());
            translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                @Override
                public void onSuccess(String translatedText) {
                    String title = translatedText;
                    holder.txtTitle.setText(title);
                }
                @Override
                public void onFailure(String ErrorText) {
                    Log.d("idcn", "onFailure: "+ErrorText);
                }
            });
        }else{
            holder.txtTitle.setText(itemList.getTitle());
        }
        holder.txtPerson.setText(itemList.getServings()+" " + getServingchislo(Integer.parseInt(itemList.getServings())));
        holder.txtLikes.setText(itemList.getLikes()+" "+ getLikechislo(Integer.parseInt(itemList.getLikes())));
        holder.txtTime.setText(itemList.getMinutes()+" " + getMinutechislo(Integer.parseInt(itemList.getMinutes())));
        Picasso.get().load(itemList.getImage()).transform(new RoundedCornersTransform()).into(holder.dishImage);
        SharedPreferences uri = mContext.getSharedPreferences(PATH_MARKS_LOCATION, Context.MODE_PRIVATE);
        int size = uri.getInt("marksSize", 0);
        for(int i=0; i < size; i++)
            marksM.add(uri.getString("marks" + i, ""));
        holder.dishListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecipeClicked(String.valueOf(marksM.get(holder.getAdapterPosition())));
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView dishListCard;

        public TextView txtTitle, txtPerson, txtLikes, txtTime;
        public ImageView dishImage;
        public ViewHolder(View itemView) {
            super(itemView);
            dishListCard = itemView.findViewById(R.id.dishListCard);
            txtTitle = (TextView) itemView.findViewById(R.id.titleTxt);
            txtPerson = (TextView) itemView.findViewById(R.id.personsTxt);
            txtLikes = (TextView) itemView.findViewById(R.id.likesTxt);
            txtTime = (TextView) itemView.findViewById(R.id.timeTxt);
            dishImage = (ImageView) itemView.findViewById(R.id.dishImage);
        }
    }

}
