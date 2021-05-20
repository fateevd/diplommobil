package com.example.raiq.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.raiq.R;
import com.example.raiq.WebviewActivity;
import com.example.raiq.model.ScanHisrory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ScanHistoryAdapter extends RecyclerView.Adapter<ScanHistoryAdapter.ViewHolder> {

    private Context mcontext;
    private List<ScanHisrory> scanHisrories;

    public ScanHistoryAdapter(Context context, List<ScanHisrory> scanHisrories) {
        mcontext = context;
        this.scanHisrories = scanHisrories;
    }

    @NonNull
    @Override
    public ScanHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.history_item,parent,false);
        return new ScanHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanHistoryAdapter.ViewHolder holder, int position) {
        final ScanHisrory item = scanHisrories.get(position);
        if(position == 0){
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.topMargin = 0;
            holder.historyItem.setLayoutParams(params);
        }
        holder.scanName.setText(item.getName());
        holder.scanAddress.setText(item.getAddress());
        holder.historyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcontext.startActivity(new Intent(mcontext, WebviewActivity.class).putExtra("URL",item.getURL()).putExtra("History","No"));
            }
        });
        String[] date = hoursDiffernt(item.getScanTime());
        holder.scanTime.setText(date[1]+" в "+date[0]);
    }

    private String[] hoursDiffernt(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ssZ");
        String DateTime = format.format(new Date(timestamp));
        Date value;
        String[] date_and_timeGet = {"",""};
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        try
        {
            value = format.parse(DateTime);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd.MM.yyyy'T'HH:mm");
            newFormat.setTimeZone(TimeZone.getDefault());
            DateTime = newFormat.format(value);
            date_and_timeGet[0] = DateTime.substring(DateTime.indexOf("T")+1);
            date_and_timeGet[1] = DateTime.substring(0,DateTime.indexOf("T"));
        }
        catch (ParseException e) {
            e.printStackTrace();
            date_and_timeGet = new String[]{"", ""};
        }
        return date_and_timeGet;
    }

    @Override
    public int getItemCount() {
        return scanHisrories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout historyItem;
        private TextView scanName, scanAddress, scanTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            historyItem = itemView.findViewById(R.id.history_item);
            scanName = itemView.findViewById(R.id.history_item_scan_name);
            scanAddress = itemView.findViewById(R.id.history_item_scan_address);
            scanTime = itemView.findViewById(R.id.history_item_scan_time);
        }
    }
}
