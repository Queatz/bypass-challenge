package com.bypassmobile.octo;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bypassmobile.octo.adapters.MemberAdapter;
import com.bypassmobile.octo.model.User;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MembersListActivity extends BaseActivity {

    public static final String PARAMETER_MEMBER = "member";

    ListView membersListView;
    MemberAdapter membersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orginization_members);

        membersListView = (ListView) findViewById(R.id.membersListView);
        TextView currentMember = (TextView) findViewById(R.id.currentMember);

        ActionBar actionBar = getActionBar();

        if (getIntent() != null && getIntent().hasExtra(PARAMETER_MEMBER)) {
            String memberName = getIntent().getStringExtra(PARAMETER_MEMBER);

            currentMember.setVisibility(View.VISIBLE);
            currentMember.setText(getString(R.string.member_follows, memberName));

            loadMember(memberName);

            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        } else {
            currentMember.setVisibility(View.GONE);
            loadOrganization(getString(R.string.organization_name));
        }

        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchMembers(query);
        }
    }

    private void searchMembers(String query) {
        if (membersAdapter != null) {
            membersAdapter.setSearchQuery(query);
        }
    }

    private void loadMember(String member) {
        getEndpoint().getFollowingUser(member, new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                refreshMembers(users);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MembersListActivity.this,
                        R.string.error_could_not_load_followers_of_user,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadOrganization(String organization) {
        getEndpoint().getOrganizationMember(organization, new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                refreshMembers(users);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MembersListActivity.this,
                        getString(R.string.error_could_not_load_orginization_members),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshMembers(List<User> members) {
        if (membersAdapter == null) {
            membersAdapter = new MemberAdapter(this);
            membersListView.setAdapter(membersAdapter);
            membersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    User user = membersAdapter.getItem(position);

                    showFollowersOf(user);
                }
            });
        }

        membersAdapter.setMembersList(members);
    }

    private void showFollowersOf(User user) {
        Intent intent = new Intent(this, MembersListActivity.class);
        intent.putExtra(PARAMETER_MEMBER, user.getName());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchMembers(searchView.getQuery().toString());
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }
}
