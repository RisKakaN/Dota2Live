package martin.so.dota2live;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.Date;
import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    private Context context;
    private List<Match> matchList;

    public MatchAdapter(Context context, List<Match> matchList) {
        this.context = context;
        this.matchList = matchList;
    }

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_match_overview, null);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
        Match match = matchList.get(position);

        holder.textViewRadiantTeam.setText(match.getRadiantTeam());
        holder.textViewDireTeam.setText(match.getDireTeam());

        Date date = new Date(match.getStartTime() * 1000);
        holder.textViewDate.setText(date.toString());

        holder.imageViewRadiant.setDefaultImageResId(R.drawable.ic_error_sign);
        holder.imageViewDire.setDefaultImageResId(R.drawable.ic_error_sign);
        holder.imageViewRadiant.setErrorImageResId(R.drawable.ic_error_sign);
        holder.imageViewDire.setErrorImageResId(R.drawable.ic_error_sign);
        holder.imageViewRadiant.setImageUrl(match.getRadiantImageUrl(), VolleySingleton.getInstance(context).getImageLoader());
        holder.imageViewDire.setImageUrl(match.getDireImageUrl(), VolleySingleton.getInstance(context).getImageLoader());
    }

    @Override
    public int getItemCount() {
        if (matchList != null && !matchList.isEmpty()) {
            return matchList.size();
        } else {
            return 0;
        }
    }

    class MatchViewHolder extends RecyclerView.ViewHolder {

        TextView textViewRadiantTeam, textViewDireTeam, textViewDate;
        NetworkImageView imageViewRadiant, imageViewDire;

        MatchViewHolder(View itemView) {
            super(itemView);
            textViewRadiantTeam = itemView.findViewById(R.id.textViewRadiantName);
            textViewDireTeam = itemView.findViewById(R.id.textViewDireName);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            imageViewRadiant = itemView.findViewById(R.id.imageViewRadiant);
            imageViewDire = itemView.findViewById(R.id.imageViewDire);
        }
    }
}