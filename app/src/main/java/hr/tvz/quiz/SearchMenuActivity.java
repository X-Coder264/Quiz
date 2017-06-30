package hr.tvz.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import hr.tvz.quiz.model.Subject;
import hr.tvz.quiz.model.User;
import hr.tvz.quiz.rest.STOMPwebScoket;

public class SearchMenuActivity extends Activity {
    ListView lv;
    SearchView sv;
    String[] teams={"Man Utd","Man City","Chelsea","Arsenal","Liverpool","Totenham"};
    List<String> opponents = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    private Activity activity;
    private STOMPwebScoket socket;

    private User user;
    private Integer examId;
    private Subject subject;
    private HashMap<String, String> usersMap = new HashMap<>();

    private STOMPwebScoket stomp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_menu_multiplayer);

        subject = (Subject) getIntent().getSerializableExtra("SUBJECT");
        user = (User) getIntent().getSerializableExtra("USER");
        examId = (Integer) getIntent().getSerializableExtra("EXAM_ID");
        usersMap = (HashMap<String, String>) getIntent().getSerializableExtra("OPPONENTS");

        for (String i : usersMap.keySet()) {
            opponents.add(i);
        }

        this.activity = this;

        lv=(ListView) findViewById(R.id.listView1);
        sv=(SearchView) findViewById(R.id.searchView1);
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,opponents);
        lv.setAdapter(adapter);
        sv.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                // TODO Auto-generated method stub
                return false;
            }
            @Override
            public boolean onQueryTextChange(String text) {
                adapter.getFilter().filter(text);
                return false;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String opponent = lv.getItemAtPosition(position).toString();
                stomp.sendGameInvitation(opponent, subject.getId());

                Intent intent = new Intent(activity, MultiPlayerActivity.class);
                intent.putExtra("EXAM_ID", examId);
                intent.putExtra("SUBJECT", subject);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        stomp = STOMPwebScoket.getInstance(this, user);
    }
}