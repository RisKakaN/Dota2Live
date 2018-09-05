package martin.so.dota2live;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MatchesFragment extends Fragment {

    private static final String TAG = MatchesFragment.class.getName();
    private static final String MATCH_ID_API_URL = "https://api.opendota.com/api/proMatches";
    private static final String MATCH_DETAILS_API_URL = "https://api.opendota.com/api/matches/";

    private List<Match> matchList;
    private RecyclerView recyclerView;
    private MatchAdapter matchAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);
        recyclerView = view.findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        matchList = new ArrayList<>();
        matchAdapter = new MatchAdapter(getActivity().getApplicationContext(), matchList);
        recyclerView.setAdapter(matchAdapter);
        loadMatches(5);
        return view;
    }

    private void loadMatches(final int numberOfMatches) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, MATCH_ID_API_URL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < numberOfMatches; i++) {
                    JSONObject match = (JSONObject) response.get(i);
                    String matchID = match.getString("match_id");
                    loadMatchOverviewInfo(matchID);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError e) {
                Log.e(TAG, e.getMessage());
            }
        });
        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }

    private void loadMatchOverviewInfo(String matchID) {
        String matchDetailsApiUrl = MATCH_DETAILS_API_URL + matchID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, matchDetailsApiUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject radiantTeamDetails = response.getJSONObject("radiant_team");
                    String radiantTeamName = radiantTeamDetails.getString("name");
                    String radiantTeamImage = radiantTeamDetails.getString("logo_url");

                    JSONObject direTeamDetails = response.getJSONObject("dire_team");
                    String direTeamName = direTeamDetails.getString("name");
                    String direTeamImage = direTeamDetails.getString("logo_url");

                    long startTime = response.getLong("start_time");
                    Date date = new Date(startTime * 1000);

                    matchList.add(new Match(radiantTeamName, direTeamName, date.toString(), radiantTeamImage, direTeamImage));
                    matchAdapter = new MatchAdapter(getActivity().getApplicationContext(), matchList);
                    recyclerView.setAdapter(matchAdapter);
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError e) {
                Log.e(TAG, e.getMessage());
            }
        });
        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}