package dinamic_grid_google.dinamicgridgoogle;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import dinamic_grid_google.dinamicgridgoogle.fragment.ImageFragment;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private EditText editText;
    private SeekBar seekBar;
    private TextView numberColumn;
    private TextView btnSearch;

    private final int Min = 1;
    private int Max = 6;
    private int Default = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.edit_text);
        createSeekBar();
        numberColumn = (TextView) findViewById(R.id.number_column);
        createBtnSearch();

    }

    private void createSeekBar() {
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setMax(Max - Min);
        seekBar.setProgress(Default - Min);
        seekBar.setOnSeekBarChangeListener(this);
    }

    private void createBtnSearch() {
        btnSearch = (TextView) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().equals("")) {
                    createFragment();
                } else
                    Toast.makeText(getApplicationContext(), "invalid query", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void createFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("numberColumn", numberColumn.getText().toString());
        bundle.putString("query", editText.getText().toString());
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(bundle);
        ft.replace(R.id.activity_main, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        int current = progress + Min;
        numberColumn.setText(String.valueOf(current));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
