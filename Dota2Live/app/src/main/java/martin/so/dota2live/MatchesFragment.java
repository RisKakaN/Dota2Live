package martin.so.dota2live;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MatchesFragment extends Fragment {

    private static final String TAG = MatchesFragment.class.getName();
    private static final String MATCH_ID_API_URL = "https://api.opendota.com/api/proMatches";
    private static final String MATCH_DETAILS_API_URL = "https://api.opendota.com/api/matches/";

    private CountDownLatch allDoneSignal;

    private volatile List<Match> matchList;
    private RecyclerView recyclerView;
    private MatchAdapter matchAdapter;

    private View view;
    private Context fragmentContext;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_matches, container, false);
            recyclerView = view.findViewById(R.id.recylcerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            matchList = new ArrayList<>();
            fragmentContext = getActivity().getApplicationContext();
            matchAdapter = new MatchAdapter(fragmentContext, matchList);
            recyclerView.setAdapter(matchAdapter);
            int numberOfMatchesToShow = 10;
            allDoneSignal = new CountDownLatch(10);
            loadMatches(numberOfMatchesToShow);

            final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        allDoneSignal.await(5, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        Log.e(TAG, "" + e);
                    }
                    mainThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Collections.sort(matchList, new Comparator<Match>() {
                                public int compare(Match m1, Match m2) {
                                    long j1 = m1.getStartTime();
                                    long j2 = m2.getStartTime();
                                    return (Long.compare(j2, j1));
                                }
                            });
                            matchAdapter = new MatchAdapter(fragmentContext, matchList);
                            recyclerView.setAdapter(matchAdapter);
                        }
                    });
                }
            }).start();
        }
        return view;
    }

    private void showNetworkIssuesLabel() {
        TextView networkIssuesLabel = view.findViewById(R.id.textViewNetworkIssues);
        networkIssuesLabel.setVisibility(View.VISIBLE);
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
                    Log.e(TAG, "" + e);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError e) {
                showNetworkIssuesLabel();
                allDoneSignal.countDown();
                Log.e(TAG, "" + e);
            }
        });
        VolleySingleton.getInstance(fragmentContext).addToRequestQueue(jsonArrayRequest);
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
                    matchList.add(new Match(radiantTeamName, direTeamName, startTime, radiantTeamImage, direTeamImage));
                } catch (JSONException e) {
                    Log.e(TAG, "" + e);
                } finally {
                    allDoneSignal.countDown();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError e) {
                showNetworkIssuesLabel();
                allDoneSignal.countDown();
                Log.e(TAG, "" + e);
            }
        });
        VolleySingleton.getInstance(fragmentContext).addToRequestQueue(jsonObjectRequest);
    }
}