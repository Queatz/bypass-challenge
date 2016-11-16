package com.bypassmobile.octo.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bypassmobile.octo.BaseActivity;
import com.bypassmobile.octo.R;
import com.bypassmobile.octo.image.ImageLoader;
import com.bypassmobile.octo.model.User;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jacob on 11/14/16.
 */

public class MemberAdapter extends BaseAdapter {

    private Context context;
    private List<User> membersList = new ArrayList<User>();
    private List<User> rawMembersList;

    public MemberAdapter(Context context) {
        this.context = context;
    }

    /**
     * Set the list of members.  Invalidates the adapter and search string.
     * @param members The new set of members
     */
    public void setMembersList(List<User> members) {
        Collections.sort(members, new Comparator<User>() {
            @Override
            public int compare(User a, User b) {
                return a.getName().compareToIgnoreCase(b.getName());
            }
        });

        rawMembersList = membersList = members;
        notifyDataSetInvalidated();
    }

    /**
     * Filter the members list via a string.  Requires members list to be loaded.
     * @param searchQuery The search string
     */
    public void setSearchQuery(String searchQuery) {
        if (rawMembersList == null) {
            return;
        }

        if (searchQuery == null || searchQuery.isEmpty()) {
            membersList = rawMembersList;
            notifyDataSetChanged();
            return;
        }

        membersList = new ArrayList<User>();

        searchQuery = ".*" + searchQuery.trim() + ".*";

        // Basic filtering
        for (User user : rawMembersList) {
            if (user.getName().toLowerCase().matches(searchQuery)) {
                membersList.add(user);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return membersList.isEmpty();
    }

    @Override
    public int getCount() {
        return membersList.size();
    }

    @Override
    public User getItem(int i) {
        return membersList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return membersList.get(i).getName().hashCode();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(context, R.layout.member_list_item, null);
        }

        ImageView memberAvatar = (ImageView) view.findViewById(R.id.memberAvatar);
        TextView memberName = (TextView) view.findViewById(R.id.memberName);

        User member = membersList.get(position);

        Transformation transformation = new RoundedTransformationBuilder().oval(true).build();

        ImageLoader.getImageLoader(context)
                .load(member.getProfileURL())
                .placeholder(R.drawable.grey_circle)
                .fit()
                .transform(transformation)
                .into(memberAvatar);

        memberName.setText(member.getName());

        refreshFollowing(member, view);

        return view;
    }

    private void refreshFollowing(User member, View view) {
        TextView followerCount = (TextView) view.findViewById(R.id.followerCount);

        if (member.getFollowing() == null) {
            loadFollowing(member, view);
            followerCount.setVisibility(View.INVISIBLE);
        } else if (member.getFollowing() > 0) {
            followerCount.setText(Integer.toString(member.getFollowing()));
            followerCount.setVisibility(View.VISIBLE);
        } else {
            followerCount.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Loads the following count of a member.  Move into a more centralized location as project grows.
     * @param member The member to load
     */
    private void loadFollowing(final User member, final View view) {
        ((BaseActivity) context).getEndpoint().getUser(member.getName(), new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                member.setFollowing(user.getFollowing());
                refreshFollowing(member, view);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
