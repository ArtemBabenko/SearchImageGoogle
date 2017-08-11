package dinamic_grid_google.dinamicgridgoogle.fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import dinamic_grid_google.dinamicgridgoogle.R;
import dinamic_grid_google.dinamicgridgoogle.adapter.ImageResultArrayAdapter;
import dinamic_grid_google.dinamicgridgoogle.models.ImageResult;
import dinamic_grid_google.dinamicgridgoogle.utils.EndlessScrollListener;
import dinamic_grid_google.dinamicgridgoogle.utils.SearchClient;


public class ImageFragment extends Fragment {

    public static final int LAYOUT = R.layout.fragment;
    public static final int MAX_PAGE = 10;

    private View view;

    private ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
    private ImageResultArrayAdapter adapter;
    private GridView gvResults;
    private ProgressBar progressBar;
    private SearchClient client;
    private int numberColumn;
    private String query;
    private int startPage = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        includeArrow();
        getBundle();
        createGridResult(numberColumn);
        onImageSearch(1);

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (page <= MAX_PAGE) {
                    onImageSearch((10 * (page - 1)) + 1);
                }
            }
        });
        return view;
    }

    private void includeArrow() {
        ImageView arrow = (ImageView) view.findViewById(R.id.arrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.arrow) {
                    getActivity().getFragmentManager().popBackStack();
                }
            }
        });
    }

    private void getBundle() {
        numberColumn = Integer.valueOf(getArguments().getString("numberColumn"));
        query = getArguments().getString("query");
    }

    private void createGridResult(int numberColumn) {
        gvResults = (GridView) view.findViewById(R.id.gvResults);
        gvResults.setNumColumns(numberColumn);
    }


    public void onImageSearch(int start) {

        if (isNetworkAvailable()) {
            client = new SearchClient();
            startPage = start;
            progressBar.setVisibility(View.VISIBLE);
            if (startPage == 1)
                imageResults.clear();
            client.getSearch(query, startPage, getActivity(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                JSONArray imageJsonResults;
                                if (response != null) {
                                    imageJsonResults = response.getJSONArray("items");
                                    imageResults.addAll(ImageResult.fromJSONArray(imageJsonResults));
                                    adapter = new ImageResultArrayAdapter(getActivity(), imageResults);
                                    gvResults.setAdapter(adapter);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getActivity(), "invalid data", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            Toast.makeText(getActivity(), "service unavailable", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
            );
        } else {
            Toast.makeText(getActivity(), "no internet connection", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

}

