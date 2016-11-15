package com.bypassmobile.octo.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bypassmobile.octo.R;
import com.bypassmobile.octo.image.ImageLoader;
import com.bypassmobile.octo.model.User;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacob on 11/14/16.
 */

public class MemberAdapter extends BaseAdapter {

    private Context context;
    private List<User> membersList = new ArrayList<User>();

    public MemberAdapter(Context context) {
        this.context = context;
    }

    /**
     * Set the list of members.  Invalidates the adapter.
     * @param members The new set of memebers
     */
    public void setMembersList(List<User> members) {
        this.membersList = members;
        notifyDataSetInvalidated();
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
        TextView followerCount = (TextView) view.findViewById(R.id.followerCount);

        User member = membersList.get(position);

        Transformation transformation = new RoundedTransformationBuilder().oval(true).build();

        ImageLoader.getImageLoader(context)
                .load(member.getProfileURL())
                .placeholder(R.drawable.grey_circle)
                .fit()
                .transform(transformation)
                .into(memberAvatar);

        memberName.setText(member.getName());

        if (member.getFollowersCount() > 0) {
            followerCount.setVisibility(View.VISIBLE);
            followerCount.setText(Integer.toString(member.getFollowersCount()));
        } else {
            followerCount.setVisibility(View.INVISIBLE);
        }

        return view;
    }
}
